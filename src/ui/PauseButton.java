package ui;

import java.awt.Rectangle;

public class PauseButton {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected Rectangle bounds;

	public PauseButton(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		createBounds();
	}

	protected void createBounds() {
		bounds = new Rectangle(x, y, width, height);
	}

	private void updateBounds() {
		if (bounds != null) {
			bounds.setBounds(x, y, width, height);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		updateBounds();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		updateBounds();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		updateBounds();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		updateBounds();
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
}
