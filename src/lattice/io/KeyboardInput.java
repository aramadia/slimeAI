/**
 * 
 */
package lattice.io;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author aramadia
 * This converts the event-triggered keyboard input into a static poll method
 * It remembers when the key is pressed, and it will contineu to report it as pressed
 * If it loses the focus, it will not continue to process keyevents as keydown
 * TODO fix shiftDown
 */
public class KeyboardInput implements KeyListener, FocusListener {

	//Remembers which keys are down.  True = key down  False = key up
	/**
	 * True when the key is down at the point of time
	 */
	private boolean[] keyDown = new boolean[1024];

	/**
	 * True when the key has not been processed
	 */
	private boolean[] keyEvent = new boolean[1024];
	
	/** Indicates this modifier key has been pressed */
	public boolean ctrlDown, altDown, shiftDown;
	
	private static boolean DEBUG = false;


	/**
	 * Automatically adds the listeners
	 * @param frame
	 */
	public KeyboardInput(Component comp) {
		comp.addKeyListener(this);
		comp.addFocusListener(this);
		resetKeys();
	}

	/**
	 * This makes a copy of the keys pushed down
	 * @return
	 */
	public boolean[] poll() {
		boolean[] keys = new boolean[keyDown.length];

		System.arraycopy(keyDown, 0, keys, 0, keyDown.length);
		return keys;
	}


	/**
	 * Is true if the user just presses the key (true only once)
	 * @param keyCode
	 * @return
	 */
	public boolean keyDown(int keyCode) {

		if (keyEvent[keyCode]) {
			keyEvent[keyCode] = false;
			return true;
		}
		return false;
	}

	/**
	 * Resets all the keys to the up position
	 *
	 */
	public void resetKeys() {
		for (int i = 0; i < keyDown.length; i++) {
			keyDown[i] = false;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {

		if (DEBUG) System.out.println(e);
		if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0 ) {
			ctrlDown = true;
		}
		else if ((e.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) > 0) {
			altDown = true;
		}
		else if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) > 0) {
			shiftDown = true;
		}
		//Presses the key down
		keyDown[e.getKeyCode()] = true;
		keyEvent[e.getKeyCode()] = true;
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if (DEBUG) System.out.println(e);
		if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) == 0 ) {

			ctrlDown = false;
		}
		else if ((e.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) == 0) {
			altDown = false;
		}
		else if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK)== 0) {
			shiftDown = false;
		}
		// Release the key to the up position
		keyDown[e.getKeyCode()] = false;

	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
		// Don't do anything here
		if (DEBUG) System.out.println(e);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
		if (DEBUG) System.out.println(e);
		// Put all keys into the up position
		resetKeys();

	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		if (DEBUG) System.out.println(e);
		// In order to prevent keys from triggering movement when the screen is deactivated
		resetKeys();

	}
	
	

}
