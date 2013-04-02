package neuralnetwork.neuroevolution;
import graphing.DiscreteDataSet;
import graphing.Graphing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;

import neuralnetwork.core.NeuralNetwork;

import SlimeGame.GameResult;

import lattice.Engine;

public class Neuroevolution implements Runnable{

	private static final double mutationRate = .025;
	private static final boolean verbose = true;
	final int numAgents = 30; //30
	final int numIterations = 2000;
	
	static final int HIGH_PRECISION_FITNESS = 100;
	
	static final double crossoverPrb = 0.50;
	
	static DiscreteDataSet bestFitnessDS = new DiscreteDataSet();
	static DiscreteDataSet avgFitnessDS = new DiscreteDataSet();
	
	public boolean finishedIteration;
	public int maxExecuteIteration;
	
    private Agent allBestAgent;
	private double allBestFitness;

    public void evolve() throws InterruptedException, ExecutionException {
		
		Random r = new Random();

		Agent[] parents = new Agent[numAgents];
		double[] fitness = new double[numAgents];

		//RouletteWheel wheel = new RouletteWheel();
		
		allBestAgent = null;
		allBestFitness = 0.0;

		// Make a population of random agents
		for (int i = 0; i < numAgents; i++) {
			parents[i] = new SlimeAgent();

		}
		
		final int NTHREADS = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(NTHREADS);
        ArrayList<Future<Double>> futureFitness = new ArrayList<Future<Double>>();
		

		for (int iteration = 0; iteration < numIterations; iteration++) {
			assert numAgents == parents.length;
			
			// Evaluate fitness
			double totalFitness = 0;
			double realTotalFitness = 0;
			double bestFitness = 0;
			
			futureFitness.clear();

			// Crossover new population
			Agent[] children = performCrossover(parents);
			
			if(iteration == 0)
				parents = new Agent[0];
			
			// Mutate
			performMutation(children);

			// Evaluate
			
			for (int i = 0; i < children.length; i++) {
				Future<Double> f = service.submit(children[i]);
				futureFitness.add(f);
			}
			
			for (int i = 0; i < children.length; i++) {
				//fitness[i] = agents[i].evaluateFitness();
				
				fitness[i] = futureFitness.get(i).get();
				
				if (fitness[i] > allBestFitness) {
					// If you see this too often, itll need to optimized
					System.out.println("Fitness check");
					
					// Accurately determine the fitness, ight of just got lucky
					double accurateFitness = children[i].evaluateFitness(HIGH_PRECISION_FITNESS);
					if (accurateFitness > allBestFitness)
					{
						allBestAgent = children[i];
						allBestFitness = accurateFitness;
						System.out.println("New best record of " + allBestFitness);
					}
				}
				bestFitness = Math.max(bestFitness, fitness[i]);
				realTotalFitness += fitness[i];
				
				//wheel.addAgent(agents[i], fitness[i]);

			}
			
			if (iteration % 25 == 24) {
				// print best iteration

		        System.out.println("Saving agent of " + allBestFitness);
		        allBestAgent.save();
			}

			double avgFitness = realTotalFitness / numAgents;

			DecimalFormat f = new DecimalFormat("0.00");
			if (iteration % 20 == 0) {
				System.out.println("Gen : " + iteration + "\t"
						+ f.format(avgFitness) + "\t" + f.format(bestFitness));
			}
			
			bestFitnessDS.addPoint(bestFitness);
			avgFitnessDS.addPoint(avgFitness);
			
			Agent[] allAgents = new Agent[parents.length + children.length];
			System.arraycopy(parents, 0, allAgents, 0, parents.length);
			System.arraycopy(children, 0, allAgents, parents.length, children.length);
			Arrays.sort(allAgents);
			
			parents = performSelect(allAgents);	

        }
        System.out.println("evolve finished");
        allBestAgent.save();

    }
    
    Agent[] performSelect(Agent[] allAgents){    	
    	Agent[] newGenAgents = new Agent[numAgents];
    	// Get top n agents from the current pool
    	int numTopAgents = numAgents/2;
    	for(int i = 0; i<numTopAgents; i++){
    		newGenAgents[i] = allAgents[allAgents.length-1-i];
    	}
    	
    	// Use Roulette wheel to select the rest numAgents-numTopAgents agents by using roulette wheel
    	double totalFitness = 0;
    	int numRemainingAgents = allAgents.length-numTopAgents;
		for (int i = 0; i<numRemainingAgents; i++) {
			totalFitness += allAgents[i].getFitness();
		}
		if (totalFitness == 0.0){
			return allAgents;
		}
		double[] prob = new double[numRemainingAgents];
		for (int i = 0; i < prob.length; i++) {
			prob[i] = allAgents[i].getFitness() / totalFitness;
			if (i > 0)
				prob[i] += prob[i - 1];
		}
		
    	for(int i = numTopAgents; i<numAgents; i++){
    		double r = Math.random();
    		for (int j = 0; j < numRemainingAgents; j++) {
    			if (r < prob[j]) {
    				newGenAgents[i] = allAgents[j];
    			}
    		}
    		
    	}
    	return newGenAgents;
    }
    void performMutation(Agent[] population){
    	for (int n = 0; n < numAgents; n++) {
    		Agent curAgent = population[n];
			curAgent.mutate(mutationRate);
		}
    }
    Agent[] performCrossover(Agent[] parents) {
    	Agent[] children = new Agent[numAgents];
    	for(int i = 0; i<parents.length/2; i++){
    		Agent parent1 = parents[2*i];
    		Agent parent2 = parents[2*i+1];
    		
    		double[] weights_1 = parent1.getNN().retrieveWeights();
    		double[] weights_2 = parent2.getNN().retrieveWeights();
    		int crossoverPoint = weights_1.length;
    		if(Math.random() <= crossoverPrb){
    			Random r = new Random();
    			crossoverPoint = r.nextInt(weights_1.length);
    		}
    		//System.out.println(crossoverPoint);
    		double[] new_weights_1 = new double[weights_1.length];
    		double[] new_weights_2 = new double[weights_1.length];
    		for(int j = 0; j<weights_1.length; j++){
    			if(j<=crossoverPoint){
    				new_weights_1[j] = weights_1[j];
    				new_weights_2[j] = weights_2[j];
    			}
    			else {
    				new_weights_1[j] = weights_2[j];
    				new_weights_2[j] = weights_1[j];
    			}
    		}
    		Agent child1 = new SlimeAgent();
    		child1.setWeights(new_weights_1);
    		Agent child2 = new SlimeAgent();
    		child2.setWeights(new_weights_2);
    		children[2*i] = child1;
    		children[2*i+1] = child2;
    	}
		return children;
    }
    @Override
	public void run() {
		try {
			evolve();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public static void main(String[] args) {
	
		Neuroevolution e = new Neuroevolution();
		e.maxExecuteIteration = 10;
		Graphing graphing = new Graphing();
		graphing.addSet(bestFitnessDS);
		graphing.addSet(avgFitnessDS);
		
		JFrame frame = Engine.newFrame(graphing);
		frame.setResizable(true);
		graphing.f = frame;
		
		
		try {
			e.evolve();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
