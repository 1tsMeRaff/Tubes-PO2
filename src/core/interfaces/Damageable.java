package core.interfaces;

/**
 * Interface representing any game entity that can sustain damage and has health.
 * Part of ISP and LSP modernization.
 */
public interface Damageable {
    void hurt(int amount);
    int getCurrentHealth();
    int getMaxHealth();
    boolean isAlive();
}
