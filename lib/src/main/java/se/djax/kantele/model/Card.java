package se.djax.kantele.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.djax.kantele.Entity;
import se.djax.kantele.renderer.Renderer;

/**
 * A playing card
 *
 */
public class Card extends Entity {

	public static final float WIDTH = Renderer.RENDER_WIDTH / 11f;
	public static final float HEIGHT = WIDTH * 1.5f;

	public enum Suit {
		Diamond(Color.Red), Spades(Color.Black), Hearts(Color.Red), Clubs(Color.Black);

		private Color color;

		private Suit(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}
	}

	private enum Color {
		Red, Black;
	}

	public enum Rank {
		Ace(1), N2(2), N3(3), N4(4), N5(5), N6(6), N7(7), N8(8), N9(9), N10(10), Jack(11), Queen(12), King(13);

		private int value;

		private Rank(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private final Suit suit;
	private final Rank rank;
	private boolean faceDown;

	private Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;

		super.setWidth(Card.WIDTH);
		super.setHeight(Card.HEIGHT);
	}

	@Override
	public final void setHeight(float h) {
		throw new UnsupportedOperationException("Card size can not be changed");
	}

	@Override
	public final void setWidth(float w) {
		throw new UnsupportedOperationException("Card size can not be changed");
	}

	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}

	public void setFaceDown(boolean b) {
		this.faceDown = b;
	}

	public boolean isFaceDown() {
		return faceDown;
	}

	@Override
	public String toString() {
		return rank + " of " + suit;
	}

	/**
	 * @param shuffle
	 *            if the deck should be suffled
	 * @return a standard deck of 52 cards
	 */
	public static List<Card> createStandardDeck(boolean shuffle) {
		List<Card> cards = new ArrayList<Card>();

		// Create a card for every suit and rank
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				card.setFaceDown(true);
				cards.add(card);
			}
		}

		if (shuffle) {
			Collections.shuffle(cards);
		}

		return cards;
	}

}
