package gameStates;

import entity.Player;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.GameCore;
import ui.PauseOverlay;
import world.WorldManager;

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

	public PlayStates(GameCore gc) {
		super(gc);
		initClasses();
		pauseOverlay = new PauseOverlay(this);
		calcLvlOffset(); 
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
		worldManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		
		if (paused) {
			pauseOverlay.draw(g);
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