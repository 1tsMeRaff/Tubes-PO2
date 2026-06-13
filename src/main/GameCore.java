package main;

import java.awt.Graphics;
import gameStates.GameStates;
import gameStates.MainMenu;
import gameStates.PlayStates;

public class GameCore implements Runnable {
	
	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	
	private PlayStates play;
	private MainMenu menu;
	
	public final static int TILE_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.25f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILE_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	public GameCore() {
		initClasses();
		
		gamePanel = new GamePanel(this);
		gameFrame = new GameFrame(gamePanel);
		gamePanel.setRequestFocusEnabled(true);
		gamePanel.requestFocus();
		
		startGameLoop();
	}
	
	private void initClasses() {
		menu = new MainMenu(this);
		play = new PlayStates(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch(GameStates.state) {
			case MENU:
				menu.update();
				break;
			case PLAYING:
				play.update();
				break;
			case OPTIONS:
				// Dikosongkan sementara untuk fitur masa depan, tidak langsung exit
				break;
			case QUIT:
				System.exit(0);
				break;
			default:
				break;
		}
	}
	
	public void render(Graphics g) {
		switch(GameStates.state) {
			case MENU:
				menu.draw(g);
				break;
			case PLAYING:
				play.draw(g);
				break;
			default:
				break;
		}
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
				System.out.println("FPS : " + fps + " | UPS : " + ups);
				fps = 0;
				ups = 0;
			}
		}
	}
	
	public void windowFocusLost() {
		if(GameStates.state == GameStates.PLAYING) {
			play.getPlayer().resetDirBooleans();
		}
	}
	
	public MainMenu getMenu() {
		return menu;
	}
	
	public PlayStates getPlay() {
		return play;
	}
}