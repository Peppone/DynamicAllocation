#!bin/bash
if [ $# -lt 3 ]; then
    echo Usage: `basename $0` cplex_run[true-false] jmetal_run[true-false] file_generation[true-false] 1>&2
    exit 1
fi

BINPATH="/home/peppone/workspace/JMetalVM/bin"
INPUT="/home/peppone/workspace/JMetalVM/input"
OUTPUT="/home/peppone/workspace/JMetalVM/output"
SERVPERRACK=24
RACKPERPOD=8

FEASIBLE=""
rm -r $INPUT/*.txt 
for j in `seq 1 100`; do
VM=`expr $j \* 10`
if [ "$3" = "true" -o "$3" = "t" ]
then 	
	
	java -cp /home/peppone/workspace/JMetalVM/bin/ generator.FileGenerator $VM
	for i in `ls *.txt`;do
		#Rimuove l'estensione
		FILE=`echo "${i%%.*}"`
		mv $i $FILE$VM.ttxt
	done
	for i in `ls *.ttxt`;do
		#Rimuove l'estensione
		FILE=`echo "${i%%.*}"`
		mv $i $FILE.txt
	done
	mv *.txt /home/peppone/workspace/JMetalVM/input/
	for i in `ls *.dat`;do
		#Rimuove l'estensione
		FILE=`echo "${i%%.*}"`
		mv $i $FILE$VM.tdat
	done
	for i in `ls *.tdat`;do
		FILE=`echo "${i%%.*}"`
		mv $i $FILE.dat
	done
	mv *.dat /home/peppone/opl/MultiBinPackaging/input/
fi
for k in `seq 6 8`; do
	#si divide per 1 perchè altrimenti scale0 non funziona....
	SERVER=`echo "scale=0; ($VM*$k* 0.05)/1"| bc -l`
	if [ "$1" = "true" -o "$1" = "t" ]
	then
#La stringa di sotto modifica brutalmente il sorgente model.mod. Quando viene trovata una stringa del tipo int vm = ed altro, viene sosituita
		perl -pi -e 's/int vm[\s]* = [\d]*;/int vm  = '$VM';/g' /home/peppone/opl/MultiBinPackaging/model.mod
		echo "server = $SERVER;" > /home/peppone/opl/MultiBinPackaging/server.dat;
		oplrun /home/peppone/opl/MultiBinPackaging/model.mod /home/peppone/opl/MultiBinPackaging/input/cpu$VM.dat /home/peppone/opl/MultiBinPackaging/input/disk$VM.dat /home/peppone/opl/MultiBinPackaging/input/mem$VM.dat /home/peppone/opl/MultiBinPackaging/server.dat | tee "$OUTPUT/CPLEX.$VM.$SERVER"
fi
#if [ "$2" != "false" -o "$2" != "f" ]
if [ "$2" = "true" -o "$2" = "t" ]
then
TIMEFILE="$INPUT/time$VM.txt"
CPUFILE="$INPUT/cpu$VM.txt"
MEMFILE="$INPUT/mem$VM.txt"
DISKFILE="$INPUT/disk$VM.txt"
BWFILE="$INPUT/bw$VM.txt"
java -cp $BINPATH:/usr/lib/jvm/jmetal MyExperiment $VM $SERVER $SERVPERRACK $RACKPERPOD $TIMEFILE $CPUFILE $MEMFILE $DISKFILE $BWFILE 40
perl -pi -e 's/\\documentclass{article}/\\documentclass[landscape]{article}/' "$OUTPUT/latex/VMProblem$VM.$SERVER.tex"
fi
if [ "$2" = "true" -o "$2" = "t" ]
then
mv "$OUTPUT/data" "$OUTPUT/data$VM.$SERVER"
mv "$OUTPUT/latex" "$OUTPUT/latex$VM.$SERVER"
mv "$OUTPUT/referenceFronts" "$OUTPUT/referenceFronts$VM.$SERVER"
mv "$OUTPUT/R" "$OUTPUT/R$VM.$SERVER"
fi
done
done
#Compilazione di tutti i file latex
if [ "$2" = "true" -o "$2" = "t" ]
then
	for j in `seq 1 100`; do
	VM=`expr $j \* 10`
	for k in `seq 5 7`; do
	SERVER=`echo "scale=0; ($VM*$k* 0.05)/1"| bc -l`
	for i in `ls $OUTPUT/latex$VM.$SERVER/*.tex`;do
	pdflatex -output-directory "$OUTPUT/latex$VM.$SERVER/" $i
	done
	for i in `ls $OUTPUT/R$VM.$SERVER/*.R`;do
	Rscript $i
	done
	for i in `ls $OUTPUT/R$VM.$SERVER/*.R`;do
	Rscript $i
	done
	for i in `ls $OUTPUT/R$VM.$SERVER/*.tex`;do
	pdflatex -output-directory "$OUTPUT/R$VM.$SERVER/" $i
	done

	done
	done
fi
#2> /dev/null
#FEASIBLE=$FEASIBLE:`cat $OUTPUT/$VM/$i/FUN |wc -l `
#awk '{if(min==""){min=max=$1; omin=omax=$2}; if (min>$1) {min=$1; omin=$2}; if(max<$1){max=$1; omax=$2};}END{print "min = "min, omin; print "max = "max, omax}' $OUTPUT/$VM/$i/FUN #| sort -n | awk '{if(NR<2) print $1}'
#done
#echo $FEASIBLE
