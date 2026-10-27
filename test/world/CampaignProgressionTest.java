package world;

import entity.Enemy;

/**
 * Headless unit verification test suite for SinglePassLevelParser and WorldManager campaign progression.
 * Verifies single-pass streaming parsing and seamless multi-level campaign cycling.
 */
public class CampaignProgressionTest {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING CAMPAIGN PROGRESSION & SINGLE-PASS PARSER TESTS ===");

        if (testSinglePassLevelParsing()) {
            System.out.println("[PASS] testSinglePassLevelParsing");
            passed++;
        } else {
            System.err.println("[FAIL] testSinglePassLevelParsing");
            failed++;
        }

        if (testCampaignProgressionCycling()) {
            System.out.println("[PASS] testCampaignProgressionCycling");
            passed++;
        } else {
            System.err.println("[FAIL] testCampaignProgressionCycling");
            failed++;
        }

        System.out.println("===============================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testSinglePassLevelParsing() {
        World tutorialWorld = SinglePassLevelParser.parse("/map_tutorial_fix.txt", "outside_sprites.png", "playing_bg_img.png");

        if (tutorialWorld == null) return false;
        int[][] tiles = tutorialWorld.getWorldData();
        if (tiles == null || tiles.length != 14 || tiles[0].length != 200) {
            System.err.println("Unexpected tiles dimensions: " + (tiles == null ? "null" : tiles.length + "x" + tiles[0].length));
            return false;
        }

        if (tutorialWorld.getEnemies() == null || tutorialWorld.getEnemies().isEmpty()) {
            System.err.println("Expected enemies to be parsed from /map_tutorial_fix.txt, but list was empty");
            return false;
        }

        for (Enemy e : tutorialWorld.getEnemies()) {
            if (e == null || e.getHitBox() == null) {
                return false;
            }
        }

        return true;
    }

    private static boolean testCampaignProgressionCycling() {
        // Instantiate WorldManager with null GameCore in headless mode
        WorldManager wm = new WorldManager(null);

        if (wm.getTotalWorlds() != 3) {
            System.err.println("Expected 3 campaign worlds, found: " + wm.getTotalWorlds());
            return false;
        }

        if (wm.getWorldIndex() != 0) {
            System.err.println("Initial world index should be 0");
            return false;
        }

        // Advance to Level 2 (Jungle)
        wm.loadNextWorld();
        if (wm.getWorldIndex() != 1 || !"/jungle_map.txt".equals(wm.getCurrentMap().getMapFilePath())) {
            System.err.println("Expected Level 2 Jungle map, got: " + wm.getCurrentMap().getMapFilePath());
            return false;
        }

        // Advance to Level 3 (Castle)
        wm.loadNextWorld();
        if (wm.getWorldIndex() != 2 || !"/main_tileset.txt".equals(wm.getCurrentMap().getMapFilePath())) {
            System.err.println("Expected Level 3 Castle map, got: " + wm.getCurrentMap().getMapFilePath());
            return false;
        }

        // Loop back to Level 1 (Tutorial)
        wm.loadNextWorld();
        if (wm.getWorldIndex() != 0 || !"/map_tutorial_fix.txt".equals(wm.getCurrentMap().getMapFilePath())) {
            System.err.println("Expected wrap around to Level 1 Tutorial map");
            return false;
        }

        return true;
    }
}
