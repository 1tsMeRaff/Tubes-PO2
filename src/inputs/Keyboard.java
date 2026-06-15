package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gameStates.GameStates;
import main.GamePanel;

import static utilitytools.Konstanta.Directions.*;

public class Keyboard implements KeyListener {
	
	private GamePanel gamePanel;
	public Keyboard(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().keyReleased(e);
			break;
		default: 
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().keyPressed(e);
			break;
		default: 
			break;
		case OPTIONS:
	        gamePanel.getGame().getGameOptions().keyPressed(e);
	        break;
		}
		
	}
}
