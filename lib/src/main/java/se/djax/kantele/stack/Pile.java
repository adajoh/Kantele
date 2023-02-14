package se.djax.kantele.stack;

import java.util.List;

import se.djax.kantele.model.Card;

/**
 * 
 * Simple stack with configurable boolean rule
 *
 */
public class Pile extends Stack {

	private final boolean canAdd;

	public Pile(float x, float y, boolean canAdd, List<Card> initialCards) {
		super(x, y, initialCards, false);
		this.canAdd = canAdd;
	}

	@Override
	public boolean canAdd(Card card) {
		return canAdd;
	}

}
