
public class VM {

		private double cpu;
		private double memory;
		private double disk;
		private double bandwidth;
		private double minExecTime;

		public VM(double time, double cpu, double mem, double disk, double band) {
			minExecTime=time;
			this.cpu=cpu;
			memory=mem;
			this.disk=disk;
			bandwidth = band;

		}

		public double getCpu(){
			return cpu;
		}
		public double getMemory(){
			return memory;
		}
		public double getDisk(){
			return disk;
		}
		public double getBandwidth() {
			return bandwidth;
		}
		public double getMinimumExecutionTime(){
			return minExecTime;
		}

}
