package pl.mbogusz3.invaders.model;

import com.sun.istack.internal.Nullable;
import pl.mbogusz3.invaders.types.InvadersGameEndException;

import java.util.Arrays;
import java.util.Random;

/**
 * Invaders' enemy 'army' model.
 */
public class Enemy {
	/**
	 * Default number of rows of enemy army.
	 */
	public final static int rows = 4;
	/**
	 * Default number of columns of enemy army.
	 */
	public final static int columns = 8;
	/**
	 * Default initial speed of enemy army - fraction of screen travelled per second.
	 */
	public final static double initialSpeed = 0.1;
	/**
	 * Enemy unit's height.
	 */
	public final static double unitHeight = 0.05;
	/**
	 * Probability of enemy army shooting once per second.
	 */
	public final static double shotProbability = 0.3;
	/**
	 * Enemy unit's width, calculated in constructor.
	 */
	private final double unitWidth;
	/**
	 * Random number generator, used in shooting.
	 */
	private final Random randomGenerator;
	/**
	 * State of enemy's army, per-unit (true = alive, false = dead).
	 */
	private boolean[][] state;
	/**
	 * Enemy army's position from top (i.e. where it begins from).
	 */
	private double positionTop;
	/**
	 * Enemy army's average position from left (i.e. middle column's)
	 */
	private double positionLeft;
	/**
	 * Current speed of enemy's army.
	 * @see #initialSpeed
	 */
	private double speed;
	/**
	 * Current movement direction of enemy's army. 1 is right, -1 is left.
	 */
	private int direction;
	/**
	 * Index of first active column in enemy's army (0 on respawn).
	 */
	private int firstColumn;
	/**
	 * Index of last active column in enemy's army ({@link #columns}-1 on respawn).
	 */
	private int lastColumn;
	/**
	 * Index of first (closest to player) row column in enemy's army ({@link #rows}-1 on respawn).
	 */
	private int firstRow;
	/**
	 * Whether the enemy army is moving. Unused.
	 */
	private boolean isMoving;

	/**
	 * Constructor of Invaders' enemy model.
	 */
	public Enemy() {
		this.state = new boolean[Enemy.rows][Enemy.columns];
		this.respawn();
		// In one screen there is space for 3 times the enemy columns - one is taken, one is spaces between them and one is spare space on sides
		this.unitWidth = 1.0 / (Enemy.columns * 3);
		this.randomGenerator = new Random();
	}

	/**
	 * Respawn enemy's army, marking all units as alive and setting initial position.
	 */
	public void respawn() {
		for(boolean[] s : this.state) {
			Arrays.fill(s, true);
		}
		this.firstColumn = 0;
		this.lastColumn = Enemy.columns - 1;
		this.firstRow = Enemy.rows - 1;
		this.speed = Enemy.initialSpeed;
		this.positionTop = 0.0;
		this.positionLeft = 0.5;
		this.direction = 1;
		this.isMoving = true;
	}

	/**
	 * Move the enemy army.
	 * @param time time for which the army has moved (fraction of second).
	 * @throws InvadersGameEndException if enemy has reached obstacles
	 */
	public void move(double time) throws InvadersGameEndException {
		// Determine enemy "army" width
		double halfWidth = this.unitWidth * ((double)(this.lastColumn - this.firstColumn + 1) - 0.5);
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
		double height = (2 * this.firstRow + 1) * Enemy.unitHeight;
		if(this.positionTop + height >= 0.8) {
			System.out.println("GAME END");
			throw new InvadersGameEndException();
		}
	}

	/**
	 * Decide whether to shoot and if so return the shot projectile.
	 * @param time time for which to calculate probability of shooting (fraction of second).
	 * @return {@link Projectile} or null.
	 */
	@Nullable
	public Projectile shoot(double time) {
		// shooting probability
		if(this.randomGenerator.nextDouble() > (Enemy.shotProbability * time)) {
			return null;
		}

		// Decide which column is to shoot, then check whether any unit exists in that column
		int shootingColumn;
		if(this.lastColumn > this.firstColumn) {
			shootingColumn = this.randomGenerator.nextInt(this.lastColumn - this.firstColumn) + this.firstColumn;
		} else {
			shootingColumn = this.firstColumn;
		}
		int shootingRow;
		for(shootingRow = this.firstRow; shootingRow >= 0; shootingRow--) {
			if(this.state[shootingRow][shootingColumn]) {
				break;
			}
		}
		if(shootingRow < 0) {
			return null;
		}

		// Calculate projectile's position (here: shooting enemy unit's position)
		double x = this.positionLeft + (2 * shootingColumn - this.lastColumn + this.firstColumn) * this.unitWidth;
		double y = (2 * shootingRow + 1) * Enemy.unitHeight + this.positionTop;

		// Return the new projectile, flying down (increasing Y position)
		return new Projectile(x, y, 1, 0.3);
	}

	/**
	 * Destroy an enemy unit.
	 * @param row row of unit to destroy.
	 * @param column column of unit to destroy.
	 */
	public void destroyUnit(int row, int column) {
		this.state[row][column] = false;
		if(column == this.firstColumn || column == this.lastColumn) {
			this.checkBorderColumn(column);
		}
		if(row == this.firstRow) {
			boolean anyLeft = false;
			for(int i = this.firstColumn; i <= this.lastColumn; i++) {
				if(this.state[row][i]) {
					anyLeft = true;
					break;
				}
			}
			if(!anyLeft) {
				this.firstRow--;
			}
		}
	}

	/**
	 * Check whether border (i.e. first or last) column is empty and if so modify indices accordingly.
	 * @param column index of column to check.
	 */
	private void checkBorderColumn(int column) {
		if(column != this.firstColumn && column != this.lastColumn) {
			return;
		}

		boolean anyLeft = false;
		for(int i = 0; i <= this.firstRow; i++) {
			if(this.state[i][column]) {
				anyLeft = true;
				break;
			}
		}
		if(!anyLeft) {
			if(column == this.firstColumn) {
				this.firstColumn++;
				this.positionLeft += this.unitWidth;
				if(this.firstColumn <= this.lastColumn) {
					this.checkBorderColumn(column + 1);
				}
			} else {
				this.lastColumn--;
				this.positionLeft -= this.unitWidth;
				if(this.firstColumn <= this.lastColumn) {
					this.checkBorderColumn(column - 1);
				}
			}
		}
	}

	/**
	 * Retrieve current state of enemy army.
	 * @return enemy army's state.
	 */
	public boolean[][] getState() {
		return this.state;
	}

	/**
	 * Retrieve current vertical position (distance from top) of enemy army.
	 * @return enemy army's vertical position.
	 */
	public double getPositionTop() {
		return positionTop;
	}

	/**
	 * Retrieve current horizontal position (distance from left) of enemy army, measured to middle column.
	 * @return enemy army's horizontal position.
	 */
	public double getPositionLeft() {
		return positionLeft;
	}

	/**
	 * Retrieve calculated width of enemy army's unit.
	 * @return unit's width.
	 */
	public double getUnitWidth() {
		return unitWidth;
	}

	/**
	 * Retrieve index of first active (not empty) column in army.
	 * @return first column's index.
	 */
	public int getFirstColumn() {
		return firstColumn;
	}

	/**
	 * Retrieve index of last active (not empty) column in army.
	 * @return last column's index.
	 */
	public int getLastColumn() {
		return lastColumn;
	}

	/**
	 * Retrieve index of first (from bottom, i.e. biggest index) active (not empty) row in army.
	 * @return first row's index.
	 */
	public int getFirstRow() {
		return firstRow;
	}

	/**
	 * Retrieve information whether the enemy's army is moving.
	 * @return whether army is moving.
	 */
	public boolean isMoving() {
		return isMoving;
	}
}