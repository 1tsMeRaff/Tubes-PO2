package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gameStates.GameStates;
import gameStates.PlayStates;
import main.GameCore;

public class GameOverOverlay {

	private static final Color OVERLAY_BG = new Color(0, 0, 0, 200);

	private PlayStates playStates;

	public GameOverOverlay(PlayStates playStates) {
		this.playStates = playStates;
	}

	public void draw(Graphics g) {
		// Bikin layar jadi gelap transparan
		g.setColor(OVERLAY_BG);
		g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

		// Tulisan Game Over sederhana
		g.setColor(Color.WHITE);
		g.drawString("GAME OVER", GameCore.GAME_WIDTH / 2 - 35, (int)(150 * GameCore.SCALE));
		g.drawString("Tekan ESC untuk kembali ke Menu Utama", GameCore.GAME_WIDTH / 2 - 110, (int)(200 * GameCore.SCALE));
		g.drawString("Tekan R / Spasi / Enter untuk Main Lagi", GameCore.GAME_WIDTH / 2 - 115, (int)(250 * GameCore.SCALE));
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE) {
			playStates.resetAll(200, 200);
			GameStates.state = GameStates.MENU;
			
			playStates.getGameCore().getAudioPlayer().playSong(audio.AudioPlayer.MENU_1);
			
		} else if (code == KeyEvent.VK_R || code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
			playStates.resetAll(200, 200); 
		}
	}
}