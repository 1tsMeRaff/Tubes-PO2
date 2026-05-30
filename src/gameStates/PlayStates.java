package gameStates;

import entity.Player;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.GameCore;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import world.WorldManager;

public class PlayStates extends States implements StateMethods {

	private Player player;
	private WorldManager worldManager;
	private PauseOverlay pauseOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private boolean paused = false;
	private boolean lvlCompleted = false;
	
	public PlayStates(GameCore gc) {
		super(gc);
		initClasses();
		pauseOverlay = new PauseOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
	}
	
	private void initClasses() {
		worldManager = new WorldManager(gc);
		player = new Player(200, 200, (int) (64 * GameCore.SCALE), (int) (40 * GameCore.SCALE));
		player.loadmapData(worldManager.getCurrentMap().getWorldData());
	}

	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelCompletedOverlay.update();
		} else {
			worldManager.update();
			player.update();
			checkCloseToBorder(); // Mengecek apakah player sudah di ujung layar
		}
	}

	// [LOGIKA SEMENTARA] Jika x player melebihi layar, anggap level selesai
	private void checkCloseToBorder() {
		if (player.getHitbox().x >= GameCore.GAME_WIDTH - 50) {
			setLevelCompleted(true);
		}
	}

	@Override
	public void draw(Graphics g) {
		worldManager.draw(g);
		player.render(g);
		
		if (paused) {
			pauseOverlay.draw(g);
		} else if (lvlCompleted) {
			levelCompletedOverlay.draw(g);
		}
	}

	public void loadNextLevel() {
		resetAll(); // Reset state
		worldManager.loadNextWorld(); // Pindah ke index map berikutnya
		// Reload collision map untuk player ke map yang baru
		player.loadmapData(worldManager.getCurrentMap().getWorldData()); 
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (paused || lvlCompleted) return;

		if(e.getButton() == MouseEvent.BUTTON1) {
			player.setAttack(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (paused) {
			pauseOverlay.mousePressed(e);
		} else if (lvlCompleted) {
			levelCompletedOverlay.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (paused) {
			pauseOverlay.mouseReleased(e);
		} else if (lvlCompleted) {
			levelCompletedOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
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
		if (paused || lvlCompleted) return;
		
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
		// Fungsi ini mereset posisi player dan status game.
		// Kita tidak perlu "new WorldManager()" lagi karena ArrayList sudah disimpan
		player.resetAll(); 
		paused = false;
		lvlCompleted = false;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.lvlCompleted = levelCompleted;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public Player getPlayer() {
		return player;
	}
}