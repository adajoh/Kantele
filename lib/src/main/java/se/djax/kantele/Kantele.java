package se.djax.kantele;

import se.djax.kantele.input.InputGenerator;
import se.djax.kantele.input.InputListener;
import se.djax.kantele.input.InputManager;
import se.djax.kantele.model.Card;
import se.djax.kantele.model.GameBoard;
import se.djax.kantele.renderer.Renderer;
import se.djax.kantele.stack.Foundation;
import se.djax.kantele.stack.PickedStack;
import se.djax.kantele.stack.Stack;

/**
 * Main class of the game
 *
 */
public class Kantele implements InputListener {

	private static final float WINSCREEN_TIME = 1.5f;

	private GameBoard board;
	private final Renderer<?> renderer;
	private final InputManager inputManager;

	private PickedStack dragedCards;
	private float winTimer;

	public Kantele(Renderer<?> renderer, InputGenerator inputGenerator) {
		this.renderer = renderer;
		this.inputManager = new InputManager(inputGenerator);
		reset();
	}

	/**
	 * Reset the game to initial state
	 */
	public void reset() {
		board = new GameBoard();
		winTimer = 0;
	}

	/**
	 * 
	 * Run the game logic one tick
	 * 
	 * @param delta
	 *            time since last update in seconds
	 * 
	 */
	public void update(float delta) {
		inputManager.update(delta, this);
		inputManager.updateHovers(board.getStacks());

		if (dragedCards != null) {
			updateDragedCardsPosition();
		}

		checkWinCondition(delta);
	}

	private void checkWinCondition(float delta) {
		if (board.isWon()) {
			winTimer += delta;
			if (winTimer > WINSCREEN_TIME) {
				reset();
			}
		}
	}

	/**
	 * Set the draged cards if any to the position of the mouse
	 * 
	 */
	private void updateDragedCardsPosition() {
		Entity pointer = inputManager.getPointer();
		dragedCards.setX(pointer.getX() - dragedCards.getWidth() / 2);
		dragedCards.setY(pointer.getY() - Card.HEIGHT + Stack.CARD_HEIGHT_DELTA / 2);
	}

	public Stack getDragedCards() {
		return dragedCards;
	}

	/**
	 * Render the game
	 */
	public void render() {
		renderer.render(this);
	}

	public GameBoard getBoard() {
		return board;
	}

	public Renderer<?> getRenderer() {
		return renderer;
	}

	/**
	 * 
	 * Attempt to add a card automatically to any foundation that accepts it
	 * 
	 * @param stack
	 *            source stack of the card
	 * @param card
	 */
	private void autoAddFoundation(Stack stack, Card card) {
		for (Stack foundation : board.getFoundations()) {
			if (foundation.canAdd(card)) {
				foundation.add(card);
				stack.removeTop();
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.input.InputListener#onCardClicked(se.djax.kantele.stack.
	 * Stack, se.djax.kantele.Card)
	 */
	@Override
	public void onCardClicked(Stack stack, Card card) {
		// Picka a card from the deck
		if (stack == board.getDeck()) {
			stack.removeTop();
			card.setFaceDown(false);
			board.getPickedDeck().add(card);
		} else {
			// Turn a card from the tableaus face up
			if (card.isFaceDown() && stack.getTop() == card) {
				card.setFaceDown(false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.djax.kantele.input.InputListener#onDraggedStop(se.djax.kantele.stack.
	 * Stack)
	 */
	@Override
	public void onDraggedStop(Stack stack) {
		if (dragedCards != null) {

			// Make sure we dragged to a stack and it is not the picked deck
			if (stack != null && stack != board.getPickedDeck()) {

				// Foundation stacks should only accept one card at a time
				if (!(stack instanceof Foundation) || dragedCards.size() == 1) {
					while (!dragedCards.isEmpty()) {

						// Get the bottom card from the draged stack
						Card card = dragedCards.getBottom();

						// Add the card to the target stack of possible
						if (stack.canAdd(card)) {
							stack.add(card);
							dragedCards.removeBottom();
						} else {
							break;
						}
					}
				}
			}

			// Restore any left over cards to source stack
			dragedCards.restore();
			dragedCards = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * se.djax.kantele.input.InputListener#onStackClicked(se.djax.kantele.stack.
	 * Stack)
	 */
	@Override
	public void onStackClicked(Stack stack) {

		// Restore all cards from the picked deck if the deck is empty
		if (stack == board.getDeck() && stack.isEmpty()) {
			board.resetDeck();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * se.djax.kantele.input.InputListener#onDraggedStart(se.djax.kantele.stack.
	 * Stack, se.djax.kantele.Card)
	 */
	@Override
	public void onDraggedStart(Stack stack, Card card) {

		// Create a picked stack starting from the clicked card as long as it is not the
		// deck
		if (stack != board.getDeck()) {
			dragedCards = new PickedStack(stack, card);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * se.djax.kantele.input.InputListener#onCardDoubleClicked(se.djax.kantele.stack
	 * .Stack, se.djax.kantele.Card)
	 */
	@Override
	public void onCardDoubleClicked(Stack stack, Card card) {

		// Attempt to automatically add the clicked card to a foundation stack
		if (stack != board.getDeck() && stack.getTop() == card && !card.isFaceDown()) {
			autoAddFoundation(stack, card);
		}
	}

}
