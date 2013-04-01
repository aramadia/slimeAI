package neuralnetwork.neuroevolution;

public interface Agent {

	public double evaluateFitness();
	/**
	 * Creates a similar agent that has been modified in some random fashion
	 * @return
	 */
	public Agent mutate(double mutationRate);

    public void save();
    public void load();
}
