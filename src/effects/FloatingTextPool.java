package effects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import main.GameCore;

/**
 * Zero-allocation pool for Floating Combat Text (damage, heal, critical hits).
 */
public class FloatingTextPool {

    public static final int DEFAULT_CAPACITY = 50;
    private final FloatingText[] pool;
    private final Font damageFont = new Font("Arial", Font.BOLD, (int) (14 * GameCore.SCALE));

    public static final Color DAMAGE_DEALT = new Color(255, 220, 60);  // Gold/Yellow
    public static final Color DAMAGE_TAKEN = new Color(255, 70, 70);   // Red
    public static final Color HEAL_TEXT    = new Color(80, 240, 90);   // Green
    public static final Color MANA_TEXT    = new Color(80, 160, 255);  // Blue
    public static final Color CURSE_TEXT   = new Color(210, 80, 255);  // Purple/Curse

    public FloatingTextPool() {
        this(DEFAULT_CAPACITY);
    }

    public FloatingTextPool(int capacity) {
        pool = new FloatingText[capacity];
        for (int i = 0; i < capacity; i++) {
            pool[i] = new FloatingText();
        }
    }

    private FloatingText acquire() {
        for (FloatingText ft : pool) {
            if (!ft.isActive()) {
                return ft;
            }
        }
        return pool[0];
    }

    public void spawn(float x, float y, String text, Color color) {
        FloatingText ft = acquire();
        ft.init(x, y, text, color, 45); // 45 frames lifetime
    }

    public void spawnDamageDealt(float x, float y, int amount) {
        spawn(x, y, "-" + amount, DAMAGE_DEALT);
    }

    public void spawnCurseDamage(float x, float y, int amount) {
        spawn(x, y, "HEX! -" + amount, CURSE_TEXT);
    }

    public void spawnDamageTaken(float x, float y, int amount) {
        spawn(x, y, "-" + amount, DAMAGE_TAKEN);
    }

    public void spawnHeal(float x, float y, int amount) {
        spawn(x, y, "+" + amount, HEAL_TEXT);
    }

    public void update() {
        for (FloatingText ft : pool) {
            if (ft.isActive()) {
                ft.update();
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        for (FloatingText ft : pool) {
            if (ft.isActive()) {
                ft.draw(g, xLvlOffset, damageFont);
            }
        }
    }

    public int getActiveCount() {
        int count = 0;
        for (FloatingText ft : pool) {
            if (ft.isActive()) count++;
        }
        return count;
    }

    public void clear() {
        for (FloatingText ft : pool) {
            ft.deactivate();
        }
    }
}
