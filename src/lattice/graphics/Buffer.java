package lattice.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * A class to handle off screen buffering aka double buffering
 * This is useful if you are doing graphic programs with
 * a high refresh rate.  The screen flicker can be effectively
 * eliminated.
 * @author danny
 *
 */
public class Buffer {

	private BufferedImage buffer; //This holds the buffer
	private Graphics2D bufferGFX; //This holds the graphics context

	/**
	 * Creates an offscreen buffer at a certain size
	 * It also sets the graphics for high quality rendering
	 * @param width
	 * @param height
	 */
	public Buffer(int width, int height) {

		
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		bufferGFX = buffer.createGraphics();
		
		//Turn on buffer to maximum quality graphics
		bufferGFX.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		bufferGFX.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	/**
	 * Returns the width of the buffer
	 * @return
	 */
	public int getWidth() {
		return buffer.getWidth();
	}
	/**
	 * Returns the height of the buffer
	 * @return
	 */
	public int getHeight() {
		return buffer.getHeight();
	}
	
	/**
	 * This turns on anti-aliasing yet results in a loss of speed
	 */
	public void highQuality() {
		bufferGFX.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
	}

	/**
	 * This saves the image to the hard drive
	 * @param Filename
	 */
	public void saveImage(String Filename) {
		File outputFile = new File(Filename + ".png");
		try {
			ImageIO.write(buffer, "PNG"	,outputFile );
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Uses the Graphics to draw
	 * @return The Graphics2D to draw with
	 */
	public Graphics2D getGraphics() {
		return bufferGFX;
	}

	/**
	 * This draws a single pixel to the buffer
	 * @param x
	 * @param y
	 * @param c
	 */
	public void drawPixel(int x, int y, Color c) {
		
		buffer.setRGB(x, y, c.getRGB());

	}


	/**
	 * This draws at the top left corner of the screen
	 * @param g
	 */
	public void draw(Graphics g) {
		g.drawImage(buffer, 0, 0, null);

	}

	/**
	 * This clears the buffer screen
	 * @param c Color to fill the screen
	 */
	public void clear(Color c) {
		bufferGFX.setColor(c);
		
		bufferGFX.fillRect(0, 0, getWidth(), getHeight());

	}

	/**
	 * This draws the image transparently
	 * This is useful to wash out an image
	 * @param g
	 * @param alpha
	 */
	public void drawTransparent(Graphics g, float alpha) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setComposite(makeComposite(alpha));
		g.drawImage(buffer, 0, 0, null);
	}

	/**
	 * Creates an alpha composite based on the alpha value
	 * @param alpha
	 * @return
	 */
	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return(AlphaComposite.getInstance(type, alpha));
	}

}
