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

import static utilitytools.Konstanta.KonstantaPlayerRight.*;
import static utilitytools.Konstanta.Directions.*;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Mouse mouse;
	private int deltaX = 100, deltaY = 100;
	private BufferedImage image;
	private BufferedImage[][] animasi;
	private int aniTick, aniIndex, aniSpeed = 15;
	
	private int playerAction = IDLE_ACTIVE;
	private int playerDirection = -1;
	private boolean moving = false;
	
	public GamePanel() {
		
		mouse = new Mouse(this);
		
		importImg();
		
		loadAnimations();
		
		setPanelSize();
		addKeyListener(new Keyboard(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		setFocusable(true);
		requestFocusInWindow();
		
	}
	
	private void loadAnimations() {
		animasi = new BufferedImage[10][16];
		
		for(int j = 0; j < animasi.length; j++) {
			for(int i = 0; i < animasi[j].length; i ++) {
				animasi[j][i] = image.getSubimage(i * 80, j * 64, 80, 64); 
			}
		}
		
		
	}

	private void importImg() {
		InputStream is = getClass().getResourceAsStream("/player_right.png");
		
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setPanelSize() {
		Dimension size = new Dimension(500, 500);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}

	public void setDirection(int direction) {
		this.playerDirection = direction;
		moving = true;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	private void updateAnimationTick() {
		
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
			}
		}
		
	}
	
	private void setAnimation() {
		
		if(moving) {
			playerAction = LARI;
		}else {
			playerAction = IDLE_ACTIVE;
		}
	}
	
	private void updatePos() {
		
		if(moving) {
			switch(playerDirection) {
			case LEFT:
				deltaX -= 5;
				break;
			case UP:
				deltaY -= 5;
				break;
			case RIGHT:
				deltaX += 5;
				break;
			case DOWN:
				deltaY += 5;
				break;
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		updateAnimationTick();
		
		setAnimation();
		updatePos();

		g.drawImage(animasi[playerAction][aniIndex], (int) deltaX, (int) deltaY, 160, 128, null);
	}

	
}
