package se.djax.kantele;

/**
 * 
 * Represent a position in 2D-space
 *
 */
public class Vector2 {

	private float x;
	private float y;

	public Vector2() {
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param x
	 * @param y
	 * @return the distance to the other point from this
	 */
	public float distanceTo(float x, float y) {
		float xLenght = x - this.x;
		float yLenght = y - this.y;
		return (float) Math.sqrt(xLenght * xLenght + yLenght * yLenght);
	}

}
