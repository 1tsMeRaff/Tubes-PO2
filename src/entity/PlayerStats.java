package entity;

/**
 * Encapsulates RPG progression, leveling, health, mana, defense, and DPS logic.
 * Extracted from Player.java to enforce SRP (Single Responsibility Principle).
 */
public class PlayerStats {

    private int level = 1;
    private int exp = 0;
    private int maxExp = 100;
    private float dps = 5.0f;

    private int maxHealth = 100;
    private int currentHealth = 100;
    private int maxMana = 100;
    private int currentMana = 100;

    private int baseDefense = 5;
    private int totalDefense = 5;

    public PlayerStats() {
    }

    public void gainExp(int amount) {
        this.exp += amount;
        System.out.println("Mendapatkan " + amount + " EXP!");
        while (this.exp >= this.maxExp) {
            levelUp();
        }
    }

    public void levelUp() {
        this.level++;
        this.exp -= this.maxExp;
        this.maxExp = (int) (this.maxExp * 1.5);

        this.maxHealth += 20;
        this.currentHealth = this.maxHealth;
        this.maxMana += 10;
        this.currentMana = this.maxMana;
        this.baseDefense += 2;
        this.dps += 1.5f;

        System.out.println("Level Up! Sekarang Level " + this.level);
    }

    /**
     * Updates health by delta value. Negative values inflict damage reduced by total defense.
     * @return true if damage was actually sustained (not invincible), false otherwise.
     */
    public boolean changeHealth(int value, boolean isInvincible) {
        if (value < 0) {
            if (isInvincible) return false;
            int incomingDamage = Math.abs(value);
            int finalDamage = Math.max(1, incomingDamage - totalDefense);
            this.currentHealth -= finalDamage;
            if (this.currentHealth <= 0) {
                this.currentHealth = 0;
            }
            return true;
        } else {
            this.currentHealth += value;
            if (this.currentHealth >= this.maxHealth) {
                this.currentHealth = this.maxHealth;
            }
            return false;
        }
    }

    public void changeMana(int value) {
        this.currentMana += value;
        if (this.currentMana < 0) {
            this.currentMana = 0;
        } else if (this.currentMana > this.maxMana) {
            this.currentMana = this.maxMana;
        }
    }

    public void recalculateDefense(int equipmentDefense) {
        this.totalDefense = this.baseDefense + equipmentDefense;
    }

    // --- GETTERS & SETTERS ---
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getExp() { return exp; }
    public void setExp(int exp) { this.exp = exp; }

    public int getMaxExp() { return maxExp; }
    public void setMaxExp(int maxExp) { this.maxExp = maxExp; }

    public float getDps() { return dps; }
    public void setDps(float dps) { this.dps = dps; }

    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }

    public int getMaxMana() { return maxMana; }
    public void setMaxMana(int maxMana) { this.maxMana = maxMana; }

    public int getCurrentMana() { return currentMana; }
    public void setCurrentMana(int currentMana) { this.currentMana = currentMana; }

    public int getBaseDefense() { return baseDefense; }
    public void setBaseDefense(int baseDefense) { this.baseDefense = baseDefense; }

    public int getTotalDefense() { return totalDefense; }
    public void setTotalDefense(int totalDefense) { this.totalDefense = totalDefense; }
}
