package main;

import java.awt.Graphics;

import entity.Player;

public class GameCore implements Runnable {
	
	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	private Player player;
	
	public final static int TILE_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.0f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILE_SIZE = (int) (TILE_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILE_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILE_SIZE * TILES_IN_HEIGHT;
	
	public GameCore() {
		
		initClasses();
		
		gamePanel = new GamePanel(this);
		gameFrame = new GameFrame(gamePanel);
		gamePanel.setRequestFocusEnabled(true);
		gamePanel.requestFocus();
		
		startGameLoop();
	}
	
	private void initClasses() {
		player = new Player(200,200);
		
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		player.update();
	}
	
	public void render(Graphics g) {
		player.render(g);
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
			
//			if (deltaU > 5) {
//	            deltaU = 5; 
//	        }
			
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
			
//			try {
//			    Thread.sleep(1); 
//			} catch (InterruptedException e) {
//			    e.printStackTrace();
//			}
		}
		
	}
	
	public void windowFocusLost() {
		player.resetDirBooleans();
	}
	
	public Player getPlayer() {
		return player;
	}
}
