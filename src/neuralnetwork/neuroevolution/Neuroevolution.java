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

import SlimeGame.GameResult;

import lattice.Engine;

public class Neuroevolution implements Runnable{

	private static final double mutationRate = .025;
	private static final boolean verbose = true;
	final int numAgents = 60; //30
	final int numIterations = 2000;
	
	static DiscreteDataSet bestFitnessDS = new DiscreteDataSet();
	static DiscreteDataSet avgFitnessDS = new DiscreteDataSet();
	
	public boolean finishedIteration;
	public int maxExecuteIteration;
	
    private Agent allBestAgent;
	private double allBestFitness;

    public void evolve() throws InterruptedException, ExecutionException {
		
		Random r = new Random();

		Agent[] agents = new Agent[numAgents];
		double[] fitness = new double[numAgents];
		RouletteWheel wheel = new RouletteWheel();
		
		allBestAgent = null;
		allBestFitness = 0.0;

		// Make a population of random agents
		for (int i = 0; i < numAgents; i++) {
			agents[i] = new SlimeAgent();

		}
		
		final int NTHREADS = 2;
        ExecutorService service = Executors.newFixedThreadPool(NTHREADS);
        ArrayList<Future<Double>> futureFitness = new ArrayList<Future<Double>>();
		

		for (int iteration = 0; iteration < numIterations; iteration++) {
			
            finishedIteration = false;

			// Evaluate fitness
			double totalFitness = 0;
			double realTotalFitness = 0;
			double bestFitness = 0;
			
			wheel.clear();
			futureFitness.clear();
			
			// evaluate all the fitnesses
			
			for (int i = 0; i < agents.length; i++) {
				Future<Double> f = service.submit(agents[i]);
				futureFitness.add(f);
			}
			
			for (int i = 0; i < agents.length; i++) {
				//fitness[i] = agents[i].evaluateFitness();
				
				fitness[i] = futureFitness.get(i).get();
								
				if (fitness[i] > allBestFitness) {
					allBestAgent = agents[i];
					allBestFitness = fitness[i];
				}
				bestFitness = Math.max(bestFitness, fitness[i]);
				realTotalFitness += fitness[i];

				wheel.addAgent(agents[i], fitness[i]);

			}
			wheel.process();
			
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

			

			// Rebuild new population
			ArrayList<Agent> newPopulation = new ArrayList<Agent>(numAgents);

			for (int n = 0; n < numAgents; n++) {
				Agent curAgent = wheel.getAgent();

				
				newPopulation.add(curAgent.mutate(mutationRate));

			}

			Object[] temp = newPopulation.toArray();
			for (int i = 0; i < temp.length; i++) {
				agents[i] = (Agent) temp[i];
			}
			
			//Finished
			finishedIteration = true;
			
        }
        System.out.println("evolve finished");
        allBestAgent.save();

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
