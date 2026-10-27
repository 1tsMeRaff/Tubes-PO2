package effects;

import java.awt.Graphics;
import entity.Player;
import gameStates.PlayStates;

/**
 * Zero-allocation object pool for CurseProjectile instances.
 * Pre-allocates and recycles projectiles to enforce strict GC budgets.
 */
public class CurseProjectilePool {

    public static final int DEFAULT_CAPACITY = 10;
    private final CurseProjectile[] pool;

    public CurseProjectilePool() {
        this(DEFAULT_CAPACITY);
    }

    public CurseProjectilePool(int capacity) {
        pool = new CurseProjectile[capacity];
        for (int i = 0; i < capacity; i++) {
            pool[i] = new CurseProjectile();
        }
    }

    private CurseProjectile acquire() {
        for (CurseProjectile cp : pool) {
            if (!cp.isActive()) {
                return cp;
            }
        }
        return pool[0]; // Recycle oldest if pool saturated
    }

    public CurseProjectile spawn(float startX, float startY, int dir, int damage) {
        CurseProjectile cp = acquire();
        cp.init(startX, startY, dir, damage);
        return cp;
    }

    public void update(PlayStates playStates, Player player) {
        for (CurseProjectile cp : pool) {
            if (cp.isActive()) {
                cp.update(playStates, player);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        for (CurseProjectile cp : pool) {
            if (cp.isActive()) {
                cp.draw(g, xLvlOffset);
            }
        }
    }

    public void reset() {
        for (CurseProjectile cp : pool) {
            cp.setActive(false);
        }
    }

    public CurseProjectile[] getPool() {
        return pool;
    }
}
