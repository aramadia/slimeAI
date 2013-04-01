package neuralnetwork.neuroevolution;

public interface Agent {

	public double evaluteFitness();
	/**
	 * Creates a similar agent that has been modified in some random fashion
	 * @return
	 */
	public Agent mutate(double mutationRate);
}
