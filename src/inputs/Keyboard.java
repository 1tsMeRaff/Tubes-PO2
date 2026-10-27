package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;

public class Keyboard implements KeyListener {

	private final InputDispatcher dispatcher;

	public Keyboard(GamePanel gamePanel) {
		this.dispatcher = new InputDispatcher(gamePanel != null ? gamePanel.getGame() : null);
	}

	public Keyboard(InputDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (dispatcher != null) {
			dispatcher.dispatchKeyReleased(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (dispatcher != null) {
			dispatcher.dispatchKeyPressed(e);
		}
	}
}
