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
	private boolean isGameWon;

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
		this.isGameWon = false;
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
		// Player projectile first, notice it moves from Y=1.0 towards Y=0.0
		if(this.playerProjectile != null) {
			boolean projectileCollision = false;
			double projectileLeft = this.playerProjectile.getPositionX() - Projectile.width / 2.0;
			double projectileRight = this.playerProjectile.getPositionX() + Projectile.width / 2.0;
			double projectileTop = this.playerProjectile.getPositionY() - Projectile.height / 2.0;
			double projectileBottom = this.playerProjectile.getPositionY() + Projectile.height / 2.0;
			// First, collisions with obstacles (no damage)
			double obstacleTop = Obstacles.positionTop - Obstacles.height / 2.0;
			double obstacleBottom = Obstacles.positionTop + Obstacles.height / 2.0;
			// Direction is -1 therefore the reversed comparison
			if(projectileTop < obstacleBottom && projectileBottom > obstacleTop) {
				for(int i = 0; i < this.obstacles.getCount(); i++) {
					if(this.obstacles.getState()[i] == 0) {
						continue;
					}
					double obstacleLeft = (2.0 * i + 1.0) * this.obstacles.getWidth();
					double obstacleRight = (2.0 * i + 2.0) * this.obstacles.getWidth();
					if(projectileRight > obstacleLeft && projectileLeft < obstacleRight) {
						projectileCollision = true;
						break;
					}
				}
			}

			double enemiesBottom = this.enemy.getPositionTop() + (2 * this.enemy.getFirstRow() + 1) * Enemy.unitHeight;
			// Then, collisions with enemies
			if(!projectileCollision && projectileTop < enemiesBottom) {
				// For each row
				for(int i = this.enemy.getFirstRow(); i >= 0; i--) {
					// Check whether projectile is at row's height
					double enemyBottom = enemiesBottom - 2.0 * (this.enemy.getFirstRow() - i) * Enemy.unitHeight;
					double enemyTop = enemyBottom - Enemy.unitHeight;
					if(projectileTop > enemyBottom || projectileBottom < enemyTop) {
						continue;
					}

					double middleColumn = (this.enemy.getLastColumn() - this.enemy.getFirstColumn()) / 2.0;
					// Then for each column
					for(int j = this.enemy.getFirstColumn(); j <= this.enemy.getLastColumn(); j++) {
						if(!this.enemy.getState()[i][j]) {
							continue;
						}

						// Calculate enemy horizontal position
						// (Enemy unit middle is twice the unit width times count of units from middle position)
						double enemyLeft = this.enemy.getPositionLeft() - (middleColumn - j) * 2 * this.enemy.getUnitWidth() - this.enemy.getUnitWidth() / 2.0;
						double enemyRight = enemyLeft + this.enemy.getUnitWidth();
						// And compare it to projectile's position
						if(projectileRight > enemyLeft && projectileLeft < enemyRight) {
							projectileCollision = true;
							this.enemy.destroyUnit(i, j);
							if(this.enemy.getFirstColumn() > this.enemy.getLastColumn()) {
								this.isGameWon = true;
							}
							break;
						}
					}
					if(projectileCollision) {
						break;
					}
				}
			}
			if(projectileCollision) {
				this.playerProjectile = null;
			}
		}
		// Enemy projectiles
		if(this.enemyProjectile != null) {
			///TODO
		}

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

	public boolean isGameWon() {
		return isGameWon;
	}
}