import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.nsgaII.NSGAII_main;
import jmetal.operators.mutation.MyRebalanceMutation;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;
import operator.TwoCutPointsCrossover;


public class Main extends NSGAII_main {
	
	private static int popSizeFactor=1;
	
	public static Algorithm setup(Problem problem) {
		Algorithm algorithm;
		algorithm = new NSGAII(problem);
		algorithm.setInputParameter("populationSize",100*popSizeFactor);
		algorithm.setInputParameter("maxEvaluations", 10000*problem.getNumberOfVariables()/*25000*/);
		return algorithm;
	}
		
	public static double[] readVector(String filename) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(new File(filename)));
		StringTokenizer tokenizer= new StringTokenizer(br.readLine());
		int limit =tokenizer.countTokens();
		double result[]= new double[limit];
		for(int i=0;i<limit;++i){
			result[i]=Double.parseDouble(tokenizer.nextToken());
		}
		br.close();
		return result;
	}
	
	public static ArrayList <VM> createVMList(double[] time, double[] cpu, double[] mem, double[] disk, double[] bandwidth){
		//if(cpu.length!=bandwidth.length)return null;
		int length=cpu.length;
		ArrayList <VM> result=new ArrayList<VM>();
		VM vm;
		for(int i =0;i<length;++i){
			vm=new VM(time[i], cpu[i], mem[i], disk[i],bandwidth[i]);
			result.add(vm);
		}
		return result;
	}
	
	public static NonDominatedSolutionList compact(SolutionSet population) throws JMException{
		NonDominatedSolutionList ndl = new NonDominatedSolutionList();
		HashSet<String> solutions = new HashSet<String>();		
		for (int i = 0; i < population.size(); ++i) {
			Solution s = population.get(i);
			Variable [] dv= s.getDecisionVariables();
			String temp="";
			for(int j=0; j< dv.length; ++j){
				temp+=(int)dv[j].getValue()+";";
			}
			if (!solutions.contains(temp)) {

				solutions.add(temp);
				ndl.add(s);
			}
		}
		return ndl;
	}

	public static void main(String[] args) throws SecurityException, IOException, JMException, ClassNotFoundException {
		if(args.length<11){
			System.err.println("Usage: program_name vm_num serv_num servOnRack rackOnPod timefile cpufile memfile diskfile bwfile experimentType outputPath");
			return;
		}
		Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		
		
		int task=Integer.parseInt(args[0]);
		int server=Integer.parseInt(args[1]);
		int serverPerRack=Integer.parseInt(args[2]);
		int rackPerPod=Integer.parseInt(args[3]);
		int type=Integer.parseInt(args[9]);
		String outputPath=args[10];
		if(outputPath.lastIndexOf('/')<outputPath.length()-1){
			outputPath+="/";
		}
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler(outputPath+"VMProblem.log");
		logger_.addHandler(fileHandler_);
		parameters.put("serverNumber", server);
		double[] time = readVector(args[4]);
		double[] cpu=readVector(args[5]);
		double[] mem=readVector(args[6]);
		double[] disk=readVector(args[7]);
		double [] bw= readVector(args[8]);
		ArrayList <VM> vm=createVMList(time, cpu, mem, disk,bw);
		problem = new VMProblem(task,server,serverPerRack,rackPerPod, vm, type);
		algorithm = Main.setup(problem);
		popSizeFactor=(int)Math.pow(10,type);
		parameters.put("crossoverProbability", 0.9);
		//crossover = new UniformCrossover(parameters);
		crossover= new TwoCutPointsCrossover (parameters);
		parameters.put("mutationProbability",
				1.0 / problem.getNumberOfVariables());
		mutation = new MyRebalanceMutation(parameters);

		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament",
				parameters);

		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		long initTime = System.currentTimeMillis();
		SolutionSet population = algorithm.execute();
		long estimatedTime = System.currentTimeMillis() - initTime;
		logger_.info("Total execution time: " + estimatedTime + "ms");
		population.printObjectivesToFile(outputPath+"FUNaaa");
		NonDominatedSolutionList ndl = compact(population);
		ndl.printFeasibleVAR(outputPath+"VAR");
		logger_.info("Variables values have been written to file VAR");
		ndl.printFeasibleFUN(outputPath+"FUN");
		logger_.info("Objectives values have been written to file FUN");
		System.out.println("popolazione "+100*popSizeFactor+", exectime "+estimatedTime/1000.0);

		
	}

}

