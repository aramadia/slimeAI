package neuralnetwork.trainer;
import java.util.Random;

import core.NeuralNetwork;



public class NANDTrainer implements Trainer{
	
	private int currentIteration;
	private Random r;
	
	public NANDTrainer() {
		r = NeuralNetwork.r;
	}
	@Override
	public TestSet getNextSet() {
		TestSet set = new TestSet();
		
		boolean a = r.nextBoolean();
		boolean b = r.nextBoolean();
		boolean c = true;
		
		boolean y = !(a && b);
		
		double[] in =  {b2d(a), b2d(b), b2d(c) };
		double[] out = {b2dSoft(y)};
		
		set.input = in;
		set.output = out;
		
		return set;
		
	}
	
	private static double b2d(boolean b) {
		if (b) return 1.0;
		return 0.0;
	}
	
	private static double b2dSoft(boolean b) {
		if (b) return 0.9;
		return 0.1;
	}
}
