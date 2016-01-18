package pl.mbogusz3.invaders.model;

/**
 * Invaders' player model.
 */
public class Player {
	/**
	 * Player's height.
	 */
	public final static double height = 0.1;
	/**
	 * Player's width.
	 */
	public final static double width = 0.1;
	/**
	 * Player's vertical position (distance from top).
	 */
	public final static double positionTop = 0.95;
	/**
	 * Player's speed (fraction of screen traveled per second).
	 */
	public final static double speed = 0.4;
	/**
	 * Player's default maximum health.
	 */
	public final static int defaultHealth = 3;
	/**
	 * Player's default horizontal position.
	 */
	public final static double defaultPosition = 0.5;
	/**
	 * Player's maximum health.
	 * Defaults to {@link #defaultHealth}.
	 */
	private final int maxHealth;
	/**
	 * Player's current health.
	 */
	private int health;
	/**
	 * Player's horizontal position.
	 */
	private double position;

	/**
	 * Player's model constructor, defaulting {@link #maxHealth} to {@link #defaultHealth}.
	 */
	public Player() {
		this(Player.defaultHealth);
	}

	/**
	 * Player's model constructor.
	 * @param health player's maximum health.
	 */
	public Player(int health) {
		this.maxHealth = health;
	}

	/**
	 * Move the player.
	 * @param direction -1: to the left (negative), 1: to the right (positive).
	 * @param time time (as fraction of a second) the player was moving.
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

	/**
	 * Respawn player, resetting {@link #position} and {@link #health}.
	 */
	public void respawn() {
		this.position = Player.defaultPosition;
		this.health = this.maxHealth;
	}

	/**
	 * Inflict 1 damage on player.
	 */
	public void damage() {
		this.damage(1);
	}

	/**
	 * Inflict damage on player.
	 * @param damageValue amount of damage to inflict.
	 */
	public void damage(int damageValue) {
		this.health -= damageValue;
	}

	/**
	 * Retrieve current horizontal position of player.
	 * @return player's horizontal position, between 0.0 and 1.0.
	 */
	public double getPosition() {
		return position;
	}

	/**
	 * Retrieve current health of player.
	 * @return player's health.
	 */
	public int getHealth() {
		return health;
	}
}