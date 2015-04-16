#!bin/sh
BINPATH="/home/peppone/workspace/JMetalVM/bin"
INPUT="/home/peppone/workspace/JMetalVM/input"
OUTPUT="/home/peppone/workspace/JMetalVM/output"
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
perl -pi -e 's/int vm[\s]* = [\d]*;/int vm  = '$1';/g' /home/peppone/opl/MultiBinPackaging/model.mod
echo "server = $2;" > /home/peppone/opl/MultiBinPackaging/server.dat;
oplrun /home/peppone/opl/MultiBinPackaging/model.mod /home/peppone/opl/MultiBinPackaging/cpu.dat /home/peppone/opl/MultiBinPackaging/disk.dat /home/peppone/opl/MultiBinPackaging/mem.dat /home/peppone/opl/MultiBinPackaging/server.dat > "$OUTPUT/CPLEX.txt"
fi
#for i in `seq 0 2`;do
#java -cp $BINPATH:/usr/lib/jvm/jmetal MyExperiment $VM $SERVER $SERVPERRACK $RACKPERPOD $TIMEFILE $CPUFILE $MEMFILE $DISKFILE $BWFILE 30
#2> /dev/null
#FEASIBLE=$FEASIBLE:`cat $OUTPUT/$VM/$i/FUN |wc -l `
#awk '{if(min==""){min=max=$1; omin=omax=$2}; if (min>$1) {min=$1; omin=$2}; if(max<$1){max=$1; omax=$2};}END{print "min = "min, omin; print "max = "max, omax}' $OUTPUT/$VM/$i/FUN #| sort -n | awk '{if(NR<2) print $1}'
#done
#echo $FEASIBLE
