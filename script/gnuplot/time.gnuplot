set terminal postscript enhanced font 'Verdana,20'
set output '| ps2pdf - time.pdf'
set key top right
set xlabel "VM"
set ylabel "Execution Time [s]"
plot	"/home/portaluri/workspace/JMetalVM/output/avgtime3.txt" using 1:2 title "NSGA100" lw 2 lt rgb "blue" ps 1.5 with linespoint, \
	"/home/portaluri/workspace/JMetalVM/output/avgtime1.txt" using 1:2 title "NSGA1000" lw 2 lt rgb "brown" ps 1.5 with linespoint,\
    	"/home/portaluri/workspace/JMetalVM/output/avgtime2.txt" using 1:2 title "NSGAVIT" lw 2 lt rgb "magenta" ps 1.5 with linespoint,\
	"/home/portaluri/workspace/JMetalVM/output/avgtime4.txt" using 1:2 title "NSGA095" lw 2 lt rgb "green" ps 1.5 with linespoint, \
	"/home/portaluri/workspace/JMetalVM/output/avgtime5.txt" using 1:2 title "NSGA05" lw 2 lt rgb "yellow" ps 1.5 with linespoint, \
	"/home/portaluri/workspace/JMetalVM/output/avgtimeCPLEX.txt" using 1:2 title "CPLEX" lw 2 lt rgb "black" ps 1.5 with linespoint;
unset output
quit
