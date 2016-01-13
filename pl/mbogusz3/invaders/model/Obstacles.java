package pl.mbogusz3.invaders.model;

import java.util.Arrays;

/**
 *
 */
public class Obstacles {
	public final static int defaultCount = 3;
	public final static int defaultHealth = 4;
	public final static double height = 0.1;
	public final static double positionTop = 0.85;
	private final int count;
	private final int maxHealth;
	private int[] state;

	public Obstacles() {
		this(Obstacles.defaultCount);
	}

	public Obstacles(int count) {
		this(count, Obstacles.defaultHealth);
	}

	public Obstacles(int count, int maxHealth) {
		this.count = count;
		this.maxHealth = maxHealth;
		this.state = new int[count];
	}

	public void respawn() {
		Arrays.fill(this.state, this.maxHealth);
	}

	public void damage(int index) {
		this.damage(index, 1);
	}

	public void damage(int index, int damage) {
		if(index < 0 || index > this.count) {
			return;
		}

		this.state[index] -= damage;
		if(this.state[index] < 0) {
			this.state[index] = 0;
		}
	}

	public int getCount() {
		return count;
	}

	public int[] getState() {
		return state;
	}

	public double getWidth() {
		return 1.0 / (this.count * 2 + 1);
	}
}