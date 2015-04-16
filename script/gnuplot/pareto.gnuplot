set terminal postscript enhanced font 'Verdana,20'
set output '| ps2pdf - pareto.pdf'
set key bottom right

set xlabel "Excess%"
set ylabel "Exceeding servers"
plot	"/home/peppone/workspace/JMetalVM/output/referenceFronts/VMProblem0.500.220.new.rf" using 1:2 title "VMP0" lw 2 lt rgb "brown" ps 1.5 with linespoint,\
    	"/home/peppone/workspace/JMetalVM/output/referenceFronts/VMProblem1.500.220.new.rf" using 1:2 title "VMP1" lw 2 lt rgb "magenta" ps 1.5 with linespoint,\
	"/home/peppone/workspace/JMetalVM/output/referenceFronts/VMProblem2.500.220.new.rf" using 1:2 title "VMP2" lw 2 lt rgb "blue" ps 1.5 with linespoint;

unset output
quit

