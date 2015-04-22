package input;

public class State {
	int allocatedVM;
	int server;
	int torSwitch;
	int aggSwitch;
	int rackPerPod;
	int servPerRack;
	double [][] servResource;
	int objectives=	3; // CPU,DISK,RAM
	int CPU = 0;
	int RAM = 1;
	int DISK = 2;
	double servBW[];
	double rackBW[];
	
	public State(int vm ,int serv, int spr, int rpp){
		allocatedVM = vm;
		server = serv;
		servPerRack=spr;
		rackPerPod = rpp;
		torSwitch = server / servPerRack;
		if (server % servPerRack !=0){
			torSwitch++;
		}
		aggSwitch= torSwitch/rackPerPod;
		if (torSwitch % rackPerPod!=0){
			aggSwitch++;
		}
		//START - DEBUG
		System.out.println("La topologia correntemente definita è:");
		System.out.println("VM "+this.allocatedVM
				+"\nServer "+server+"\nRack "+torSwitch+
				"\nPod "+aggSwitch);
		//END - DEBUG
		servResource=new double [server][objectives];
		servBW = new double [server];
		rackBW  = new double [torSwitch];
		
	}
	public double getCpuOccupiedPercentage(int server){
		return servResource[server][CPU];
	}
	public double getRamOccupiedPercentage(int server){
		return servResource[server][RAM];
	}
	
	public double getDiskOccupiedPercentage(int server){
		return servResource[server][DISK];
	}
	public double getServerBWOccupancy(int server){
		return servBW[server];
	}
	public double getRackBWOccupancy(int rack){
		return rackBW[rack];
	}
}

