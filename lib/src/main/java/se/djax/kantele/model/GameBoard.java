package se.djax.kantele.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.djax.kantele.model.Card.Rank;
import se.djax.kantele.model.Card.Suit;
import se.djax.kantele.renderer.Renderer;
import se.djax.kantele.stack.Foundation;
import se.djax.kantele.stack.Pile;
import se.djax.kantele.stack.Stack;
import se.djax.kantele.stack.Tableau;

/**
 * Represents the data of the game with all the stacks and cards. The positions
 * of the stacks are somewhat hardcoded for optimal layout.
 *
 */
public class GameBoard {

	private static final int TABLEAU_COUNT = 7;

	private Stack deck;
	private final Stack pickedDeck;
	private final Stack[] tableau;
	private final Stack[] foundations;

	/**
	 * A helper list containing all the stacks in the game
	 */
	private final List<Stack> stacks;

	public GameBoard() {
		stacks = new ArrayList<Stack>();
		tableau = new Tableau[TABLEAU_COUNT];

		// Create as many foundations as there are suits in a deck of cards
		foundations = new Foundation[Suit.values().length];

		createFoundations();
		createDeck(Card.createStandardDeck(true));

		pickedDeck = new Pile(Renderer.RENDER_WIDTH - Card.WIDTH * 2.2f, Card.HEIGHT / 2, true, new ArrayList<Card>());
		stacks.add(pickedDeck);

		addCardsToTableaus();
	}

	/**
	 * Create the four foundations
	 */
	private void createFoundations() {
		foundations[0] = new Foundation(Renderer.RENDER_WIDTH - Card.WIDTH, Renderer.RENDER_HEIGHT - Card.HEIGHT);
		stacks.add(foundations[0]);
		foundations[1] = new Foundation(Renderer.RENDER_WIDTH - Card.WIDTH * 2.2f, Renderer.RENDER_HEIGHT - Card.HEIGHT);
		stacks.add(foundations[1]);
		foundations[2] = new Foundation(Renderer.RENDER_WIDTH - Card.WIDTH, Renderer.RENDER_HEIGHT - Card.HEIGHT * 2.2f);
		stacks.add(foundations[2]);
		foundations[3] = new Foundation(Renderer.RENDER_WIDTH - Card.WIDTH * 2.2f, Renderer.RENDER_HEIGHT - Card.HEIGHT * 2.2f);
		stacks.add(foundations[3]);
	}

	private void createDeck(List<Card> cards) {
		deck = new Pile(Renderer.RENDER_WIDTH - Card.WIDTH, Card.HEIGHT / 2, false, cards);
		stacks.add(deck);
	}

	/**
	 * Add cards from left to right to the tableaus. Number of cards increases with
	 * every tableau
	 */
	private void addCardsToTableaus() {
		for (int pile = 0; pile < tableau.length; pile++) {

			List<Card> cards = new ArrayList<Card>();

			// Facedown cards
			for (int card = 0; card < pile; card++) {
				Card c = deck.removeBottom();
				cards.add(c);
			}

			// One card face up
			Card card = deck.removeBottom();
			card.setFaceDown(false);
			cards.add(card);

			tableau[pile] = new Tableau(Card.WIDTH * 1.20f * pile, Renderer.RENDER_HEIGHT - Card.HEIGHT, cards);
			stacks.add(tableau[pile]);
		}
	}

	public Stack getPickedDeck() {
		return pickedDeck;
	}

	public List<Stack> getStacks() {
		return stacks;
	}

	public Stack[] getFoundations() {
		return foundations;
	}

	public Stack getDeck() {
		return deck;
	}

	/**
	 * Called when all cards have been moved from the deck to the picked deck
	 */
	public void resetDeck() {
		// A new deck will be created therefore remove the current one
		stacks.remove(deck);

		// Get all cards from the picked deck and reverse the order
		List<Card> cards = new ArrayList<Card>(pickedDeck.getCards());
		Collections.reverse(cards);

		// Set all the cards to facedown again
		for (Card card : cards) {
			card.setFaceDown(true);
		}

		// Create the new deck and add it to the board
		createDeck(cards);

		// Finally remove all cards from the picked deck
		pickedDeck.removeAll();
	}

	/**
	 * @return true if every card is in the foundation stacks
	 */
	public boolean isWon() {
		int cards = 0;
		for (Stack stack : foundations) {
			cards += stack.size();
		}

		return cards == Suit.values().length * Rank.values().length;
	}

}
