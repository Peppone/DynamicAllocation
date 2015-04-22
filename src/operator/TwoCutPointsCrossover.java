package operator;

import java.util.HashMap;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class TwoCutPointsCrossover extends Operator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TwoCutPointsCrossover(HashMap<String, Object> parameters)
			throws JMException {
		super(null);
		if (!parameters.containsKey("crossoverProbability"))
			throw new JMException("Missing crossoverProbability");
		probability = (double) parameters.get("crossoverProbability");
	}

	// private static final long serialVersionUID;;
	private double probability;

	/**
	 * Perform the crossover operation.
	 * 
	 * @param probability
	 *            Crossover probability
	 * @param parent1
	 *            The first parent
	 * @param parent2
	 *            The second parent
	 * @return An array containing the two offsprings
	 * @throws JMException
	 */
	public Solution[] doCrossover(double probability, Solution parent1,
			Solution parent2) throws JMException {
		Solution[] offSpring = new Solution[2];

		offSpring[0] = new Solution(parent1);
		offSpring[1] = new Solution(parent2);

		try {
			if (PseudoRandom.randDouble() < probability) {
				int len = offSpring[0].numberOfVariables();

				Variable[] vars1 = offSpring[0].getDecisionVariables();
				Variable[] vars2 = offSpring[1].getDecisionVariables();

				int half = len >> 1;
				int displacement = 0;
				if (len > 3)
					displacement = 1;
				int firstCrossPnt = PseudoRandom.randInt(0 + displacement,half - 1);
				int secondCrossPnt = len - firstCrossPnt;
				boolean directCopy = false;
				int bufferLength = firstCrossPnt-1;
				// Optimization: save space as much as possible copying only the
				// shortest parts
				if (secondCrossPnt - firstCrossPnt <= firstCrossPnt * 2 -2) {
					directCopy = true;
					bufferLength = secondCrossPnt - firstCrossPnt - 2;
				}
				Variable[] buffer = new Variable[bufferLength];
				// System.arraycopy( srcObj , srcPosition , dstObj , destPos ,
				// length );
				if (directCopy) {
					System.arraycopy(vars1, firstCrossPnt, buffer, 0,bufferLength);
					System.arraycopy(vars2, firstCrossPnt, vars1,firstCrossPnt, bufferLength);
					System.arraycopy(buffer, 0, vars2, firstCrossPnt,bufferLength);
				} else {
					System.arraycopy(vars1, 0, buffer, 0, bufferLength);
					System.arraycopy(vars2, 0, vars1,0,  bufferLength);
					System.arraycopy(buffer, 0, vars2, 0,bufferLength);
					System.arraycopy(vars1, secondCrossPnt+1, buffer, 0, bufferLength);
					System.arraycopy(vars2, secondCrossPnt+1, vars1,secondCrossPnt+1,  bufferLength);
					System.arraycopy(buffer, 0, vars2, secondCrossPnt+1,bufferLength);
				}

			} // if
		} // try
		catch (ClassCastException e1) {
			Configuration.logger_
					.severe("OnePointUniformCrossover.doCrossover: Cannot perfom "
							+ "OnePointUniformCrossover");
			throw new JMException(
					"Exception in OnePointUniformCrossover.doCrossover()");
		} // catch
		return offSpring;
	} // doCrossover

	/**
	 * Executes the operation
	 * 
	 * @param object
	 *            An object containing an array of two solutions
	 * @param none
	 * @return An object containing an array with the offSprings
	 * @throws JMException
	 */
	public Object execute(Object object) throws JMException {
		Solution[] parents = (Solution[]) object;

		if (parents.length < 2) {
			Configuration.logger_
					.severe("OnePointUniformCrossover.execute: operator "
							+ "needs two parents");
			throw new JMException(
					"Exception in OnePointUniformCrossover.execute()");
		} // if

		Solution[] offSpring;
		offSpring = doCrossover(probability, parents[0], parents[1]);

		for (int i = 0; i < offSpring.length; i++) {
			offSpring[i].setCrowdingDistance(0.0);
			offSpring[i].setRank(0);
		} // for
		return offSpring;
	} // execute


}
