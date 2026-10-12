package entity;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Headless unit verification test suite for PlayerPhysics and PlayerCombat controllers.
 * Confirms SRP decomposition, knockback mechanics, gear defense calculation, and attack boxes.
 */
public class PlayerDecompositionTest {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING PLAYER DECOMPOSITION (SRP) TESTS ===");

        if (testPlayerCombatDefenseCalculation()) {
            System.out.println("[PASS] testPlayerCombatDefenseCalculation");
            passed++;
        } else {
            System.err.println("[FAIL] testPlayerCombatDefenseCalculation");
            failed++;
        }

        if (testPlayerCombatChargeAndAttackBox()) {
            System.out.println("[PASS] testPlayerCombatChargeAndAttackBox");
            passed++;
        } else {
            System.err.println("[FAIL] testPlayerCombatChargeAndAttackBox");
            failed++;
        }

        if (testPlayerPhysicsKnockbackAndDash()) {
            System.out.println("[PASS] testPlayerPhysicsKnockbackAndDash");
            passed++;
        } else {
            System.err.println("[FAIL] testPlayerPhysicsKnockbackAndDash");
            failed++;
        }

        System.out.println("=================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testPlayerCombatDefenseCalculation() {
        Player player = new Player(100, 100, 64, 40, null);
        PlayerCombat combat = player.getCombat();
        PlayerStats stats = player.getStats();

        ArrayList<Integer> testInventory = new ArrayList<>();
        testInventory.add(10); // Helm (+5 def)
        testInventory.add(11); // Armor (+15 def)

        combat.equipItem(0, "head", testInventory, stats);
        if (stats.getTotalDefense() != 10) { // 5 baseDefense + 5 helm
            System.err.println("Expected total defense 10 after helm, got: " + stats.getTotalDefense());
            return false;
        }

        combat.equipItem(0, "body", testInventory, stats);
        if (stats.getTotalDefense() != 25) { // 5 baseDefense + 5 helm + 15 armor
            System.err.println("Expected total defense 25 after armor, got: " + stats.getTotalDefense());
            return false;
        }

        return true;
    }

    private static boolean testPlayerCombatChargeAndAttackBox() {
        Player player = new Player(100, 100, 64, 40, null);
        PlayerCombat combat = player.getCombat();

        combat.setCharging(true, false, false, false);
        if (!combat.isCharging()) {
            System.err.println("Player should be charging");
            return false;
        }

        // Simulate charging ticks
        for (int i = 0; i < PlayerCombat.CHARGE_DURATION_NEEDED + 5; i++) {
            combat.update(null);
        }

        combat.releaseAttack(null);
        if (!combat.isExecutingChargeAttack()) {
            System.err.println("Expected charge attack to execute");
            return false;
        }

        Rectangle2D.Float box = combat.getAttackBox();
        if (box == null || box.width <= 0 || box.height <= 0) {
            System.err.println("Attack box bounds invalid: " + box);
            return false;
        }

        return true;
    }

    private static boolean testPlayerPhysicsKnockbackAndDash() {
        Player player = new Player(100, 100, 64, 40, null);
        PlayerPhysics physics = player.getPhysics();

        physics.startDash();
        if (!physics.isDashing()) {
            System.err.println("Expected physics dashing to be active");
            return false;
        }

        physics.applyKnockback(1, true);
        if (!physics.isKnockedDown()) {
            System.err.println("Expected player to be knocked down from boss attack");
            return false;
        }

        physics.reset(200, 200);
        if (physics.isKnockedDown() || physics.isDashing()) {
            System.err.println("Expected reset to clear knockback and dash");
            return false;
        }

        return true;
    }
}
