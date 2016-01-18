package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Enemy;

import java.io.Serializable;

/**
 * {@link Enemy}'s model DTO class
 */
public class EnemyDTO implements Serializable {
	/**
	 * Width of an enemy unit.
	 */
	public final double unitWidth;
	/**
	 * Total number of rows in enemy army.
	 */
	public final int rows;
	/**
	 * Total number of columns in enemy army.
	 */
	public final int columns;
	/**
	 * Current enemy army's state.
	 */
	public final boolean[][] state;
	/**
	 * Vertical position of enemy army.
	 */
	public final double positionTop;
	/**
	 * Horizontal position of enemy army.
	 */
	public final double positionLeft;
	/**
	 * Index of first non-empty column in enemy army.
	 */
	public final int firstColumn;
	/**
	 * Index of last non-empty column in enemy army.
	 */
	public final int lastColumn;
	/**
	 * Index of first non-empty row in enemy army.
	 */
	public final int firstRow;

	/**
	 * Enemy DTO class constructor.
	 * @param enemy {@link Enemy} object to copy data from.
	 */
	public EnemyDTO(Enemy enemy) {
		this.unitWidth = enemy.getUnitWidth();
		this.rows = Enemy.rows;
		this.columns = Enemy.columns;
		this.state = enemy.getState();
		this.positionTop = enemy.getPositionTop();
		this.positionLeft = enemy.getPositionLeft();
		this.firstColumn = enemy.getFirstColumn();
		this.lastColumn = enemy.getLastColumn();
		this.firstRow = enemy.getFirstRow();
	}

	/**
	 * Retrieve unit width.
	 * @return unit width
	 */
	public double getUnitWidth() {
		return unitWidth;
	}

	/**
	 * Retrieve total number of enemy rows.
	 * @return total number of enemy rows.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Retrieve total number of enemy columns.
	 * @return total number of enemy columns.
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Retrieve state of enemy army.
	 * @return state of enemy army.
	 */
	public boolean[][] getState() {
		return state;
	}

	/**
	 * Retrieve enemy army vertical position.
	 * @return enemy army vertical position.
	 */
	public double getPositionTop() {
		return positionTop;
	}

	/**
	 * Retrieve enemy army horizontal position.
	 * @return enemy army horizontal position.
	 */
	public double getPositionLeft() {
		return positionLeft;
	}

	/**
	 * Retrieve index of first non-empty column in enemy army.
	 * @return index of first non-empty column in enemy army.
	 */
	public int getFirstColumn() {
		return firstColumn;
	}

	/**
	 * Retrieve index of last non-empty column in enemy army.
	 * @return index of last non-empty column in enemy army.
	 */
	public int getLastColumn() {
		return lastColumn;
	}

	/**
	 * Retrieve index of first non-empty row in enemy army.
	 * @return index of first non-empty row in enemy army.
	 */
	public int getFirstRow() {
		return firstRow;
	}
}
