package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gameStates.GameStates;
import gameStates.PlayStates;
import main.GameCore;

public class GameOverUI {

	private PlayStates playStates;

	public GameOverUI(PlayStates playStates) {
	this.playStates = playStates;
	}
	public void draw(Graphics g) {
	g.setColor(new Color(0, 0, 0, 200));
	g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

	g.setColor(Color.white);
	g.drawString("Game Over", GameCore.GAME_WIDTH / 2, 150);
	g.drawString("Press esc to enter Main Menu!", GameCore.GAME_WIDTH / 2, 300);
	
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playStates.resetAll(200, 200);
			GameStates.state = GameStates.MENU;
		}
		
	}
}
