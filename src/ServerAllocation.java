import java.util.ArrayList;
import java.util.Random;


public class ServerAllocation {

	private ArrayList<Double> allocation;
	private ArrayList<Double> virtualCpu;
	private ArrayList<Double> memory;
	private ArrayList<Double> disk;
	
	//Required resources
 	private double totalCpuReq;
 	private double totalMemReq;
 	private double totalDiskReq;
 	
 	//Available resources
	private double serverCpu;
	private double serverMemory;
	private double serverDisk;
	
	//Constraints
	private double cpuConstraint;
	private double memoryConstraint;
	private double diskConstraint;
	private boolean constraintAvailable;
	
	public ServerAllocation(double availableCpu,double availableMem,double availableDisk) {
		allocation=new ArrayList<Double>();
		virtualCpu=new ArrayList<Double>();
		memory=new ArrayList<Double>();
		disk=new ArrayList<Double>();
		totalCpuReq=0;
		totalMemReq=0;
		totalDiskReq=0;
		
		serverCpu=availableCpu;
		serverMemory=availableMem;
		serverDisk=availableDisk;
		
		cpuConstraint=0;
		memoryConstraint=0;
		diskConstraint=0;
		constraintAvailable=false;
		
	}
	
	public void addTask(double virtualTime, double cpu, double ram, double disk){
		allocation.add(virtualTime/cpu);
		virtualCpu.add(cpu);
		memory.add(ram);
		this.disk.add(disk);
		totalCpuReq+=cpu;
		totalMemReq+=ram;
		totalDiskReq+=disk;
	}
	
	public void removeTask(int index){
		allocation.remove(index);
		totalCpuReq-=virtualCpu.get(index);
		totalMemReq-=memory.get(index);
		totalDiskReq-=disk.get(index);
		virtualCpu.remove(index);
		
	}

	private void sort(){
		quickSort(0,allocation.size()-1);
	}
	
	private void quickSort(int lower, int higher){
		int i = lower;
        int j = higher;
        Random r=new Random();
        // calculate pivot number, I am taking pivot as middle index number
        if (i==j || i>j)return;
      
        		int indexPivot=r.nextInt(higher-lower)+lower;
        		double allocationPivot=allocation.get(indexPivot);
       
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (allocation.get(i) < allocationPivot) {
                i++;
            }
            while (allocation.get(j)  > allocationPivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lower < j)
            quickSort(lower, j);
        if (i < higher)
            quickSort(i, higher);
	}
	
	private void exchangeNumbers(int i, int j){
		Double temp=allocation.get(i);
		allocation.set(i, allocation.get(j));
		allocation.set(j,temp);
		temp=virtualCpu.get(i);
		virtualCpu.set(i, virtualCpu.get(j));
		virtualCpu.set(j, temp);	
	}
	public void reset(){
		allocation=new ArrayList<Double>();
		virtualCpu=new ArrayList<Double>();
		memory=new ArrayList<Double>();
		disk=new ArrayList<Double>();
		totalCpuReq=0;
		totalMemReq=0;
		totalDiskReq=0;
		cpuConstraint=0;
		memoryConstraint=0;
		diskConstraint=0;
		constraintAvailable=false;
	}
	
	
	public double executionTime(){
		sort();
		/* Viene ordinato l'array dei tempi reali (tempo virtuale / cpu virtuale)
		 * subito dopo viene verificato quale task richiede più tempo e quello è il massimo
		 * tempo di esecuzione del server.
		 * Se il totale di cpu richiesto è maggiore di uno, bisogna vedere fin quando lo rimane
		 * e rimuovere via via i task che finiscono per liberare la cpu
		 */
		double offset=0;
		double percentageCpuReq=totalCpuReq/serverCpu;
		if(totalMemReq> serverMemory){
			memoryConstraint=totalMemReq-serverMemory;
		}
		if(totalDiskReq> serverDisk){
			memoryConstraint=totalDiskReq-serverDisk;
		}
		
		
		while(percentageCpuReq>1 && allocation.size()>0){
			Double minTime=allocation.remove(0);
			minTime-=offset;
			offset+=minTime/totalCpuReq;
			totalCpuReq-=virtualCpu.remove(0);
			percentageCpuReq=totalCpuReq/serverCpu;
		}
		cpuConstraint=offset;
		double maxTime=0;
		if(allocation.size()>0){
			maxTime=(allocation.get(allocation.size()-1)-offset)*virtualCpu.get(virtualCpu.size()-1);
		}
		maxTime+=offset;
		constraintAvailable=true;
		return maxTime;
	}
	
	public Double getCpuConstraint(){
		if(constraintAvailable)return cpuConstraint;
		else return null;	
	}
	
	public Double getMemConstraint(){
		if(constraintAvailable)return memoryConstraint;
		else return null;	
	}
	
	public Double getDiskConstraint(){
		if(constraintAvailable)return diskConstraint;
		else return null;	
	}
	
	public double getCpuRequest(){
		return totalCpuReq;
	}

}
