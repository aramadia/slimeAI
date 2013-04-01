package graphing;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import lattice.Engine;
import lattice.Helper;
import lattice.util.Point2i;
import lattice.util.Vector2D;

/**
 * Ultimate plotting tool
 * @author dlam
 *
 */
public class Graphing extends Engine{
	public JFrame f;
	int width = 600, height = 400;
	
	final int offX = 0, offY = 0;
	
	ArrayList<AbstractDataSet> plots;
	Color[] plotColors = {Color.orange, Color.blue, Color.red, Color.green, Color.pink};
	
	double axisMaxX, axisMaxY;
	
	public Graphing() {
		plots = new ArrayList<AbstractDataSet>();
	}
	
	public void addSet(AbstractDataSet set) {
		plots.add(set);
	}
	
	private static void test() {
		Scanner in = new Scanner(System.in);
		while (true) {
			try {
				System.out.println(divisionSize(in.nextDouble()));
			} catch (Exception e) {
				
				e.printStackTrace();
				break;
			}
		}
	}

	@Override
	public void update(Graphics2D g) {
		
		width = f.getWidth();
		height = f.getHeight();
		
		//System.out.println(g.getClipBounds());
		g.translate(offX, offY);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, width, height);
		
		/** Points of the grid in real coordinates */
		Point2i bl = new Point2i(50, 75);
		Point2i tr = new Point2i(width - 15, height - 15);
		
		Rectangle graph = new Rectangle(bl.x, height - tr.y, tr.x - bl.x, tr.y - bl.y);
				
		g.setColor(new Color(220, 220, 220));
		g.fill(graph);
		g.setColor(Color.black);
		g.draw(graph);
	
		//Create affinetransform for grid coordinates
		AffineTransform a = new AffineTransform();
		
		for (AbstractDataSet plot: plots) {
			if (plot.maxX() > axisMaxX|| axisMaxX < 3) {
				axisMaxX = Math.max(10, plot.maxX() * 1.5);
			}
			if (plot.maxY() > axisMaxY || axisMaxY < 3) {
				axisMaxY = Math.max(10, plot.maxY() * 1.5);
			}
		}
		
		//TODO watch out if the graph has been reset (cleared)
		
		a.scale(1.0, -1.0);
		a.translate(bl.x, -height + bl.y);
		a.scale((tr.x - bl.x)/axisMaxX, (tr.y - bl.y)/axisMaxY);
			
		g.setColor(Color.red);
		//g.draw(new Rectangle(maxX, 100));
		//g.fill(a.createTransformedShape(new Rectangle(maxX, 100)));
		
		//Grab input
		Point2i mousePos = mouseInput.getPos();		
		AffineTransform invA = null;
		try {
			invA = a.createInverse();
		} catch (NoninvertibleTransformException e) {		
			e.printStackTrace();
		}
		Point2D tempPoint = new Point2D.Double(mousePos.x, mousePos.y);
		tempPoint = invA.transform(tempPoint, null);
		Vector2D mPos = new Vector2D(tempPoint.getX(), tempPoint.getY());
		
		//Hunt for closes point to mPos
//		g.setColor(Color.black);
//		drawPoint(g, a.transform(tempPoint, null), 3.0, Color.black, Color.yellow);
		
		
		
		
		//Draw x axis
		g.setColor(Color.black);
		for (int x = 0; x <= axisMaxX; x+=divisionSize(axisMaxX)) {
			Point2D t = new Point2D.Double(x, 0);
			
			t = a.transform(t, null);			
			g.drawLine((int)t.getX(), (int)t.getY(), (int)t.getX(), (int)(t.getY() + 5));
			
			g.drawString("" + x, (float)t.getX() - 7, (float)t.getY() + 22);
		}
		
		//Draw y axis	
		for (int y = 0; y <= axisMaxY; y += divisionSize(axisMaxY)) {
			Point2D t = new Point2D.Double(0, y);
			
			t = a.transform(t, null);			
			g.drawLine((int)t.getX(), (int)t.getY(), (int)t.getX() - 5, (int)(t.getY()));
			
			//Draw lines running across (rows)
			if (true) {
				g.drawLine((int)t.getX(), (int)t.getY(), (int)tr.x, (int)(t.getY()));		
			}
			
			g.drawString("" + y, (float)t.getX() - 40, (float)t.getY() + 3);
		}
		
		boolean foundClosePoint = false;
		
		//Draw points
		//Assume discrete graph
		for (int i = 0; i < plots.size(); i++) {
			AbstractDataSet plot = plots.get(i);
		
			for (int x = plot.minX(); x <= plot.maxX(); x++) {
				double y = plot.getData(x);
				Point2D p = new Point2D.Double(x, y);
				
				
				p = a.transform(p, null);	
				
				//Check if the mouse is close to this point				
				if (!foundClosePoint) {
					Vector2D vecMouse = new Vector2D(mousePos.x, mousePos.y);
					Vector2D vecPoint = new Vector2D(p.getX(), p.getY());
					if (vecMouse.distance(vecPoint) < 4) {
						drawPoint(g, p, 4, darkenColor(plotColors[i % plotColors.length])/*Color.black*/, plotColors[i % plotColors.length]);
						foundClosePoint = true;
					}
				}
				
				drawPoint(g, p, 1.5, darkenColor(plotColors[i % plotColors.length])/*Color.black*/, plotColors[i % plotColors.length]);
				
				//Draw error bar
				if (plot instanceof DiscreteErrorDataSet) {
					DiscreteErrorDataSet ds = (DiscreteErrorDataSet) plot;
					Point2D pE = new Point2D.Double(x, y + ds.getError(x));
					pE = a.transform(pE, null);
					double delta = pE.getY() - p.getY();
					
					g.setColor(new Color(0, 0, 0, 60));
					g.drawLine(Helper.toInt(p.getX()), Helper.toInt(p.getY() - delta), Helper.toInt(p.getX()), Helper.toInt(p.getY() + delta));
					
				}
			
			}
		}
		
		
		//Check input
		if (keyInput.keyDown(KeyEvent.VK_P)) {
			printData();
		}
		
		
		
		
	}
	
	private static Color darkenColor(Color c) {
		float factor = 0.6f/255.0f;
		return new Color((float)(c.getRed() * factor), (float)(c.getGreen() * factor),(float)(c.getBlue() * factor));
	}
	
	private void printData() {
		int curI = 1;
		while (true) {
			boolean anyPrint = false;
			
			for (AbstractDataSet plot: plots) {
				if (curI <= plot.maxX()) {
					System.out.print(plot.getData(curI) + "\t");
					anyPrint = true;
				}
				
			}
			System.out.println();
			curI++;
			if (!anyPrint) break;
		}
		
	}

	/**
	 * Finds the largest divisor in form (1,2,5)x10^n such there are more than 6 divisions
	 * in the given range
	 * @param range
	 * @return
	 */
	private static double divisionSize(double range) {
		double divisor = Math.pow(10, (int)(Math.log(range) + 1));
		
		while (range/divisor < 6) {
			
			//5xxx
			divisor /= 2.0;
			if (range/divisor >= 6) break;
		
			//2xxx
			divisor /= 2.5;
			if (range/divisor >= 6) break;
			
			//1xxx
			divisor /= 2.0;
			if (range/divisor >= 6) break;
		}
		
		return divisor;
	}
	
	/**
	 * Draws a point on the graph
	 * @param g
	 * @param p
	 * @param radius
	 * @param outline
	 * @param fill
	 */
	private static void drawPoint(Graphics2D g, Point2D p, double radius, Color outline, Color fill) {
		g.setColor(fill);
		g.fillOval(Helper.toInt(p.getX() - radius), Helper.toInt(p.getY() - radius), Helper.toInt(radius * 2), Helper.toInt(radius * 2));
		
		g.setColor(outline);
		g.drawOval(Helper.toInt(p.getX() - radius), Helper.toInt(p.getY() - radius), Helper.toInt(radius * 2), Helper.toInt(radius * 2));
		
		
	}

	public static void main(String[] args) {
		
		Graphing g = new Graphing();
		DiscreteErrorDataSet dds = new DiscreteErrorDataSet();
		g.addSet(dds);
		JFrame f = newFrame(g);
		g.f = f;
		f.setSize(800, 600);
		f.setResizable(true);
		
		for (int i = 0; i < 50; i++) {
			dds.addPoint(i * Math.sqrt(i), 3 * i);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}

	public void resize() {
		axisMaxX = 0; axisMaxY = 0;
	
		for (AbstractDataSet plot: plots) {
			if (plot.maxX() > axisMaxX|| axisMaxX < 3) {
				axisMaxX = Math.max(10, plot.maxX() * 1.1);
			}
			if (plot.maxY() > axisMaxY || axisMaxY < 3) {
				axisMaxY = Math.max(10, plot.maxY() * 1.1);
			}
		}
	}
	
}
