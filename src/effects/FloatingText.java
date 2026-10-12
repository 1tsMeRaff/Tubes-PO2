package effects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Reusable floating combat text for showing damage and healing numbers.
 */
public class FloatingText {

    private float x, y;
    private float vy;
    private String text;
    private Color color;
    private int life;
    private int maxLife;
    private boolean active = false;

    public FloatingText() {
        this.active = false;
    }

    public void init(float x, float y, String text, Color color, int maxLife) {
        this.x = x;
        this.y = y;
        this.vy = -1.2f;
        this.text = text;
        this.color = color;
        this.maxLife = maxLife;
        this.life = maxLife;
        this.active = true;
    }

    public void update() {
        if (!active) return;

        y += vy;
        vy *= 0.95f; // Decelerate upward drift
        life--;

        if (life <= 0) {
            active = false;
        }
    }

    public void draw(Graphics g, int xLvlOffset, Font font) {
        if (!active || text == null) return;

        int drawX = (int) (x - xLvlOffset);
        int drawY = (int) y;

        g.setFont(font);

        // Draw shadow/outline for readability
        g.setColor(Color.BLACK);
        g.drawString(text, drawX + 1, drawY + 1);

        g.setColor(color);
        g.drawString(text, drawX, drawY);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}
