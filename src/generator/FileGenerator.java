package generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileGenerator {
	
	public static void fileGeneration(){
		//fileGeneration(10,4,20,40,1e6,20e6, 0.1,0.4, 20e3,200e3,500e9, 1e9);
		fileGeneration(10,1,1,1,1, 0.1,0.1, 4e9,6e9,0, 0);
	}
	
	public static void fileGeneration (int vm, double mintime, double maxtime, double minbw, double maxbw,
			double mincpu, double maxcpu, double minmem, double maxmem, double mindisk, double maxdisk){
			genFile(vm,mintime,maxtime, "time.dat");
			genFile(vm,minbw,maxbw,"bw.dat");
			genFile(vm,minmem,maxmem,"mem.dat");
			genFile(vm,mindisk,maxdisk,"disk.dat");
			genFile(vm,mincpu,maxcpu,"cpu.dat");
			return;
	}
	
	
	private static void genFile(int vm, double min, double max, String name) {
		Random r = new Random();
		File file = new File(name);
		try {
			if (file.exists()) {
				file.delete();
			}
			FileWriter fw = new FileWriter(file);
			for (int i = 0; i < vm; ++i) {

				double number = r.nextDouble() * (max - min) + min;
				// number=Math.floor(number);
				Double num = new Double(number);
				char[] buffer = num.toString().toCharArray();
				for (int j = 0; j < buffer.length; ++j) {
					fw.append(buffer[j]);
				}
				fw.append(" ");
			}
			fw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	public static void main(String args[]){
		fileGeneration();
	}
}
