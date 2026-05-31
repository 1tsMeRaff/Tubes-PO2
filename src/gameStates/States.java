package gameStates;

import java.awt.event.MouseEvent;

import main.GameCore;
import ui.MainMenuButton;

public class States {

	protected GameCore gc;
	
	public States(GameCore gc) {
		this.gc = gc;
	}
	
	public boolean isIn(MouseEvent e, MainMenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}
	
	public GameCore GetGame() {
		return gc;
	}
}
