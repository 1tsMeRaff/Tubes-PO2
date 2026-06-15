package main;

import audio.AudioPlayer;
import gameStates.GameStates;
import gameStates.MainMenu;
import gameStates.PlayStates;
import gameStates.GameOptions; // [TAMBAHAN] Import GameOptions
import java.awt.Graphics;

public class GameCore implements Runnable {
	
	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 60;
	private final int UPS_SET = 60;
	
	private PlayStates play;
	private MainMenu menu;
	private GameOptions gameOptions; // [TAMBAHAN] Deklarasi GameOptions
	private AudioPlayer audioPlayer;
	
	public final static int TILE_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.5f;
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
		audioPlayer = new audio.AudioPlayer();
		
		startGameLoop();
	}
	
	private void initClasses() {
		menu = new MainMenu(this);
		play = new PlayStates(this);
		gameOptions = new GameOptions(this); // [TAMBAHAN] Inisialisasi GameOptions
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
				gameOptions.update(); // [TAMBAHAN] Memanggil update Options
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
			case OPTIONS:
				gameOptions.draw(g); // [TAMBAHAN] Memanggil render/draw Options
				break;
			default:
				break;
		}
	}
	
	@Override
	public void run() {
	    // 1 detik = 1.000.000.000 nanodetik
	    final double TIME_PER_UPDATE = 1000000000.0 / UPS_SET; // Target 60 UPS
	    final double TIME_PER_FRAME = 1000000000.0 / FPS_SET;  // Target 60 FPS

	    long previousTime = System.nanoTime();

	    double deltaU = 0;
	    double deltaF = 0;

	    // Debug
	    long lastCheck = System.currentTimeMillis();
	    int updates = 0;
	    int frames = 0;

	    while (gameThread != null) {
	        long currentTime = System.nanoTime();
	        
	        deltaU += (currentTime - previousTime) / TIME_PER_UPDATE;
	        deltaF += (currentTime - previousTime) / TIME_PER_FRAME;
	        previousTime = currentTime;

	        while (deltaU >= 1) {
	            update();
	            updates++;
	            deltaU--;
	        }

	        if (deltaF >= 1) {
	            gamePanel.repaint();
	            frames++;
	            deltaF--;
	        }

	        if (System.currentTimeMillis() - lastCheck >= 1000) {
	            lastCheck = System.currentTimeMillis();
	            System.out.println("FPS: " + frames + " | UPS: " + updates);
	            frames = 0;
	            updates = 0;
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

	public GameOptions getGameOptions() {
		return gameOptions;
	}
	
	public audio.AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}