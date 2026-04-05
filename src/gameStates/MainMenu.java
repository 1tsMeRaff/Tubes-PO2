package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.GameCore;
import ui.MainMenuButton;

public class MainMenu extends States implements StateMethods {
	
	private MainMenuButton[] buttons = new MainMenuButton[3]; 

	public MainMenu(GameCore gc) {
		super(gc);
		loadButtons();
		// TODO Auto-generated constructor stub
	}

	private void loadButtons() {
		buttons[0] = new MainMenuButton(GameCore.GAME_WIDTH / 2, (int) (150 * GameCore.SCALE), 0, GameStates.PLAYING);
//		buttons[0] = new MainMenuButton(GameCore.GAME_WIDTH / 2, (int) (150 * GameCore.SCALE), 0, GameStates.OPTIONS);
//		buttons[0] = new MainMenuButton(GameCore.GAME_WIDTH / 2, (int) (150 * GameCore.SCALE), 0, GameStates.QUIT);
		
	}

	@Override
	public void update() {
		for(MainMenuButton mb : buttons) {
			mb.update();
		}
		
	}

	@Override
	public void draw(Graphics g) {
		for(MainMenuButton mb : buttons) {
			mb.draw(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(MainMenuButton mb : buttons) {
			
		}
		
	}

	@Override
	public void mouseRelease(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			GameStates.state = GameStates.PLAYING;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}
