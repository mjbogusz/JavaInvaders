package pl.mbogusz3.invaders.model;

import com.sun.istack.internal.Nullable;
import pl.mbogusz3.invaders.DTO.InvadersModelDTO;
import pl.mbogusz3.invaders.types.InvadersGameEndException;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

/**
 * Model class of Invaders game, extends Java Observable class.
 * Responsible of storing data, performing actual calculations (e.g. movement, collisions etc) and sending updates to Observers (View).
 */
public class InvadersModel extends Observable {
	/**
	 * Initial count of obstacles when starting a new game.
	 */
	public final static int obstacleCount = 3;
	/**
	 * Initial health of player when starting a new game.
	 */
	public final static int playerHealth = 3;

	/**
	 * Key state map, true - pressed down, false - released.
	 */
	private boolean keyDownMap[];
	/**
	 * Player model.
	 */
	private final Player player;
	/**
	 * Obstacles model.
	 */
	private final Obstacles obstacles;
	/**
	 * Enemy model.
	 */
	private final Enemy enemy;
	/**
	 * Player projectile model, nullable.
	 */
	@Nullable
	private Projectile playerProjectile;
	/**
	 * Enemy projectile model, nullable.
	 */
	@Nullable
	private Projectile enemyProjectile;
	/**
	 * Whether the model state has changed, used to decide whether to notify observers.
	 */
	private boolean hasChanged;
	/**
	 * Whether the game is currently running.
	 */
	private boolean isGameRunning;
	/**
	 * Whether the game has been lost.
	 */
	private boolean isGameOver;
	/**
	 * Whether the game has been won.
	 */
	private boolean isGameWon;

	/**
	 * Constructor of InvadersModel class, initializing model state and inner classes.
	 */
	public InvadersModel() {
		this.keyDownMap = new boolean[256];
		this.player = new Player(InvadersModel.playerHealth);
		this.obstacles = new Obstacles(InvadersModel.obstacleCount);
		this.enemy = new Enemy();
		this.isGameRunning = false;
		this.isGameOver = false;
		this.isGameWon = false;
	}

	/**
	 * Notify view (observers) - wrapper for Observable's notifyObservers with DTO constructor.
	 */
	public void notifyView() {
		this.notifyObservers(new InvadersModelDTO(this));
	}

	/**
	 * Start a new game, resetting player, enemies, obstacles etc to initial state (position, health etc)
	 */
	public void startNewGame() {
		Arrays.fill(this.keyDownMap, false);
		this.player.respawn();
		this.enemy.respawn();
		this.obstacles.respawn();
		this.playerProjectile = null;
		this.enemyProjectile = null;
		this.isGameRunning = true;
		this.isGameOver = false;
		this.isGameWon = false;
		this.setChanged();
		this.notifyView();
	}

	/**
	 * Recalculate new game state, based on passed time.
	 * @param frameTime frame time, in seconds
	 */
	public void recalculate(double frameTime) {
		this.recalculate(frameTime, false);
	}

	/**
	 * Recalculate new game state, based on passed time.
	 * @param frameTime frame time, in seconds
	 * @param forceUpdate whether to force update to observers
	 */
	public void recalculate(double frameTime, boolean forceUpdate) {
		this.hasChanged = false;

		// Player and enemy actions (movement and shooting)
		this.recalculatePlayerActions(frameTime);
		this.recalculateEnemyActions(frameTime);

		// Collision detection
		this.collisionDetectionPlayerProjectile();
		this.collisionDetectionEnemyProjectile();

		if(this.hasChanged || forceUpdate) {
			this.setChanged();
			this.notifyView();
		}
	}

	/**
	 * Recalculate player actions (movement and shooting). Called from recalculate().
	 * @param frameTime frame time, in seconds.
	 */
	private void recalculatePlayerActions(double frameTime) {
		// Player movement
		boolean leftKeyDown = this.getKeyState(KeyEvent.VK_LEFT);
		boolean rightKeyDown = this.getKeyState(KeyEvent.VK_RIGHT);
		if(leftKeyDown && !rightKeyDown) {
			this.player.move(-1, frameTime);
			this.hasChanged = true;
		} else if(!leftKeyDown && rightKeyDown) {
			this.player.move(1, frameTime);
			this.hasChanged = true;
		}

		// Player shooting
		boolean spaceKeyDown = this.getKeyState(KeyEvent.VK_SPACE);
		if(this.playerProjectile == null && spaceKeyDown) {
			this.playerProjectile = new Projectile(this.player.getPosition(), 0.9);
			this.hasChanged = true;
		}
		if(this.playerProjectile != null) {
			this.playerProjectile.move(frameTime);
			if(this.playerProjectile.isInvalid()) {
				this.playerProjectile = null;
			}
			this.hasChanged = true;
		}
	}

	/**
	 * Recalculate enemy actions (movement and shooting). Called from recalculate().
	 * @param frameTime frame time, in seconds.
	 */
	private void recalculateEnemyActions(double frameTime) {
		// Enemy movement
		if(this.enemy.isMoving()) {
			try {
				this.enemy.move(frameTime);
			} catch (InvadersGameEndException e) {
				this.isGameOver = true;
				this.isGameRunning = false;
			}
			this.hasChanged = true;
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
			this.hasChanged = true;
		}
	}

	/**
	 * Perform player projectile collision detection. Called from recalculate().
	 */
	private void collisionDetectionPlayerProjectile() {
		if(this.playerProjectile == null) {
			return;
		}

		double projectileLeft = this.playerProjectile.getPositionX() - Projectile.width / 2.0;
		double projectileRight = this.playerProjectile.getPositionX() + Projectile.width / 2.0;
		double projectileTop = this.playerProjectile.getPositionY() - Projectile.height / 2.0;
		double projectileBottom = this.playerProjectile.getPositionY() + Projectile.height / 2.0;
		// First, collisions with obstacles (no damage)
		double obstacleTop = Obstacles.positionTop - Obstacles.height / 2.0;
		double obstacleBottom = Obstacles.positionTop + Obstacles.height / 2.0;
		// Direction is -1 (top has lower value than bottom) thus the reversed comparison
		if(projectileTop < obstacleBottom && projectileBottom > obstacleTop) {
			for(int i = 0; i < this.obstacles.getCount(); i++) {
				if(this.obstacles.getState()[i] == 0) {
					continue;
				}
				double obstacleLeft = (2.0 * i + 1.0) * this.obstacles.getWidth();
				double obstacleRight = (2.0 * i + 2.0) * this.obstacles.getWidth();
				if(projectileRight > obstacleLeft && projectileLeft < obstacleRight) {
					this.playerProjectile = null;
					return;
				}
			}
		}

		// Then, collisions with enemies
		double enemiesBottom = this.enemy.getPositionTop() + (2 * this.enemy.getFirstRow() + 1) * Enemy.unitHeight;
		// First, check whether we're at enemy's "orbit" (height) at all
		if(projectileTop > enemiesBottom) {
			return;
		}

		// Then for each row of enemies
		for(int i = this.enemy.getFirstRow(); i >= 0; i--) {
			// Check whether projectile is at row's height
			double enemyBottom = enemiesBottom - 2.0 * (this.enemy.getFirstRow() - i) * Enemy.unitHeight;
			double enemyTop = enemyBottom - Enemy.unitHeight;
			// Skip if not
			if(projectileTop > enemyBottom || projectileBottom < enemyTop) {
				continue;
			}

			double middleColumn = (this.enemy.getLastColumn() - this.enemy.getFirstColumn()) / 2.0;
			// Then for each column
			for(int j = this.enemy.getFirstColumn(); j <= this.enemy.getLastColumn(); j++) {
				// If enemy is already "dead", skip the calculations
				if(!this.enemy.getState()[i][j]) {
					continue;
				}

				// Else, calculate enemy horizontal position
				// (Enemy unit middle is twice the unit width times count of units from middle position)
				double enemyLeft = this.enemy.getPositionLeft() - (middleColumn - j) * 2 * this.enemy.getUnitWidth() - this.enemy.getUnitWidth() / 2.0;
				double enemyRight = enemyLeft + this.enemy.getUnitWidth();
				// And compare it to projectile's position
				if(projectileRight > enemyLeft && projectileLeft < enemyRight) {
					this.hasChanged = true;
					this.playerProjectile = null;
					this.enemy.destroyUnit(i, j);
					// If enemy was shot down do game win check
					if(this.enemy.getFirstColumn() > this.enemy.getLastColumn()) {
						this.isGameWon = true;
						this.isGameRunning = false;
					}
					return;
				}
			}
		}
	}

	/**
	 * Perform enemy projectile collision detection. Called from recalculate().
	 */
	private void collisionDetectionEnemyProjectile() {
		if(this.enemyProjectile == null) {
			return;
		}

		double projectileLeft = this.enemyProjectile.getPositionX() - Projectile.width / 2.0;
		double projectileRight = this.enemyProjectile.getPositionX() + Projectile.width / 2.0;
		double projectileTop = this.enemyProjectile.getPositionY() - Projectile.height / 2.0;
		double projectileBottom = this.enemyProjectile.getPositionY() + Projectile.height / 2.0;

		// First, collisions with obstacles (same as player projectile's but with damage)
		double obstacleTop = Obstacles.positionTop - Obstacles.height / 2.0;
		double obstacleBottom = Obstacles.positionTop + Obstacles.height / 2.0;
		if(projectileTop < obstacleBottom && projectileBottom > obstacleTop) {
			for(int i = 0; i < this.obstacles.getCount(); i++) {
				if(this.obstacles.getState()[i] == 0) {
					continue;
				}

				double obstacleLeft = (2.0 * i + 1.0) * this.obstacles.getWidth();
				double obstacleRight = (2.0 * i + 2.0) * this.obstacles.getWidth();
				if(projectileRight > obstacleLeft && projectileLeft < obstacleRight) {
					this.hasChanged = true;
					this.enemyProjectile = null;
					this.obstacles.damage(i);
					return;
				}
			}
		}

		// Then, collisions with player
		double playerTop = Player.positionTop - Player.height / 2.0;
		double playerBottom = Player.positionTop + Player.height / 2.0;
		double playerLeft = this.player.getPosition() - Player.width / 2.0;
		double playerRight = this.player.getPosition() + Player.width / 2.0;

		if(projectileTop < playerBottom && projectileBottom > playerTop && projectileLeft < playerRight && projectileRight > playerLeft) {
			this.hasChanged = true;
			this.enemyProjectile = null;
			this.player.damage();
			if(this.player.getHealth() <= 0) {
				this.isGameOver = true;
				this.isGameRunning = false;
			}
		}
	}

	/**
	 * Perform cleanup operations and exit.
	 */
	public void exit() {
		System.out.println("Exiting, wait 200ms when we pretend to do a cleanup...");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			System.out.println("Lolwut @OnExitEvent");
		}
		System.out.println("Exiting finished, closing now!");
	}

	/**
	 * Mark that a key has been pressed down.
	 * @param keyCode Code of the key to mark.
	 */
	public void setKeyDown(int keyCode) {
		if(keyCode > 255) {
			return;
		}
		this.keyDownMap[keyCode] = true;
	}

	/**
	 * Mark that a key has been released.
	 * @param keyCode Code of the key to mark.
	 */
	public void setKeyUp(int keyCode) {
		if(keyCode > 255) {
			return;
		}
		this.keyDownMap[keyCode] = false;
	}

	/**
	 * Retrieve state of a key.
	 * @param keyCode Code of the key to retrieve state of.
	 * @return Whether the key is pressed down.
	 */
	public boolean getKeyState(int keyCode) {
		if(keyCode > 255) {
			return false;
		}
		return this.keyDownMap[keyCode];
	}

	/**
	 * Retrieve player model.
	 * @return {@link Player} model.
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Retrieve enemy model.
	 * @return {@link Enemy} model.
	 */
	public Enemy getEnemy() {
		return enemy;
	}

	/**
	 * Retrieve obstacles model.
	 * @return {@link Obstacles} model.
	 */
	public Obstacles getObstacles() {
		return obstacles;
	}

	/**
	 * Retrieve player projectile.
	 * @return player {@link Projectile}, or null.
	 */
	@Nullable
	public Projectile getPlayerProjectile() {
		return playerProjectile;
	}

	/**
	 * Retrieve enemy projectile.
	 * @return enemy {@link Projectile}, or null.
	 */
	@Nullable
	public Projectile getEnemyProjectile() {
		return enemyProjectile;
	}

	/**
	 * Check whether the game is currently running.
	 * @return whether the game is running.
	 */
	public boolean isGameRunning() {
		return isGameRunning;
	}

	/**
	 * Check whether the game is over (i.e. lost).
	 * @return whether the game is over.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * Check whether the game is won.
	 * @return whether the game is won.
	 */
	public boolean isGameWon() {
		return isGameWon;
	}
}