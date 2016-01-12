package pl.mbogusz3.invaders.model;

import pl.mbogusz3.invaders.types.InvadersGameEndException;

import java.util.Arrays;

/**
 *
 */
public class Enemy {
	public final static int rows = 4;
	public final static int columns = 8;
	public final static double initialSpeed = 0.1;
	public final static double unitHeight = 0.05;
	private final double unitWidth;
	private boolean[][] state;
	private double positionTop;
	private double positionLeft;
	private double speed;
	private int direction;
	private int firstColumn;
	private int lastColumn;
	private int firstRow;
	private boolean isMoving;

	public Enemy() {
		this.state = new boolean[Enemy.rows][Enemy.columns];
		this.respawn();
		// In one screen there is space for 3 times the enemy columns - one is taken, one is spaces between them and one is spare space on sides
		this.unitWidth = 1.0 / (Enemy.columns * 3);
	}

	public void respawn() {
		for(boolean[] s : this.state) {
			Arrays.fill(s, true);
		}
		this.firstColumn = 0;
		this.lastColumn = Enemy.columns;
		this.firstRow = Enemy.rows;
		this.speed = Enemy.initialSpeed;
		this.positionTop = 0.0;
		this.positionLeft = 0.5;
		this.direction = 1;
		this.isMoving = true;
	}

	public void move(double time) throws InvadersGameEndException {
		// Determine enemy "army" width
		double halfWidth = this.unitWidth * ((double)(this.lastColumn - this.firstColumn) - 0.5);
		if(this.direction == 1) {
			// Compare half the width of army with maximum position
			if(this.positionLeft < (1.0 - halfWidth)) {
				// If not yet at the edge, simply move forward
				this.positionLeft += this.speed * time;
				if(this.positionLeft > (1.0 - halfWidth)) {
					this.positionLeft = 1.0 - halfWidth;
				}
			} else {
				// Else, reverse movement direction and move half row down
				this.positionTop += Enemy.unitHeight / 2;
				this.direction = -1;
			}
		} else if (direction == -1) {
			// Same for opposite direction
			if(this.positionLeft > halfWidth) {
				this.positionLeft -= this.speed * time;
				if(this.positionLeft < halfWidth) {
					this.positionLeft = halfWidth;
				}
			} else {
				this.positionTop += Enemy.unitHeight / 2;
				this.direction = 1;
			}
		} else {
			System.out.println("Something strange happened... enemy direction was incorrect");
			this.direction = 1;
		}

		// Detect end of game
		double height = (2 * this.firstRow - 1) * Enemy.unitHeight;
		if(this.positionTop + height >= 0.8) {
			System.out.println("GAME END");
			throw new InvadersGameEndException();
		}
	}

	public boolean[][] getState() {
		return this.state;
	}

	public double getPositionTop() {
		return positionTop;
	}

	public double getPositionLeft() {
		return positionLeft;
	}

	public double getUnitWidth() {
		return unitWidth;
	}

	public int getDirection() {
		return direction;
	}

	public int getFirstColumn() {
		return firstColumn;
	}

	public int getLastColumn() {
		return lastColumn;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public double getSpeed() {
		return speed;
	}
}