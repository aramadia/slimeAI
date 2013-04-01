package lattice.util;

/**
 * This is Point using x and y coordinate systems
 * Each value has double precision
 * @author aramadia
 *
 */
public class Point2i {
	public int x,y;
	
	public Point2i() {
		
	}
	
	/**
	 * Creates a point at (x,y) coordinates
	 * @param x
	 * @param y
	 */
	public Point2i(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	
	/**
	 * Creates a point at (x,y) coordinates.
	 * Rounds to nearest integer
	 * @param x
	 * @param y
	 */
	public Point2i(double x, double y) {
		this.x = (int)Math.round(x);
		this.y = (int)Math.round(y);
		
	}
	
	/**
	 * Displays the coordinates
	 */
	public String toString() {
		String s = "X: " + x + " Y: " + y;
		return s;
	}
	
		
}
