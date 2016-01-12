package pl.mbogusz3.invaders.model;

import pl.mbogusz3.invaders.DTO.InvadersModelDTO;
import pl.mbogusz3.invaders.types.InvadersGameEndException;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *
 */
public class InvadersModel extends Observable {
	private final static int obstacleCount = 3;
	private final static int playerHealth = 3;

	private boolean keyDownMap[];
	private final Player player;
	private final Obstacles obstacles;
	private final Enemy enemy;
	private Projectile playerProjectile;
	private Projectile enemyProjectile;
	private boolean isGameOver;

	public InvadersModel() {
		this.enemy = new Enemy();
		this.player = new Player(playerHealth);
		this.obstacles = new Obstacles(obstacleCount);
		this.keyDownMap = new boolean[256];
	}

	public void notifyView() {
		this.notifyObservers(new InvadersModelDTO(this));
	}

	public void startNewGame() {
		this.player.respawn();
		this.enemy.respawn();
		this.obstacles.respawn();
		this.playerProjectile = null;
		this.isGameOver = false;
		this.setChanged();
		this.notifyView();
	}

	/**
	 * Recalculate new game state
	 * @param frameTime frame time, in seconds
	 */
	public void recalculate(double frameTime) {
		recalculate(frameTime, false);
	}

	/**
	 * Recalculate new game state
	 * @param frameTime frame time, in seconds
	 * @param forceUpdate whether to force update
	 */
	public void recalculate(double frameTime, boolean forceUpdate) {
		boolean hasChanged = false;

		// Player movement
		boolean leftKeyDown = this.getKeyState(KeyEvent.VK_LEFT);
		boolean rightKeyDown = this.getKeyState(KeyEvent.VK_RIGHT);
		if(leftKeyDown && !rightKeyDown) {
			this.player.move(-1, frameTime);
			hasChanged = true;
		} else if(!leftKeyDown && rightKeyDown) {
			this.player.move(1, frameTime);
			hasChanged = true;
		}

		// Player shooting
		boolean spaceKeyDown = this.getKeyState(KeyEvent.VK_SPACE);
		if(this.playerProjectile == null && spaceKeyDown) {
			this.playerProjectile = new Projectile(this.player.getPosition(), 0.9);
			hasChanged = true;
		}
		if(this.playerProjectile != null) {
			this.playerProjectile.move(frameTime);
			if(this.playerProjectile.isInvalid()) {
				this.playerProjectile = null;
			}
			hasChanged = true;
		}

		// Enemy movement
		if(this.enemy.isMoving()) {
			try {
				this.enemy.move(frameTime);
			} catch (InvadersGameEndException e) {
				this.isGameOver = true;
			}
			hasChanged = true;
		}

		// Enemy shots
		if(this.enemyProjectile == null) {
			this.enemyProjectile = this.enemy.shoot(frameTime);
		}
		if(this.enemyProjectile != null) {
			this.enemyProjectile.move(frameTime);
			if(this.enemyProjectile.isInvalid()) {
				this.enemyProjectile = null;
			}
			hasChanged = true;
		}
		// Collision detection

		if(hasChanged || forceUpdate) {
			this.setChanged();
			this.notifyView();
		}
	}

	public void exit() {
		System.out.println("Exiting, wait 200ms when we pretend to do a cleanup...");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			System.out.println("Lolwut @OnExitEvent");
		}
		System.out.println("Exiting finished, closing now!");
	}

	public void setKeyDown(int keyCode) {
		if(keyCode > 255) {
			return;
		}
		this.keyDownMap[keyCode] = true;
	}

	public void setKeyUp(int keyCode) {
		if(keyCode > 255) {
			return;
		}
		this.keyDownMap[keyCode] = false;
	}

	public boolean getKeyState(int keyCode) {
		if(keyCode > 255) {
			return false;
		}
		return this.keyDownMap[keyCode];
	}

	public Player getPlayer() {
		return this.player;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public Obstacles getObstacles() {
		return obstacles;
	}

	public Projectile getPlayerProjectile() {
		return playerProjectile;
	}

	public Projectile getEnemyProjectile() {
		return enemyProjectile;
	}

	public boolean isGameOver() {
		return isGameOver;
	}
}