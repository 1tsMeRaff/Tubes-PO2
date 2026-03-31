package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitBox;
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
	}
	
	protected void drawHitbox(Graphics g) {
		// debug
		g.setColor(Color.pink);
		g.drawRect((int) hitBox.x, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
	}

	protected void initHitBox(float x, float y, float widht, float height) {
		
		hitBox = new Rectangle2D.Float(x, y, widht, height);
		
	}
	
//	protected void updateHitBox() {
//		hitBox.x = (int) x;
//		hitBox.y = (int) y;
//	}
	
	public Rectangle2D.Float getHitBox() {
		return hitBox;
	}
}
