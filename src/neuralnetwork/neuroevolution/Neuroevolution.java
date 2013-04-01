package neuralnetwork.neuroevolution;
import graphing.DiscreteDataSet;
import graphing.Graphing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

import lattice.Engine;

public class Neuroevolution implements Runnable{

	private static final double mutationRate = .025;
	private static final boolean verbose = true;
	final int numAgents = 60; //30
	final int numIterations = 20000;
	
	static DiscreteDataSet bestFitnessDS = new DiscreteDataSet();
	static DiscreteDataSet avgFitnessDS = new DiscreteDataSet();
	
	public boolean finishedIteration;
	public double best;
	public int maxExecuteIteration;
	
	public void evolve() {
		
		Random r = new Random();

		Agent[] agents = new Agent[numAgents];
		double[] fitness = new double[numAgents];
		RouletteWheel wheel = new RouletteWheel();
		Agent bestAgent = null;

		// Make a population of random agents
		for (int i = 0; i < numAgents; i++) {
			agents[i] = new SlimeAgent();

		}
		
		

		for (int iteration = 0; iteration < numIterations; iteration++) {
			
			while (true) {
				if (iteration >= maxExecuteIteration) {
					Thread.yield();
				}
				else {
					best = 0;
					finishedIteration = false;
					break;
				}
			}

			// Evaluate fitness
			double totalFitness = 0;
			double realTotalFitness = 0;
			double bestFitness = 0;
			
			wheel.clear();
			
			for (int i = 0; i < agents.length; i++) {
				fitness[i] = agents[i].evaluteFitness();
				if (fitness[i] > bestFitness) {
					bestAgent = agents[i];
				}
				bestFitness = Math.max(bestFitness, fitness[i]);
				realTotalFitness += fitness[i];

				wheel.addAgent(agents[i], fitness[i]);

			}
			wheel.process();
			
			if (verbose && iteration % 25 == 24) {
				// print best iteration
			}

			double avgFitness = realTotalFitness / numAgents;

			DecimalFormat f = new DecimalFormat("0.00");
			if (iteration % 20 == 0) {
				System.out.println("Gen : " + iteration + "\t"
						+ f.format(avgFitness) + "\t" + f.format(bestFitness));
			}
			best = bestFitness;
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

	}

	@Override
	public void run() {
		evolve();
		
	}

	public static void main(String[] args) {
	
		Neuroevolution e = new Neuroevolution();
		e.maxExecuteIteration = 1500;
		Graphing graphing = new Graphing();
		graphing.addSet(bestFitnessDS);
		graphing.addSet(avgFitnessDS);
		
		JFrame frame = Engine.newFrame(graphing);
		frame.setResizable(true);
		graphing.f = frame;
		
		
		e.evolve();
		
		
	
	}

}
