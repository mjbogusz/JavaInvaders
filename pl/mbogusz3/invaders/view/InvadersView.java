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

		this.playerProjectile = new JLabel("");
		this.contentPane.add(this.playerProjectile);
		this.playerProjectile.setOpaque(true);
		this.playerProjectile.setBackground(new Color(255, 0, 0));
		this.playerProjectile.setVisible(false);

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
		double playerPosition = model.getPlayer().getPosition();
		double playerWidth = model.getPlayer().getWidth();
		double playerPositionLeft = (playerPosition - 0.5 * playerWidth) * paneSize.width + paneInsets.left;
		double playerPositionTop = (1 - playerWidth) * paneSize.height + paneInsets.top;
		player.setText(Integer.toString(model.getPlayer().getHealth()));
		player.setBounds((int)(playerPositionLeft), (int)(playerPositionTop), (int)(playerWidth * paneSize.width), (int)(playerWidth * paneSize.height));

		// Draw obstacles
		int obstacleCount = model.getObstacles().getCount();
		// First-time init of obstacles - we don't know their count before the game begins and first update comes
		if(this.obstacles == null) {
			this.obstacles = new JLabel[obstacleCount];
			for(int i = 0; i < obstacleCount; i++) {
				this.obstacles[i] = new JLabel(Integer.toString(model.getObstacles().getStrength()));
				this.contentPane.add(this.obstacles[i]);
				this.obstacles[i].setOpaque(true);
				this.obstacles[i].setBackground(new Color(0, 207, 51));
			}
		}
		double obstacleSpacerWidth = (paneSize.width / (2 * obstacleCount + 1));
		for(int i = 0; i < obstacleCount; i++) {
			double obstaclePositionLeft = obstacleSpacerWidth * (2 * i + 1) + paneInsets.left;
			double obstaclePositionTop = 0.8 * paneSize.height + paneInsets.left;
			this.obstacles[i].setText(Integer.toString(model.getObstacles().getState()[i]));
			this.obstacles[i].setBounds((int)(obstaclePositionLeft), (int)(obstaclePositionTop), (int)(obstacleSpacerWidth), (int)(0.1 * paneSize.height));
		}

		// Draw enemy
		int enemyRows = model.getEnemy().getFirstRow();
		int enemyColumns = model.getEnemy().getLastColumn() - model.getEnemy().getFirstColumn();
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
		double enemyWidth = model.getEnemy().getUnitWidth() * paneSize.width;
		double unitPositionTop = model.getEnemy().getPositionTop() * paneSize.height;
		for(int i = 0; i < enemyRows; i++) {
			double unitPositionLeft = model.getEnemy().getPositionLeft() * paneSize.width;
			unitPositionLeft -= (enemyColumns - 0.5) * enemyWidth;

			for(int j = model.getEnemy().getFirstColumn(); j < model.getEnemy().getLastColumn(); j++) {
				if(model.getEnemy().getState()[i][j]) {
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
		if(model.getPlayerProjectile() == null) {
			this.playerProjectile.setVisible(false);
		} else {
			this.playerProjectile.setVisible(true);
			double projectilePositionLeft = (paneSize.width * model.getPlayerProjectile().getPositionX()) + paneInsets.left - (playerWidth / 10.0);
			double projectilePositionTop = (paneSize.height * model.getPlayerProjectile().getPositionY()) + paneInsets.top - (playerWidth / 10.0);
			double projectileWidth = paneSize.width * playerWidth / 5.0;
			double projectileHeight = paneSize.height * playerWidth / 5.0;
			this.playerProjectile.setBounds((int)(projectilePositionLeft), (int)(projectilePositionTop), (int)(projectileWidth), (int)(projectileHeight));
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