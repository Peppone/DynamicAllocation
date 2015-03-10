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

	
	
	
	public JVMSettings(String problem,Problem p){
		super(problem);
		problem_=p;
		
	}
	
	
	@Override
	public Algorithm configure() throws JMException {
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		algorithm = new NSGAII ( problem_) ;
		parameters.put("crossoverProbability", 0.9);
		//crossover = new UniformCrossover(parameters);
		crossover= new TwoCutPointsCrossover (parameters);
		parameters.put("mutationProbability",
				1.0 / problem_.getNumberOfVariables());
		parameters.put("serverNumber",((VMProblem)problem_).SERV_NUM);
		mutation = new MyRebalanceMutation(parameters);

		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament",
				parameters);

		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);
		algorithm.setInputParameter("populationSize",100);
		algorithm.setInputParameter("maxEvaluations", 10000*problem_.getNumberOfVariables()/*25000*/);
		return algorithm;
	}

}
