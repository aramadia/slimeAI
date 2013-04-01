package graphing;
public abstract class AbstractDataSet {

	public abstract int minX();

	public abstract int maxX();

	public abstract double getData(int i);

	public double maxY() {		
		if (maxX() == 0) return 0;
		double maxY = getData(1);
		for (int i = 2; i <= maxX(); i++) {
			maxY = Math.max(maxY, getData(i));
		}
		return maxY;
	}
	

	public double minY() {
		if (maxX() == 0) return 0;
		double minY = getData(1);
		for (int i = 2; i <= maxX(); i++) {
			minY = Math.min(minY, getData(i));
		}
		return minY;
	}
	

}