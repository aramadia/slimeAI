package neuralnetwork.core;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import neuralnetwork.trainer.BooleanTrainer;
import neuralnetwork.trainer.TestSet;
import neuralnetwork.trainer.Trainer;
import util.DataSet;


/**
 * Neural Network
 * 1. Create a neural network
 * 2. Initialize random weights
 * 3. Supervised learning
 * 4. Testing (automated, user, application)
 * @author aramadia
 *
 */
public class NeuralNetwork {
	
	int numIterations = 10000;
	protected final int printIterationDelay = 50;
	final int numTestIterations = 1000;
	final double maxError = 0.2;
	protected final boolean PRINT = false;
	public static double learningRate = .8;
	
	public NeuralLayer[] layer;
	public static Random r = new Random();
	
	//int numInputNodes, numMiddleNodes, numOutputNodes;
	
	/**
	 * Creates a three layer feed forward neural network.  
	 * @param numInput Number of inputs
	 * @param numMiddleLayerNeurons Number of neurons in the hidden layer
	 * @param numOutput Number of neurons in the output layer
	 */
	public NeuralNetwork(int numInput, int numMiddleLayerNeurons, int numOutput) {
		
		//initalize(numInput, numMiddleLayerNeurons, numOutput);
		
		ArrayList<Integer> layers = new ArrayList<Integer>();
		layers.add(numInput);
		layers.add(numMiddleLayerNeurons);
		layers.add(numOutput);
		initalize(layers);
		
//		int weights = numInput * numMiddleLayerNeurons + (numMiddleLayerNeurons + 1) * numOutput;
//		
//		int calc = numWeights();
//		if (weights != calc) {
//			throw new IllegalArgumentException();
//		}
		
	}
	
	public NeuralNetwork(int[] layers) {
		ArrayList<Integer> layerSizes = new ArrayList<Integer>();
		for (int i =0 ; i < layers.length; i++) {
			layerSizes.add(layers[i]);
		}
		initalize(layerSizes);
	}

//	protected void initalize(int numInput, int numMiddleLayerNeurons,
//			int numOutput) {
//		numInputNodes = numInput;
//		numMiddleNodes = numMiddleLayerNeurons;
//		numOutputNodes = numOutput;
//		layer = new NeuralLayer[2];
//		final int numHiddenNeurons = numMiddleLayerNeurons;
//		layer[0] = new NeuralLayer(numHiddenNeurons, numInput, true);		
//		layer[1] = new NeuralLayer(numOutput, numHiddenNeurons + 1, false);		
//		
//		layer[0].initialize(layer[1]);
//	}
	
	/**
	 * Initialize a variable number of layers
	 * First one is input, then n -1 nueral layers, final layer is output
	 * @param layerSize
	 */
	protected void initalize(ArrayList<Integer> layerSize) {
		
		layer = new NeuralLayer[layerSize.size() - 1];
		
		for (int i = 0 ; i < layerSize.size() - 1; i++)
		{
			
			boolean needBias= true;
			
			// the last layer has no bias
			if (i == layerSize.size() - 2) {
				needBias = false;
			}
			
			int numNodes = layerSize.get(i + 1);
			
			int numInputs = layerSize.get(i);
			// all layers except the first one has an extra bias input
			if (i >= 1) {
				numInputs += 1;
			}
			
			
			layer[i] = new NeuralLayer(numNodes, numInputs, needBias);
			
			if (i >= 1) {
				layer[i - 1].initialize(layer[i]);
			}
		}
		
		// 3 layers
//		layer[0] = new NeuralLayer(numHiddenNeurons, numInput, true);		
//		layer[1] = new NeuralLayer(numOutput, numHiddenNeurons + 1, false);		
//		
//		layer[0].initialize(layer[1]);
//		
	}
	
	

    public void setNumIterations(int n) {
        numIterations = n;
    }
    
    public static void setLearningRate(double rate) {
    	NeuralNetwork.learningRate = rate;
    }
	
	/**
	 * Randomize weights in the neural network
	 */
	public void randomizeWeights() {
		for (NeuralLayer l: layer) {
			l.randomizeWeights();
		}
	}	
	
	public void learn(Trainer trainer) {
		
		for (int iteration = 0; iteration < numIterations; iteration++) {
			
			if (iteration % 1000 == 0) System.out.println(iteration);
			
			TestSet set = trainer.getNextSet();
			
			//Evaluate network
			double [][] input = new double[layer.length + 1][];
			input[0] = set.input;
			for (int i = 0; i < layer.length; i++) {
				input[i + 1] = layer[i].evaluate(input[i]);
			}
			
			
			double[] out = input[layer.length];			
			double[] delta = new double[out.length];
			
			for (int i = 0; i < delta.length; i++) {
				delta[i] = set.output[i] - out[i];
			}
			
			//Reset error
			for (int l = 0; l < layer.length; l++) {
				for (int j = 0; j < layer[l].node.length; j++) {
					if (layer[l].node[j] instanceof Perceptron) {
						((Perceptron)layer[l].node[j]).error = 0;
					}
				}
			}
			
			//Calculate error
			NeuralLayer outputLayer = layer[layer.length - 1];
			for (int i = 0; i < outputLayer.node.length; i++) {
				Perceptron p = (Perceptron)outputLayer.node[i];
				p.error = delta[i];
			}
			
			
			//Propagate error
			for (int l = 0; l < layer.length - 1; l++) {
				for (int j = 0; j < layer[l].node.length - 1; j++) {
					Perceptron p = (Perceptron)layer[l].node[j];
					for (int w = 0; w < layer[l + 1].node.length; w++) {
						p.error += ((Perceptron)layer[l + 1].node[w]).weight[j] * ((Perceptron)layer[l + 1].node[w]).error;
					}
				}
			}
			
			//Adjust weights
			for (int i = 0; i < layer.length; i++) {
				for (int j = 0; j < layer[i].node.length; j++) {
					if (layer[i].node[j] instanceof Perceptron) {
						((Perceptron)layer[i].node[j]).trainWeights(input[i]);
					}
				}
			}
			

			if (iteration % printIterationDelay == 0 && PRINT) {
				System.out.println("Iteration: " + iteration);
				System.out.println(set);
				
				System.out.println();
				for (int i = 0 ; i < input.length; i++) {
					System.out.println("Input: " + i + Arrays.toString(input[i]));
				}
				System.out.println();
				
				System.out.println("Actual Output: " + Arrays.toString(out) + " Delta: " + Arrays.toString(delta));
				System.out.println("New Layer");
				for (int i = 0; i < layer.length; i++) {
					System.out.println("Layer " + i + layer[i]);
				}
				System.out.println();
			}
		}
	}
	
	/**
	 * Indicates that the neural network is sufficiently trained
	 * @param trainer
	 * @return
	 */
	public boolean test(Trainer trainer) {
		
		
		DataSet error = new DataSet(1000);
		int numErrors = 0;
		
		
		for (int iteration = 0; iteration < numTestIterations; iteration++) {
			TestSet set = trainer.getNextSet();
			
			//Evaluate network
			double [][] input = new double[layer.length + 1][];
			input[0] = set.input;
			for (int i = 0; i < layer.length; i++) {
				input[i + 1] = layer[i].evaluate(input[i]);
			}
			
			
			double[] out = input[layer.length];			
			
			boolean exceedError = false;
			for (int i = 0; i < out.length; i++) {
				double e = Math.abs(set.output[i] - out[i]);
				if (e > maxError) exceedError = true;
				
				error.add(e);
			}
            if (exceedError) numErrors++;
		}
		
		System.out.println("Test run");
		System.out.println("Error: " + error);
		System.out.println("Error Percentage: " + (((double)numErrors)/numTestIterations)*100 + "%");
		if (error.max() < maxError) {
			System.out.println("Neural Training successful");
			return true;
		}
		else {
			System.out.println("Neural Training failed");
			return false;
		}
	}


    public double[] process(double[] in) {
        //Evaluate network
        double [][] input = new double[layer.length + 1][];
        input[0] = in;
        for (int i = 0; i < layer.length; i++) {
            input[i + 1] = layer[i].evaluate(input[i]);
        }


        double[] out = input[layer.length];
        return out;
    }
	
    public double[] retrieveWeights() {
    	
    	double[] weights = new double[numWeights()];
    	int cur = 0;
    	for (NeuralLayer l: layer) {
    		for (Node n: l.node) {
    			if (n instanceof Perceptron) {
    				Perceptron p = (Perceptron)n;
    				for (int i = 0; i < p.weight.length; i++) {
    					weights[cur] = p.weight[i];
    					cur++;
    				}
    			}
    		}
    	}
    	
    	return weights;
    }
    
    public int numWeights() {
    	int total = 0;
    	for (int i = 0 ; i < layer.length; i++) {
    		total += layer[i].numWeights();
    	}
    	return total;
    	// 12, 25, 2
    	//return numInputNodes * numMiddleNodes + (numMiddleNodes + 1) * numOutputNodes;
 
    }
    public void loadWeights(double[] weights) {    	
    	if (weights.length != numWeights()) throw new IllegalArgumentException("Incorrect length");
    	int cur = 0;
    	for (NeuralLayer l: layer) {
    		for (Node n: l.node) {
    			if (n instanceof Perceptron) {
    				Perceptron p = (Perceptron)n;
    				for (int i = 0; i < p.weight.length; i++) {
    					p.weight[i] = weights[cur];
    					cur++;
    				}
    			}
    		}
    	}
    	   	
    	
    }
	
	public static void main(String[] args) {
		
		long timeStart = System.currentTimeMillis();
		final int numHiddenNodes = 100;
		System.out.println("Num Hidden Nodes: " + numHiddenNodes);
		int numSuccess = 0;
		final int numTrials = 10;
		
		for (int iteration = 0; iteration < numTrials; iteration++) {
			NeuralNetwork nn = new NeuralNetwork(5, numHiddenNodes, 16);
			
			nn.randomizeWeights();		
			
			Trainer trainer;
			trainer = new BooleanTrainer();
			
//			for (int i = 0; i < 10; i++) {
//				System.out.println(trainer.getNextSet());
//			}
			
			nn.learn(trainer);
			boolean success = nn.test(trainer);
			if (success) numSuccess++;
		}
		
		double successRate = ((double)numSuccess)/numTrials;
		System.out.println("Success Rate: " + successRate);
		System.out.println("Time to execute: " + ((double)System.currentTimeMillis() - timeStart)/1000 + "ms");
		
	}
	
}
