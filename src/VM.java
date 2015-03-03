
public class VM {

		private double cpu;
		private double memory;
		private double disk;
		private double bandwidth;
		private double time;

		public VM(double time, double cpu, double mem, double disk, double band) {
			this.time=time;
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
		public double getTime(){
			return time;
		}

}
