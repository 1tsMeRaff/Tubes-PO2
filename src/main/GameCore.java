package main;

public class GameCore implements Runnable {
	
	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPSSET = 120;
	
	public GameCore() {
		
		gamePanel = new GamePanel();
		gameFrame = new GameFrame(gamePanel);
		gamePanel.setRequestFocusEnabled(true);
		gamePanel.requestFocus();
		startGameLoop();
	
	}
	
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		
		double timePerFrame = 1000000000.0 / FPSSET;
		long lastFrame = System.nanoTime();
		long now = System.nanoTime();
		
		int fps = 0;
		long lastCheck = System.currentTimeMillis();
		
		while(true) {
			
			now = System.nanoTime();
			if(now - lastFrame >= timePerFrame) {
				
				gamePanel.repaint();
				lastFrame = now;
				fps++;
			}
			
			
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS : " + fps);
				fps = 0;
				
			}
			
		}
		
	}

}
