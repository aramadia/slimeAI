package neuralnetwork.neuroevolution;
import java.util.ArrayList;


/**
 * Facilitates selection like a roulette wheel
 * Everybody has a chance, but some have a better chance
 * @author dlam
 *
 */
public class RouletteWheel {
	
	ArrayList<Agent> agents;
	ArrayList<Double> fitnesses;
	
	double[] prob;
	
	public RouletteWheel() {
		agents = new ArrayList<Agent>();
		fitnesses = new ArrayList<Double>();
	}	

	public void addAgent(Agent a, double fitness) {
		// Square the fitness to make it mroe desirable
		
		if (fitness <= 0) fitness = 0.001;
		agents.add(a);
		fitnesses.add(fitness);		
	}	
	
	public void process() {		
		// Roulette selection
		double totalFitness = 0;
		for (Double f: fitnesses) {
			totalFitness += f;
		}
	
		prob = new double[agents.size()];
		for (int i = 0; i < prob.length; i++) {
			prob[i] = fitnesses.get(i) / totalFitness;
			if (i > 0)
				prob[i] += prob[i - 1];
		}
	}
	
	public Agent getAgent() {
		double r = Math.random();
		
		for (int i = 0; i < agents.size(); i++) {
			if (r < prob[i]) {
				return agents.get(i);
			}
		}
		return null;
	}
	
	public void clear() {
		agents.clear();
		fitnesses.clear();
	}
}
