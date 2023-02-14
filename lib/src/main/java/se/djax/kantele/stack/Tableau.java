package se.djax.kantele.stack;

import java.util.List;

import se.djax.kantele.model.Card;
import se.djax.kantele.model.Card.Rank;

/**
 * 
 * The stack used for the 7 initial cardholding stacks
 * 
 *
 */
public class Tableau extends Stack {

	public Tableau(float x, float y, List<Card> initialCards) {
		super(x, y, initialCards);
	}

	@Override
	public boolean canAdd(Card card) {

		// Not allowed to move around face down cards
		if (card.isFaceDown()) {
			return false;
		}

		// First card must be a king
		if (isEmpty()) {
			return card.getRank() == Rank.King;
		}

		Card top = getTop();

		// Do not allow a card to be added if the player has not flipped the topmost
		// card faceup
		if (top.isFaceDown()) {
			return false;
		}

		// Standard rule, every other color and exactly one rank above the topmost card
		if (card.getSuit().getColor() != top.getSuit().getColor() && card.getRank().getValue() == top.getRank().getValue() - 1) {
			return true;
		}

		// Default false
		return false;
	}

}
