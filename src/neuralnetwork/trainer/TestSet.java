package neuralnetwork.trainer;
import java.util.Arrays;


public class TestSet {
	public double[] input,output;
	
	@Override
	public String toString() {
		String s = "in: " + Arrays.toString(input) + " out: " + Arrays.toString(output);
		return s;
	}
}
