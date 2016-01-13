package pl.mbogusz3.invaders.model;

/**
 *
 */
public class Player {
	public final static double height = 0.1;
	public final static double width = 0.1;
	public final static double positionTop = 0.95;
	public final static double speed = 0.4;
	public final static int defaultHealth = 3;
	public final static double defaultPosition = 0.5;
	private final int maxHealth;
	private int health;
	private double position;

	public Player() {
		this(Player.defaultHealth);
	}

	public Player(int health) {
		this.maxHealth = health;
	}

	/**
	 * Move the player object.
	 * @param direction -1: to the left, 1: to the right
	 * @param time time in seconds (e.g. 0.01) the player was moving
	 */
	public void move(int direction, double time) {
		this.position += direction * Player.speed * time;
		double halfWidth = Player.width / 2.0;
		if(this.position < halfWidth) {
			this.position = halfWidth;
		} else if(this.position > (1.0 - halfWidth)) {
			this.position = (1.0 - halfWidth);
		}
	}

	public void respawn() {
		this.position = Player.defaultPosition;
		this.health = this.maxHealth;
	}

	public void damage() {
		this.damage(1);
	}

	public void damage(int damageValue) {
		this.health -= damageValue;
	}

	public double getPosition() {
		return position;
	}

	public int getHealth() {
		return health;
	}
}