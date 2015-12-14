package pl.mbogusz3.invaders.model;

/**
 *
 */
public class Player {
	private final static double width = 0.1;
	private final static double speed = 0.4;
	private final int maxHealth;
	private final double halfWidth;
	private int health;
	private double position;

	public Player() {
		this(3);
	}

	public Player(int health) {
		this.position = 0.5;
		this.maxHealth = health;
		this.health = health;
		this.halfWidth = width / 2;
	}

	/**
	 * Move the player object.
	 * @param direction -1: to the left, 1: to the right
	 * @param time time in seconds (e.g. 0.01) the player was moving
	 */
	public void move(int direction, double time) {
		this.position += direction * speed * time;
		if(this.position < this.halfWidth) {
			this.position = this.halfWidth;
		} else if(this.position > (1.0 - this.halfWidth)) {
			this.position = (1.0 - this.halfWidth);
		}
	}

	public void respawn() {
		this.position = 0.5;
		this.health = this.maxHealth;
	}

	public double getPosition() {
		return this.position;
	}

	public double getWidth() {
		return width;
	}

	public int getHealth() {
		return this.health;
	}
}