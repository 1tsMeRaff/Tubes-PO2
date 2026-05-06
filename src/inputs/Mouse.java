package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gameStates.GameStates;
import main.GamePanel;

public class Mouse implements MouseListener, MouseMotionListener {
	
	private GamePanel gamePanel;
	
	public Mouse(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseMoved(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().mouseMoved(e);
			break;
		case PAUSE:
			gamePanel.getGame().getPause().mouseMoved(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch(GameStates.state) {
		case PLAYING:
			gamePanel.getGame().getPlay().mouseClicked(e);
			break;
		case PAUSE:
			gamePanel.getGame().getPause().mouseClicked(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().mousePressed(e);
			break;
		case PAUSE:
			gamePanel.getGame().getPause().mousePressed(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().mouseReleased(e);
			break;
		case PAUSE:
			gamePanel.getGame().getPause().mouseReleased(e);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
