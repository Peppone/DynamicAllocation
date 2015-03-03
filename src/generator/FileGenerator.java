package generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileGenerator {
	
	public static void fileGeneration(){
		//fileGeneration(10,4,20,40,1e6,20e6, 0.1,0.4, 20e3,200e3,500e9, 1e9);
		fileGeneration(10,1,100,1,20, 10,80, 10,40,10, 40);
	}
	
	public static void fileGeneration (int vm, double mintime, double maxtime, double minbw, double maxbw,
			double mincpu, double maxcpu, double minmem, double maxmem, double mindisk, double maxdisk){
			genFile(vm,mintime,maxtime, "time");
			genFile(vm,minbw,maxbw,"bw");
			genFile(vm,minmem,maxmem,"mem");
			genFile(vm,mindisk,maxdisk,"disk");
			genFile(vm,mincpu,maxcpu,"cpu");
			return;
	}
	
	
	private static void genFile(int vm, double min, double max, String name) {
		Random r = new Random();
		File fileCPLEX = new File(name+".dat");
		File fileJMETAL= new File(name+".txt");
		try {
			if (fileCPLEX.exists()) {
				fileCPLEX.delete();
			}
			if(fileJMETAL.exists()){
				fileJMETAL.delete();
			}
			FileWriter fwC = new FileWriter(fileCPLEX);
			FileWriter fwJ = new FileWriter(fileJMETAL);
			fwC.append(name+"=[ ");
			for (int i = 0; i < vm; ++i) {

				double number = r.nextDouble() * (max - min) + min;
				// number=Math.floor(number);
				Double num = new Double(number);
				char[] buffer = num.toString().toCharArray();
				for (int j = 0; j < buffer.length; ++j) {
					fwC.append(buffer[j]);
					fwJ.append(buffer[j]);
				}
				fwJ.append(" ");
				fwC.append(" ");
			}
			fwC.append(" ];");
			fwJ.close();
			fwC.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	public static void main(String args[]){
		fileGeneration();
	}
}
