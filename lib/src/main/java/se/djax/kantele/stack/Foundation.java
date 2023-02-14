package se.djax.kantele.stack;

import se.djax.kantele.model.Card;
import se.djax.kantele.model.Card.Rank;

/**
 * A type of stack used for the 4 stacks where cards are added by suit
 *
 */
public class Foundation extends Stack {

	public Foundation(float x, float y) {
		super(x, y);
		setSlide(false);
	}

	@Override
	public boolean canAdd(Card card) {

		// Does not make sense to add a facedown card
		if (card.isFaceDown()) {
			return false;
		}

		// The first card must be an ace
		if (isEmpty() && card.getRank() == Rank.Ace) {
			return true;
		}

		// Standard rule, new card must be of same suit and exactly on rank above the
		// top card
		if (!isEmpty()) {
			Card top = getTop();
			if (card.getSuit() == top.getSuit() && card.getRank().getValue() == top.getRank().getValue() + 1) {
				return true;
			}
		}

		// Default false
		return false;
	}

}
