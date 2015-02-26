awk   '{if(NR <11) {for (i=1;i<NF;++i) print $i > NR".txt"}}' ../VAR
#awk   '{ for (i=1;i<NF;++i) a[$i]++} END{print a[0] > NR".dat"}' 1.txt 
