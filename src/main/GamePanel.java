package main;

import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.Keyboard;
import inputs.Mouse;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int fps = 0;
	private long lastCheck = 0;
	
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
	}
	
	public void changeDeltaY(int value) {
		this.deltaY += value;
	}
	
	public void setRectPos(int x, int y) {
		this.deltaX = x;
		this.deltaY = y;
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		updateRectangle();
		
		g.fillRect(deltaX, deltaY, 200, 50);
		
		fps++;
		if(System.currentTimeMillis() - lastCheck >= 1000) {
			lastCheck = System.currentTimeMillis();
			System.out.println("FPS : " + fps);
			fps = 0;
			
		}
		
		repaint();
		
	}

	private void updateRectangle() {
		deltaX++;
		deltaY++;
	}

}
