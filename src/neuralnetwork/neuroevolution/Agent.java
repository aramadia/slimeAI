package neuralnetwork.neuroevolution;

public abstract class Agent {

	public abstract double evaluteFitness();
	/**
	 * Creates a similar agent that has been modified in some random fashion
	 * @return
	 */
	public abstract Agent mutate(double mutationRate);
}
