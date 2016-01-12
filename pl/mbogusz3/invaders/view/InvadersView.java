package pl.mbogusz3.invaders.view;

import pl.mbogusz3.invaders.controller.InvadersController;
import pl.mbogusz3.invaders.DTO.InvadersModelDTO;
import pl.mbogusz3.invaders.types.InvadersEvent;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;
import java.util.Observer;
import java.util.Observable;
import javax.swing.*;

public class InvadersView implements Observer {
	private final InvadersController controller;
	private InvadersModelDTO model;

	private JFrame frame;
	private Container contentPane;
	private JMenuItem menuQuitItem;
	private JMenuItem menuNewGameItem;
	private JLabel fpsLabel;
	private JLabel player;
	private JLabel playerProjectile;
	private JLabel enemyProjectile;
	private JLabel[] obstacles;
	private JLabel[][] enemies;

	public InvadersView(InvadersController controller) {
		this.controller = controller;

		this.frame = new JFrame("Invaders!");
		this.frame.setSize(640, 480);
		this.frame.setLocationRelativeTo(null);
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.setFocusable(true);

		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		this.menuNewGameItem = new JMenuItem("New game");
		this.menuNewGameItem.setMnemonic(KeyEvent.VK_N);
		this.menuNewGameItem.setToolTipText("Start a new game");
		menuFile.add(this.menuNewGameItem);
		this.menuQuitItem = new JMenuItem("Exit");
		this.menuQuitItem.setMnemonic(KeyEvent.VK_E);
		this.menuQuitItem.setToolTipText("Exit application");
		menuFile.add(this.menuQuitItem);
		menuBar.add(menuFile);
		this.frame.setJMenuBar(menuBar);

		this.contentPane = this.frame.getContentPane();
		this.contentPane.setLayout(null);
		this.player = new JLabel("0.5");
		this.contentPane.add(this.player);
		this.player.setOpaque(true);
		this.player.setBackground(new Color(0, 0, 255));
		this.player.setForeground(new Color(255, 255, 255));
		this.player.setHorizontalTextPosition(SwingConstants.CENTER);
		this.player.setVerticalTextPosition(SwingConstants.CENTER);

		this.playerProjectile = new JLabel("");
		this.contentPane.add(this.playerProjectile);
		this.playerProjectile.setOpaque(true);
		this.playerProjectile.setBackground(new Color(255, 0, 0));
		this.playerProjectile.setVisible(false);

		this.enemyProjectile = new JLabel("");
		this.contentPane.add(this.enemyProjectile);
		this.enemyProjectile.setOpaque(true);
		this.enemyProjectile.setBackground(new Color(255, 146, 0));
		this.enemyProjectile.setVisible(false);

		this.fpsLabel = new JLabel("0.0");
		this.contentPane.add(fpsLabel);
	}

	public void initialize() {
		SwingUtilities.invokeLater(() -> {
			Insets frameInsets = this.frame.getInsets();
			Dimension frameSize = new Dimension(frameInsets.left + frameInsets.right + 640, frameInsets.top + frameInsets.bottom + 320);
			this.frame.setMinimumSize(frameSize);
			this.frame.setSize(frameSize);
			this.frame.setVisible(true);
			controller.putEvent(new InvadersEvent("windowResized"));

			this.addListeners();
		});
	}

	public void update(Observable observable, Object arg) {
		this.model = (InvadersModelDTO)(arg);
		SwingUtilities.invokeLater(this::redraw);
	}

	private void redraw() {
		Insets paneInsets = this.contentPane.getInsets();
		Dimension paneSize = this.contentPane.getSize();

		// Draw player
		double playerPosition = this.model.getPlayer().getPosition();
		double playerWidth = this.model.getPlayer().getWidth();
		double playerPositionLeft = (playerPosition - 0.5 * playerWidth) * paneSize.width + paneInsets.left;
		double playerPositionTop = (1 - playerWidth) * paneSize.height + paneInsets.top;
		this.player.setText(Integer.toString(this.model.getPlayer().getHealth()));
		this.player.setBounds((int)(playerPositionLeft), (int)(playerPositionTop), (int)(playerWidth * paneSize.width), (int)(playerWidth * paneSize.height));

		// Draw obstacles
		int obstacleCount = this.model.getObstacles().getCount();
		// First-time init of obstacles - we don't know their count before the game begins and first update comes
		if(this.obstacles == null) {
			this.obstacles = new JLabel[obstacleCount];
			for(int i = 0; i < obstacleCount; i++) {
				this.obstacles[i] = new JLabel("");
				this.contentPane.add(this.obstacles[i]);
				this.obstacles[i].setOpaque(true);
				this.obstacles[i].setBackground(new Color(0, 207, 51));
			}
		}
		double obstacleSpacerWidth = (paneSize.width / (2 * obstacleCount + 1));
		for(int i = 0; i < obstacleCount; i++) {
			int state = this.model.getObstacles().getState()[i];
			if(state == 0) {
				this.obstacles[i].setVisible(false);
				continue;
			} else {
				this.obstacles[i].setVisible(true);
			}
			double obstaclePositionLeft = obstacleSpacerWidth * (2 * i + 1) + paneInsets.left;
			double obstaclePositionTop = 0.8 * paneSize.height + paneInsets.left;
			this.obstacles[i].setText(Integer.toString(state));
			this.obstacles[i].setBounds((int)(obstaclePositionLeft), (int)(obstaclePositionTop), (int)(obstacleSpacerWidth), (int)(0.1 * paneSize.height));
		}

		// Draw enemy
		int enemyRows = this.model.getEnemy().getFirstRow() + 1;
		int enemyColumns = this.model.getEnemy().getLastColumn() - this.model.getEnemy().getFirstColumn() + 1;
		// First time init of enemies
		if(this.enemies == null) {
			this.enemies = new JLabel[enemyRows][enemyColumns];
			for(int i = 0; i < enemyRows; i++) {
				for(int j = 0; j < enemyColumns; j++) {
					this.enemies[i][j] = new JLabel();
					this.contentPane.add(this.enemies[i][j]);
					this.enemies[i][j].setOpaque(true);
					this.enemies[i][j].setBackground(new Color(0, 0, 0));
				}
			}
		}
		double enemyWidth = this.model.getEnemy().getUnitWidth() * paneSize.width;
		double unitPositionTop = this.model.getEnemy().getPositionTop() * paneSize.height;
		for(int i = 0; i < enemyRows; i++) {
			double unitPositionLeft = this.model.getEnemy().getPositionLeft() * paneSize.width;
			unitPositionLeft -= (enemyColumns - 0.5) * enemyWidth;

			for(int j = this.model.getEnemy().getFirstColumn(); j <= this.model.getEnemy().getLastColumn(); j++) {
				if(this.model.getEnemy().getState()[i][j]) {
					this.enemies[i][j].setVisible(true);
					this.enemies[i][j].setBounds((int)(unitPositionLeft), (int)(unitPositionTop), (int)(enemyWidth), (int)(0.05 * paneSize.height));
				} else {
					this.enemies[i][j].setVisible(false);
				}
				unitPositionLeft += 2 * enemyWidth;
			}
			unitPositionTop += 0.1 * paneSize.height;
		}

		// Draw shots
		// Player projectile
		if(this.model.getPlayerProjectile() == null) {
			this.playerProjectile.setVisible(false);
		} else {
			this.playerProjectile.setVisible(true);
			double projectileWidth = paneSize.width * this.model.getPlayerProjectile().getWidth();
			double projectileHeight = paneSize.height * this.model.getPlayerProjectile().getHeight();
			double projectilePositionLeft = paneSize.width * this.model.getPlayerProjectile().getPositionX() - projectileWidth / 2 + paneInsets.left;
			double projectilePositionTop = paneSize.height * this.model.getPlayerProjectile().getPositionY() - projectileHeight / 2 + paneInsets.top;
			this.playerProjectile.setBounds((int)(projectilePositionLeft), (int)(projectilePositionTop), (int)(projectileWidth), (int)(projectileHeight));
		}
		// Enemy projectile
		if(this.model.getEnemyProjectile() == null) {
			this.enemyProjectile.setVisible(false);
		} else {
			this.enemyProjectile.setVisible(true);
			double projectileWidth = paneSize.width * this.model.getEnemyProjectile().getWidth();
			double projectileHeight = paneSize.height * this.model.getEnemyProjectile().getHeight();
			double projectilePositionLeft = paneSize.width * this.model.getEnemyProjectile().getPositionX() - projectileWidth / 2 + paneInsets.left;
			double projectilePositionTop = paneSize.height * this.model.getEnemyProjectile().getPositionY() - projectileHeight / 2 + paneInsets.top;
			this.enemyProjectile.setBounds((int)(projectilePositionLeft), (int)(projectilePositionTop), (int)(projectileWidth), (int)(projectileHeight));
		}

		// Additional draws
		fpsLabel.setText(Double.toString(controller.getFPS()));
		fpsLabel.setBounds(5, 5, 60, 10);
	}

	private void addListeners() {
		InvadersController controller = this.controller;
		menuNewGameItem.addActionListener(event -> controller.putEvent(new InvadersEvent("newGame")));

		menuQuitItem.addActionListener(event -> {
			controller.putEvent(new InvadersEvent("exitGame"));
			closeView();
		});

		frame.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent event) {}

			public void keyPressed(KeyEvent event) {
				controller.putEvent(new InvadersEvent("keyDown") {{
					payload.put("keyCode", Integer.toString(event.getKeyCode()));
				}});
			}

			public void keyReleased(KeyEvent event) {
				controller.putEvent(new InvadersEvent("keyUp") {{
					payload.put("keyCode", Integer.toString(event.getKeyCode()));
				}});
			}
		});

		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				controller.putEvent(new InvadersEvent("windowResized"));
				super.componentResized(e);
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				controller.putEvent(new InvadersEvent("exitGame"));
				closeView();
			}
		});
	}

	private void closeView() {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(false);
			frame.dispose();
		});
	}
}