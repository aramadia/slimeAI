package neuralnetwork.core;
import java.util.Arrays;


public class Perceptron implements Node{

	public double[] weight;
	public double error;
	double prevAct;
	
	public Perceptron(int numInputs) {
		weight = new double[numInputs];
	}
	
	public double evalute (double[] input) {
		
		double sum = 0;
		for (int i = 0; i < input.length; i++) {
			sum += weight[i] * input[i];
		}
		prevAct = actFunc(sum);
		return prevAct;
	}
	
	public void randomizeWeights() {
		for (int i = 0 ; i < weight.length; i++) {
			weight[i] = -1.0 + 2 * NeuralNetwork.r.nextDouble();
		}
	}
	
	public void trainWeights(double[] parentInput) {
		
		double dW = NeuralNetwork.learningRate * error * prevAct * (1 - prevAct);
		
		for (int i = 0; i < parentInput.length; i++) {
			weight[i] += dW * parentInput[i];
		}
	}
	
	/**
	 * Activation function
	 * @param in
	 * @return
	 */
	public final static double actFunc(double in) {
//		if (in > 0) return 1.0;
//		else return 0.0;
		
		
		return 1.0/(1.0 + Math.exp(-in));
		
	}
	
	/**
	 * Derivative of activation function
	 * @param x
	 * @return
	 */
	public final static double actDeriv(double x) {
		double t = actFunc(x);
		return t * (1-t);
	}
	
	@Override
	public String toString() {
		String s = "weights: " + Arrays.toString(weight);;
		return s;
		
	}
}
