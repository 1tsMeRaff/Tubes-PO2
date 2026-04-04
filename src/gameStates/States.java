package gameStates;

import main.GameCore;

public class States {

	protected GameCore gc;
	
	public States(GameCore gc) {
		this.gc = gc;
	}
	
	public GameCore GetGame() {
		return gc;
	}
}
