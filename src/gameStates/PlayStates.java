package gameStates;

import entity.Player;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.GameCore;
import ui.PauseOverlay;
import utilitytools.LoadSave;
import world.WorldManager;
import static utilitytools.Konstanta.Environment.*;
import java.util.Random;


public class PlayStates extends States implements StateMethods {

	private Player player;
	private WorldManager worldManager;
	private PauseOverlay pauseOverlay;
	private boolean paused = false;
	
	// Variabel Kamera
	private int xLvlOffset;
	private int leftBorder = (int) (0.2 * GameCore.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * GameCore.GAME_WIDTH);
	private int maxLvlOffsetX;

	private BufferedImage backgroundImg,clouds_01,clouds_02; 
	private int[] clouds_02Pos;
	private Random rnd = new Random(); 
	
	
	public PlayStates(GameCore gc) {
		super(gc);
		initClasses();
		pauseOverlay = new PauseOverlay(this);
		calcLvlOffset(); 
		
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAY_BACKGROUND_IMG);
		clouds_01 = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS_01);
		clouds_02 = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS_01);
		clouds_02Pos = new int[8];
		for(int i = 0; i < clouds_02Pos.length; i++)
			clouds_02Pos[i] = (int)(90 * GameCore.SCALE) +  rnd.nextInt((int)(100*GameCore.SCALE));
	}
	
	private void initClasses() {
		worldManager = new WorldManager(gc);
		player = new Player(200, 200, (int) (64 * GameCore.SCALE), (int) (40 * GameCore.SCALE));
		player.loadmapData(worldManager.getCurrentMap().getWorldData());
	}
	
	private void calcLvlOffset() {
	    int mapWidth = worldManager.getCurrentMap().getWorldData()[0].length;
	    maxLvlOffsetX = (mapWidth - GameCore.TILES_IN_WIDTH) * GameCore.TILES_SIZE;
	}

	private void checkCloseToBorder() {
	    int playerX = (int) player.getHitbox().x;
	    int diff = playerX - xLvlOffset;

	    if (diff > rightBorder) {
	        xLvlOffset += diff - rightBorder;
	    } 
	    else if (diff < leftBorder) {
	        xLvlOffset += diff - leftBorder;
	    }

	    if (xLvlOffset > maxLvlOffsetX) {
	        xLvlOffset = maxLvlOffsetX;
	    } else if (xLvlOffset < 0) {
	        xLvlOffset = 0;
	    }
	}

	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
			return;
		}
		worldManager.update();
		player.update();
		checkCloseToBorder();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
		
		drawClounds(g);
		
		worldManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		
		if (paused) {
			pauseOverlay.draw(g);
		}
	}

	private void drawClounds(Graphics g) {
	    for (int i = 0; i < 3 ; i++) {
	        g.drawImage(clouds_01, i * CLOUDS_01_WIDTH - (int)(xLvlOffset * 0.3), (int) (204 * GameCore.SCALE), CLOUDS_01_WIDTH, CLOUDS_01_HEIGHT, null);
	    }
	    
	    for (int i = 0; i < clouds_02Pos.length; i++) {
	        g.drawImage(clouds_02, CLOUDS_02_WIDTH * 4 * i - (int)(xLvlOffset * 0.7), clouds_02Pos[i], CLOUDS_02_WIDTH, CLOUDS_02_HEIGHT, null);
	    }
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (paused) {
			return;
		}
		if(e.getButton() == MouseEvent.BUTTON1) {
			player.setAttack(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (paused) {
			pauseOverlay.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (paused) {
			pauseOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (paused) {
			pauseOverlay.mouseMoved(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (paused) {
			pauseOverlay.mouseDragged(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			paused = !paused;
			return;
		}
		if (paused) {
			return;
		}

		switch(e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(true);
			break;	
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (paused) {
			return;
		}
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(false);
			break;
		}
	}
	
	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public void resetAll() {
		initClasses();
		paused = false;
		xLvlOffset = 0; 
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public Player getPlayer() {
		return player;
	}
}