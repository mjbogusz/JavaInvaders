package pl.mbogusz3.invaders.model;

import java.util.Arrays;

/**
 * Invaders' obstacles model.
 */
public class Obstacles {
	/**
	 * Default count of obstacles.
	 */
	public final static int defaultCount = 3;
	/**
	 * Default health of obstacles.
	 */
	public final static int defaultHealth = 4;
	/**
	 * Height of obstacles (fraction of screen).
	 */
	public final static double height = 0.1;
	/**
	 * Obstacles' position from top.
	 */
	public final static double positionTop = 0.85;
	/**
	 * Count of obstacles, defaults to {@link #defaultCount}.
	 */
	private final int count;
	/**
	 * Maximum health of obstacles, defaults to {@link #defaultHealth}.
	 */
	private final int maxHealth;
	/**
	 * Current state (health) of obstacles, initialized to {@link #maxHealth}.
	 */
	private int[] state;

	/**
	 * Obstacles model constructor, defaulting all values (maximum/initial health and count).
	 */
	public Obstacles() {
		this(Obstacles.defaultCount);
	}

	/**
	 * Obstacles model constructor, defaulting obstacle maximum/initial health.
	 * @param count count of obstacles.
	 */
	public Obstacles(int count) {
		this(count, Obstacles.defaultHealth);
	}

	/**
	 * Obstacles model constructor
	 * @param count count of obstacles.
	 * @param maxHealth maximum/initial health of obstacles.
	 */
	public Obstacles(int count, int maxHealth) {
		this.count = count;
		this.maxHealth = maxHealth;
		this.state = new int[count];
	}

	/**
	 * Respawn the obstacles, restoring their health to saved maximum.
	 */
	public void respawn() {
		Arrays.fill(this.state, this.maxHealth);
	}

	/**
	 * Inflict 1 damage to an obstacle.
	 * @param index index of obstacle to inflict damage to.
	 */
	public void damage(int index) {
		this.damage(index, 1);
	}

	/**
	 * Inflict damate to an obstacle.
	 * @param index index of obstacle to inflict damage to.
	 * @param damage damage to inflict.
	 */
	public void damage(int index, int damage) {
		if(index < 0 || index > this.count) {
			return;
		}

		this.state[index] -= damage;
		if(this.state[index] < 0) {
			this.state[index] = 0;
		}
	}

	/**
	 * Retrieve count of obstacles.
	 * @return number of obstacles (both 'alive' and 'dead')
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Retrieve state of obstacles.
	 * @return array containing health of obstacles.
	 */
	public int[] getState() {
		return state;
	}

	/**
	 * Retrieve single obstacle width.
	 * @return width of a single obstacle.
	 */
	public double getWidth() {
		return 1.0 / (this.count * 2 + 1);
	}
}