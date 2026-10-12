package utilitytools;

import java.awt.image.BufferedImage;

/**
 * Headless verification test to ensure all reorganized resources
 * resolve correctly through LoadSave and classpath.
 */
public class ResourceLoaderTest {

    public static void main(String[] args) {
        System.out.println("=== RUNNING RESOURCE LOADER TESTS ===");
        int passed = 0;
        int failed = 0;

        // Test 1: Character sprite
        BufferedImage player = LoadSave.GetSpriteAtlas("player_right.png");
        if (player != null && player.getWidth() > 64) {
            System.out.println("[PASS] player_right.png resolved (" + player.getWidth() + "x" + player.getHeight() + ")");
            passed++;
        } else {
            System.err.println("[FAIL] player_right.png failed to load");
            failed++;
        }

        // Test 2: UI image
        BufferedImage menuBg = LoadSave.GetSpriteAtlas("mainn_menu.jpeg");
        if (menuBg != null && menuBg.getWidth() > 64) {
            System.out.println("[PASS] mainn_menu.jpeg resolved (" + menuBg.getWidth() + "x" + menuBg.getHeight() + ")");
            passed++;
        } else {
            System.err.println("[FAIL] mainn_menu.jpeg failed to load");
            failed++;
        }

        // Test 3: Map data
        int[][] mapData = LoadSave.GetTilesData("/map_tutorial_fix.txt");
        if (mapData != null && mapData.length > 0 && mapData[0].length > 0) {
            System.out.println("[PASS] /map_tutorial_fix.txt resolved (" + mapData.length + " rows x " + mapData[0].length + " cols)");
            passed++;
        } else {
            System.err.println("[FAIL] /map_tutorial_fix.txt failed to load");
            failed++;
        }

        // Test 4: Audio clip stream
        java.io.InputStream audioStream = LoadSave.getResourceStream("attack1.wav");
        if (audioStream != null) {
            System.out.println("[PASS] attack1.wav audio stream resolved");
            passed++;
            try { audioStream.close(); } catch (Exception ignored) {}
        } else {
            System.err.println("[FAIL] attack1.wav audio stream failed to load");
            failed++;
        }

        System.out.println("======================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }
}
