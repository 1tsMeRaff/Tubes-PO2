package entity;

public abstract class Boss extends Enemy {

    protected int currentPhase = 1;

    public Boss(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height, enemyType);
    }

    @Override
    public void hurt(int value) {
        super.hurt(value);
        checkPhaseTransition();
    }
    
    protected void checkPhaseTransition() {
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }
}