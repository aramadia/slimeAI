package neuralnetwork.neuroevolution;

import java.util.concurrent.Callable;


public interface Agent extends Callable<Double>{

	public double evaluateFitness();
	/**
	 * Creates a similar agent that has been modified in some random fashion
	 * @return
	 */
	public Agent mutate(double mutationRate);
	public neuralnetwork.core.NeuralNetwork getNN();
	public double getFitness();
	public void setWeights(double[] weights);
    public void save();
    public void load();
    
}
