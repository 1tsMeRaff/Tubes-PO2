package gameStates;

import entity.EnemyManager;
import entity.Player;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.GameCore;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import ui.GameOverOverlay; // Import baru untuk layar Game Over
import world.WorldManager;

public class PlayStates extends States implements StateMethods {

	private Player player;
	private WorldManager worldManager;
	private EnemyManager enemyManager;
	private PauseOverlay pauseOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private GameOverOverlay gameOverOverlay; // Deklarasi Game Over

	private boolean paused = false;
	private boolean lvlCompleted = false;
	private boolean gameOver = false; // Status kematian player
	
	// Variabel Kamera
	public int xLvlOffset;
	private int leftBorder = (int) (0.2 * GameCore.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * GameCore.GAME_WIDTH);
	private int maxLvlOffsetX;

	public PlayStates(GameCore gc) {
		super(gc);
		initClasses();
		calcLvlOffset(); 
	}

	private void initClasses() {
		worldManager = new WorldManager(gc);
		enemyManager = new EnemyManager(this); 
		player = new Player(200, 200, (int) (64 * GameCore.SCALE), (int) (40 * GameCore.SCALE));
		player.loadmapData(worldManager.getCurrentMap().getWorldData());
		
		pauseOverlay = new PauseOverlay(this); 
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameOverOverlay = new GameOverOverlay(this); // Inisialisasi
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
		} else if (lvlCompleted) {
			levelCompletedOverlay.update();
		} else if (gameOver) {
			// Saat game over, layar akan freeze (tidak ada update ke player/musuh)
			// Biarkan kosong sampai ada animasi khusus Game Over jika diperlukan
		} else {
			worldManager.update();
			player.update();
			enemyManager.update(worldManager.getCurrentMap().getWorldData()); 
			checkCloseToBorder();
			
			// Cek Transisi Level
			int endOfMapX = (worldManager.getCurrentMap().getWorldData()[0].length * GameCore.TILES_SIZE) - 50;
			if (player.getHitbox().x >= endOfMapX) {
				setLevelCompleted(true);
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		worldManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset); 
		
		if (paused) {
			pauseOverlay.draw(g);
		} else if (lvlCompleted) {
			levelCompletedOverlay.draw(g);
		} else if (gameOver) {
			gameOverOverlay.draw(g); // Gambar layar gelap & teks Game Over
		}
	}

	public void loadNextLevel() {
		resetAll(); 
		worldManager.loadNextWorld(); 
		player.loadmapData(worldManager.getCurrentMap().getWorldData()); 
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (paused || lvlCompleted || gameOver) return;

		if(e.getButton() == MouseEvent.BUTTON1) {
			player.setAttack(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gameOver) return; // Tidak ada klik mouse saat Game Over saat ini

		if (paused) {
			pauseOverlay.mousePressed(e);
		} else if (lvlCompleted) {
			levelCompletedOverlay.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameOver) return;

		if (paused) {
			pauseOverlay.mouseReleased(e);
		} else if (lvlCompleted) {
			levelCompletedOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gameOver) return;

		if (paused) {
			pauseOverlay.mouseMoved(e);
		} else if (lvlCompleted) {
			levelCompletedOverlay.mouseMoved(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (paused) {
			pauseOverlay.mouseDragged(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver) {
			gameOverOverlay.keyPressed(e);
			return;
		}
		if (lvlCompleted) return;

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			paused = !paused;
			return;
		}
		
		if (paused) return;

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
		if (paused || lvlCompleted || gameOver) return;
		
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
		player.resetAll(); 
		paused = false;
		lvlCompleted = false;
		gameOver = false; // Reset game over
		xLvlOffset = 0; 
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.lvlCompleted = levelCompleted;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public boolean isPaused() {
		return paused;
	}

	public Player getPlayer() {
		return player;
	}
}