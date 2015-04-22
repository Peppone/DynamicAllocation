package input;

public class State {
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
	
	public State(int serv, int spr, int rpp){
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
		System.out.println("\nServer "+server+"\nRack "+torSwitch+
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
	public State setServResource(double[][] res){
		if(res.length!= server){
			System.err.println("Serv Res Error: server number ("+server
					+ ") different from what expected("+res.length+")");
			return null;
		}
		servResource=res;
		return this;
		
	}
	public State setServBW(double [] bw){
		if(bw.length!= server){
			System.err.println("Serv BW Error: server number ("+server
					+ ") different from what expected("+bw.length+")");
			return null;
		}
		servBW=bw;
		return this;
	}
	
	public State setRackBW(double [] bw){
		if(bw.length!= torSwitch){
			System.err.println("Rack BW Error: server number ("+torSwitch
					+ ") different from what expected("+bw.length+")");
			return null;
		}
		rackBW=bw;
		return this;
	}
}

