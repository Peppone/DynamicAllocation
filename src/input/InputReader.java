package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputReader {
		
	
	public static State readState(File vmAllocation,
			File bwStats, File commonData){
		if (!vmAllocation.exists()){
			System.err.println("File "+vmAllocation.getName()
					+" does not exist");
			return null;
		}
		if (!bwStats.exists()){
			System.err.println("File "+bwStats.getName()+
					" does not exist");
			return null;
		}
		
		if (!commonData.exists()){
			System.err.println("File "+commonData.getName()+
					" does not exist");
			return null;
		}
		int server=readCommonData(commonData);
		double vmStats[][]=readVMStat(vmAllocation,server);
		
		//START - DEBUG
			InputReader.printMatrix(vmStats);
		//END - DEBUG
		
		return null;//new State();
	}
	private static double[][] readVMStat(File f,int server){
		/*
		 * File Format:
		 * SERVER CPU RAM DISK
		 * 
		 */
		FileReader fr;
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		BufferedReader bf = new BufferedReader(fr);
		String line;
		double occupancy[][] = new double [server][3];
		
		try {
			while((line=bf.readLine())!=null){
				String parameters [] = line.split("\\s+");
				if (parameters.length != 4){
					System.err.println("Wrong parameter size,"
							+ "file not well formatted"
							+"Expected 4, real "+parameters.length+"\n "+parameters[0]);
					return null;
				}
				int serverNum = Integer.parseInt(parameters[0]);
				for(int i=0;i<3;++i){
					occupancy[serverNum][i]+=
							Double.parseDouble(parameters[i+1]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		return occupancy;
	}
	
	private static void printMatrix(double v[][]){
		for (int i =0;i< v.length;++i){
			for (int j=0;j<v[i].length;++j){
				System.out.print(v[i][j]+" ");
			}
			System.out.println();
		}
	}
	private static int readCommonData(File f){
		int server=-1;
		FileReader fr;
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		BufferedReader br = new BufferedReader(fr);
		try {
			server = Integer.parseInt(br.readLine());
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return server;
	}
	
	public static void main (String [] args){
		File vmAllocation = new File ("/home/portaluri/workspace/"
				+ "DynamicAllocation/input/vm.dat");
		File bwStats=vmAllocation;
		File commonData = new File("/home/portaluri/workspace/"
				+ "DynamicAllocation/input/common.dat");
		InputReader.readState(vmAllocation, bwStats, commonData);
	}
}
