import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.experiments.Experiment;
import jmetal.experiments.util.Friedman;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;



public class MyExperiment extends Experiment{
Problem problem;
public Problem problemSetup(String args[]) throws IOException{
	
	int task=Integer.parseInt(args[0]);
	int server=Integer.parseInt(args[1]);
	int serverPerRack=Integer.parseInt(args[2]);
	int rackPerPod=Integer.parseInt(args[3]);
	int type=Integer.parseInt(args[9]);
	String outputPath=args[10];
	if(outputPath.lastIndexOf('/')<outputPath.length()-1){
		outputPath+="/";
	}
	double[] time = Main.readVector(args[4]);
	double[] cpu= Main.readVector(args[5]);
	double[] mem= Main.readVector(args[6]);
	double[] disk= Main.readVector(args[7]);
	double [] bw=  Main.readVector(args[8]);
	ArrayList <VM> vm= Main.createVMList(time, cpu, mem, disk,bw);
	problem = new VMProblem(task,server,serverPerRack,rackPerPod, vm, type);
	
	return problem;
}
	@Override
	public void algorithmSettings(String arg0, int arg1, Algorithm[] arg2)
			throws ClassNotFoundException {
		for(int i=0;i<arg2.length;++i){
			try {
				JVMSettings settings = new JVMSettings(arg0,problem);;
				arg2[i]=settings.configure();
			} catch (JMException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) throws JMException, IOException{
		if(args.length<12){
			System.err.println("Usage: program_name vm_num serv_num servOnRack rackOnPod timefile cpufile memfile diskfile bwfile experimentType outputPath numberOfExperimetns");
			return;
		}
		int numberOfIndipendentRuns=Integer.parseInt(args[11]);
		MyExperiment me = new MyExperiment();
		Problem problem=me.problemSetup(args);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		int task=Integer.parseInt(args[0]);
		int server=Integer.parseInt(args[1]);
		int serverPerRack=Integer.parseInt(args[2]);
		int rackPerPod=Integer.parseInt(args[3]);
		int type=Integer.parseInt(args[9]);
		String outputPath=args[10];
		me.experimentName_="VMProblem"+task+";"+server;
		me.experimentBaseDirectory_="/home/peppone/workspace/JMetalVM/output";
		me.algorithmNameList_= new String[]{"NSGA100","NSGA1000","NSGAVIT", "NSGA09", "NSGA095","NSGA05"};
		me.problemList_=new String []{"VMProblem"};
		me.paretoFrontDirectory_="/home/peppone/workspace/JMetalVM/output";
		me.paretoFrontFile_=new String[]{"FUN"};
		me.indicatorList_=new String[]{"HV" , "SPREAD" , "IGD" , "EPSILON"};
		JVMSettings []settings=new JVMSettings[me.algorithmNameList_.length];
		for(int i=0;i<me.algorithmNameList_.length;++i){
			settings[i]=new JVMSettings(me.algorithmNameList_[i],problem);
		}
		//Logger logger_ = Configuration.logger_;
		//FileHandler fileHandler_ = new FileHandler(outputPath+"VMProblem.log");
		//logger_.addHandler(fileHandler_);
		parameters.put("serverNumber", server);
		me.algorithmSettings_=settings;
		me.independentRuns_=numberOfIndipendentRuns;
		me.initExperiment();
		int numberOfThreads=1;
		
		me.runExperiment(numberOfThreads);
		me.generateQualityIndicators();
		me.generateLatexTables();
		
		int rows;
		int columns;
		
		String prefix;
		String [] problems;
		rows=2;
		columns=3;
		prefix = new String("Problems");
		problems= new String []{"VMProblem"};
		me.generateRBoxplotScripts(rows, columns, problems, prefix, true, me);
		
		me.generateRWilcoxonScripts(problems, prefix, me);
		
		Friedman test= new Friedman(me);
		test.executeTest("EPSILON");
		test.executeTest("HV");
		test.executeTest("SPREAD");
		
		
	}

}
