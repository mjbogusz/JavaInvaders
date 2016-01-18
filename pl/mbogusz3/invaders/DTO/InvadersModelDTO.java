package pl.mbogusz3.invaders.DTO;

import com.sun.istack.internal.Nullable;
import pl.mbogusz3.invaders.model.*;

import java.io.Serializable;

/**
 * {@link InvadersModel}'s DTO class.
 */
public class InvadersModelDTO implements Serializable {
	/**
	 * Player's DTO object.
	 */
	public final PlayerDTO player;
	/**
	 * Enemy's DTO object.
	 */
	public final EnemyDTO enemy;
	/**
	 * Obstacles' DTO object.
	 */
	public final ObstaclesDTO obstacles;
	/**
	 * Player projectile's DTO object.
	 */
	@Nullable
	public final ProjectileDTO playerProjectile;
	/**
	 * Enemy projectile's DTO object.
	 */
	@Nullable
	public final ProjectileDTO enemyProjectile;
	/**
	 * Whether the game is running.
	 */
	public final boolean isGameRunning;
	/**
	 * Whether the game has been won.
	 */
	public final boolean isGameWon;
	/**
	 * Whether the game has been lost.
	 */
	public final boolean isGameOver;

	/**
	 * Invaders model DTO constructor.
	 * @param model Invaders' model instance to copy data from.
	 */
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
		this.isGameRunning = model.isGameRunning();
		this.isGameWon = model.isGameWon();
		this.isGameOver = model.isGameOver();
	}

	/**
	 * Retrieve player model's DTO.
	 * @return player model's DTO.
	 */
	public PlayerDTO getPlayer() {
		return player;
	}

	/**
	 * Retrieve enemy model's DTO.
	 * @return enemy model's DTO.
	 */
	public EnemyDTO getEnemy() {
		return enemy;
	}

	/**
	 * Retrieve obstacles model's DTO.
	 * @return obstacles model's DTO.
	 */
	public ObstaclesDTO getObstacles() {
		return obstacles;
	}

	/**
	 * Retrieve player projectile's DTO.
	 * @return player projectile's DTO or null.
	 */
	@Nullable
	public ProjectileDTO getPlayerProjectile() {
		return playerProjectile;
	}

	/**
	 * Retrieve enemy projectile's DTO.
	 * @return enemy projectile's DTO or null.
	 */
	@Nullable
	public ProjectileDTO getEnemyProjectile() {
		return enemyProjectile;
	}

	/**
	 * Retrieve information whether game is running.
	 * @return whether game is running.
	 */
	public boolean isGameRunning() {
		return isGameRunning;
	}

	/**
	 * Retrieve information whether game has been won.
	 * @return whether game has been won.
	 */
	public boolean isGameWon() {
		return isGameWon;
	}

	/**
	 * Retrieve information whether game has been lost.
	 * @return whether game has been lost.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
}
