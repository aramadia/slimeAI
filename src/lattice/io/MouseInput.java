/**
 * Author: Daniel Lam
 * Creation Date: Apr 17, 2007
 * Filename: MouseInput.java
 * Project: Deadly Creations Game Engine
 *
 * History:
 * Version 1.0 (Apr 17, 2007)
 */
package lattice.io;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import lattice.util.Point2i;


/**
 * MouseInput receives messages from the Java event based message system and
 * converts it to an on demand poll system.  It records the position of the 
 * mouse and remembers when the mouse is pressed.
 * @author aramadia
 *
 */
public class MouseInput implements MouseMotionListener, MouseListener, FocusListener {

	private Point2i pos;	// position of the mouse
	private boolean[] mouseDown;
	boolean clicked = false;
	/** True to indicates a new even has been passed */
	private boolean[] mousePressed;
	/** Mouse just released */
	private boolean[] mouseReleased;
	
	private static final int LEFT = 1;
	private static final int MIDDLE = 2;
	private static final int RIGHT = 3;
	
	/**
	 * Attaches the mouse input device to a window
	 *
	 */
	public MouseInput(Component frame, Component comp) {
		this(comp);
		frame.addFocusListener(this);		
		
		
	}
	
	/**
	 * Attaches the mouse input to a component without focus features
	 * @param comp
	 */
	public MouseInput(Component comp) {
		pos = new Point2i();
		mouseDown = new boolean[4];
		mousePressed = new boolean[4];
		mouseReleased = new boolean[4];
		
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
		
	}
	
	/**
	 * This will return the current state of the mouse by making a copy
	 * of the point
	 * @return
	 */
	public Point2i getPos() {
		return new Point2i(pos.x, pos.y);
	}
	
	public int getX() {
		return pos.x;
	}
	
	public int getY() {
		return pos.y;
	}
	
	/**
	 * Returns true if the mouse was just clicked.
	 * However, only true once.
	 * @return
	 */
	public boolean leftPressed() {
		if ( mousePressed[LEFT]) {
			mousePressed[LEFT] = false;
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if mouse was just released.  Only once though.
	 * @return
	 */
	public boolean leftReleased() {
		if ( mouseReleased[LEFT]) {
			mouseReleased[LEFT] = false;
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the mouse was just clicked.
	 * However, only true once.
	 * @return
	 */
	public boolean rightPressed() {
		if ( mousePressed[RIGHT]) {
			mousePressed[RIGHT] = false;
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if mouse was just released.  Only once though.
	 * @return
	 */
	public boolean rightReleased() {
		if ( mouseReleased[RIGHT]) {
			mouseReleased[RIGHT] = false;
			return true;
		}
		return false;
	}
	
	/**
	 * Indicates a new click event
	 * @return
	 */
	public boolean click() {
		if (clicked) {
			clicked = false;
			return true;
			
		}
		return false;
	}
	
	/**
	 * True if the left mouse is down
	 * @return
	 */
	public boolean left() {
		return mouseDown[LEFT];
	}
	
	/**
	 * True if the middle mouse is down
	 * @return
	 */
	public boolean middle() {
		return mouseDown[MIDDLE];
	}
	
	/**
	 * True if the right mouse is down
	 * @return
	 */
	public boolean right() {
		return mouseDown[RIGHT];
	}
	
	public void resetButtons() {
		for (int i = 0; i < mouseDown.length; i++) {
			mouseDown[i] = false;
			mouseReleased[i] = false;
			mousePressed[i] = false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		pos.x = e.getX();
		pos.y = e.getY();

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		pos.x = e.getX();
		pos.y = e.getY();

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		pos.x = e.getX();
		pos.y = e.getY();
		

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		mouseDown[e.getButton()] = true;
		mousePressed[e.getButton()] = true;
		clicked = true;

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		mouseDown[e.getButton()] = false;
		mouseReleased[e.getButton()] = true;

	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {		
		resetButtons();

	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		System.out.println("Lost focus");
		resetButtons();

	}

}
