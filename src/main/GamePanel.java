package main;

import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.Keyboard;
import inputs.Mouse;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Mouse mouse;
	private int deltaX = 100, deltaY = 100; 
	
	public GamePanel() {
		
		mouse = new Mouse(this);
		addKeyListener(new Keyboard(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
	}
	
	public void changeDeltaX(int value) {
		this.deltaX += value;
		repaint();
	}
	
	public void changeDeltaY(int value) {
		this.deltaY += value;
		repaint();
	}
	
	public void setRectPos(int x, int y) {
		this.deltaX = x;
		this.deltaY = y;
		repaint();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.fillRect(deltaX, deltaY, 200, 50);
		
	}

}
