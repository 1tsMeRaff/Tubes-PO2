package effects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Zero-allocation Particle Pool to prevent GC frame drops.
 * Recycles particles from a fixed-size internal array.
 */
public class ParticlePool {

    public static final int DEFAULT_CAPACITY = 150;
    private final Particle[] pool;
    private final Random rand = new Random();

    private static final Color SPARK_COLOR_1 = new Color(255, 230, 100);
    private static final Color SPARK_COLOR_2 = new Color(255, 140, 40);
    private static final Color DUST_COLOR = new Color(180, 175, 170, 180);
    private static final Color BLOOD_COLOR = new Color(200, 30, 30);
    private static final Color SOUL_COLOR_1 = new Color(190, 80, 255);
    private static final Color SOUL_COLOR_2 = new Color(110, 210, 255);

    public ParticlePool() {
        this(DEFAULT_CAPACITY);
    }

    public ParticlePool(int capacity) {
        pool = new Particle[capacity];
        for (int i = 0; i < capacity; i++) {
            pool[i] = new Particle();
        }
    }

    private Particle acquire() {
        for (Particle p : pool) {
            if (!p.isActive()) {
                return p;
            }
        }
        // If all are active, steal the oldest particle (index 0)
        return pool[0];
    }

    public void spawn(float x, float y, float vx, float vy, float size, Color color, int maxLife, float gravity) {
        Particle p = acquire();
        p.init(x, y, vx, vy, size, color, maxLife, gravity);
    }

    public void spawnHitSparks(float x, float y) {
        spawnHitSparks(x, y, 6);
    }

    public void spawnHitSparks(float x, float y, int count) {
        for (int i = 0; i < count; i++) {
            float angle = (float) (rand.nextDouble() * Math.PI * 2);
            float speed = 2.0f + (rand.nextFloat() * 4.0f);
            float vx = (float) Math.cos(angle) * speed;
            float vy = (float) Math.sin(angle) * speed - 1.0f;
            Color c = rand.nextBoolean() ? SPARK_COLOR_1 : SPARK_COLOR_2;
            spawn(x, y, vx, vy, 4.0f + rand.nextInt(3), c, 12 + rand.nextInt(8), 0.25f);
        }
    }

    public void spawnDust(float x, float y) {
        spawnDust(x, y, 4);
    }

    public void spawnDust(float x, float y, int count) {
        for (int i = 0; i < count; i++) {
            float vx = (rand.nextFloat() - 0.5f) * 2.5f;
            float vy = -0.5f - (rand.nextFloat() * 1.5f);
            spawn(x, y, vx, vy, 5.0f + rand.nextInt(3), DUST_COLOR, 15 + rand.nextInt(10), -0.02f);
        }
    }

    public void spawnBlood(float x, float y) {
        spawnBlood(x, y, 6);
    }

    public void spawnBlood(float x, float y, int count) {
        for (int i = 0; i < count; i++) {
            float vx = (rand.nextFloat() - 0.5f) * 3.5f;
            float vy = -1.0f - (rand.nextFloat() * 3.0f);
            spawn(x, y, vx, vy, 4.0f + rand.nextInt(2), BLOOD_COLOR, 18 + rand.nextInt(8), 0.35f);
        }
    }

    public void spawnSoulMagic(float x, float y) {
        spawnSoulMagic(x, y, 12);
    }

    public void spawnSoulMagic(float x, float y, int count) {
        for (int i = 0; i < count; i++) {
            float angle = (float) (rand.nextDouble() * Math.PI * 2);
            float speed = 2.5f + (rand.nextFloat() * 4.5f);
            float vx = (float) Math.cos(angle) * speed;
            float vy = (float) Math.sin(angle) * speed - 0.5f;
            Color c = rand.nextBoolean() ? SOUL_COLOR_1 : SOUL_COLOR_2;
            spawn(x, y, vx, vy, 4.0f + rand.nextInt(3), c, 16 + rand.nextInt(10), 0.15f);
        }
    }

    public void spawnSoulTrail(float x, float y) {
        float vx = (rand.nextFloat() - 0.5f) * 1.5f;
        float vy = (rand.nextFloat() - 0.5f) * 1.5f;
        Color c = rand.nextBoolean() ? SOUL_COLOR_1 : SOUL_COLOR_2;
        spawn(x, y, vx, vy, 3.5f, c, 12, 0.05f);
    }

    public void update() {
        for (Particle p : pool) {
            if (p.isActive()) {
                p.update();
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        for (Particle p : pool) {
            if (p.isActive()) {
                p.draw(g, xLvlOffset);
            }
        }
    }

    public int getActiveCount() {
        int count = 0;
        for (Particle p : pool) {
            if (p.isActive()) count++;
        }
        return count;
    }

    public void clear() {
        for (Particle p : pool) {
            p.deactivate();
        }
    }
}
