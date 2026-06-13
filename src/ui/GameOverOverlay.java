package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gameStates.GameStates;
import gameStates.PlayStates;
import main.GameCore;

public class GameOverOverlay {

	private PlayStates playStates;

	public GameOverOverlay(PlayStates playStates) {
		this.playStates = playStates;
	}

	public void draw(Graphics g) {
		// Bikin layar jadi gelap transparan
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

		// Tulisan Game Over sederhana
		g.setColor(Color.WHITE);
		g.drawString("GAME OVER", GameCore.GAME_WIDTH / 2 - 35, 150);
		g.drawString("Tekan ESC untuk kembali ke Menu Utama", GameCore.GAME_WIDTH / 2 - 110, 200);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playStates.resetAll(200, 200);
			GameStates.state = GameStates.MENU;
		}
	}
}