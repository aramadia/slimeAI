/**
 * 
 */
package lattice;


import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import lattice.graphics.Buffer;


/**
 * GamePanel
 * 
 * This class is suppose to encapsulate a game panel
 * This includes automatic buffering and automatically calls the engine update
 * @author aramadia
 * 
 *
 */
public class Panel extends JPanel {

	private boolean useBuffer;				// Used for buffering in Java 1.5
	private Buffer buffer = null;			// Stores the buffer in memory

	private Engine engine = null;		// Link to the game engine to call update

	/**
	 * 
	 * @param engine
	 * @param doubleBuffer Set this to true to use graphics double buffer
	 */
	public Panel(Engine engine, boolean doubleBuffer) {
		super(!doubleBuffer);	//Dont use automatic double buffering

		this.engine = engine;
		useBuffer = doubleBuffer;

		if (useBuffer) {
			createBuffer();
		}

		this.setFocusable(false);

	}
		
	private void createBuffer() {
		if (this.getWidth() != 0 && this.getHeight() != 0) {
			buffer = new Buffer(this.getWidth(), this.getHeight());
		}
	}

	public void paint(Graphics g) {

		if (useBuffer) {
			if (buffer == null) {
				createBuffer();
			}
			
			if ( buffer != null) {
				
				engine.update(buffer.getGraphics());
				buffer.draw(g);
			}
		}
		else {
			engine.update((Graphics2D)g);
		}
	}


}
