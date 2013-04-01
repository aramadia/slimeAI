package neuralnetwork.neuroevolution;

import java.util.concurrent.Callable;


public interface Agent extends Callable<Double>{

	public double evaluateFitness();
	/**
	 * Creates a similar agent that has been modified in some random fashion
	 * @return
	 */
	public Agent mutate(double mutationRate);

    public void save();
    public void load();
    
}
