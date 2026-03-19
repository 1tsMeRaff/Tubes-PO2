package main;

public class GameCore implements Runnable {
	
	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private Thread gamethread;
	private final int FPSSET = 120;
	
	public GameCore() {
		
		gamePanel = new GamePanel();
		gameFrame = new GameFrame(gamePanel);
		gamePanel.setRequestFocusEnabled(true);
		gamePanel.requestFocus();
	
	}

	@Override
	public void run() {
		
		double timePerFrame = 1000000000.0 / FPSSET;
		
		while(true) {
			
		}
		
	}

}
