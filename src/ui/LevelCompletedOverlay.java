package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import gameStates.GameStates;
import gameStates.PlayStates;
import main.GameCore;

public class LevelCompletedOverlay {

	private PlayStates play;
	private UrmButton menu, next;
	
	public LevelCompletedOverlay(PlayStates play) {
		this.play = play;
		initButtons();
	}

	private void initButtons() {
		int menuX = (int) (330 * GameCore.SCALE);
		int nextX = (int) (445 * GameCore.SCALE);
		int y = (int) (195 * GameCore.SCALE);
		int urmSize = (int) (56 * GameCore.SCALE);
		
		menu = new UrmButton(menuX, y, urmSize, urmSize, 2);
		next = new UrmButton(nextX, y, urmSize, urmSize, 0);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
		
		g.setColor(Color.WHITE);
		g.fillRect((int)(250 * GameCore.SCALE), (int)(100 * GameCore.SCALE), (int)(350 * GameCore.SCALE), (int)(200 * GameCore.SCALE));
		
		menu.draw(g);
		next.draw(g);
	}

	public void update() {
		menu.update();
		next.update();
	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		next.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e))
			menu.setMouseOver(true);
		else if (isIn(next, e))
			next.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				// [PERBAIKAN] Berikan koordinat spawn awal saat kembali ke Menu
				play.resetAll(200, 200);
				GameStates.state = GameStates.MENU;
			}
		} else if (isIn(next, e)) {
			if (next.isMousePressed()) {
				play.loadNextLevel(); 
				System.out.println("Tombol NEXT ditekan: Memuat Level Berikutnya!");
			}
		}

		menu.resetBools();
		next.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e))
			menu.setMousePressed(true);
		else if (isIn(next, e))
			next.setMousePressed(true);
	}
}