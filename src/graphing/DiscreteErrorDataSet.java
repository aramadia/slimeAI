package graphing;
import java.util.ArrayList;


public class DiscreteErrorDataSet extends DiscreteDataSet {
	ArrayList<Double> errors = new ArrayList<Double>();
	

	public void addPoint(double y, double e) {
	
		addPoint(y);
		errors.add(e);
	}
	
	public double getError(int i) {
		return errors.get(i - 1);
	}
}
