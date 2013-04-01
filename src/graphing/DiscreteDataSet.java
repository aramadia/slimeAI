package graphing;
import java.util.Vector;


/**
 * Discrete data set.  From 1 to n
 * @author dlam
 *
 */
public class DiscreteDataSet extends AbstractDataSet{

	Vector<Double> points = new Vector<Double>();
	double total = 0;
	double max, min;
	
	public void addPoint(double y) {
		points.add(y);
		total += y;
		if (points.size() == 1) {
			max = y;
			min = y;
		}
		if (y < min) min = y;
		if (y > max) max = y;
	}

	public int minX() {
		return 1;
	}
	

	public int maxX() {
		return points.size();
	}
	
	public double getData(int i) {
		return points.get(i - 1);
	}
	
	public double avg() {
		return total/points.size();
	}
	
	/**
	 * Standard deviation
	 * @return
	 */
	public double stdDev() {
		double avg =avg();
		double sum = 0;
		for (int i = 0; i < points.size(); i++) {
			double t = points.get(i) - avg;
			sum += t * t;
		}
		return Math.sqrt(sum/points.size());
	}
	
	public double range() {
		return max - min;
	}
	
	public void clear() {
		points.clear();
	}
	
	
	
	
}
