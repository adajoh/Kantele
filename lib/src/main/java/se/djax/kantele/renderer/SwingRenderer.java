package se.djax.kantele.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import se.djax.kantele.Vector2;
import se.djax.kantele.input.InputGenerator;

/**
 * An implementation of a renderer using Swing
 *
 */
public class SwingRenderer extends Renderer<BufferedImage> implements InputGenerator {

	private final BufferedImage buffer;
	private final Graphics graphics;
	private boolean isTouched;
	private final Vector2 pointer;
	private final Component gamePanel;
	private final Color backgroundColor;

	/**
	 * @param gamePanel
	 *            the component which will be drawn on
	 */
	public SwingRenderer(final Component gamePanel) {
		this.buffer = new BufferedImage(RENDER_WIDTH, RENDER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.gamePanel = gamePanel;
		this.graphics = buffer.getGraphics();
		this.pointer = new Vector2();
		this.backgroundColor = new Color(0, 150, 0);

		gamePanel.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				updatePointerLocation(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				updatePointerLocation(e);
			}
		});

		gamePanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				isTouched = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				isTouched = false;
			}

		});
	}

	/**
	 * Update the location of the pointer in world space
	 * 
	 * @param e
	 */
	private void updatePointerLocation(MouseEvent e) {
		pointer.set(e.getX() * getScreenToWorldFactorX(), transformY(e.getY() * getScreenToWorldFactorY(), 0));
	}

	/**
	 * @return the difference between the internal target resolution and the drawing
	 *         surface
	 */
	private float getScreenToWorldFactorX() {
		return (buffer.getWidth() + 0f) / gamePanel.getWidth();
	}

	/**
	 * @return the difference between the internal target resolution and the drawing
	 *         surface
	 */
	private float getScreenToWorldFactorY() {
		return (buffer.getHeight() + 0f) / gamePanel.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#getPointerInWorldCoordinates()
	 */
	@Override
	public Vector2 getPointerInWorldCoordinates() {
		return pointer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#isTouched()
	 */
	@Override
	public boolean isTouched() {
		return isTouched;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#drawImage(java.lang.Object, float, float,
	 * float, float)
	 */
	@Override
	protected void drawImage(BufferedImage image, float x, float y, float width, float height) {
		y = transformY(y, height);
		graphics.drawImage(image, (int) x, (int) y, (int) width, (int) height, gamePanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#loadImage(java.lang.String)
	 */
	@Override
	protected BufferedImage loadImage(String path) {
		File file = new File(path);
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(gamePanel, "Could not load image:" + file.getAbsolutePath() + "\n is your working directory correctly set?");
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#clearScreen()
	 */
	@Override
	protected void clearScreen() {
		graphics.setColor(backgroundColor);
		graphics.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#beginDrawImages()
	 */
	@Override
	protected void beginDrawImages() {
		// Nothing needs to be done here with this renderer

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#endDrawImages()
	 */
	@Override
	protected void endDrawImages() {
		gamePanel.getGraphics().drawImage(buffer, 0, 0, gamePanel.getWidth(), gamePanel.getHeight(), gamePanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#drawRect(float, float, float, float)
	 */
	@Override
	protected void drawRect(float x, float y, float width, float height) {
		y = transformY(y, height);
		graphics.drawRect((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * The origin in Swing is in the top left corner. This method transforms a
	 * Y-coordinate so that the origin is in the bottom left corner
	 * 
	 * @param y
	 * @param height
	 * @return
	 */
	private float transformY(float y, float height) {
		return buffer.getHeight() - y - height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#beginDrawShapes()
	 */
	@Override
	protected void beginDrawShapes() {
		graphics.setColor(Color.BLACK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.Renderer#endDrawShapes()
	 */
	@Override
	protected void endDrawShapes() {
		// Nothing needs to be done here with this renderer

	}

}
