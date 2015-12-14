package pl.mbogusz3.invaders.model;

import java.util.Arrays;

/**
 *
 */
public class Enemy {
	private final int rows;
	private final int columns;
	private boolean[][] state;
	private double positionTop;
	private double positionLeft;

	public Enemy(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.state = new boolean[rows][columns];
	}

	public void respawn() {
		for(boolean[] s : state) {
			Arrays.fill(s, true);
		}
		this.positionTop = 0.0;
		this.positionLeft = 0.5;
	}

	public boolean[][] getState() {
		return this.state;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public double getPositionTop() {
		return positionTop;
	}

	public double getPositionLeft() {
		return positionLeft;
	}
}