#!bin/bash
BINPATH="/home/peppone/workspace/JMetalVM/bin"
INPUT="/home/peppone/workspace/JMetalVM/input"
OUTPUT="/home/peppone/workspace/JMetalVM/output"
SERVPERRACK=800
RACKPERPOD=2
TIMEFILE="$INPUT/time.txt"
CPUFILE="$INPUT/cpu.txt"
MEMFILE="$INPUT/mem.txt"
DISKFILE="$INPUT/disk.txt"
BWFILE="$INPUT/bw.txt"
FEASIBLE=""
for j in `seq 1 100`; do
VM=`expr $j \* 10`
java -cp /home/peppone/workspace/JMetalVM/bin/ generator.FileGenerator $VM
mv *.txt /home/peppone/workspace/JMetalVM/input/
mv *.dat /home/peppone/opl/MultiBinPackaging/
for k in `seq 5 7`; do
#si divide per 1 perchè altrimenti scale0 non funziona....
SERVER=`echo "scale=0; ($VM*$k* 0.05)/1"| bc -l`
if [ "$1" = "true" -o "$1" = "t" ]
then
#La stringa di sotto modifica brutalmente il sorgente model.mod. Quando viene trovata una stringa del tipo int vm = ed altro, viene sosituita
perl -pi -e 's/int vm[\s]* = [\d]*;/int vm  = '$VM';/g' /home/peppone/opl/MultiBinPackaging/model.mod
echo "server = $SERVER;" > /home/peppone/opl/MultiBinPackaging/server.dat;
oplrun /home/peppone/opl/MultiBinPackaging/model.mod /home/peppone/opl/MultiBinPackaging/cpu.dat /home/peppone/opl/MultiBinPackaging/disk.dat /home/peppone/opl/MultiBinPackaging/mem.dat /home/peppone/opl/MultiBinPackaging/server.dat | tee "$OUTPUT/CPLEX.$VM.$SERVER"
fi
#if [ "$2" != "false" -o "$2" != "f" ]
if [ "$2" = "true" -o "$2" = "t" ]
then
java -cp $BINPATH:/usr/lib/jvm/jmetal MyExperiment $VM $SERVER $SERVPERRACK $RACKPERPOD $TIMEFILE $CPUFILE $MEMFILE $DISKFILE $BWFILE 40
perl -pi -e 's/\\documentclass{article}/\\documentclass[landscape]{article}/' "$OUTPUT/latex/VMProblem$VM.$SERVER.tex"
for i in `ls $OUTPUT/latex/*.tex`;do
pdflatex -output-directory "$OUTPUT/latex/" $i
done
for i in `ls $OUTPUT/R/*.R`;do
Rscript $i
done
for i in `ls $OUTPUT/R/*.R`;do
Rscript $i
done
for i in `ls $OUTPUT/R/*.tex`;do
pdflatex -output-directory "$OUTPUT/R/" $i
done
fi
done
if [ "$2" = "true" -o "$2" = "t" ]
then
mv "$OUTPUT/data" "$OUTPUT/data$VM.$SERVER"
mv "$OUTPUT/latex" "$OUTPUT/latex$VM.$SERVER"
mv "$OUTPUT/referenceFronts" "$OUTPUT/referenceFronts$VM.$SERVER"
mv "$OUTPUT/R" "$OUTPUT/R$VM.$SERVER"
fi
done
#2> /dev/null
#FEASIBLE=$FEASIBLE:`cat $OUTPUT/$VM/$i/FUN |wc -l `
#awk '{if(min==""){min=max=$1; omin=omax=$2}; if (min>$1) {min=$1; omin=$2}; if(max<$1){max=$1; omax=$2};}END{print "min = "min, omin; print "max = "max, omax}' $OUTPUT/$VM/$i/FUN #| sort -n | awk '{if(NR<2) print $1}'
#done
#echo $FEASIBLE
