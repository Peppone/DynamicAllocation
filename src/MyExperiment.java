import input.EmptyState;
import input.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.experiments.Experiment;
import jmetal.experiments.util.Friedman;
import jmetal.util.Configuration;
import jmetal.util.JMException;



public class MyExperiment extends Experiment{
//Problem problem;
double[] time;
double[] cpu;
double[] mem;
double[] disk;
double [] bw;
ArrayList<VM> vm;
int task;
int server;
int serverPerRack;
int rackPerPod;
public void problemSetup(String args[]) throws IOException{
	
	task=Integer.parseInt(args[0]);
	Double tempserver=Double.parseDouble(args[1]);
	server=(int)Math.round(tempserver);
	serverPerRack=Integer.parseInt(args[2]);
	rackPerPod=Integer.parseInt(args[3]);
	time = Main.readVector(args[4]);
	cpu= Main.readVector(args[5]);
	mem= Main.readVector(args[6]);
	disk= Main.readVector(args[7]);
	bw=  Main.readVector(args[8]);
	vm= Main.createVMList(time, cpu, mem, disk,bw);
	//problem = new VMProblem(task,server,serverPerRack,rackPerPod, vm, type);
	
	//return problem;
}
	@Override
	public void algorithmSettings(String arg0, int arg1, Algorithm[] arg2)
			throws ClassNotFoundException {
		for(int i=0;i<arg2.length;++i){
		
			Problem problem;
			State state = new EmptyState(task,server,serverPerRack,rackPerPod);
			try {
				if(arg0.startsWith("VMProblem0")){
					problem = new VMProblem(task,server,serverPerRack,rackPerPod, vm, 0,state);
				}else if(arg0.startsWith("VMProblem1")){
						problem = new VMProblem(task,server,serverPerRack,rackPerPod, vm, 1,state);
				}else if(arg0.startsWith("VMProblem2")){
					problem = new VMProblem(task,server,serverPerRack,rackPerPod, vm, 2,state);
				}else{
					problem=null;
				}
				
				JVMSettings settings = new JVMSettings(arg0,problem);;
				arg2[i]=settings.configure();
				} catch (JMException e) {
					e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) throws JMException, IOException{
		//TODO Actually timeFile is useless
		
		if(args.length<10){
			System.err.println("Usage: program_name vm_num serv_num servOnRack rackOnPod timefile cpufile memfile diskfile bwfile numberOfExperiments");
			System.err.println("Number of args passed "+args.length);
			return;
		}
		int numberOfIndipendentRuns=Integer.parseInt(args[9]);
		MyExperiment me = new MyExperiment();
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		int task=Integer.parseInt(args[0]);
		int server=Integer.parseInt(args[1]);
		//int serverPerRack=Integer.parseInt(args[2]);
		//int rackPerPod=Integer.parseInt(args[3]);
		me.experimentName_="VMProblem"+task+"."+server;
		me.experimentBaseDirectory_="/home/peppone/workspace/JMetalVM/output";
		me.algorithmNameList_= new String[]{"NSGA100","NSGA1000","NSGAVIT", "NSGA09", "NSGA095","NSGA05"};
		me.problemList_=new String [3];
		for(int i=0;i<me.problemList_.length;++i){
			me.problemList_[i]="VMProblem"+i+"."+task+"."+server;
		}
		me.paretoFrontDirectory_="/home/peppone/workspace/JMetalVM/output";
		me.paretoFrontFile_=new String[]{"FUN0","FUN1","FUN2"};
		me.indicatorList_=new String[]{"HV" , "SPREAD" , "IGD" , "EPSILON"};
		me.problemSetup(args);
		//JVMSettings []settings=new JVMSettings[me.algorithmNameList_.length];
		//Logger logger_ = Configuration.logger_;
		//FileHandler fileHandler_ = new FileHandler(outputPath+"VMProblem.log");
		//logger_.addHandler(fileHandler_);
		parameters.put("serverNumber", server);
		//me.algorithmSettings_=settings;
		me.independentRuns_=numberOfIndipendentRuns;
		me.initExperiment();
		int numberOfThreads=3;
		long initTime = System.nanoTime();
		me.runExperiment(numberOfThreads);
		long finishTime=System.nanoTime()-initTime;
		Logger logger_ = Configuration.logger_;
		FileHandler fileHandler_ = new FileHandler("VMProblem"+task+"."+server);
		logger_.info("Total execution time: " + (double)finishTime/1e9 + "s");
		logger_.info("Avg execution time per instance " + (double)finishTime/me.independentRuns_*1e9 + "s");
		
		me.generateQualityIndicators();
		me.generateLatexTables();
		int rows;
		int columns;
		String prefix;
		String [] problems;
		rows=3;
		columns=3;
		prefix = new String("Problems");
		problems= me.problemList_;
		me.generateRBoxplotScripts(rows, columns, problems, prefix, true, me);
		me.generateRWilcoxonScripts(problems, prefix, me);
		Friedman test= new Friedman(me);
		test.executeTest("EPSILON");
		test.executeTest("HV");
		test.executeTest("SPREAD");
		
		
	}

}
