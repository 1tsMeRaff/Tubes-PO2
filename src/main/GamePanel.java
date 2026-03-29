package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

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
		Dimension size = new Dimension(500, 500);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}

	public void myUpdate() {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		gc.render(g);
	}
	
	public GameCore getGame() {
		return gc;
	}
}
