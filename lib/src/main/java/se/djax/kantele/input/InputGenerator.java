package se.djax.kantele.input;

import se.djax.kantele.Vector2;

public interface InputGenerator {

	/**
	 * @return should return true when screen are touched/mouse button is held down
	 */
	boolean isTouched();

	/**
	 * @return the position of touched point/mouse pointer in internal target
	 *         resolution space
	 */
	Vector2 getPointerInWorldCoordinates();

}
