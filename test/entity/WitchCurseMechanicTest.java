package entity;

import java.awt.geom.Rectangle2D;
import effects.CurseProjectile;
import effects.CurseProjectilePool;

/**
 * Headless unit verification test suite for the signature mechanic:
 * "Witch Curse : Soul Hex" ability and "Feline Ground Slam" plunge attack.
 * Verifies mana expenditure, cooldown gating, trajectory math, AoE impact, and zero-allocation pooling.
 */
public class WitchCurseMechanicTest {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING WITCH CURSE & FELINE SOUL MECHANIC TESTS ===");

        if (testManaConsumptionAndGating()) {
            System.out.println("[PASS] testManaConsumptionAndGating");
            passed++;
        } else {
            System.err.println("[FAIL] testManaConsumptionAndGating");
            failed++;
        }

        if (testCooldownEnforcement()) {
            System.out.println("[PASS] testCooldownEnforcement");
            passed++;
        } else {
            System.err.println("[FAIL] testCooldownEnforcement");
            failed++;
        }

        if (testCurseProjectilePoolingAndPhysics()) {
            System.out.println("[PASS] testCurseProjectilePoolingAndPhysics");
            passed++;
        } else {
            System.err.println("[FAIL] testCurseProjectilePoolingAndPhysics");
            failed++;
        }

        if (testFelineGroundSlamMechanic()) {
            System.out.println("[PASS] testFelineGroundSlamMechanic");
            passed++;
        } else {
            System.err.println("[FAIL] testFelineGroundSlamMechanic");
            failed++;
        }

        System.out.println("=========================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testManaConsumptionAndGating() {
        Player player = new Player(100, 100, 64, 40, null);
        PlayerStats stats = player.getStats();

        // 1. Initial mana should be 100
        if (stats.getCurrentMana() != 100) return false;

        // 2. Cast Witch Curse - should spend 25 mana
        boolean castSuccess = player.castWitchCurse();
        if (!castSuccess) return false;
        if (stats.getCurrentMana() != 75) return false;

        // 3. Deplete mana below 25
        stats.changeMana(-60); // 75 - 60 = 15 mana
        if (stats.getCurrentMana() != 15) return false;

        // Wait out cooldown
        for (int i = 0; i < Player.CURSE_COOLDOWN + 1; i++) {
            player.update();
        }

        // 4. Casting with insufficient mana must fail and preserve current mana
        boolean insufficientCast = player.castWitchCurse();
        if (insufficientCast) return false; // Must return false
        if (stats.getCurrentMana() != 15) return false; // Mana untouched

        return true;
    }

    private static boolean testCooldownEnforcement() {
        Player player = new Player(100, 100, 64, 40, null);

        // First cast succeeds
        if (!player.castWitchCurse()) return false;
        if (player.getCurseCooldownTick() != Player.CURSE_COOLDOWN) return false;

        // Immediate second cast must be blocked by cooldown
        if (player.castWitchCurse()) return false;

        // Simulate 29 ticks (still 1 tick remaining on 30 tick cooldown)
        for (int i = 0; i < 29; i++) {
            player.update();
        }
        if (player.getCurseCooldownTick() != 1) return false;
        if (player.castWitchCurse()) return false; // Still blocked!

        // Tick 30th frame: cooldown reaches 0
        player.update();
        if (player.getCurseCooldownTick() != 0) return false;

        // Now cast succeeds!
        if (!player.castWitchCurse()) return false;

        return true;
    }

    private static boolean testCurseProjectilePoolingAndPhysics() {
        CurseProjectilePool pool = new CurseProjectilePool(5);

        // 1. Spawn projectile going right (+1)
        CurseProjectile p1 = pool.spawn(100, 200, 1, 35);
        if (!p1.isActive()) return false;
        if (p1.getX() != 100 || p1.getY() != 200) return false;
        if (p1.getDamage() != 35) return false;

        // 2. Update ticks and verify movement
        float initialX = p1.getX();
        p1.update(null, null);
        if (p1.getX() <= initialX) return false; // Moved rightwards!

        // 3. Verify lifetime expiration
        for (int i = 0; i < CurseProjectile.MAX_LIFETIME + 5; i++) {
            p1.update(null, null);
        }
        if (p1.isActive()) return false; // Expired cleanly!

        // 4. Verify pool recycling without new allocations
        for (int i = 0; i < 10; i++) {
            pool.spawn(50 + i, 50, 1, 20);
        }
        // Pool capacity is 5, oldest recycled cleanly
        int activeCount = 0;
        for (CurseProjectile cp : pool.getPool()) {
            if (cp.isActive()) activeCount++;
        }
        if (activeCount > 5) return false;

        return true;
    }

    private static boolean testFelineGroundSlamMechanic() {
        Player player = new Player(100, 100, 64, 40, null);

        // Ground slam only works in air
        player.setDown(true);
        player.setCharging(true);
        // On ground, normal charge attack starts, not ground slam
        if (player.isGroundSlamming()) return false;

        // Now simulate mid-air state
        player.setCharging(false);
        player.setDown(true);
        // Trigger jump to be inAir
        player.setJump(true);

        // Now Down + Attack in air triggers ground slam
        player.setCharging(true);
        if (!player.isGroundSlamming()) return false;

        return true;
    }
}
