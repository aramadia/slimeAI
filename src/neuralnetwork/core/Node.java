package neuralnetwork.core;

public interface Node {
	
	public double evalute (double[] input);
	
	public void randomizeWeights() ;
	public int numWeights();
	
}
