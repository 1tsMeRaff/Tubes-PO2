package effects;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Reusable particle object for the zero-allocation ParticlePool.
 * Designed to eliminate garbage collector churn in the render loop.
 */
public class Particle {

    public enum Type {
        SPARK,
        DUST,
        BLOOD,
        DASH_TRAIL
    }

    private float x, y;
    private float vx, vy;
    private float size;
    private int life;
    private int maxLife;
    private Color color;
    private boolean active = false;
    private float gravity = 0.15f;

    public Particle() {
        this.active = false;
    }

    public void init(float x, float y, float vx, float vy, float size, Color color, int maxLife, float gravity) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.color = color;
        this.maxLife = maxLife;
        this.life = maxLife;
        this.gravity = gravity;
        this.active = true;
    }

    public void update() {
        if (!active) return;

        x += vx;
        y += vy;
        vy += gravity;
        life--;

        if (life <= 0) {
            active = false;
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (!active) return;

        int drawX = (int) (x - xLvlOffset);
        int drawY = (int) y;
        int currentSize = (int) Math.max(1, size * ((float) life / maxLife));

        g.setColor(color);
        g.fillRect(drawX, drawY, currentSize, currentSize);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}
