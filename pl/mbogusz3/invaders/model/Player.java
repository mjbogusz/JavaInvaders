package pl.mbogusz3.invaders.model;

/**
 *
 */
public class Player {
	private int health;
	private final static double width = 0.05;
	private final double halfWidth;
	private final static double speed = 0.4;
	private double position;

	public Player(int health) {
		this.position = 0.5;
		this.health = health;
		this.halfWidth = this.width / 2;
	}

	/**
	 * Move the player object.
	 * @param direction -1: to the left, 1: to the right
	 * @param time fraction of second the player was moving
	 */
	public void move(int direction, double time) {
		this.position += direction * (this.speed / time);
		if(this.position < this.halfWidth) {
			this.position = this.halfWidth;
		} else if(this.position > (1.0 - this.halfWidth)) {
			this.position = (1.0 - this.halfWidth);
		}
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public double getPosition() {
		return this.position;
	}

	public double getWidth() {
		return this.width;
	}

	public int getHealth() {
		return this.health;
	}
}