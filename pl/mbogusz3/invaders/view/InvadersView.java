package pl.mbogusz3.invaders.view;

import pl.mbogusz3.invaders.controller.InvadersController;
import pl.mbogusz3.invaders.model.InvadersModelDTO;
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
	private JLabel playerPositionLabel;

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
		this.playerPositionLabel = new JLabel("0.5");
		this.contentPane.add(this.playerPositionLabel);
		this.playerPositionLabel.setOpaque(true);
		this.playerPositionLabel.setBackground(new Color(255, 0, 0));
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
		double playerPosition = model.getPlayer().getPosition();
		double playerWidth = model.getPlayer().getWidth();
		double playerPositionLeft = (playerPosition - 0.5 * playerWidth) * paneSize.width + paneInsets.left;
		double playerPositionTop = (1 - playerWidth) * paneSize.height + paneInsets.top;
		playerPositionLabel.setText(Integer.toString(model.getPlayer().getHealth()));
		playerPositionLabel.setBounds((int)(playerPositionLeft), (int)(playerPositionTop), (int)(playerWidth * paneSize.width), (int)(playerWidth * paneSize.height));
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