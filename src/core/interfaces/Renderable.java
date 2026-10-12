package core.interfaces;

import java.awt.Graphics;

/**
 * Interface for any visual game element that can be drawn to the screen.
 * Part of ISP (Interface Segregation Principle) modernization.
 */
public interface Renderable {
    void draw(Graphics g);
}
