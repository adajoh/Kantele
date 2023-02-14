package se.djax.kantele.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import se.djax.kantele.Entity;
import se.djax.kantele.model.Card;

/**
 * Represents a stack of cards with a position in 2D space. The stack can be
 * empty and cards can be added and removed.
 *
 */
public abstract class Stack extends Entity {

	/**
	 * How much the stack should "slide" down for each card if slide is used
	 */
	public static final float CARD_HEIGHT_DELTA = Card.HEIGHT / 4;

	private final List<Card> cards;

	/**
	 * The base y position for the bottom card
	 */
	private float baseY;

	/**
	 * If the stack should be drawn with the top if each card visible
	 */
	private boolean slide = true;

	/**
	 * A temporary list of for the moment picked cards that are removed from this
	 * stack
	 */
	private final List<Card> pickedCards;

	public Stack(float x, float y) {
		cards = new ArrayList<Card>();
		pickedCards = new ArrayList<Card>();

		setX(x);
		setY(y);
		super.setWidth(Card.WIDTH);
		super.setHeight(Card.HEIGHT);
	}

	public Stack(float x, float y, List<Card> initialCards) {
		this(x, y);
		cards.addAll(initialCards);
		updatePositions();
	}

	public Stack(float x, float y, List<Card> initialCards, boolean slide) {
		this(x, y);
		this.slide = slide;
		cards.addAll(initialCards);
		updatePositions();
	}

	@Override
	public final void setHeight(float h) {
		throw new UnsupportedOperationException("Stack height can not be changed");
	}

	@Override
	public final void setWidth(float w) {
		throw new UnsupportedOperationException("Stack width can not be changed");
	}

	public final void add(Card card) {
		if (!canAdd(card)) {
			throw new UnsupportedOperationException("Card can not be added to stack:" + card);
		}

		cards.add(card);
		updatePositions();
	}

	public final Card getTop() {
		if (isEmpty()) {
			throw new UnsupportedOperationException("Stack is empty");
		}
		return cards.get(size() - 1);
	}

	public final Card getBottom() {
		if (isEmpty()) {
			throw new UnsupportedOperationException("Stack is empty");
		}
		return cards.get(0);
	}

	public final Card removeTop() {
		if (isEmpty()) {
			throw new UnsupportedOperationException("Stack is empty");
		}
		return remove(size() - 1);
	}

	public final Card removeBottom() {
		if (isEmpty()) {
			throw new UnsupportedOperationException("Stack is empty");
		}
		return remove(0);
	}

	@Override
	public final void setY(float y) {
		this.baseY = y;
		updatePositions();
	}

	@Override
	public final void setX(float x) {
		super.setX(x);
		updatePositions();
	}

	/**
	 * @param card
	 *            the card which will be checked
	 * @return true if the card is allowed to be added on the stack according to the
	 *         stack rules
	 */
	public abstract boolean canAdd(Card card);

	private final void updatePositions() {
		updateBoundingBox();
		updateCardPositions();
	}

	/**
	 * Update the y coordinate of the stack (Which depends on the bottom left corner
	 * of the top card) and the total height of the stack (Depending on the number
	 * of cards if slide is being used)
	 */
	private final void updateBoundingBox() {
		if (slide) {
			super.setY(baseY - CARD_HEIGHT_DELTA * Math.max(0f, cards.size() - 1));
			super.setHeight(Card.HEIGHT + CARD_HEIGHT_DELTA * Math.max(0f, cards.size() - 1));
		} else {
			super.setY(baseY);
			super.setHeight(Card.HEIGHT);
		}
	}

	/**
	 * Update the coordinates of all cards in the stack based on the base Y of the
	 * stack and if slide is being used
	 */
	private final void updateCardPositions() {
		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);

			card.setX(getX());
			if (slide) {
				card.setY(baseY - CARD_HEIGHT_DELTA * i);
			} else {
				card.setY(baseY);
			}
		}
	}

	public final List<Card> getCards() {
		return Collections.unmodifiableList(cards);
	}

	/**
	 * Removes all cards in the stack beginning from firstCard and adds them to an
	 * internal list. Cards can be restored by calling {@link Stack#restore()}
	 * 
	 * @param firstCard
	 *            the bottom most card where we till start picking from
	 * @return the list of picked cards (The list is backed by the internal list in
	 *         the stack)
	 */
	public final List<Card> pickCards(Card firstCard) {

		if (!cards.contains(firstCard)) {
			throw new RuntimeException("Card not in stack!");
		}

		if (!pickedCards.isEmpty()) {
			throw new RuntimeException("There are already cards picked");
		}

		boolean cardFound = false;
		for (Iterator<Card> iterator = cards.iterator(); iterator.hasNext();) {
			Card card = iterator.next();
			if (card == firstCard) {
				cardFound = true;
			}
			if (cardFound) {
				pickedCards.add(card);
			}
		}

		cards.removeAll(pickedCards);
		updatePositions();

		return pickedCards;
	}

	/**
	 * Adds back all cards from the picked list to the stack. Warning: Rules are not
	 * taken into account
	 */
	public void restore() {
		cards.addAll(pickedCards);
		pickedCards.clear();
		updatePositions();
	}

	/**
	 * Remove all cards from this stack
	 */
	public final void removeAll() {
		cards.clear();
		updatePositions();
	}

	/**
	 * Remove the card at the specified index. Warning: The rules of the stack will
	 * not be taken into account
	 * 
	 * @param i
	 * @return the card that was removed
	 */
	protected Card remove(int i) {
		Card card = cards.remove(i);
		updatePositions();
		return card;
	}

	public final boolean isEmpty() {
		return cards.isEmpty();
	}

	/**
	 * @return the number of cards in the stack
	 */
	public final int size() {
		return cards.size();
	}

	/**
	 * see {@link Stack#slide}
	 */
	public final void setSlide(boolean slide) {
		this.slide = slide;
	}

}
