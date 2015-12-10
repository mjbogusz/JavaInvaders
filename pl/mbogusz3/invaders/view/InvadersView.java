package pl.mbogusz3.invaders.view;

import pl.mbogusz3.invaders.controller.InvadersController;
import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEvent;
import java.awt.Container;
import java.awt.event.*;
import java.util.Observer;
import java.util.Observable;
import javax.swing.*;

public class InvadersView implements Observer {
	private final InvadersController controller;
	private final InvadersModel model;

	private JFrame frame;
	private Container contentPane;
	private GroupLayout mainLayout;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem menuQuitItem;
	private JMenuItem menuNewGameItem;
	private JLabel playerPositionLabel;

	public InvadersView(InvadersController controller, InvadersModel model) {
		this.controller = controller;
		this.model = model;

		this.frame = new JFrame("Invaders!");
		this.frame.setSize(640, 480);
		this.frame.setLocationRelativeTo(null);
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.setFocusable(true);
		this.contentPane = frame.getContentPane();
		this.mainLayout = new GroupLayout(contentPane);
		this.mainLayout.setAutoCreateContainerGaps(true);
		this.menuBar = new JMenuBar();
		this.menuFile = new JMenu("File");
		this.menuNewGameItem = new JMenuItem("New game");
		this.menuQuitItem = new JMenuItem("Exit");
		this.playerPositionLabel = new JLabel("0.5");
		this.contentPane.setLayout(mainLayout);
		menuNewGameItem.setMnemonic(KeyEvent.VK_N);
		menuNewGameItem.setToolTipText("Start a new game");
		menuQuitItem.setMnemonic(KeyEvent.VK_E);
		menuQuitItem.setToolTipText("Exit application");
		menuFile.add(menuNewGameItem);
		menuFile.add(menuQuitItem);
		menuBar.add(menuFile);

		contentPane.add(playerPositionLabel);
		frame.setJMenuBar(menuBar);
		frame.pack();
	}

	public void initialize() {
		SwingUtilities.invokeLater(() -> {
			System.out.println("View: initializing frame");
			this.frame.setVisible(true);
			this.addListeners();
		});
	}

	public void update(Observable observable, Object arg) {
		SwingUtilities.invokeLater(() -> {
			double playerPosition = model.getPlayer().getPosition();
			playerPositionLabel.setText(Double.toString(playerPosition));
		});
	}

	private void addListeners() {
		InvadersController controller = this.controller;
		menuNewGameItem.addActionListener(event -> controller.putEvent(new InvadersEvent("newGame")));

		menuQuitItem.addActionListener(event -> {
			controller.putEvent(new InvadersEvent("exitGame"));
			closeView();
		});

		System.out.println("View: adding key listeners");
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