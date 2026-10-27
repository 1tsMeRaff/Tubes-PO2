package effects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import entity.Player;
import gameStates.PlayStates;
import main.GameCore;

/**
 * Zero-allocation projectile representing the "Witch Curse : Soul Hex" ability.
 * Pierces enemies, spends mana, and produces vibrant soul particles.
 */
public class CurseProjectile {

    private float x, y;
    private int dir; // 1 = Right, -1 = Left
    private float speed;
    private int damage;
    private boolean active = false;
    private int lifetime = 0;
    public static final int MAX_LIFETIME = 40; // ~0.66 seconds range

    private final Rectangle2D.Float hitBox = new Rectangle2D.Float(0, 0, 32 * GameCore.SCALE, 20 * GameCore.SCALE);

    private static final Color CURSE_GLOW = new Color(90, 20, 180, 130);
    private static final Color CURSE_BODY = new Color(175, 45, 245);
    private static final Color CURSE_CORE = new Color(245, 200, 255);
    private static final Color CURSE_AURA = new Color(110, 220, 255, 150);

    public CurseProjectile() {
        this.speed = 7.5f * GameCore.SCALE;
    }

    public void init(float startX, float startY, int dir, int damage) {
        this.x = startX;
        this.y = startY;
        this.dir = dir;
        this.damage = damage;
        this.lifetime = MAX_LIFETIME;
        this.active = true;
        this.hitBox.x = startX;
        this.hitBox.y = startY;
    }

    public void update(PlayStates playStates, Player player) {
        if (!active) return;

        x += speed * dir;
        hitBox.x = x;
        hitBox.y = y;

        // Spawn trailing soul particles
        if (playStates != null && playStates.getParticlePool() != null) {
            playStates.getParticlePool().spawnSoulTrail(x + (dir == 1 ? 5 : hitBox.width - 5), y + (hitBox.height / 2.0f));
        }

        // Check enemy collision
        if (playStates != null && playStates.getEnemyManager() != null) {
            playStates.getEnemyManager().checkCurseHit(hitBox, damage, dir, player);
        }

        // Check container/object collision
        if (playStates != null && playStates.getObjectManager() != null) {
            playStates.getObjectManager().checkObjectHit(hitBox);
        }

        lifetime--;
        if (lifetime <= 0) {
            active = false;
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (!active) return;

        Graphics2D g2 = (Graphics2D) g;
        int drawX = (int) (x - xLvlOffset);
        int drawY = (int) y;
        int w = (int) hitBox.width;
        int h = (int) hitBox.height;

        // Outer Glow Aura
        g2.setColor(CURSE_GLOW);
        g2.fillOval(drawX - 4, drawY - 4, w + 8, h + 8);

        // Crescent Body
        g2.setColor(CURSE_BODY);
        g2.fillOval(drawX, drawY, w, h);

        // Cyan Edge Aura
        g2.setColor(CURSE_AURA);
        if (dir == 1) {
            g2.fillArc(drawX + (w / 3), drawY, w / 2, h, -90, 180);
        } else {
            g2.fillArc(drawX, drawY, w / 2, h, 90, 180);
        }

        // Inner Core
        g2.setColor(CURSE_CORE);
        g2.fillOval(drawX + (dir == 1 ? w / 2 : 4), drawY + (h / 4), w / 3, h / 2);
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Rectangle2D.Float getHitBox() { return hitBox; }
    public int getDamage() { return damage; }
    public float getX() { return x; }
    public float getY() { return y; }
}
