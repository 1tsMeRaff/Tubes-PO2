package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gameStates.GameStates;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.UI.Frames.*;

public class MainMenuButton {
	
	private int posX, posY, rowIndex, index;
	private int xOffSetCenter = B_WIDTH / 2;
	private boolean mouseOver, mousePressed;
	private GameStates states;
	private BufferedImage[] image;
	private Rectangle bounds;
	
	public MainMenuButton(int posX, int posY, int rowIndex, GameStates states) {
		this.posX = posX;
		this.posY = posY;
		this.rowIndex = rowIndex;
		this.states = states;
		loadImages();
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(posX - xOffSetCenter, posY, B_WIDTH, B_HEIGHT);
		
	}

	private void loadImages() {
	    image = new BufferedImage[3]; // tiga state: normal, hover, pressed
	    BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
	    for (int i = 0; i < image.length; i++) {
	        image[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT,
	                                     B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
	    }
	}
	
	public void draw(Graphics g) {
		
		g.drawImage(image[index], posX - xOffSetCenter, posY, B_WIDTH, B_HEIGHT, null);
	}
	
	public void update() {
		index = 0;
		if(mouseOver) {
			index = 1;
		}
		if(mousePressed) {
			index = 2;
		}
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void applyGameStates() {
		GameStates.state = states;
	}
	
	public void resetBoolean() {
		mouseOver = false;
		mousePressed = false;
	}

}
