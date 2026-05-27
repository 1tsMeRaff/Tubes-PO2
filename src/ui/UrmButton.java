package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

public class UrmButton extends PauseButton {
	private boolean mouseOver;
	private boolean mousePressed;
	private int rowIndex;

	public UrmButton(int x, int y, int width, int height, int rowIndex) {
		super(x, y, width, height);
		this.rowIndex = rowIndex;
	}

	public void update() {
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Color fill = new Color(0, 0, 0, 200);
		Color border = Color.white;
		Color icon = Color.white;
		if (mouseOver) {
			border = new Color(240, 190, 90);
		}
		if (mousePressed) {
			fill = new Color(240, 190, 90);
			border = Color.white;
			icon = Color.black;
		}

		int arc = Math.max(10, width / 3);
		g2.setColor(fill);
		g2.fillRoundRect(x, y, width, height, arc, arc);
		g2.setColor(border);
		g2.setStroke(new BasicStroke(Math.max(2f, width / 12f)));
		g2.drawRoundRect(x, y, width, height, arc, arc);

		drawIcon(g2, icon);
		g2.dispose();
	}

	private void drawIcon(Graphics2D g2, Color icon) {
		int pad = Math.max(6, width / 5);
		int ix = x + pad;
		int iy = y + pad;
		int iw = width - pad * 2;
		int ih = height - pad * 2;
		g2.setColor(icon);
		g2.setStroke(new BasicStroke(Math.max(2f, width / 10f)));

		switch (rowIndex) {
		case 0:
			Polygon play = new Polygon();
			play.addPoint(ix, iy);
			play.addPoint(ix, iy + ih);
			play.addPoint(ix + iw, iy + ih / 2);
			g2.fillPolygon(play);
			break;
		case 1:
			g2.drawArc(ix, iy, iw, ih, 45, 270);
			Polygon arrow = new Polygon();
			arrow.addPoint(ix + iw, iy + ih / 2);
			arrow.addPoint(ix + iw - pad / 2, iy + ih / 2 - pad);
			arrow.addPoint(ix + iw - pad / 2, iy + ih / 2 + pad);
			g2.fillPolygon(arrow);
			break;
		case 2:
			int lineH = Math.max(2, ih / 6);
			int gap = lineH;
			g2.fillRect(ix, iy, iw, lineH);
			g2.fillRect(ix, iy + lineH + gap, iw, lineH);
			g2.fillRect(ix, iy + (lineH + gap) * 2, iw, lineH);
			break;
		default:
			g2.drawRect(ix, iy, iw, ih);
			break;
		}
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
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

	public int getRowIndex() {
		return rowIndex;
	}
}
