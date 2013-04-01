package neuralnetwork.neuroevolution;

import SlimeGame.LoseAI;
import SlimeGame.SlimeAI;
import SlimeGame.SlimeV2;

import java.util.Random;

public class SlimeAgent extends SlimeAI implements Agent {
	
	public neuralnetwork.core.NeuralNetwork nn;
	//NeuralNetworkSlimeAi ai;
	
	public static final int NUM_HIDDEN_NODES = 25;
	/** Outputs passed back to the input at each day */
	public static final int NUM_MEMORY_NODES = 0;

    public static final int NUM_INPUT_NODES = 12;
    public static final int NUM_OUTPUT_NODES = 2;

	
	private static Random r = new Random();
	
	
	public SlimeAgent() {
		nn = new neuralnetwork.core.NeuralNetwork(NUM_INPUT_NODES + NUM_MEMORY_NODES, NUM_HIDDEN_NODES, NUM_OUTPUT_NODES + NUM_MEMORY_NODES);
		nn.randomizeWeights();
	}
	
	public double evaluateFitness() {
//
//		// play 10 games

        int NUM_GAMES = 10;

        int wins = 0;
        for (int i = 0; i < NUM_GAMES; i++) {
            SlimeAI ai = new LoseAI();
            int winner = SlimeV2.determineVictor(false, SlimeV2.ServeSide.LEFT, ai, this);
            if (winner == 1) {
                wins++;
            }
        }

        return wins/NUM_GAMES;
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


    @Override
    public void moveSlime() {
        double[] inputs = new double[]{ballX, ballY, ballVX, ballVY, p1X, p1Y, p1XV, p1YV, p2X, p2Y, p2XV, p2YV};
        double[] outputs = nn.process(inputs);

        if (outputs[0] < 0.4) {
            startMoveTowardsNet();
        } else if (outputs[0] > 0.6) {
            startMoveAwayFromNet();
        } else {
            stopMoving();
        }

        if (outputs[1] > 0.5) {
            startJump();
        }
    }
}
