package pl.mbogusz3.invaders.model;

import java.util.Arrays;

/**
 *
 */
public class Obstacles {
	private final int count;
	private final int strength;
	private final boolean[][] state;

	public Obstacles(int count) {
		this(count, 2);
	}

	public Obstacles(int count, int strength) {
		this.count = count;
		this.strength = strength;
		this.state = new boolean[count][strength];
	}

	public void respawn() {
		for(boolean[] s : this.state) {
			Arrays.fill(s, true);
		}
	}

	public int getCount() {
		return count;
	}

	public int getStrength() {
		return strength;
	}

	public boolean[][] getState() {
		return state;
	}
}