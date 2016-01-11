package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Enemy;

/**
 */
public class EnemyDTO {
	public final double unitWidth;
	public final boolean[][] state;
	public final double positionTop;
	public final double positionLeft;
	public final int firstColumn;
	public final int lastColumn;
	public final int firstRow;

	public EnemyDTO(Enemy enemy) {
		this.unitWidth = enemy.getUnitWidth();
		this.state = enemy.getState();
		this.positionTop = enemy.getPositionTop();
		this.positionLeft = enemy.getPositionLeft();
		this.firstColumn = enemy.getFirstColumn();
		this.lastColumn = enemy.getLastColumn();
		this.firstRow = enemy.getFirstRow();
	}

	public double getUnitWidth() {
		return unitWidth;
	}

	public boolean[][] getState() {
		return state;
	}

	public double getPositionTop() {
		return positionTop;
	}

	public double getPositionLeft() {
		return positionLeft;
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
}
