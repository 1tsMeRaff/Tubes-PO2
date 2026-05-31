package gameStates;

import entity.EnemyManager;
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
	private EnemyManager enemyManager;
	private PauseOverlay pauseOverlay;
	private boolean paused = false;

	public PlayStates(GameCore gc) {
		super(gc);
		initClasses();
		pauseOverlay = new PauseOverlay(this);
	}

	private void initClasses() {
		worldManager = new WorldManager(gc);
		enemyManager = new EnemyManager(this);
		player = new Player(200, 200, (int) (64 * GameCore.SCALE), (int) (40 * GameCore.SCALE));
		player.loadmapData(worldManager.getCurrentMap().getWorldData());
		pauseOverlay = new PauseOverlay(this);

	}

	@Override
	public void update() {
		if (!paused) {
			worldManager.update();
			player.update();
			enemyManager.update(worldManager.getCurrentMap().getWorldData());
		} else {
			pauseOverlay.update();
		}
	}

	@Override
	public void draw(Graphics g) {
		worldManager.draw(g);
		player.render(g);
		enemyManager.draw(g);
		if (paused) {
			pauseOverlay.draw(g);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (paused) {
			return;
		}

		if (e.getButton() == MouseEvent.BUTTON1) {
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

		switch (e.getKeyCode()) {
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

		switch (e.getKeyCode()) {
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
