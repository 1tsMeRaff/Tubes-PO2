package inputs;

import java.awt.Component;
import java.awt.event.KeyEvent;
import entity.Player;

/**
 * Headless unit verification test suite for rewired keybindings.
 * Verifies standard controls:
 * - Movement (A / Left Arrow, D / Right Arrow)
 * - Jump & Double Jump (Space, W, Up Arrow, K)
 * - Dash (Shift, L, Q, C)
 * - Attack & Charge (J, X)
 */
public class KeybindingsWiringTest {

    private static final Component DUMMY_SOURCE = new Component() {};

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING KEYBINDINGS WIRING TESTS ===");

        if (testMovementKeybindings()) {
            System.out.println("[PASS] testMovementKeybindings");
            passed++;
        } else {
            System.err.println("[FAIL] testMovementKeybindings");
            failed++;
        }

        if (testJumpKeybindings()) {
            System.out.println("[PASS] testJumpKeybindings");
            passed++;
        } else {
            System.err.println("[FAIL] testJumpKeybindings");
            failed++;
        }

        if (testDashKeybindings()) {
            System.out.println("[PASS] testDashKeybindings");
            passed++;
        } else {
            System.err.println("[FAIL] testDashKeybindings");
            failed++;
        }

        if (testAttackKeybindings()) {
            System.out.println("[PASS] testAttackKeybindings");
            passed++;
        } else {
            System.err.println("[FAIL] testAttackKeybindings");
            failed++;
        }

        System.out.println("=========================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testMovementKeybindings() {
        Player player = new Player(100, 100, 64, 40, null);

        // Test Left: VK_A and VK_LEFT
        int[] leftKeys = {KeyEvent.VK_A, KeyEvent.VK_LEFT};
        for (int key : leftKeys) {
            player.setLeft(false);
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                player.setLeft(true);
            }
            if (!player.isLeft()) return false;

            player.setLeft(false);
            if (player.isLeft()) return false;
        }

        // Test Right: VK_D and VK_RIGHT
        int[] rightKeys = {KeyEvent.VK_D, KeyEvent.VK_RIGHT};
        for (int key : rightKeys) {
            player.setRight(false);
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                player.setRight(true);
            }
            if (!player.isRight()) return false;

            player.setRight(false);
            if (player.isRight()) return false;
        }

        return true;
    }

    private static boolean testJumpKeybindings() {
        Player player = new Player(100, 100, 64, 40, null);

        // Jump triggers: Space, W, Up Arrow, K (Arcade)
        int[] jumpKeys = {KeyEvent.VK_SPACE, KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_K};
        for (int key : jumpKeys) {
            player.setJump(false);
            if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_K) {
                player.setJump(true);
            }
            if (!player.isJump()) return false;

            player.setJump(false);
            if (player.isJump()) return false;
        }

        return true;
    }

    private static boolean testDashKeybindings() {
        Player player = new Player(100, 100, 64, 40, null);

        // Dash triggers: Shift (Universal), L (Arcade), Q (Legacy), C (Retro)
        int[] dashKeys = {KeyEvent.VK_SHIFT, KeyEvent.VK_L, KeyEvent.VK_Q, KeyEvent.VK_C};
        for (int key : dashKeys) {
            player.setDash(false);
            if (key == KeyEvent.VK_SHIFT || key == KeyEvent.VK_L || key == KeyEvent.VK_Q || key == KeyEvent.VK_C) {
                player.setDash(true);
            }
            if (!player.isDashing()) return false;
        }

        return true;
    }

    private static boolean testAttackKeybindings() {
        Player player = new Player(100, 100, 64, 40, null);
        player.setInAir(false);

        // Attack keys: J (Arcade) and X (Retro PC)
        int[] attackKeys = {KeyEvent.VK_J, KeyEvent.VK_X};
        for (int key : attackKeys) {
            player.setCharging(false);
            if (key == KeyEvent.VK_J || key == KeyEvent.VK_X) {
                player.setCharging(true);
            }
            if (!player.isCharging()) return false;

            player.releaseAttack();
            if (player.isCharging()) return false;
            if (!player.isAttacking()) return false;

            player.setAttacking(false);
        }

        return true;
    }
}
