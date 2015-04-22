#!bin/sh
BINPATH="/home/portaluri/workspace/DynamicAllocation/bin"
INPUT="/home/portaluri/workspace/DynamicAllocation/input"
OUTPUT="/home/portaluri/workspace/DynamicAllocation/output"
CPLEX="/home/portaluri/opl/MultiBinPackaging"
VM=$1
SERVER=$2
SERVPERRACK=800
RACKPERPOD=2
TIMEFILE="$INPUT/time.txt"
CPUFILE="$INPUT/cpu.txt"
MEMFILE="$INPUT/mem.txt"
DISKFILE="$INPUT/disk.txt"
BWFILE="$INPUT/bw.txt"
FEASIBLE=""
if [ "$3" = "true" -o "$3" = "t" ]
then
#La stringa di sotto modifica brutalmente il sorgente model.mod. Quando viene trovata una stringa del tipo int vm = ed altro, viene sosituita
perl -pi -e 's/int vm[\s]* = [\d]*;/int vm  = '$1';/g' $CPLEX/model.mod
echo "server = $2;" > $CPLEX/server.dat;
oplrun $CPLEX/model.mod $CPLEX/cpu.dat $CPLEX/disk.dat $CPLEX/mem.dat $CPLEX/server.dat | tee "$OUTPUT/CPLEX_$1_$2"
fi
#for i in `seq 0 2`;do
if [ "$4" != "false" -o "$4" != "f" ]
then
java -cp $BINPATH:/usr/lib/jvm/jmetal MyExperiment $VM $SERVER $SERVPERRACK $RACKPERPOD $TIMEFILE $CPUFILE $MEMFILE $DISKFILE $BWFILE 40
perl -pi -e 's/\\documentclass{article}/\\documentclass[landscape]{article}/' "$OUTPUT/latex/VMProblem$1.$2.tex"
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
#2> /dev/null
#FEASIBLE=$FEASIBLE:`cat $OUTPUT/$VM/$i/FUN |wc -l `
#awk '{if(min==""){min=max=$1; omin=omax=$2}; if (min>$1) {min=$1; omin=$2}; if(max<$1){max=$1; omax=$2};}END{print "min = "min, omin; print "max = "max, omax}' $OUTPUT/$VM/$i/FUN #| sort -n | awk '{if(NR<2) print $1}'
#done
#echo $FEASIBLE
