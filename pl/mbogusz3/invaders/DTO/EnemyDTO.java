package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Enemy;

/**
 */
public class EnemyDTO {
	public final int rows;
	public final int columns;
	public final boolean[][] state;
	public final double positionTop;
	public final double positionLeft;

	public EnemyDTO(Enemy enemy) {
		this.rows = enemy.getRows();
		this.columns = enemy.getColumns();
		this.state = enemy.getState();
		this.positionTop = enemy.getPositionTop();
		this.positionLeft = enemy.getPositionLeft();
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
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
}
