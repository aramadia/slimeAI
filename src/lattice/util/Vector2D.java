package lattice.util;



/**
 * This handles the operation of a 2D vector
 * @author aramadia
 *
 */
public class Vector2D extends Point{

	//We want high percision
	//public double x,y;

	/**
	 * Initalizes the Vector2D to a zero length vector
	 *
	 */
	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Always initialize the vector
	 * @param x
	 * @param y
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(Point2i p) {
		this.x = p.x;
		this.y = p.y;
		
	}
	
	/**
	 * Set both the x and y values
	 * @param x
	 * @param y
	 */
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Calculates the dot product of two vectors
	 * @param v
	 * @return
	 */
	public double dot(Vector2D v) {
		return this.x * v.x + this.y * v.y;
	}
	
	

	/**
	 * This function normalizes the vector
	 * This means it will make the distance one
	 *
	 */
	public void normalize() {
		double len = magnitude();
		
		this.x = this.x / len;
		this.y = this.y / len;

	}
	
	/**
	 * Returns true if it is a zero length vector
	 * @return
	 */
	public boolean zero() {
		if (x == 0 && y == 0) {
			return true;
		}
		return false;
	}

	/**
	 * This calculates the magnitude of the vector
	 * @return
	 */
	public double magnitude() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	/**
	 * This calculates the magnitude squared of the vector
	 * @return
	 */
	public double magnitudeSquared() {
		return this.x * this.x + this.y * this.y;
	}

	/**
	 * This returns the distance between two vectors
	 * Doesn't use the pow function (extremely slow) compared to sqrt or *
	 * @param b The second vector to compare with
	 * @return
	 */
	public double distance(Vector2D b) {
		// Stores the results rather than recalculating
		double dx = b.x - this.x;
		double dy = b.y - this.y;
		return Math.sqrt(dx * dx + dy * dy);

	}

	/**
	 * Returns the square of the distance
	 * Doesn't use the pow function at all
	 * @param b
	 * @return
	 */
	public double distanceSquared(Vector2D b) {
		// Stores the results rather than recalculating
		double dx = b.x - this.x;
		double dy = b.y - this.y;
		return dx * dx + dy * dy;
	}

	/**
	 * This adds another vector to this one
	 * @param b
	 */
	public void add(Vector2D b) {
		this.x += b.x;
		this.y += b.y;

	}

	/**
	 * This finds the angle of a vector (where right is 0 degrees)
	 * @return An angle in degrees (geometric)
	 */
	public double angle() {
		double mag = magnitude();
		// There is no direction for a 0 length vector
		if (mag == 0) {
			return Double.NaN;
		}
		else {
			if (x >= 0) {
				return Math.toDegrees(Math.asin(y/ mag));
			}
			else {
				return 180 + Math.toDegrees(Math.asin(y/ mag));
			}
		}


	}

	/**
	 * This multiply each component by a scalar
	 * @param b
	 * @return
	 */
	public void multiply(double b) {
		this.x *= b;
		this.y *= b;
	}
	
	/**
	 * Negates all components of the vector
	 *
	 */
	public void inverse() {
		multiply(-1);
	}
	
	

	/**
	 * Returns a string version of the vector
	 * in this format (x,y).
	 * ex. (3,4)
	 */
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";		
	}

	/**
	 * Creates a new vector with the same internal variables
	 * @return a new Vector class
	 */
	public Vector2D copy() {
		return new Vector2D(this.x, this.y);
	}

	/**
	 * Returns true if all the components of the vector are equals
	 */
	public boolean equals(Object o) {
		Vector2D v = (Vector2D)o;
		if (v.x == this.x && v.y == this.y) {
			return true;
		}
		return false;
	}
	
	/**
	 * Converts this to a vector pointing in a perpendicular direction
	 *
	 */
	public void perpendicular() {
		double temp = x;
		x = y;
		y = -temp;
	}


}
