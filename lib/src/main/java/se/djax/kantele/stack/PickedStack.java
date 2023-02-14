package se.djax.kantele.stack;

import java.util.List;

import se.djax.kantele.model.Card;

/**
 * 
 * Special stack used for moving cards between stacks
 *
 */
public class PickedStack extends Stack {

	private final Stack sourceStack;
	private final List<Card> pickedCards;

	public PickedStack(Stack sourceStack, Card firstCard) {
		super(0, 0);

		this.sourceStack = sourceStack;
		pickedCards = sourceStack.pickCards(firstCard);
		for (Card card : pickedCards) {
			add(card);
		}
	}

	@Override
	public Card remove(int i) {
		pickedCards.remove(i);
		return super.remove(i);
	}

	@Override
	public void restore() {
		sourceStack.restore();
		removeAll();
	}

	@Override
	public boolean canAdd(Card card) {
		return true;
	}

}
