set terminal postscript enhanced font 'Verdana,20'
set output '| ps2pdf - VMP3.pdf'
set key top left
set xlabel "VM"
set ylabel "Excess"
plot	"/home/portaluri/workspace/JMetalVM/output/best3.2.txt" using 1:2 title "NSGA100" lw 2 lt rgb "blue" ps 1.5 with linespoint, \
	"/home/portaluri/workspace/JMetalVM/output/best1.2.txt" using 1:2 title " NSGA1000" lw 2 lt rgb "brown" ps 1.5 with linespoint,\
    	"/home/portaluri/workspace/JMetalVM/output/best2.2.txt" using 1:2 title "NSGAVIT" lw 2 lt rgb "magenta" ps 1.5 with linespoint,\
	"/home/portaluri/workspace/JMetalVM/output/best4.2.txt" using 1:2 title "NSGA095" lw 2 lt rgb "green" ps 1.5 with linespoint, \
	"/home/portaluri/workspace/JMetalVM/output/best5.2.txt" using 1:2 title "NSGA05" lw 2 lt rgb "black" ps 1.5 with linespoint,\
	"/home/portaluri/workspace/JMetalVM/output/bestCPLEX.txt" using 1:2 title "CPLEX" lw 2 lt rgb "red" ps 1.5 with linespoint;
unset output
quit
