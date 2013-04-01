package neuralnetwork.core;

public class Bias implements Node {
	
	
	@Override
	public double evalute(double[] input) {
		return 1.0;
	}

	@Override
	public void randomizeWeights() {
		return;

	}

	@Override
	public int numWeights() {
		return 0;
	}

}
