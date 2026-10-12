package gameStates;

public enum GameStates {
	
	MENU, PLAYING, OPTIONS, QUIT;
	
	public static volatile GameStates state = MENU;

	public static GameStates getState() {
		return state;
	}

	public static void setState(GameStates newState) {
		state = java.util.Objects.requireNonNull(newState, "GameState cannot be null");
	}

}
