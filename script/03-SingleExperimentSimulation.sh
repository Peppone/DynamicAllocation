#!bin/bash
if [ $# -lt 3 ]; then
    echo Usage: `basename $0` cplex_run[true-false] jmetal_run[true-false] file_generation[true-false] 1>&2
    exit 1
fi

BINPATH="/home/portaluri/workspace/DynamicAllocation/bin"
INPUT="/home/portaluri/workspace/DynamicAllocation/input"
OUTPUT="/home/portaluri/workspace/DynamicAllocation/output"
CPLEX="/home/portaluri/opl/MultiBinPackaging"
SERVPERRACK=24
RACKPERPOD=8
FEASIBLE=""
#rm -r $INPUT/*.txt
#Vengono eseguite simulazioni con 100 - 2000 VM (fino a 900 già fatte)
#for j in `seq 1 20`; do
for j in `seq 8 11`; do
VM=`expr $j \* 100`
#Esegue la parte di generazione dei file
if [ "$3" = "true" -o "$3" = "t" ]
then 	
	
	java -cp $BINPATH generator.FileGenerator $VM
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
	mv *.txt $INPUT
	for i in `ls *.dat`;do
		#Rimuove l'estensione
		FILE=`echo "${i%%.*}"`
		mv $i $FILE$VM.tdat
	done
	for i in `ls *.tdat`;do
		FILE=`echo "${i%%.*}"`
		mv $i $FILE.dat
	done
	mv *.dat $CPLEX/input/
fi
#Determina il numero di server in uso
	SERVER=768
	#768 = 4 (pod) * 8 (rack) * 24(server)
	if [ "$1" = "true" -o "$1" = "t" ]
	then
#La stringa di sotto modifica brutalmente il sorgente model.mod. Quando viene trovata una stringa del tipo int vm = ed altro, viene sosituita
		perl -pi -e 's/int vm[\s]* = [\d]*;/int vm  = '$VM';/g' $CPLEX/model.mod
		echo "server = $SERVER;" > $CPLEX/server.dat;
		oplrun $CPLEX/model.mod $CPLEX/input/cpu$VM.dat $CPLEX/input/disk$VM.dat $CPLEX/input/mem$VM.dat $CPLEX/server.dat | tee "$OUTPUT/CPLEX.$VM.$SERVER"
	fi
	#Esegue la parte jMetal
	if [ "$2" = "true" -o "$2" = "t" ]
	then
	TIMEFILE="$INPUT/time$VM.txt"
	CPUFILE="$INPUT/cpu$VM.txt"
	MEMFILE="$INPUT/mem$VM.txt"
	DISKFILE="$INPUT/disk$VM.txt"
	BWFILE="$INPUT/bw$VM.txt"
		for type in `seq 0 5`; do
			java -cp $BINPATH:/usr/lib/jvm/jmetal ExperimentMain $VM $SERVER $SERVPERRACK $RACKPERPOD $TIMEFILE $CPUFILE $MEMFILE $DISKFILE $BWFILE 40 $type
		perl -pi -e 's/\\documentclass{article}/\\documentclass[landscape]{article}/' "$OUTPUT/latex/VMProblem$VM.$SERVER.$type.tex"
		#fi
		#if [ "$2" = "true" -o "$2" = "t" ]
		#then
		mv "$OUTPUT/data" "$OUTPUT/data$VM.$SERVER.$type"
		mv "$OUTPUT/latex" "$OUTPUT/latex$VM.$SERVER.$type"
		mv "$OUTPUT/referenceFronts" "$OUTPUT/referenceFronts$VM.$SERVER.$type"
		mv "$OUTPUT/R" "$OUTPUT/R$VM.$SERVER.$type"
		#fi
		done
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
done
#2> /dev/null
#FEASIBLE=$FEASIBLE:`cat $OUTPUT/$VM/$i/FUN |wc -l `
#awk '{if(min==""){min=max=$1; omin=omax=$2}; if (min>$1) {min=$1; omin=$2}; if(max<$1){max=$1; omax=$2};}END{print "min = "min, omin; print "max = "max, omax}' $OUTPUT/$VM/$i/FUN #| sort -n | awk '{if(NR<2) print $1}'
#done
#echo $FEASIBLE
