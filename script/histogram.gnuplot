set terminal jpeg gian font "Helvetica" 16
set output 'histogram.jpeg'
set grid y
set xrange [-1:5]
set autoscale y
set style data histograms
set boxwidth 0.5
set style fill solid 1.0 border -1
set ylabel "Allocation"
plot "1.txt" smooth frequency with boxes
