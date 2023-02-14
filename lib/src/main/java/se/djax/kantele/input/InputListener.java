package se.djax.kantele.input;

import se.djax.kantele.model.Card;
import se.djax.kantele.stack.Stack;

public interface InputListener {

	/**
	 * Called when a card is clicked
	 * 
	 * @param stack
	 *            the source stack of the card
	 * @param card
	 *            the clicked card
	 */
	public void onCardClicked(Stack stack, Card card);

	/**
	 * Same as {@link InputListener#onCardClicked(Stack, Card)} but for double click
	 * 
	 * @param stack
	 * @param card
	 */
	public void onCardDoubleClicked(Stack stack, Card card);

	/**
	 * Called when a stack is clicked
	 * 
	 * @param stack
	 *            the stack that was clicked
	 */
	public void onStackClicked(Stack stack);

	/**
	 * Called when a drag is started
	 * 
	 * @param stack
	 *            the source stack
	 * @param card
	 *            the source card
	 */
	public void onDraggedStart(Stack stack, Card card);

	/**
	 * Called when a drag is stopped
	 * 
	 * @param stack
	 *            the stack where the drag is stopped if any
	 */
	public void onDraggedStop(Stack stack);

}
