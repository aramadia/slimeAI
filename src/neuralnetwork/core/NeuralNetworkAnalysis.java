package neuralnetwork.core;

import neuralnetwork.trainer.BooleanTrainer;
import neuralnetwork.trainer.Trainer;

public class NeuralNetworkAnalysis extends Thread {
	
	final int numHiddenNodes;
	double finalResult = -1.0;
	
	

	public NeuralNetworkAnalysis(int numHiddenNodes) {
		super();
		this.numHiddenNodes = numHiddenNodes;
	}



	@Override
	public void run() {
		System.out.println("Num Hidden Nodes: " + numHiddenNodes);
		int numSuccess = 0;
		final int numTrials = 10;
		
		for (int iteration = 0; iteration < numTrials; iteration++) {
			NeuralNetwork nn = new NeuralNetwork(3, numHiddenNodes, 6);
			
			nn.randomizeWeights();		
			
			Trainer trainer;
			trainer = new BooleanTrainer();
			
			for (int i = 0; i < 10; i++) {
				System.out.println(trainer.getNextSet());
			}
			
			nn.learn(trainer);
			boolean success = nn.test(trainer);
			if (success) numSuccess++;
		}
		
		double successRate = ((double)numSuccess)/numTrials;
		System.out.println("Success Rate: " + successRate);
		finalResult = successRate;

	}
	
	/**
	 * Success rates 
	 * 3-15-2NN with in:[a,b,1] out:[a && b, a || b] is 47%
	 * Updated perceptron randomization weights to be [-1,1] from [0,1]
	 * New success rate is 57%
	 * 3-5-2NN with same trainer has success rate of 18%
	 * 3-10-2NN with same trainer has success rate of 38%
	 * 3-15-2NN with same trainer has success rate of 57%
	 * 3-30-2NN with same trainer has success rate of 59%
	 * @param args
	 */
	public static void main(String[] args) {
		//1 thread = 25.6s
		//2 threads = 14.94s
		//4 threads = 14.493s
		final int numThreads = 2;
		Thread[] threads = new Thread[numThreads];
		long timeStart = System.currentTimeMillis();
		double[] finalResults = new double[200];
		
		int curNumHiddenNodes = 1;
		int maxNumHiddenNodes = 60;
		
		while (true) {
			boolean anyAlive = false;
			
			for (int i = 0; i < numThreads; i++) {
				
				if (threads[i] == null && curNumHiddenNodes <= maxNumHiddenNodes) {
					NeuralNetworkAnalysis nn = new NeuralNetworkAnalysis(curNumHiddenNodes);
					threads[i]  = nn;
					threads[i].start();
					curNumHiddenNodes++;
				}
				else if (threads[i] != null && !threads[i].isAlive()) {
					NeuralNetworkAnalysis nn = (NeuralNetworkAnalysis)threads[i];
					finalResults[nn.numHiddenNodes] = nn.finalResult;	
					threads[i] = null;
					anyAlive = true;
				}
			}
			
			//Else all threads are dead or null
			
			for (int i =0 ; i < numThreads; i++) {
				if (threads[i] != null) anyAlive = true;
			}
			if (!anyAlive) break;
			
			
			Thread.yield();
			

		}
		
		System.out.println("Rates: ");
		for (int i = 1; i < finalResults.length; i++) {
			System.out.println("Hidden Nodes: " + i + "\t" + finalResults[i]);
		}
		
		long timeDiff = System.currentTimeMillis() - timeStart;
		System.out.println("Neural processing took " + ((double)timeDiff)/1000 + "s");
		
		
		
	}

}
