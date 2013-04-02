package neuralnetwork.neuroevolution;

import SlimeGame.*;
import neuralnetwork.core.NeuralNetwork;

import java.io.*;
import java.util.Random;

import neuralnetwork.core.NeuralNetwork;

public class SlimeAgent extends SlimeAI implements Agent, Comparable{
	
	public neuralnetwork.core.NeuralNetwork nn;
	//NeuralNetworkSlimeAi ai;
	
	public static final int NUM_HIDDEN_NODES = 25;
	/** Outputs passed back to the input at each day */
	public static final int NUM_MEMORY_NODES = 0;

    public static final int NUM_INPUT_NODES = 10;
    public static final int NUM_OUTPUT_NODES = 2;

    public static final int DEFAULT_NUM_GAMES = 30;
	
	private static Random r = new Random();
    private static final String SaveFile = "test.txt";

    private double fitness = 0.0;

    public SlimeAgent() {
    	int[] layerStructure = new int[] {NUM_INPUT_NODES, 45, NUM_OUTPUT_NODES};
		nn = new NeuralNetwork(layerStructure);
    	//nn = new neuralnetwork.core.NeuralNetwork(NUM_INPUT_NODES + NUM_MEMORY_NODES, NUM_HIDDEN_NODES, NUM_OUTPUT_NODES + NUM_MEMORY_NODES);
		nn.randomizeWeights();
	}
    
    /**
     * a*(1-r^n)/(1-r)
     * @param r
     * @param n
     */
    private static final double geometricSum(double r, double n) {
    	return 1*(1 - Math.pow(r, n))/(1-r);
    }
    
    private static final double weightNetCrosses(int n) {
    	double expNetCrosses = 0;
    	if (n != 0) {
    		expNetCrosses = geometricSum(0.95, n);
    	}
    	return expNetCrosses;

    }
	
	public double evaluateFitness(int precision) {

		// play a set deterministic series of games

        int NUM_GAMES = precision;

        SlimeAI crapAI = new CrapSlimeAI();
        SlimeAI dannoAI = new DannoAI();
        SlimeAI dannoAI2 = new DannoAI2();
        SlimeAI threeWaySwapAI = new ThreeSwapSlimeAI();

        double points = 0.0;
        for (int i = 0; i < NUM_GAMES; i++) {
        	SlimeAI ai = null;
        	SlimeV2.ServeSide side = null;

            ai = threeWaySwapAI;
//        	if (i < 7) {
//        		ai = crapAI;
//        	} else if (i < 14){
//
//        		ai = dannoAI;
//        	} else {
//        		ai = dannoAI2;
//        	}
        	if (i % 2 == 0) {
        		side = SlimeV2.ServeSide.RIGHT;
        	} else {
        		side = SlimeV2.ServeSide.LEFT;
        	}

            GameResult result = SlimeV2.determineVictor(false, side, ai, this, 1);
            if (result.getWinner() == 1) {
                points += 10;
            } else {
            	points += weightNetCrosses(result.getRtlNetCrosses());
            }
        }
        this.fitness = points/NUM_GAMES;
        //System.out.println(this.fitness);

        // Compute the average fitness independent of the number of games
        return this.fitness;
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
    public void save() {
        double[] weights = nn.retrieveWeights();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SaveFile));
            oos.writeObject(weights);
            oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            System.out.println("Exception ");
        }
    }

    @Override
    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SaveFile));
            double[] weights = (double[]) ois.readObject();
            nn.loadWeights(weights);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    double normalizeVelocity(double velocity)
    {
    	// what is the maximum velocity anyways?
    	final double maxVelocity = 50;
    	
    	// normalize (-maxVelocity,maxVelocity) to (-1,1)
    	return (velocity ) / maxVelocity;
    }


    @Override
    public void moveSlime() {
        setVars();
    	double gameWidth = Constants.GAME_WIDTH;
    	double gameHeight = Constants.GAME_HEIGHT;
    	
    	double whereWillBallCross = whereWillBallCross(125);
    	// normalize these inputs
        double[] inputs = new double[]{
        		ballX / gameWidth, 
        		ballY / gameHeight, 
        		normalizeVelocity(ballVX), 
        		normalizeVelocity(ballVY),
        		(whereWillBallCross - p2X) / 50.0,
        		(ballX - p2X) / 50.0,
        		(ballY - p2Y) / 50.0,
//        		p1X / gameWidth, 
//        		p1Y / gameHeight, 
//        		normalizeVelocity(p1XV), 
//        		normalizeVelocity(p1YV), 
        		p2X / gameWidth, 
        		p2Y / gameHeight, 
//        		normalizeVelocity(p2XV),
//        		normalizeVelocity(p2YV),
        		1.0	// bias node
        		};
        double[] outputs = nn.process(inputs);

        if (outputs[0] < 0.5) {
            startMoveTowardsNet();
        } else if (outputs[0] > 0.5) {
            startMoveAwayFromNet();
        } else {
            stopMoving();
        }

        if (outputs[1] > 0.5) {
            startJump();
        }
    }

    public static void main(String[] args) {
    	
    	/*
    	 * 0 -> 0.0
			1 -> 1.0
			2 -> 1.9499999999999988
			3 -> 2.8524999999999996
			4 -> 3.7098749999999994
			5 -> 4.52438125
			6 -> 5.298162187499999
			7 -> 6.033254078125
			8 -> 6.731591374218749
			9 -> 7.395011805507812
			10 -> 8.025261215232419
    	
    	for (int netCrosses = 0; netCrosses < 30; netCrosses++)
    	{
        	double expNetCrosses = weightNetCrosses(netCrosses);  
        	System.out.println(netCrosses + " -> " + expNetCrosses);
    	}
    	 */
    	
    	
        ThreeSwapSlimeAI threeSwapSlimeAI = new ThreeSwapSlimeAI();
        SlimeAgent ai2 = new SlimeAgent();
        ai2.load();
        
        SlimeAI crapAI = new CrapSlimeAI();
        SlimeAI dannoAI = new DannoAI();
        SlimeAI dannoAI2 = new DannoAI2();
        SlimeAI human = null;
        SlimeGame.SlimeV2.determineVictor(true, SlimeV2.ServeSide.RIGHT, human, ai2, 5);
    }

	@Override
	public Double call() throws Exception {
		return evaluateFitness(DEFAULT_NUM_GAMES);
	}

	@Override
	public NeuralNetwork getNN() {
		return nn;
	}

	@Override
	public void setWeights(double[] weights) {
		nn.loadWeights(weights);
	}

	@Override
	public int compareTo(Object anotherAgent) throws ClassCastException {
		if (!(anotherAgent instanceof Agent))
		      throw new ClassCastException("An Agent object expected.");
		double anotherFitness = ((Agent) anotherAgent).getFitness();
		int comparison = this.getFitness() - anotherFitness >= 0 ? 1 : -1; 
		return comparison;
	}
	
	@Override
	public double getFitness() {
		return fitness;
	}
}
