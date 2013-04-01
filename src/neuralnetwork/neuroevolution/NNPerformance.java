package neuralnetwork.neuroevolution;

import neuralnetwork.core.NeuralNetwork;
import neuralnetwork.core.Perceptron;

public class NNPerformance {

	public static void main(String[] args) {
		
		SlimeAgent agent = new SlimeAgent();
		agent.load();
		
		int numIterations = 1000000;
		long timeStart = System.currentTimeMillis();
		
		double sumOutput = 0.0;
		
		double maxError = 0.0;
		double maxRelativeError = 0.0;
		for (double x = -12.0; x <= 12.0; x += 0.017) {
			double real = Perceptron.actFunc(x);
			double fast = Perceptron.fast_sigmoid(x);
			
			double error = Math.abs(fast-real);
			
			double relError = Math.abs(1.0 - fast/real);
			maxError = Math.max(error,maxError);
			maxRelativeError = Math.max(maxRelativeError, relError);
			if (maxRelativeError > 0.8) {
				int a = 3;
			}
			System.out.println(x + " " + Math.abs(fast-real) + " Real: " + real + " Fast: " + fast);
		}
		System.out.println("max abs error" + maxError);
		System.out.println("max rel error" + maxRelativeError);
		
		for (int i =0 ; i < numIterations; i++)
		{
		
			double[] inputs = new double[12];
			
	        double[] outputs = agent.nn.process(inputs);
	        
	        sumOutput += outputs[0];
		}
		long timeEnd = System.currentTimeMillis();
		
		System.out.println("Ran " + numIterations + " in " + (timeEnd - timeStart) + "ms");
		System.out.println(sumOutput);
	}
}
