package neuralnetwork.core;
import java.util.Arrays;


public class Perceptron implements Node{

	public double[] weight;
	public double error;
	double prevAct;
	
	private static double [] sigtab = new double[401];  // values of f(x) for x values 

	
	static {
	  for(int i=0; i<401; i++) {
	      double ifloat = (i/20.0) - 10.0;
	      sigtab[i] = 1.0/(1.0 + Math.exp(-ifloat));
	  }
	}

	public final static double fast_sigmoid (double x) {
	    if (x <= -10)
	        return 0.0;
	    else if (x >= 10)
	        return 1.0;
	    else {
	        double normx = (x + 10) * 20;
	        
	        int i = (int)normx;
	        return sigtab[i];
	      
	    }
	}
	
	public Perceptron(int numInputs) {
		weight = new double[numInputs];
	}
	
	public double evalute (double[] input) {
		
		double sum = 0;
		for (int i = 0; i < input.length; i++) {
			sum += weight[i] * input[i];
		}
		prevAct = fast_sigmoid(sum);
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
		double t = fast_sigmoid(x);
		return t * (1-t);
	}
	
	@Override
	public String toString() {
		String s = "weights: " + Arrays.toString(weight);;
		return s;
		
	}
}
