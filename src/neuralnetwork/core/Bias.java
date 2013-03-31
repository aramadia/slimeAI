package neuralnetwork.core;

public class Bias implements Node {
	
	
	@Override
	public double evalute(double[] input) {
		return 1;
	}

	@Override
	public void randomizeWeights() {
		return;

	}

}
