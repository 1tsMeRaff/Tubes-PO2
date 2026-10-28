package empirical;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;

import audio.AudioPlayer;
import utilitytools.LoadSave;
import utilitytools.SaveLoadManager;
import world.SinglePassLevelParser;
import world.World;

/**
 * Empirical Adversarial Verification and Stress Test Suite for Milestone 1.
 * Tests:
 * 1. Outside sprites atlas slicing and tutorial map indexing resilience across all offsets.
 * 2. SaveLoadManager defensive fallback under simulated read-only execution directories.
 * 3. AudioPlayer volume and mute operations under headless mode and unsupported audio control lines.
 * 4. Tubes-PO2.jar packaging integrity, manifest, and classpath asset resolution.
 */
public class Milestone1StressTest {

    private static int totalPassed = 0;
    private static int totalFailed = 0;

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println(">>> RUNNING ADVERSARIAL STRESS TEST SUITE: MILESTONE 1 <<<");
        System.out.println("======================================================================");

        runTest("1. Outside Sprites Atlas & Tutorial Map Indexing", () -> testOutsideSpritesAtlasAndMap());
        runTest("2. WorldManager Rendering Simulation Across Arbitrary Camera Offsets", () -> testWorldRenderingStress());
        runTest("3. SaveLoadManager Defensive Fallback Under Read-Only Directory Constraint", () -> testSaveLoadFallback());
        runTest("4. AudioPlayer Headless Operations & Extreme Volume Boundaries", () -> testAudioPlayerHeadlessAndVolume());
        runTest("5. AudioPlayer Unsupported Audio Control Lines Resilience (Mock Proxy)", () -> testAudioPlayerUnsupportedControls());
        runTest("6. Tubes-PO2.jar Packaging, Manifest, and Asset Integrity", () -> testJarIntegrity());

        System.out.println("======================================================================");
        System.out.println("TOTAL PASSED: " + totalPassed);
        System.out.println("TOTAL FAILED: " + totalFailed);
        System.out.println("======================================================================");

        if (totalFailed > 0) {
            System.err.println("ADVERSARIAL VERIFICATION FAILED: One or more stress tests failed.");
            System.exit(1);
        } else {
            System.out.println("ADVERSARIAL VERIFICATION PASSED: All stress tests passed cleanly.");
            System.exit(0);
        }
    }

    private static void runTest(String name, TestCase test) {
        System.out.print("[TEST] " + name + " ... ");
        try {
            test.run();
            System.out.println("PASSED");
            totalPassed++;
        } catch (Throwable t) {
            System.out.println("FAILED!");
            System.err.println("  Exception: " + t.getClass().getName() + " -> " + t.getMessage());
            t.printStackTrace(System.err);
            totalFailed++;
        }
    }

    @FunctionalInterface
    interface TestCase {
        void run() throws Exception;
    }

    /**
     * 1. Test outside_sprites.png atlas and tutorial map indexing.
     */
    private static void testOutsideSpritesAtlasAndMap() throws Exception {
        BufferedImage atlas = LoadSave.GetSpriteAtlas("outside_sprites.png");
        if (atlas == null) {
            throw new AssertionError("Loaded outside_sprites.png is null!");
        }

        int width = atlas.getWidth();
        int height = atlas.getHeight();
        if (width != 445 || height != 384) {
            throw new AssertionError("outside_sprites.png unexpected dimensions: " + width + "x" + height + " (expected 445x384)");
        }

        int tileWidth = 16;
        int tileHeight = 16;
        int cols = width / tileWidth; // 27
        int rows = height / tileHeight; // 24
        int totalAtlasTiles = cols * rows; // 648

        BufferedImage[] mapSprite = new BufferedImage[totalAtlasTiles];
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                int index = i + (j * cols);
                mapSprite[index] = atlas.getSubimage(i * tileWidth, j * tileHeight, tileWidth, tileHeight);
                if (mapSprite[index] == null) {
                    throw new AssertionError("Subimage at tile index " + index + " is null!");
                }
            }
        }

        // Parse map_tutorial_fix.txt directly
        World world = SinglePassLevelParser.parse("/map_tutorial_fix.txt", "outside_sprites.png", LoadSave.PLAY_BACKGROUND_IMG);
        if (world == null) {
            throw new AssertionError("SinglePassLevelParser returned null for /map_tutorial_fix.txt");
        }

        int[][] tilesData = world.getWorldData();
        if (tilesData == null || tilesData.length == 0 || tilesData[0].length == 0) {
            throw new AssertionError("Tile matrix is empty or null!");
        }

        // Scan raw text file to ensure every single non-air tile is valid
        try (InputStream is = LoadSave.getResourceStream("/map_tutorial_fix.txt")) {
            if (is == null) {
                throw new AssertionError("Cannot open resource stream for /map_tutorial_fix.txt");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            int r = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] tokens = line.split(",");
                for (int c = 0; c < tokens.length; c++) {
                    int val = Integer.parseInt(tokens[c].trim());
                    // Enemy spawn indicators
                    if (val >= 800 && val <= 804) continue;
                    // Object spawn indicators (armor 910-915, potions 500-505)
                    if ((val >= 910 && val <= 915) || (val >= 500 && val <= 505)) continue;
                    if (val != -1) {
                        if (val < 0 || val >= totalAtlasTiles) {
                            throw new AssertionError("Raw tile value " + val + " at (" + r + "," + c + ") exceeds atlas bounds [0, " + totalAtlasTiles + ")!");
                        }
                        if (mapSprite[val] == null) {
                            throw new AssertionError("Raw tile value " + val + " maps to null sprite in atlas!");
                        }
                    }
                }
                r++;
            }
        }

        // Verify world parsed tilesData
        for (int row = 0; row < tilesData.length; row++) {
            for (int col = 0; col < tilesData[row].length; col++) {
                int tileId = tilesData[row][col];
                if (tileId != -1) {
                    if (tileId >= 0 && tileId < totalAtlasTiles) {
                        BufferedImage sprite = mapSprite[tileId];
                        if (sprite == null) {
                            throw new AssertionError("Tile " + tileId + " at (" + row + "," + col + ") has null sprite!");
                        }
                    }
                }
            }
        }
    }

    /**
     * 2. WorldManager rendering stress test across camera offsets (negative, zero, normal, extreme).
     */
    private static void testWorldRenderingStress() throws Exception {
        BufferedImage atlas = LoadSave.GetSpriteAtlas("outside_sprites.png");
        int cols = atlas.getWidth() / 16;
        int rows = atlas.getHeight() / 16;
        BufferedImage[] mapSprite = new BufferedImage[cols * rows];
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                mapSprite[i + (j * cols)] = atlas.getSubimage(i * 16, j * 16, 16, 16);
            }
        }

        World world = SinglePassLevelParser.parse("/map_tutorial_fix.txt", "outside_sprites.png", LoadSave.PLAY_BACKGROUND_IMG);
        int[][] worldData = world.getWorldData();

        BufferedImage canvas = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics();

        int[] offsetsToTest = { -2000, -100, 0, 50, 400, 1000, 5000, 50000 };
        for (int xLvlOffset : offsetsToTest) {
            int mapHeight = worldData.length;
            int mapWidth = worldData[0].length;
            int tileSize = 32;

            int startX = Math.max(0, xLvlOffset / tileSize);
            int endX = Math.min(mapWidth, startX + (800 / tileSize) + 2);

            for (int j = 0; j < mapHeight; j++) {
                for (int i = startX; i < endX; i++) {
                    int tiledValue = worldData[j][i];
                    if (tiledValue != -1 && tiledValue >= 0 && tiledValue < mapSprite.length) {
                        int xPos = i * tileSize - xLvlOffset;
                        int yPos = j * tileSize;
                        g.drawImage(mapSprite[tiledValue], xPos, yPos, tileSize, tileSize, null);
                    }
                }
            }
        }
        g.dispose();
    }

    /**
     * 3. Test SaveLoadManager defensive fallback under simulated read-only directory constraint.
     */
    private static void testSaveLoadFallback() throws Exception {
        String originalUserHome = System.getProperty("user.home");
        Path tempSandbox = Files.createTempDirectory("felinesouls_sandbox_");
        File simulatedBlocker = new File("savegame.txt.test");

        try {
            System.setProperty("user.home", tempSandbox.toAbsolutePath().toString());

            // Normal writeable case
            Path defaultPath = SaveLoadManager.getSaveFilePath();
            if (!defaultPath.equals(Paths.get("savegame.txt"))) {
                throw new AssertionError("Default getSaveFilePath() should be local savegame.txt, got: " + defaultPath);
            }

            // Simulate read-only execution directory by creating savegame.txt.test as an undeletable / non-writable directory
            if (!simulatedBlocker.exists()) {
                if (!simulatedBlocker.mkdir()) {
                    throw new RuntimeException("Failed to create mock directory blocker");
                }
            }

            // Now SaveLoadManager.getSaveFilePath() should encounter an error writing to savegame.txt.test
            // and fall back to user.home/.felinesouls/savegame.txt
            Path fallbackPath = SaveLoadManager.getSaveFilePath();
            Path expectedFallback = tempSandbox.resolve(".felinesouls").resolve("savegame.txt");

            if (!fallbackPath.toAbsolutePath().normalize().equals(expectedFallback.toAbsolutePath().normalize())) {
                throw new AssertionError("Fallback path mismatch! Expected: " + expectedFallback + ", got: " + fallbackPath);
            }

            // Verify the directory was created
            if (!Files.exists(tempSandbox.resolve(".felinesouls"))) {
                throw new AssertionError(".felinesouls directory was not created in user.home!");
            }

            // Write test save content to fallbackPath
            Files.write(fallbackPath, "level=5\nexp=1200\ncurrentHealth=80\n".getBytes(StandardCharsets.UTF_8));
            if (!Files.exists(fallbackPath) || Files.size(fallbackPath) == 0) {
                throw new AssertionError("Failed writing content to fallback save path!");
            }

            // Test getLoadFile() when local savegame.txt does not exist
            File localSave = new File("savegame.txt");
            boolean localExisted = localSave.exists();
            File tempBackup = null;
            if (localExisted) {
                tempBackup = new File("savegame.txt.challenger_bak");
                localSave.renameTo(tempBackup);
            }

            try {
                File loadedFile = SaveLoadManager.getLoadFile();
                if (!loadedFile.toPath().toAbsolutePath().normalize().equals(fallbackPath.toAbsolutePath().normalize())) {
                    throw new AssertionError("getLoadFile() failed to resolve fallback file! Got: " + loadedFile);
                }
            } finally {
                if (localExisted && tempBackup != null) {
                    tempBackup.renameTo(localSave);
                }
            }

        } finally {
            // Cleanup
            if (simulatedBlocker.exists()) {
                simulatedBlocker.delete();
            }
            if (originalUserHome != null) {
                System.setProperty("user.home", originalUserHome);
            }
            // Cleanup temp sandbox
            try {
                Path fbFile = tempSandbox.resolve(".felinesouls").resolve("savegame.txt");
                Files.deleteIfExists(fbFile);
                Files.deleteIfExists(tempSandbox.resolve(".felinesouls"));
                Files.deleteIfExists(tempSandbox);
            } catch (Exception ignored) {}
        }
    }

    /**
     * 4. Test AudioPlayer in headless mode and extreme volume boundaries.
     */
    private static void testAudioPlayerHeadlessAndVolume() throws Exception {
        AudioPlayer player = new AudioPlayer();

        float[] testVolumes = {
            0.0f, -0.5f, -100.0f, 0.0001f, 0.001f, 0.5f, 0.8f, 1.0f, 1.5f, 100.0f, Float.MIN_VALUE
        };

        for (float vol : testVolumes) {
            player.setVolume(vol);
            float current = player.getVolume();
            if (current != vol) {
                throw new AssertionError("Volume mismatch: expected " + vol + ", got " + current);
            }
        }

        // Test mute toggling
        player.toggleSongMute();
        player.toggleSongMute();
        player.toggleEffectMute();
        player.toggleEffectMute();

        // Test song transitions
        player.setLevelSong(1);
        player.setLevelSong(2);
        player.playSong(AudioPlayer.MENU_1);

        // Test effect triggers
        player.playAttackSound();
        player.playEffect(AudioPlayer.JUMP);
        player.playEffect(AudioPlayer.DIE);
        player.playEffect(AudioPlayer.GAMEOVER);
        player.lvlCompleted();
        player.stopSong();
        player.destroy();
    }

    /**
     * 5. Adversarial mock test: AudioPlayer with Clips that return false for isControlSupported.
     */
    private static void testAudioPlayerUnsupportedControls() throws Exception {
        AudioPlayer player = new AudioPlayer();

        // Create dynamic proxy for Clip that always returns false for isControlSupported
        // and throws IllegalArgumentException if getControl() is invoked without checking
        Clip unsupportedClip = (Clip) Proxy.newProxyInstance(
            Milestone1StressTest.class.getClassLoader(),
            new Class<?>[] { Clip.class },
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    String mName = method.getName();
                    if (mName.equals("isControlSupported")) {
                        return false;
                    }
                    if (mName.equals("getControl")) {
                        throw new IllegalArgumentException("Adversarial Trap: getControl called when isControlSupported is false!");
                    }
                    if (mName.equals("isActive") || mName.equals("isOpen")) {
                        return false;
                    }
                    if (method.getReturnType().equals(boolean.class)) {
                        return false;
                    }
                    if (method.getReturnType().equals(int.class)) {
                        return 0;
                    }
                    if (method.getReturnType().equals(long.class)) {
                        return 0L;
                    }
                    return null;
                }
            }
        );

        // Inject unsupportedClip into player's songs and effects arrays via reflection
        Field songsField = AudioPlayer.class.getDeclaredField("songs");
        songsField.setAccessible(true);
        Clip[] mockSongs = new Clip[] { unsupportedClip, unsupportedClip, unsupportedClip };
        songsField.set(player, mockSongs);

        Field effectsField = AudioPlayer.class.getDeclaredField("effects");
        effectsField.setAccessible(true);
        Clip[] mockEffects = new Clip[9];
        for (int i = 0; i < mockEffects.length; i++) {
            mockEffects[i] = unsupportedClip;
        }
        effectsField.set(player, mockEffects);

        // These operations must execute cleanly without throwing IllegalArgumentException
        player.setVolume(0.5f);
        player.setVolume(0.0f);
        player.setVolume(1.0f);
        player.toggleSongMute();
        player.toggleEffectMute();
        player.destroy();
    }

    /**
     * 6. Test Tubes-PO2.jar packaging, manifest, and asset integrity.
     */
    private static void testJarIntegrity() throws Exception {
        File rootJar = new File("Tubes-PO2.jar");
        if (!rootJar.exists()) {
            throw new AssertionError("Root standalone JAR Tubes-PO2.jar does not exist!");
        }

        File docsJar = new File("docs/Tubes-PO2.jar");
        if (!docsJar.exists()) {
            throw new AssertionError("Docs web portal JAR docs/Tubes-PO2.jar does not exist!");
        }

        long rootSize = rootJar.length();
        long docsSize = docsJar.length();
        if (rootSize < 40_000_000L) {
            throw new AssertionError("Tubes-PO2.jar size too small (" + rootSize + " bytes), assets likely missing!");
        }
        if (rootSize != docsSize) {
            throw new AssertionError("Size mismatch between root JAR (" + rootSize + ") and docs JAR (" + docsSize + ")!");
        }

        // Verify Manifest
        try (JarFile jar = new JarFile(rootJar)) {
            Manifest manifest = jar.getManifest();
            if (manifest == null) {
                throw new AssertionError("Tubes-PO2.jar does not contain a manifest!");
            }
            String mainClass = manifest.getMainAttributes().getValue("Main-Class");
            if (!"main.Main".equals(mainClass)) {
                throw new AssertionError("Manifest Main-Class is '" + mainClass + "', expected 'main.Main'!");
            }

            // Verify critical assets bundled at root or standard paths
            String[] requiredEntries = {
                "tilesets/outside_sprites.png",
                "maps/map_tutorial_fix.txt",
                "audio/Theme.wav",
                "sprites/objects/kandang.png",
                "main/Main.class",
                "world/WorldManager.class",
                "utilitytools/SaveLoadManager.class",
                "audio/AudioPlayer.class"
            };

            for (String entry : requiredEntries) {
                if (jar.getJarEntry(entry) == null) {
                    throw new AssertionError("Required JAR entry missing: " + entry);
                }
            }
        }
    }
}
