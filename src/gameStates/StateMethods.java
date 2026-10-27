package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import core.interfaces.Renderable;
import core.interfaces.Updatable;

public interface StateMethods extends Updatable, Renderable {
	
	@Override
	public void update();
	
	@Override
	public void draw(Graphics g);
	
	public void mouseClicked(MouseEvent e);
	
	public void mousePressed(MouseEvent e);
	
	public void mouseReleased(MouseEvent e);
	
	public void mouseMoved(MouseEvent e);
	
	public void keyPressed(KeyEvent e);
	
	public void keyReleased(KeyEvent e);

	default void mouseDragged(MouseEvent e) {}

}
