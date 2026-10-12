package entity;

import core.interfaces.Damageable;

public class BossPolymorphismTest {

    public static void main(String[] args) {
        System.out.println("=== RUNNING BOSS POLYMORPHISM & DAMAGEABLE TESTS ===");
        int passed = 0;
        int failed = 0;

        try {
            testBossPolymorphismAndTitles();
            System.out.println("[PASS] testBossPolymorphismAndTitles");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] testBossPolymorphismAndTitles: " + t.getMessage());
            failed++;
        }

        try {
            testDamageableContractOnBoss();
            System.out.println("[PASS] testDamageableContractOnBoss");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] testDamageableContractOnBoss: " + t.getMessage());
            failed++;
        }

        System.out.println("====================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void assertEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void testBossPolymorphismAndTitles() {
        Boss[] bosses = new Boss[] {
            new DemonBoss(100, 100),
            new BlueGolem(200, 200),
            new BringerOfDeath(300, 300)
        };

        assertEquals("DEMON BOSS", bosses[0].getBossTitle(), "DemonBoss must return DEMON BOSS");
        assertEquals("BLUE GOLEM", bosses[1].getBossTitle(), "BlueGolem must return BLUE GOLEM");
        assertEquals("BRINGER OF DEATH", bosses[2].getBossTitle(), "BringerOfDeath must return BRINGER OF DEATH");

        for (Boss boss : bosses) {
            assertTrue(boss instanceof Damageable, boss.getBossTitle() + " must implement Damageable");
            assertTrue(boss.isAlive(), boss.getBossTitle() + " must be alive upon instantiation");
            assertTrue(boss.getMaxHealth() > 0, boss.getBossTitle() + " max health must be greater than 0");
            assertTrue(boss.getCurrentHealth() == boss.getMaxHealth(), boss.getBossTitle() + " health should be at maximum initially");
        }
    }

    private static void testDamageableContractOnBoss() {
        Damageable boss = new DemonBoss(100, 100);
        int initialHp = boss.getCurrentHealth();

        boss.hurt(15);
        assertTrue(boss.getCurrentHealth() < initialHp, "Boss HP must decrease after taking damage");
        assertTrue(boss.isAlive(), "Boss must still be alive with positive HP");
    }
}
