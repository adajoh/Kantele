package se.djax.kantele;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import se.djax.kantele.renderer.Renderer;
import se.djax.kantele.renderer.SwingRenderer;

/**
 * Top level class of the game using Swing for rendering
 */
public class SwingStarter extends JFrame implements ActionListener {

	private static final long serialVersionUID = -3138746293767211685L;
	private final Kantele kantele;

	/**
	 * How often the game should update in milliseconds
	 */
	private final int TICK_RATE = 16;

	public static void main(String[] args) {
		new SwingStarter();
	}

	public SwingStarter() {
		setSize(Renderer.RENDER_WIDTH, Renderer.RENDER_HEIGHT);
		setLocation(200, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);
		setTitle("Kantele");

		final JPanel gamePanel = new JPanel();
		add(gamePanel, BorderLayout.CENTER);

		SwingRenderer swingRenderer = new SwingRenderer(gamePanel);
		kantele = new Kantele(swingRenderer, swingRenderer);
		Timer timer = new Timer(TICK_RATE, this);
		timer.start();
	}

	/*
	 * Update end render the game every TICK_RATE
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		kantele.update(TICK_RATE / 1000f);
		kantele.render();
	}

}
