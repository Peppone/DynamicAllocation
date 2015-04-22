#!bin/sh
GNUPLOT="/home/portaluri/workspace/DynamicAllocation/script/gnuplot"

for i in `seq 1 3`;do
	gnuplot $GNUPLOT/problem$i.gnuplot
done
	gnuplot $GNUPLOT/time.gnuplot
	gnuplot $GNUPLOT/timeGA.gnuplot
