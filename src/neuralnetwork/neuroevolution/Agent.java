package neuralnetwork.neuroevolution;

import java.util.concurrent.Callable;


public interface Agent extends Callable<Double>{

	/**
	 * Evaluate the fitness numerically, higher is better.
	 * The fitness should be independent of the precision
	 * @param precision In case of nondeterministic fitness evaluations, 
	 * precision is how accurately we determine the fitness
	 * @return
	 */
	public double evaluateFitness(int precision);
	/**
	 * Creates a similar agent that has been modified in some random fashion
	 * @return
	 */
	public Agent mutate(double mutationRate);

    public void save();
    public void load();
    
}
