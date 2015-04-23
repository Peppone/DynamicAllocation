package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputReader {
		
	
	public static State readState(File vmAllocation,
			File servBWAllocation, File rackBWAllocation, int server,int serverPerRack, int rackPerPod){
		if(vmAllocation==null){
			return new EmptyState(server,serverPerRack,rackPerPod);
		}
		if (!vmAllocation.exists()){
			System.err.println("File "+vmAllocation.getName()
					+" does not exist");
			return null;
		}
		if (!servBWAllocation.exists()){
			System.err.println("File "+servBWAllocation.getName()+
					" does not exist");
			return null;
		}
		
		if (!rackBWAllocation.exists()){
			System.err.println("File "+rackBWAllocation.getName()+
					" does not exist");
			return null;
		}
		
		double vmStats[][]=readVMStat(vmAllocation,server);
		int rack = server % serverPerRack == 0 ? server / serverPerRack
				: server / serverPerRack + 1;
		double bwStats[][] = readBWStat(servBWAllocation, rackBWAllocation,
				server, rack);
		//START - DEBUG
			InputReader.printMatrix(vmStats);
		//END - DEBUG
		State state= new State(server, serverPerRack, rackPerPod);
		state.setServResource(vmStats);
		state.setServBW(bwStats[0]);
		state.setRackBW(bwStats[1]);
		return state;
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
					System.err.println("Wrong VM parameter size,"
							+ "file not well formatted\n"
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
	
	private static double[][] readBWStat(File serv, File rack, int server,
			int tor) {
		double [][] bw = new double [2][];
		bw[0]=getBW(serv,server);
		bw[1]=getBW(rack,tor);
		//START - DEBUG
		
		
		
		//END - DEBUG
		return bw;
	}
	
	private static double[] getBW(File f, int param) {
		double result[] = new double[param];
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(f));
			String line;
			while ((line = bf.readLine()) != null) {
				String parameters[] = line.split("\\s+");
				if (parameters.length != 2) {
					System.err.println("Wrong BW parameter size,"
							+ "file not well formatted" + "\nExpected 2, real "
							+ parameters.length + "\n " + parameters[0]);
					return null;
				}
				int actualServer = Integer.parseInt(parameters[0]);
				result[actualServer] += Double.parseDouble(parameters[1]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = null;
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = null;
			}
		}

		return result;

	}
	
	private static void printMatrix(double v[][]){
		for (int i =0;i< v.length;++i){
			for (int j=0;j<v[i].length;++j){
				System.out.print(v[i][j]+" ");
			}
			System.out.println();
		}
	}
}
