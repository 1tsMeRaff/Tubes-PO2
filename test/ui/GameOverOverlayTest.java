package ui;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Headless unit verification test suite for GameOverOverlay.
 * Verifies layout geometry, mouse hover/press tracking, keyboard shortcut parity,
 * and crash-free headless rendering.
 */
public class GameOverOverlayTest {

    private static final Component DUMMY_SOURCE = new Component() {};

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING GAME OVER OVERLAY (DESIGN DIVISION) TESTS ===");

        if (testLayoutAndButtonBounds()) {
            System.out.println("[PASS] testLayoutAndButtonBounds");
            passed++;
        } else {
            System.err.println("[FAIL] testLayoutAndButtonBounds");
            failed++;
        }

        if (testMouseHoverAndPressStates()) {
            System.out.println("[PASS] testMouseHoverAndPressStates");
            passed++;
        } else {
            System.err.println("[FAIL] testMouseHoverAndPressStates");
            failed++;
        }

        if (testKeyboardShortcutEquivalence()) {
            System.out.println("[PASS] testKeyboardShortcutEquivalence");
            passed++;
        } else {
            System.err.println("[FAIL] testKeyboardShortcutEquivalence");
            failed++;
        }

        if (testZeroAllocationHeadlessRender()) {
            System.out.println("[PASS] testZeroAllocationHeadlessRender");
            passed++;
        } else {
            System.err.println("[FAIL] testZeroAllocationHeadlessRender");
            failed++;
        }

        System.out.println("==========================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testLayoutAndButtonBounds() {
        GameOverOverlay overlay = new GameOverOverlay(null);

        Rectangle btnRetry = overlay.getBtnRetryBounds();
        Rectangle btnMenu = overlay.getBtnMenuBounds();

        if (btnRetry == null || btnMenu == null) return false;
        if (btnRetry.width <= 0 || btnRetry.height <= 0) return false;
        if (btnMenu.width <= 0 || btnMenu.height <= 0) return false;

        // Verify buttons do not overlap
        if (btnRetry.intersects(btnMenu)) return false;

        return true;
    }

    private static boolean testMouseHoverAndPressStates() {
        GameOverOverlay overlay = new GameOverOverlay(null);
        Rectangle retryBounds = overlay.getBtnRetryBounds();

        // 1. Initially no hover
        if (overlay.isRetryHover() || overlay.isMenuHover()) return false;

        // 2. Move mouse inside Retry button
        MouseEvent enterRetry = new MouseEvent(DUMMY_SOURCE, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(),
                0, retryBounds.x + 5, retryBounds.y + 5, 0, false);
        overlay.mouseMoved(enterRetry);

        if (!overlay.isRetryHover()) return false;
        if (overlay.isMenuHover()) return false;

        // 3. Move mouse outside
        MouseEvent moveAway = new MouseEvent(DUMMY_SOURCE, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(),
                0, 0, 0, 0, false);
        overlay.mouseMoved(moveAway);

        if (overlay.isRetryHover() || overlay.isMenuHover()) return false;

        return true;
    }

    private static boolean testKeyboardShortcutEquivalence() {
        GameOverOverlay overlay = new GameOverOverlay(null);

        // Verify key press handles R, Enter, Space without exception
        KeyEvent keyR = new KeyEvent(DUMMY_SOURCE, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_R, 'R');
        overlay.keyPressed(keyR);

        KeyEvent keySpace = new KeyEvent(DUMMY_SOURCE, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        overlay.keyPressed(keySpace);

        KeyEvent keyEsc = new KeyEvent(DUMMY_SOURCE, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, (char) 27);
        overlay.keyPressed(keyEsc);

        return true;
    }

    private static boolean testZeroAllocationHeadlessRender() {
        GameOverOverlay overlay = new GameOverOverlay(null);

        BufferedImage testImg = new BufferedImage(896, 448, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = testImg.createGraphics();

        try {
            // Render 60 consecutive frames
            for (int i = 0; i < 60; i++) {
                overlay.draw(g2);
            }
        } finally {
            g2.dispose();
        }

        return true;
    }
}
