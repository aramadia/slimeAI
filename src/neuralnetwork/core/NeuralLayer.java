package neuralnetwork.core;

public class NeuralLayer {
	
	public Node[] node;
	/** The layer to food inputs to */
	NeuralLayer children;
	
	/**
	 * Creates new neural layer
	 * @param numNodes The number of nodes in this layer
	 * @param numInputs The number of inputs to each node. ie.
	 * number of nodes in parent layer
	 * @param bias Indicates whether you want to include a bias node in this layer 
	 * (not for output)
	 */
	public NeuralLayer(int numNodes, int numInputs, boolean bias) {
		int totalNodes = bias ? numNodes + 1 : numNodes;
		
		node = new Node[totalNodes];
		for (int i = 0; i < numNodes; i++) {
			node[i] = new Perceptron(numInputs);
		}
		if (bias) node[totalNodes - 1] = new Bias();
		
	}
	
	public void initialize(NeuralLayer children) {
		this.children = children;
	}
	
	public double[] evaluate (double[] input) {
		double[] output = new double[node.length];
		
		for (int i = 0; i < node.length; i++) {
			output[i] = node[i].evalute(input);
		}
		return output;
	}
	
	public void randomizeWeights() {
		for (Node n: node	) {
			n.randomizeWeights();
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		for (Node p: node) {
			s += "\t" + p.toString() + "\n";
		}
		return s;
	}
}
