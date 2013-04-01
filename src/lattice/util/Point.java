package lattice.util;

/**
 * This is Point using x and y coordinate systems
 * Each value has double precision
 * @author aramadia
 *
 */
public class Point {
	public double x,y;
	
	public Point() {
		
	}
	
	/**
	 * Creates a point at (x,y) coordinates
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		
	}
	
	/**
	 * Displays the coordinates
	 */
	public String toString() {
		String s = "X: " + x + " Y: " + y;
		return s;
	}
	
	/**
	 * This rounds the components of the point to the nearest factor
	 * @param factor
	 */
	public void round(double factor) {
		x = Math.round(x / factor) * factor;
		y = Math.round(y / factor) * factor;
	}
	
	
}
