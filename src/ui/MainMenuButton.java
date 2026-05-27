package ui;

import gameStates.GameStates;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import static utilitytools.Konstanta.UI.Frames.*;

public class MainMenuButton {
	
	private int posX, posY, rowIndex, index;
	private int xOffSetCenter = B_WIDTH / 2;
	private boolean mouseOver, mousePressed;
	private GameStates states;
	private Rectangle bounds;
	
	public MainMenuButton(int posX, int posY, int rowIndex, GameStates states) {
		this.posX = posX;
		this.posY = posY;
		this.rowIndex = rowIndex;
		this.states = states;
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(posX - xOffSetCenter, posY, B_WIDTH, B_HEIGHT);
		
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int x = posX - xOffSetCenter;
		int y = posY;
		Color base = new Color(34, 34, 36);
		Color hover = new Color(60, 72, 90);
		Color pressed = new Color(90, 76, 52);
		Color fill = base;
		if (index == 1) {
			fill = hover;
		} else if (index == 2) {
			fill = pressed;
		}
		int arc = Math.max(10, B_HEIGHT / 2);
		g2.setColor(fill);
		g2.fillRoundRect(x, y, B_WIDTH, B_HEIGHT, arc, arc);
		g2.setColor(new Color(210, 210, 210));
		g2.drawRoundRect(x, y, B_WIDTH, B_HEIGHT, arc, arc);

		g2.setFont(new Font("SansSerif", Font.BOLD, (int) (20 * main.GameCore.SCALE)));
		g2.setColor(new Color(230, 230, 230));
		String label = getLabel();
		int textW = g2.getFontMetrics().stringWidth(label);
		int textH = g2.getFontMetrics().getHeight();
		g2.drawString(label, x + (B_WIDTH - textW) / 2, y + (B_HEIGHT + textH) / 2 - (int) (4 * main.GameCore.SCALE));
		g2.dispose();
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

	private String getLabel() {
		switch (rowIndex) {
		case 0:
			return "PLAY";
		case 1:
			return "OPTIONS";
		case 2:
			return "QUIT";
		default:
			return "BUTTON";
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
