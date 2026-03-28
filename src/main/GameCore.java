package main;

public class GameCore implements Runnable {
	
	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	
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

	public void update() {
		gamePanel.myUpdate();
	}
	
	@Override
	public void run() {
		
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		
		long previousTime = System.nanoTime();
		
		int fps = 0;
		int ups = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0;
		double deltaF = 0;
		
		while(true) {
			long currentTime = System.nanoTime();
			
			
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if(deltaU >= 1) {
				update();
				ups++;
				deltaU--;
			}
			
			if(deltaF >= 1) {
				gamePanel.repaint();
				fps++;
				deltaF--;
			}
			
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS : " + fps + "	|	UPS : " + ups);
				fps = 0;
				ups = 0;
			}
			
		}
		
	}

}
