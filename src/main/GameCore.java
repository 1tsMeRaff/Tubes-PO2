package main;

import audio.AudioPlayer;
import gameStates.GameStates;
import gameStates.MainMenu;
import gameStates.PlayStates;
import java.awt.Graphics;

public class GameCore implements Runnable {
	
	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 60;
	private final int UPS_SET = 60;
	
	private PlayStates play;
	private MainMenu menu;
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
	
//	@Override
//	public void run() {
//		double timePerFrame = 1000000000.0 / FPS_SET;
//		double timePerUpdate = 1000000000.0 / UPS_SET;
//		
//		long previousTime = System.nanoTime();
//		
//		int fps = 0;
//		int ups = 0;
//		long lastCheck = System.currentTimeMillis();
//		
//		double deltaU = 0;
//		double deltaF = 0;
//		
//		while(true) {
//			long currentTime = System.nanoTime();
//	        
//	        deltaU += (currentTime - previousTime) / timePerUpdate;
//	        deltaF += (currentTime - previousTime) / timePerFrame;
//	        previousTime = currentTime;
//	        
//	        if (deltaU >= 1) {
//	            update();
//	            ups++;
//	            deltaU--;
//	        }
//	        
//	        if (deltaF >= 1) {
//	            gamePanel.repaint();
//	            fps++;
//	            deltaF--;
//	        }
//	        
//	        try {
//	            Thread.sleep(1); 
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }
//			
//			if(System.currentTimeMillis() - lastCheck >= 1000) {
//				lastCheck = System.currentTimeMillis();
//				System.out.println("FPS : " + fps + " | UPS : " + ups);
//				fps = 0;
//				ups = 0;
//			}
//		}
//	}
	
	@Override
	public void run() {
	    // 1 detik = 1.000.000.000 nanodetik
	    final double TIME_PER_UPDATE = 1000000000.0 / UPS_SET; // Target 60 UPS
	    final double TIME_PER_FRAME = 1000000000.0 / FPS_SET;  // Target 60 FPS

	    long previousTime = System.nanoTime();

	    double deltaU = 0;
	    double deltaF = 0;

	    // Variabel untuk monitoring performa di konsol
	    long lastCheck = System.currentTimeMillis();
	    int updates = 0;
	    int frames = 0;

	    while (gameThread != null) {
	        long currentTime = System.nanoTime();
	        
	        // Menghitung rasio waktu yang telah berlalu
	        deltaU += (currentTime - previousTime) / TIME_PER_UPDATE;
	        deltaF += (currentTime - previousTime) / TIME_PER_FRAME;
	        previousTime = currentTime;

	        // --- AKSI 1: PEMBARUAN LOGIKA GAME (FIXED TIMESTEP) ---
	        // Jika deltaU >= 1, berarti sudah waktunya logika game diperbarui
	        while (deltaU >= 1) {
	            update(); // Panggil metode update() Anda (posisi, fisika, ai)
	            updates++;
	            deltaU--; // Kurangi 1 tick
	        }

	        // --- AKSI 2: RENDERING / DRAWING (FPS) ---
	        // Jika deltaF >= 1, render frame baru ke layar
	        if (deltaF >= 1) {
	            gamePanel.repaint(); // Panggil repaint untuk memicu paintComponent()
	            frames++;
	            deltaF--;
	        }

	        // --- MONITORING FPS & UPS (Opsional, muncul setiap 1 detik) ---
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
	
	public audio.AudioPlayer getAudioPlayer() {
	    return audioPlayer;
	}
}