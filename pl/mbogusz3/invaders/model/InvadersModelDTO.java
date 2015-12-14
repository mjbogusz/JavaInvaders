package pl.mbogusz3.invaders.model;

import java.io.Serializable;

/**
 */
public class InvadersModelDTO implements Serializable {
	public final PlayerDTO player;
	public final EnemyDTO enemy;
	public final ObstaclesDTO obstacles;

	public InvadersModelDTO(InvadersModel model) {
		this.player = new PlayerDTO(model.getPlayer());
		this.enemy = new EnemyDTO(model.getEnemy());
		this.obstacles = new ObstaclesDTO(model.getObstacles());
	}

	public class PlayerDTO {
		public final int health;
		public final double width;
		public final double position;

		public PlayerDTO(Player player) {
			this.health = player.getHealth();
			this.width = player.getWidth();
			this.position = player.getPosition();
		}

		public int getHealth() {
			return health;
		}

		public double getWidth() {
			return width;
		}

		public double getPosition() {
			return position;
		}
	}

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

	public class ObstaclesDTO {
		public final int count;
		public final int strength;
		public final int[] state;

		public ObstaclesDTO(Obstacles obstacles) {
			this.count = obstacles.getCount();
			this.strength = obstacles.getStrength();
			this.state = obstacles.getState();
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
	}

	public PlayerDTO getPlayer() {
		return player;
	}

	public EnemyDTO getEnemy() {
		return enemy;
	}

	public ObstaclesDTO getObstacles() {
		return obstacles;
	}
}
