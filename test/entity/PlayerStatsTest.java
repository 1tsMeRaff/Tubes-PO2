package entity;

public class PlayerStatsTest {

    public static void main(String[] args) {
        System.out.println("=== RUNNING PLAYER STATS UNIT TESTS ===");
        int passed = 0;
        int failed = 0;

        try {
            testInitialState();
            System.out.println("[PASS] testInitialState");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] testInitialState: " + t.getMessage());
            failed++;
        }

        try {
            testDamageCalculationWithDefense();
            System.out.println("[PASS] testDamageCalculationWithDefense");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] testDamageCalculationWithDefense: " + t.getMessage());
            failed++;
        }

        try {
            testInvincibilityImmunity();
            System.out.println("[PASS] testInvincibilityImmunity");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] testInvincibilityImmunity: " + t.getMessage());
            failed++;
        }

        try {
            testLevelUpProgression();
            System.out.println("[PASS] testLevelUpProgression");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] testLevelUpProgression: " + t.getMessage());
            failed++;
        }

        try {
            testManaBounds();
            System.out.println("[PASS] testManaBounds");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] testManaBounds: " + t.getMessage());
            failed++;
        }

        System.out.println("========================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertEquals(float expected, float actual, String message) {
        if (Math.abs(expected - actual) > 0.001f) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void testInitialState() {
        PlayerStats stats = new PlayerStats();
        assertEquals(1, stats.getLevel(), "Default level should be 1");
        assertEquals(0, stats.getExp(), "Default exp should be 0");
        assertEquals(100, stats.getMaxExp(), "Default maxExp should be 100");
        assertEquals(100, stats.getMaxHealth(), "Default maxHealth should be 100");
        assertEquals(100, stats.getCurrentHealth(), "Default currentHealth should be 100");
        assertEquals(100, stats.getMaxMana(), "Default maxMana should be 100");
        assertEquals(100, stats.getCurrentMana(), "Default currentMana should be 100");
        assertEquals(5, stats.getBaseDefense(), "Default baseDefense should be 5");
        assertEquals(5, stats.getTotalDefense(), "Default totalDefense should be 5");
        assertEquals(5.0f, stats.getDps(), "Default dps should be 5.0");
    }

    private static void testDamageCalculationWithDefense() {
        PlayerStats stats = new PlayerStats();
        // Base defense is 5. Incoming damage: 20 -> Actual damage = 20 - 5 = 15.
        boolean damaged = stats.changeHealth(-20, false);
        assertTrue(damaged, "Damage should register when not invincible");
        assertEquals(85, stats.getCurrentHealth(), "Health should be 85 after 15 actual damage");

        // Test high defense reducing damage to floor 1
        stats.recalculateDefense(100);
        stats.changeHealth(-10, false);
        assertEquals(84, stats.getCurrentHealth(), "Damage should floor at 1 with high defense");
    }

    private static void testInvincibilityImmunity() {
        PlayerStats stats = new PlayerStats();
        boolean damaged = stats.changeHealth(-50, true);
        assertTrue(!damaged, "Damage must not register when invincible");
        assertEquals(100, stats.getCurrentHealth(), "Health must remain intact when invincible");
    }

    private static void testLevelUpProgression() {
        PlayerStats stats = new PlayerStats();
        stats.gainExp(150); // Exceeds 100 exp -> triggers level up
        assertEquals(2, stats.getLevel(), "Player should advance to level 2");
        assertEquals(50, stats.getExp(), "Remaining exp should be 50");
        assertEquals(150, stats.getMaxExp(), "Max exp should increase by 1.5x (150)");
        assertEquals(120, stats.getMaxHealth(), "Max health should increase by 20 to 120");
        assertEquals(120, stats.getCurrentHealth(), "Current health should replenish to max on level up");
        assertEquals(110, stats.getMaxMana(), "Max mana should increase by 10 to 110");
        assertEquals(7, stats.getBaseDefense(), "Base defense should increase by 2 to 7");
        assertEquals(6.5f, stats.getDps(), "DPS should increase by 1.5 to 6.5");
    }

    private static void testManaBounds() {
        PlayerStats stats = new PlayerStats();
        stats.changeMana(-30);
        assertEquals(70, stats.getCurrentMana(), "Mana should decrease to 70");

        stats.changeMana(-100);
        assertEquals(0, stats.getCurrentMana(), "Mana should not drop below 0");

        stats.changeMana(200);
        assertEquals(100, stats.getCurrentMana(), "Mana should not exceed maxMana");
    }
}
