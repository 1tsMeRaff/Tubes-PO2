package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.GameCore;

public class PauseStates extends States implements StateMethods {

	private final Rectangle resumeBtn;
	private final Rectangle menuBtn;
	private boolean resumeHover;
	private boolean menuHover;

	public PauseStates(GameCore gc) {
		super(gc);
		int btnWidth = (int) (180 * GameCore.SCALE);
		int btnHeight = (int) (42 * GameCore.SCALE);
		int centerX = GameCore.GAME_WIDTH / 2;
		int centerY = GameCore.GAME_HEIGHT / 2;
		resumeBtn = new Rectangle(centerX - btnWidth / 2, centerY - 10 - btnHeight, btnWidth, btnHeight);
		menuBtn = new Rectangle(centerX - btnWidth / 2, centerY + 10, btnWidth, btnHeight);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics g) {
		gc.getPlay().draw(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 140));
		g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

		g2.setFont(new Font("Arial", Font.BOLD, (int) (34 * GameCore.SCALE)));
		g2.setColor(Color.WHITE);
		String title = "PAUSED";
		int titleWidth = g2.getFontMetrics().stringWidth(title);
		g2.drawString(title, (GameCore.GAME_WIDTH - titleWidth) / 2, GameCore.GAME_HEIGHT / 2 - 70);

		drawButton(g2, resumeBtn, "RESUME ", resumeHover);
		drawButton(g2, menuBtn, "MAIN MENU", menuHover);
	}

	private void drawButton(Graphics2D g2, Rectangle bounds, String text, boolean hover) {
		g2.setColor(hover ? new Color(255, 215, 0) : Color.LIGHT_GRAY);
		g2.setFont(new Font("Arial", Font.BOLD, (int) (22 * GameCore.SCALE)));
		int stringWidth = g2.getFontMetrics().stringWidth(text);
		int stringHeight = g2.getFontMetrics().getHeight();
		g2.drawString(text,
				bounds.x + (bounds.width - stringWidth) / 2,
				bounds.y + (bounds.height + stringHeight) / 2 - 6);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (resumeHover) {
			GameStates.state = GameStates.PLAYING;
		} else if (menuHover) {
			GameStates.state = GameStates.MENU;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		resumeHover = resumeBtn.contains(e.getX(), e.getY());
		menuHover = menuBtn.contains(e.getX(), e.getY());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			GameStates.state = GameStates.PLAYING;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}