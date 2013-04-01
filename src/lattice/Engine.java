/**
 * 
 */
package lattice;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import lattice.io.KeyboardInput;
import lattice.io.MouseInput;

/**
 * Class for basic engine
 * Includes methods for painting and drawing
 * @author aramadia
 *
 */
public abstract class Engine {
	//Keyboard Inputs
	protected KeyboardInput keyInput;	
	protected MouseInput mouseInput;
	
	
	/**
	 * This is all callback mechanism called by GamePanel
	 * It means that one frame has passed and the screen needs to be updated
	 * @param g
	 */
	public abstract void update(Graphics2D g);
	
	public void addMouseInput(MouseInput mouse) {
		mouseInput = mouse;
	}

	public void addKeyboardInput(KeyboardInput keyboard) {
		keyInput = keyboard;
	}
	
	public static JFrame newFrame(Engine engine) {
		// Create a windowed frame
		JFrame frame = new JFrame("Lattice Demo Window - Created by Daniel Lam");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		frame.setSize(800, 600);
		frame.setResizable(false);
		frame.setBackground(Color.white);		
		
		//Create and add the game panel
		Panel panel = new Panel(engine, false);
		frame.getContentPane().add(panel);

		//Set up keyboard input, binding it to the frame
		KeyboardInput keyboardInput = new KeyboardInput(frame);
		engine.addKeyboardInput(keyboardInput);

		//Set up mouse input, binding it to the frame
		MouseInput mouseInput = new MouseInput(frame, panel);
		engine.addMouseInput(mouseInput);
		
		frame.setVisible(true);

		Mainloop mainloop = new Mainloop(frame);
		Thread t= new Thread(mainloop);
		t.start();
		//To obtain fps, we obtain how many frames are shown in one second	

		
		return frame;
	}

}


