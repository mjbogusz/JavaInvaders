package pl.mbogusz3.invaders.model;

import java.awt.event.KeyEvent;
import java.util.Observable;
import pl.mbogusz3.invaders.model.Player;
//import pl.mbogusz3.invaders.model.Enemy;
//import pl.mbogusz3.invaders.model.Obstacle;

/**
 *
 */
public class InvadersModel extends Observable {
//	private final static int enemyRows = 3;
//	private final static int enemyColumns = 5;
//	private final static int obstacleCount = 6;
//	private final static int obstacleHealth = 3;
	private final static int playerHealth = 3;
//	private final Enemy enemy;
	private final Player player;
//	private Obstacle obstacles[];
	private boolean keyDownMap[];

	public InvadersModel() {
//		this.enemy = new Enemy();
		this.player = new Player(playerHealth);
//		this.obstacles = new Obstacle[obstacleCount];
		this.keyDownMap = new boolean[256];
	}

	public void notifyView() {
		this.notifyObservers();
	}

	public void startNewGame() {
		System.out.println("New Game!");
	}

	/**
	 * Recalculate new game state
	 * @param frameTime frame time, in seconds
	 */
	public void recalculate(double frameTime) {
		boolean leftKeyDown = this.getKeyState(KeyEvent.VK_LEFT);
		boolean rightKeyDown = this.getKeyState(KeyEvent.VK_RIGHT);
		if(leftKeyDown && !rightKeyDown) {
			this.player.move(-1, frameTime);
		} else if(!leftKeyDown && rightKeyDown) {
			this.player.move(1, frameTime);
		}

		this.setChanged();
	}

	public void exit() {
		System.out.println("Exiting, wait 200ms...");
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
}