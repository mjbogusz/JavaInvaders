package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.*;

import java.io.Serializable;

/**
 */
public class InvadersModelDTO implements Serializable {
	public final PlayerDTO player;
	public final EnemyDTO enemy;
	public final ObstaclesDTO obstacles;
	public final ProjectileDTO playerProjectile;
	public final ProjectileDTO enemyProjectile;
	public final boolean isGameOver;

	public InvadersModelDTO(InvadersModel model) {
		this.player = new PlayerDTO(model.getPlayer());
		this.enemy = new EnemyDTO(model.getEnemy());
		this.obstacles = new ObstaclesDTO(model.getObstacles());
		if(model.getPlayerProjectile() != null) {
			this.playerProjectile = new ProjectileDTO(model.getPlayerProjectile());
		} else {
			this.playerProjectile = null;
		}
		if(model.getEnemyProjectile() != null) {
			this.enemyProjectile = new ProjectileDTO(model.getEnemyProjectile());
		} else {
			this.enemyProjectile = null;
		}
		this.isGameOver = model.isGameOver();
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

	public ProjectileDTO getPlayerProjectile() {
		return playerProjectile;
	}

	public ProjectileDTO getEnemyProjectile() {
		return enemyProjectile;
	}

	public boolean isGameOver() {
		return isGameOver;
	}
}
