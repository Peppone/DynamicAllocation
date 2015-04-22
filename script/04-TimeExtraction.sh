#!bin/sh
SERVER=768
OUTPUT="/home/portaluri/workspace/DynamicAllocation/output/100-2000"
RES="/home/portaluri/workspace/DynamicAllocation/output"
for i in `seq 2 3`; do
if [ $i -eq 2 ]
	then
	> avgtimeCPLEX.txt
	> bestCPLEX.txt
fi
	VM=`expr $i \* 25`
	for alg in `seq 1 5`;do
	#serve per svuotare il file all'inizio della computazione
	if [ $i -eq 2 ]
	then
		> avgtime$alg.txt 
	fi

		#Estrae il tempo medio di esecuzione
		TIME=`awk '{if(NR==2)print '$VM'"\t" $3}' $OUTPUT/time$alg.$VM.$SERVER.txt`
		echo $TIME | rev |cut -c5-| rev >> avgtime$alg.txt
		for probl in `seq 0 2`;do
			if [ $i -eq 2 ]
			then
				> best$alg.$probl.txt
			fi
			awk '{print $1}' $OUTPUT/referenceFronts$VM.$SERVER.$alg/VMProblem$probl.$VM.$SERVER.rf | sort -n |awk '{if(NR==1) print '$VM'"\t"$0}' >> best$alg.$probl.txt
			
		done

	
	done
 	CPLEXT=`cat $OUTPUT/CPLEX.$VM.$SERVER| grep "Time spent in solve" | awk '{print $7}'`
	#Rimuove l'ultimo carattere della variabile
	CPLEXT=${CPLEXT%?}
	#Sostituisce la virgola con il punto
	CPLEXT=$(echo $CPLEXT | sed -e 's/,/./g')
	echo $VM $CPLEXT >> avgtimeCPLEX.txt
	CPLEXRES=`cat $OUTPUT/CPLEX.$VM.$SERVER| grep objective | awk '{print $5}'`
	echo $VM $CPLEXRES >> bestCPLEX.txt 
done
for i in `seq 1 20`;do
	VM=`expr $i \* 100`
	for alg in `seq 1 5`;do
	#serve per svuotare il file all'inizio della computazione
	if [ $i -eq 1 ]
	then
		> $OUTPUT/avgtime$alg.txt 
	fi

		#Estrae il tempo medio di esecuzione
		TIME=`awk '{if(NR==2)print '$VM'"\t" $3}' $OUTPUT/time$alg.$VM.$SERVER.txt`
		echo $TIME | rev |cut -c5-| rev >> avgtime$alg.txt
		for probl in `seq 0 2`;do
			awk '{print $1}' $OUTPUT/referenceFronts$VM.$SERVER.$alg/VMProblem$probl.$VM.$SERVER.rf | sort -n |awk '{if(NR==1) print '$VM'"\t"$0}' >> best$alg.$probl.txt
			if [ $i -eq 20 ]
			then
				mv best$alg.$probl.txt $RES/best$alg.$probl.txt
			fi
		done

	if [ $i -eq 20 ]
	then
		mv avgtime$alg.txt $RES/avgtime$alg.txt
	fi
	done
 	CPLEXT=`cat $OUTPUT/CPLEX.$VM.$SERVER| grep "Time spent in solve" | awk '{print $7}'`
	#Rimuove l'ultimo carattere della variabile
	CPLEXT=${CPLEXT%?}
	#Sostituisce la virgola con il punto
	CPLEXT=$(echo $CPLEXT | sed -e 's/,/./g')
	echo $VM $CPLEXT >> avgtimeCPLEX.txt
	CPLEXRES=`cat $OUTPUT/CPLEX.$VM.$SERVER| grep objective | awk '{print $5}'`
	echo $VM $CPLEXRES >> bestCPLEX.txt 
	if [ $i -eq 20 ]
	then
	mv avgtimeCPLEX.txt $RES/avgtimeCPLEX.txt
	mv bestCPLEX.txt $RES/bestCPLEX.txt 
	fi
done

