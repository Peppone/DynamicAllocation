import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.mutation.MyRebalanceMutation;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import operator.TwoCutPointsCrossover;


public class JVMSettings extends Settings {
	String algName;
	
	
	
	public JVMSettings(String alg,Problem p){
		super(alg);
		algName=alg;
		problem_=p;
		
	}
	
	
	@Override
	public Algorithm configure() throws JMException {
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		algorithm = new NSGAII (problem_) ;
		parameters.put("mutationProbability",
				1.0 / problem_.getNumberOfVariables());
	
		parameters.put("serverNumber",((VMProblem)problem_).SERV_NUM);

		parameters.put("crossoverProbability", 0.9);
		int populationSize=100;
		int evaluations=25000;
		if(algName.compareTo("NSGA100")==0){
			populationSize=100;
		}else if(algName.compareTo("NSGA1000")==0){
			populationSize=1000;
		}else if(algName.compareTo("NSGAFIT")==0){
			evaluations=25000;
		}else if(algName.compareTo("NSGAVIT")==0){
			evaluations=10000*problem_.getNumberOfVariables();
		}		
		else if(algName.compareTo("NSGA09")==0){
			parameters.put("crossoverProbability", 0.9);
			
		}else if(algName.compareTo("NSGA095")==0){
			parameters.put("crossoverProbability", 0.95);
		}
		else if(algName.compareTo("NSGA05")==0){
			parameters.put("crossoverProbability", 0.5);
		}
		mutation = new MyRebalanceMutation(parameters);
		algorithm.addOperator("mutation", mutation);
		algorithm.setInputParameter("populationSize",populationSize);
		algorithm.setInputParameter("maxEvaluations", evaluations);
		crossover= new TwoCutPointsCrossover (parameters);
		algorithm.addOperator("crossover", crossover);
		selection = SelectionFactory.getSelectionOperator("BinaryTournament",
				parameters);
		algorithm.addOperator("selection", selection);
		return algorithm;
	}

}
