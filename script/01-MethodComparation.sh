#!bin/sh
BINPATH="/home/peppone/workspace/JMetalVM/bin"
INPUT="/home/peppone/workspace/JMetalVM/input"
OUTPUT="/home/peppone/workspace/JMetalVM/output"
VM=10
SERVER=3
SERVPERRACK=24
RACKPERPOD=5
TIMEFILE="$INPUT/time.txt"
CPUFILE="$INPUT/cpu.txt"
MEMFILE="$INPUT/mem.txt"
DISKFILE="$INPUT/disk.txt"
BWFILE="$INPUT/bw.txt"
FEASIBLE=""
rm -r $OUTPUT/$VM
mkdir $OUTPUT/$VM
for i in `seq 0 3`;do
mkdir $OUTPUT/$VM/$i
java -cp $BINPATH:/usr/lib/jvm/jmetal Main $VM $SERVER $SERVPERRACK $RACKPERPOD $TIMEFILE $CPUFILE $MEMFILE $DISKFILE $BWFILE $i "$OUTPUT/$VM/$i"
FEASIBLE=$FEASIBLE:`cat $OUTPUT/$VM/$i/FUN |wc -l `
done
echo $FEASIBLE
