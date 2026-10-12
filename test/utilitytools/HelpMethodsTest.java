package utilitytools;

import java.awt.geom.Rectangle2D;

/**
 * Headless unit verification test suite for HelpMethods.
 * Runs independently using pure Java SE without external testing frameworks.
 */
public class HelpMethodsTest {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING HELPMETHODS UNIT TESTS ===");

        // Test 1: Solid Tile Identification
        if (testIsSolidTile()) {
            System.out.println("[PASS] testIsSolidTile");
            passed++;
        } else {
            System.err.println("[FAIL] testIsSolidTile");
            failed++;
        }

        // Test 2: Bounds Checking with canMoveHere
        if (testCanMoveHere()) {
            System.out.println("[PASS] testCanMoveHere");
            passed++;
        } else {
            System.err.println("[FAIL] testCanMoveHere");
            failed++;
        }

        // Test 3: Floor Collision Detection
        if (testIsEntityOnFloor()) {
            System.out.println("[PASS] testIsEntityOnFloor");
            passed++;
        } else {
            System.err.println("[FAIL] testIsEntityOnFloor");
            failed++;
        }

        System.out.println("======================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testIsSolidTile() {
        return !HelpMethods.isSolidTile(-1)
                && HelpMethods.isSolidTile(0)
                && HelpMethods.isSolidTile(42);
    }

    private static boolean testCanMoveHere() {
        int[][] mockLevel = new int[][] {
            { -1, -1, -1 },
            { -1, -1, -1 },
            {  1,  1,  1 }
        };

        // Moving in empty air (row 0-1)
        boolean canMoveInAir = HelpMethods.canMoveHere(10, 10, 20, 20, mockLevel);
        return canMoveInAir;
    }

    private static boolean testIsEntityOnFloor() {
        int[][] mockLevel = new int[][] {
            { -1, -1, -1 },
            { -1, -1, -1 },
            {  1,  1,  1 }
        };

        // Entity standing immediately above solid ground
        Rectangle2D.Float onGroundHitbox = new Rectangle2D.Float(10, 48 * 2 - 32, 20, 32);
        boolean onFloor = HelpMethods.IsEntityOnFloor(onGroundHitbox, mockLevel);
        return onFloor;
    }
}
