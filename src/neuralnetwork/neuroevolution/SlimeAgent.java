package neuralnetwork.neuroevolution;

import java.util.Random;


public class SlimeAgent implements Agent{
	
	public neuralnetwork.core.NeuralNetwork nn;
	//NeuralNetworkSlimeAi ai;
	
	public static final int NUM_HIDDEN_NODES = 25;
	/** Outputs passed back to the input at each day */
	public static final int NUM_MEMORY_NODES = 0;
	
	private static Random r = new Random();
	
	
	public SlimeAgent() {
		nn = new neuralnetwork.core.NeuralNetwork(3 + NUM_MEMORY_NODES, NUM_HIDDEN_NODES, 1 + NUM_MEMORY_NODES);
		nn.randomizeWeights();
				
	}
	
	public double evaluteFitness() {
//		
//		// play 10 games
//		
		
		
		
		// always recieve
		
//		while (gameisNotOver)
//		{
//			input[] = {slimePos.x, pos.y, slime2.};
//			
//
//			//double[] out = nn.process(input);
//			
//			output[] = {moveleft,moveright,jump};
//			if (moveLeft > 0.5) 
//				if (jump > 0.9) jump
//				
//				game.makeMove();
//		}
//		
//		
//		// 0 to 100. 100
//		// 5. 10 0 
		return 0;
	}

	@Override
	public Agent mutate(double mutationRate) {
		double[] weights = nn.retrieveWeights();

		// mutate some of these weights
		for (int i = 0; i < weights.length; i++) {
			if (r.nextFloat() < mutationRate) {
				weights[i] = r.nextFloat() * 7 - 3.5;
			}
		}

		SlimeAgent a = new SlimeAgent();
		a.nn.loadWeights(weights);
		
		return a;
	}
	

	
	
	


}
