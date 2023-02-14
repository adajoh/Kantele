package se.djax.kantele.input;

import java.util.List;

import se.djax.kantele.Entity;
import se.djax.kantele.Vector2;
import se.djax.kantele.model.Card;
import se.djax.kantele.stack.Stack;

public class InputManager {

	/**
	 * Minimum distance for a drag to be started
	 */
	private static final float DRAGGED_TOLERANCE = 10;

	/**
	 * The maximum time between clicks to be registered as a double click
	 */
	private static final float DOUBLE_CLICK_TOLERANCE = 1;

	private final Entity pointer;
	private final InputGenerator inputGenerator;

	/**
	 * The current card at the pointer location (If none = null)
	 */
	private Card hoverCard;

	/**
	 * The current stack at the pointer location (If none = null)
	 */
	private Stack hoverStack;

	// Old states used for detecting rising/falling edges
	private boolean isDragged;
	private boolean isDraggedOld;
	private boolean isTouchedOld;

	// Helper variables used for detecting correct event
	private final Vector2 clickStartPosition;
	private float timeSinceClicked;

	public InputManager(InputGenerator inputGenerator) {
		this.pointer = new Entity();
		this.inputGenerator = inputGenerator;
		clickStartPosition = new Vector2();

		// Just set sizes to something small
		pointer.setHeight(1);
		pointer.setWidth(1);
	}

	/**
	 * Main method called every frame
	 * 
	 * @param delta
	 *            the time since the last update in seconds
	 */
	public void update(float delta, InputListener listener) {

		updatePointer();
		updateTouchStatus(delta, listener);
		updateDraggedStatus(listener);

		// Save state of last frame
		isTouchedOld = inputGenerator.isTouched();
		isDraggedOld = isDragged;
	}

	/**
	 * Update pointer to current pointer location
	 * 
	 * @param renderer
	 */
	private void updatePointer() {
		Vector2 pos = inputGenerator.getPointerInWorldCoordinates();
		pointer.setX(pos.getX());
		pointer.setY(pos.getY());
	}

	/**
	 * Update current card/stack under the pointer if any
	 */
	public void updateHovers(List<Stack> stacks) {
		hoverCard = null;
		hoverStack = null;

		for (Stack stack : stacks) {
			if (stack.colliding(pointer)) {
				hoverStack = stack;
				for (Card card : stack.getCards()) {
					if (card.colliding(pointer)) {
						hoverCard = card;
					}
				}
			}
		}
	}

	/**
	 * Detect and fire click events
	 * 
	 * @param delta
	 *            time since last frame
	 * @param renderer
	 */
	private void updateTouchStatus(float delta, InputListener listener) {
		timeSinceClicked += delta;

		// on touch down
		if (inputGenerator.isTouched() && !isTouchedOld) {
			clickStartPosition.set(pointer.getX(), pointer.getY());
		}

		// On touch up
		if (!inputGenerator.isTouched() && isTouchedOld) {
			if (!isDraggedOld && hoverStack != null) {

				// Clicked on a stack
				listener.onStackClicked(hoverStack);
				if (hoverCard != null) {

					// Clicked on a card
					listener.onCardClicked(hoverStack, hoverCard);

					// Was it a double click?
					if (timeSinceClicked < DOUBLE_CLICK_TOLERANCE) {
						listener.onCardDoubleClicked(hoverStack, hoverCard);
					}

					timeSinceClicked = 0;
				}
			}
		}
	}

	/**
	 * Detect and fire drag events
	 * 
	 * @param renderer
	 */
	private void updateDraggedStatus(InputListener listener) {
		isDragged = inputGenerator.isTouched() && (clickStartPosition.distanceTo(pointer.getX(), pointer.getY()) > DRAGGED_TOLERANCE || isDragged);
		// Start drag
		if (isDragged && !isDraggedOld) {
			if (hoverStack != null && hoverCard != null) {
				listener.onDraggedStart(hoverStack, hoverCard);
			}
		}

		// Stop drag
		if (!isDragged && isDraggedOld) {
			listener.onDraggedStop(hoverStack);
		}
	}

	public Entity getPointer() {
		return pointer;
	}

}
