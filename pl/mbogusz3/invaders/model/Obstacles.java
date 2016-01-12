package pl.mbogusz3.invaders.model;

import java.util.Arrays;

/**
 *
 */
public class Obstacles {
	public final static int defaultStrength = 4;
	public final static double height = 0.1;
	public final static double positionTop = 0.85;
	private final int count;
	private final int strength;
	private final int[] state;

	public Obstacles(int count) {
		this(count, Obstacles.defaultStrength);
	}

	public Obstacles(int count, int strength) {
		this.count = count;
		this.strength = strength;
		this.state = new int[count];
	}

	public void respawn() {
		Arrays.fill(this.state, this.strength);
	}

	public int getCount() {
		return count;
	}

	public int getStrength() {
		return strength;
	}

	public int[] getState() {
		return state;
	}

	public double getWidth() {
		return 1.0 / (this.count * 2 + 1);
	}
}