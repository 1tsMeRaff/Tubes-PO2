package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.Keyboard;
import inputs.Mouse;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Mouse mouse;
	private int deltaX = 100, deltaY = 100;
	private BufferedImage image, subImg;
	
	public GamePanel() {
		
		mouse = new Mouse(this);
		
		importImg();
		setPanelSize();
		addKeyListener(new Keyboard(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		setFocusable(true);
		requestFocusInWindow();
		
	}
	
	private void importImg() {
		InputStream is = getClass().getResourceAsStream("/WarriorMan-Sheet.png");
		
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setPanelSize() {
		Dimension size = new Dimension(500, 500);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
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
		
		subImg = image.getSubimage(1, 8,80, 80);
		g.drawImage(subImg, (int) deltaX, (int) deltaY, 128, 80, null);
	}
}
