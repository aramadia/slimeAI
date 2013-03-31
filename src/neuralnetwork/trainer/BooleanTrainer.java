package neuralnetwork.trainer;


import java.util.Random;

import neuralnetwork.core.NeuralNetwork;



public class BooleanTrainer implements Trainer{
	
	private int currentIteration;
	private Random r;
	
	public BooleanTrainer() {
		r = NeuralNetwork.r;
	}
	@Override
	public TestSet getNextSet() {
		TestSet set = new TestSet();
		
		final int pow = 4;
		
		int n = r.nextInt (1 << pow);
		
		double[] in = new double[pow + 1];
		for (int i = 0; i < pow; i++) {
			if ((n & (1 << i)) > 0) {
				in[i] = 1.0;
			}
			else {
				in[i] = 0.0;
			}
		}
		in[pow] = 1.0;
		
		double[] out = new double[1 << pow];
		for (int i = 0; i < 1 << pow; i++) {
			out[i] = 0.1;
		}
		out[n] = 0.9;
		
//		boolean a = r.nextBoolean();
//		boolean b = r.nextBoolean();
//		boolean c = r.nextBoolean();
//		boolean d = true;
//		
//		boolean y1 = a && b ^ c;
//		boolean y2 = a || b && (b ^ !c);
//		boolean y3 = (a || (b && (c ^ a)));
//		boolean y4 = a ^ b ^ c;
//		boolean y5 = !b;
//		boolean y6 = true;
//		
//		double[] in =  {b2d(a), b2d(b), b2d(c), b2d(d) };
//		double[] out = {b2dSoft(y1),b2dSoft(y2), b2dSoft(y3),
//				b2dSoft(y4), b2dSoft(y5), b2dSoft(y6)};
		
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

