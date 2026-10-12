package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import static main.GameCore.GAME_WIDTH;
import static main.GameCore.GAME_HEIGHT;

import inputs.Keyboard;
import inputs.Mouse;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Mouse mouse;
	private GameCore gc;
	
	public GamePanel(GameCore gc) {
		mouse = new Mouse(this);
		this.gc = gc;
		
		setPanelSize();
		addKeyListener(new Keyboard(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		setFocusable(true);
		requestFocusInWindow();
		
	}

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}

	public void myUpdate() {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;

		float scaleX = (float) getWidth() / GAME_WIDTH;
		float scaleY = (float) getHeight() / GAME_HEIGHT;

		g2.scale(scaleX, scaleY);

		gc.render(g2);
	}
	
	public GameCore getGame() {
		return gc;
	}
}
