package se.djax.kantele.renderer;

import java.util.List;

import se.djax.kantele.Kantele;
import se.djax.kantele.model.Card;
import se.djax.kantele.model.Card.Rank;
import se.djax.kantele.model.Card.Suit;
import se.djax.kantele.stack.Stack;

/**
 * A generic implementation of the renderer used to draw the game to the screen
 * 
 *
 * @param <T>
 *            The type used for image data
 */
public abstract class Renderer<T> {

	// Internal target resolution
	public static final int RENDER_WIDTH = 1280;
	public static final int RENDER_HEIGHT = 720;

	// Card textures
	private T[][] cardTextures;
	private T cardBackTexture;
	private T restartTexture;
	private T winTexture;

	public Renderer() {
		loadImages();
	}

	@SuppressWarnings("unchecked")
	private final void loadImages() {
		// Unsafe cast but according to Effective Java; Item 26 the best way to create
		// an array of a generic type
		cardTextures = (T[][]) new Object[Suit.values().length][Rank.values().length];

		// Load all images from the disk according to the following naming format:
		// nyy.png where:
		// n = the suit (1=diamond,2=spades,3=hearts,4=Clubs
		// yy = the rank (01-13)
		for (int x = 0; x < cardTextures.length; x++) {
			for (int y = 0; y < cardTextures[x].length; y++) {
				cardTextures[x][y] = loadImage("cards/" + (((x + 1) * 100) + y + 1) + ".png");
			}
		}

		// Misc textures
		cardBackTexture = loadImage("cards/back7.png");
		restartTexture = loadImage("restart.png");
		winTexture = loadImage("win.png");
	}

	/**
	 * Main render method called every frame
	 * 
	 * @param kantele
	 */
	public final void render(Kantele kantele) {
		clearScreen();

		beginDrawShapes();
		renderStackGrids(kantele.getBoard().getStacks());
		endDrawShapes();

		beginDrawImages();
		renderRestartDeck(kantele.getBoard().getDeck());

		for (Stack stack : kantele.getBoard().getStacks()) {
			renderCards(stack);
		}

		if (kantele.getDragedCards() != null) {
			renderCards(kantele.getDragedCards());
		}

		if (kantele.getBoard().isWon()) {
			drawImage(winTexture, RENDER_WIDTH / 4, RENDER_HEIGHT / 4, RENDER_WIDTH / 2, RENDER_HEIGHT / 2);
		}

		endDrawImages();
	}

	/**
	 * Render the rectangle below all empty stacks
	 * 
	 * @param stacks
	 */
	private final void renderStackGrids(List<Stack> stacks) {
		for (Stack stack : stacks) {
			if (stack.isEmpty()) {
				drawRect(stack.getX(), stack.getY(), stack.getWidth(), stack.getHeight());
			}
		}
	}

	/**
	 * Draw the reload symbol below the deck
	 * 
	 * @param stack
	 */
	private final void renderRestartDeck(Stack stack) {
		drawImage(restartTexture, stack.getX() + stack.getWidth() / 4, stack.getY() + stack.getHeight() / 4, stack.getWidth() / 2, stack.getHeight() / 2);
	}

	/**
	 * 
	 * Helper method for drawing a whole stack of cards
	 * 
	 * @param stack
	 */
	private final void renderCards(Stack stack) {
		for (Card card : stack.getCards()) {
			renderCard(card);
		}
	}

	/**
	 * Draw a card to the screen with the correct image
	 * 
	 * @param card
	 */
	private final void renderCard(Card card) {
		T image = null;

		// Get the right texture
		if (card.isFaceDown()) {
			image = cardBackTexture;
		} else {
			image = cardTextures[card.getSuit().ordinal()][card.getRank().getValue() - 1];
		}

		// Draw to screen
		drawImage(image, card.getX(), card.getY(), card.getWidth(), card.getHeight());
	}

	/**
	 * 
	 * Draw an image at the specified position in world space
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	protected abstract void drawImage(T image, float x, float y, float width, float height);

	/**
	 * 
	 * Draw a rectangle (not filled) at the specified position in world space
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	protected abstract void drawRect(float x, float y, float width, float height);

	/**
	 * @param path
	 *            the relative path to the image
	 * @return an instance of the loaded image
	 */
	protected abstract T loadImage(String path);

	/**
	 * Replace everything on the screen to a solid color
	 */
	protected abstract void clearScreen();

	/**
	 * Called before any images are drawn
	 */
	protected abstract void beginDrawImages();

	/**
	 * Called after all images have been drawn
	 */
	protected abstract void endDrawImages();

	/**
	 * Called before any shapes are drawn
	 */
	protected abstract void beginDrawShapes();

	/**
	 * Called after all shapes have been drawn
	 */
	protected abstract void endDrawShapes();

}
