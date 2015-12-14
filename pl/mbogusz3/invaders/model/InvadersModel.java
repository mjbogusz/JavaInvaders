package pl.mbogusz3.invaders.model;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *
 */
public class InvadersModel extends Observable {
	private final static int enemyRows = 3;
	private final static int enemyColumns = 5;
	private final static int obstacleCount = 3;
	private final static int playerHealth = 3;

	private final Enemy enemy;
	private final Player player;
	private final Obstacles obstacles;
	private boolean keyDownMap[];

	public InvadersModel() {
		this.enemy = new Enemy(enemyRows, enemyColumns);
		this.player = new Player(playerHealth);
		this.obstacles = new Obstacles(obstacleCount);
		this.keyDownMap = new boolean[256];
	}

	public void notifyView() {
		this.notifyObservers(new InvadersModelDTO(this));
	}

	public void startNewGame() {
		System.out.println("New Game!");
		this.player.respawn();
		this.enemy.respawn();
		this.obstacles.respawn();
//		this.shots.clear();
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
		boolean leftKeyDown = this.getKeyState(KeyEvent.VK_LEFT);
		boolean rightKeyDown = this.getKeyState(KeyEvent.VK_RIGHT);
		if(leftKeyDown && !rightKeyDown) {
			this.player.move(-1, frameTime);
			hasChanged = true;
		} else if(!leftKeyDown && rightKeyDown) {
			this.player.move(1, frameTime);
			hasChanged = true;
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
}