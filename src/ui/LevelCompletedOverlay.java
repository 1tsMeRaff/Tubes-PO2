package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gameStates.GameStates;
import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;
// IMPORT YANG BENAR:
import static utilitytools.Konstanta.UI.PauseButtons.*;

public class LevelCompletedOverlay {

	private PlayStates playStates;
	private UrmButton menu, next;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;

	public LevelCompletedOverlay(PlayStates playStates) {
		this.playStates = playStates;
		initImg();
		initButtons();
	}

	private void initImg() {
		// Asumsi Orang C belum membuat gambar khusus Level Completed, 
		// kita pinjam gambar Pause Background sementara agar tidak error.
		img = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		
		bgW = (int) (img.getWidth() * GameCore.SCALE);
		bgH = (int) (img.getHeight() * GameCore.SCALE);
		bgX = GameCore.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (75 * GameCore.SCALE);
	}

	private void initButtons() {
		int menuX = (int) (330 * GameCore.SCALE);
		int nextX = (int) (445 * GameCore.SCALE);
		int y = (int) (195 * GameCore.SCALE);

		next = new UrmButton(nextX, y, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, 0);
		menu = new UrmButton(menuX, y, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, 2);
	}

	public void update() {
		next.update();
		menu.update();
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

		g.drawImage(img, bgX, bgY, bgW, bgH, null);

		next.draw(g);
		menu.draw(g);
	}

	public void mouseMoved(MouseEvent e) {
		next.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e)) {
			menu.setMouseOver(true);
		} else if (isIn(next, e)) {
			next.setMouseOver(true);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				// LOGIKA: Kembali ke Main Menu
				playStates.resetAll();
				GameStates.state = GameStates.MENU;
			}
		} else if (isIn(next, e)) {
			if (next.isMousePressed()) {
				// LOGIKA: Muat Map Selanjutnya!
				playStates.loadNextLevel();
			}
		}

		menu.resetBools();
		next.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e)) {
			menu.setMousePressed(true);
		} else if (isIn(next, e)) {
			next.setMousePressed(true);
		}
	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
}