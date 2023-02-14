package se.djax.kantele;

/**
 * A simple entity with a position and a width and height
 *
 */
public class Entity {

	private final Vector2 position = new Vector2();
	private float width;
	private float height;

	public float getX() {
		return position.getX();
	}

	public float getY() {
		return position.getY();
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setX(float x) {
		position.setX(x);
	}

	public void setY(float y) {
		position.setY(y);
	}

	public void setWidth(float w) {
		this.width = w;
	}

	public void setHeight(float h) {
		this.height = h;
	}

	/**
	 * @param e
	 *            the other Entity
	 * @return true if the entities are colliding
	 */
	public boolean colliding(Entity e) {
		return position.getX() < e.getX() + e.getWidth() && position.getX() + width > e.getX() && position.getY() < e.getY() + e.getHeight() && position.getY() + height > e.getY();
	}

}
