package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import main.GamePanel;

public class Mouse implements MouseListener, MouseMotionListener {

	private final InputDispatcher dispatcher;

	public Mouse(GamePanel gamePanel) {
		this.dispatcher = new InputDispatcher(gamePanel != null ? gamePanel.getGame() : null);
	}

	public Mouse(InputDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dispatcher != null) {
			dispatcher.dispatchMouseDragged(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (dispatcher != null) {
			dispatcher.dispatchMouseMoved(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (dispatcher != null) {
			dispatcher.dispatchMouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (dispatcher != null) {
			dispatcher.dispatchMousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dispatcher != null) {
			dispatcher.dispatchMouseReleased(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
