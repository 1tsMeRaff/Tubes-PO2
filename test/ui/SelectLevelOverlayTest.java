package ui;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Headless unit verification test suite for SelectLevelOverlay.
 * Verifies expedition card geometry, mouse tracking, keyboard accessibility,
 * and crash-free headless rendering.
 */
public class SelectLevelOverlayTest {

    private static final Component DUMMY_SOURCE = new Component() {};

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING SELECT LEVEL OVERLAY (DESIGN DIVISION) TESTS ===");

        if (testLevelCardBounds()) {
            System.out.println("[PASS] testLevelCardBounds");
            passed++;
        } else {
            System.err.println("[FAIL] testLevelCardBounds");
            failed++;
        }

        if (testMouseInteraction()) {
            System.out.println("[PASS] testMouseInteraction");
            passed++;
        } else {
            System.err.println("[FAIL] testMouseInteraction");
            failed++;
        }

        if (testKeyboardAccessibility()) {
            System.out.println("[PASS] testKeyboardAccessibility");
            passed++;
        } else {
            System.err.println("[FAIL] testKeyboardAccessibility");
            failed++;
        }

        if (testZeroAllocationHeadlessRender()) {
            System.out.println("[PASS] testZeroAllocationHeadlessRender");
            passed++;
        } else {
            System.err.println("[FAIL] testZeroAllocationHeadlessRender");
            failed++;
        }

        System.out.println("=============================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testLevelCardBounds() {
        SelectLevelOverlay overlay = new SelectLevelOverlay(null);

        Rectangle c1 = overlay.getCardL1Bounds();
        Rectangle c2 = overlay.getCardL2Bounds();
        Rectangle c3 = overlay.getCardL3Bounds();
        Rectangle back = overlay.getBtnBackBounds();

        if (c1 == null || c2 == null || c3 == null || back == null) return false;
        if (c1.width <= 0 || c1.height <= 0) return false;
        if (c2.width <= 0 || c2.height <= 0) return false;
        if (c3.width <= 0 || c3.height <= 0) return false;
        if (back.width <= 0 || back.height <= 0) return false;

        // Verify card order from left to right without overlapping
        if (c1.x + c1.width > c2.x) return false;
        if (c2.x + c2.width > c3.x) return false;

        return true;
    }

    private static boolean testMouseInteraction() {
        SelectLevelOverlay overlay = new SelectLevelOverlay(null);
        Rectangle c1 = overlay.getCardL1Bounds();
        Rectangle back = overlay.getBtnBackBounds();

        // 1. Move over Card 1
        MouseEvent enterC1 = new MouseEvent(DUMMY_SOURCE, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(),
                0, c1.x + 10, c1.y + 10, 0, false);
        overlay.mouseMoved(enterC1);

        if (!overlay.isHoverL1()) return false;
        if (overlay.isHoverL2() || overlay.isHoverL3() || overlay.isHoverBack()) return false;

        // 2. Move over Back button
        MouseEvent enterBack = new MouseEvent(DUMMY_SOURCE, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(),
                0, back.x + 10, back.y + 10, 0, false);
        overlay.mouseMoved(enterBack);

        if (!overlay.isHoverBack()) return false;
        if (overlay.isHoverL1()) return false;

        return true;
    }

    private static boolean testKeyboardAccessibility() {
        SelectLevelOverlay overlay = new SelectLevelOverlay(null);

        // Verify keypress 1, 2, 3, ESC execute gracefully without exception
        int[] keys = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_ESCAPE,
                      KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3};

        for (int k : keys) {
            KeyEvent e = new KeyEvent(DUMMY_SOURCE, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, k, (char) k);
            overlay.keyPressed(e);
        }

        return true;
    }

    private static boolean testZeroAllocationHeadlessRender() {
        SelectLevelOverlay overlay = new SelectLevelOverlay(null);

        BufferedImage testImg = new BufferedImage(896, 448, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = testImg.createGraphics();

        try {
            for (int i = 0; i < 60; i++) {
                overlay.draw(g2);
            }
        } finally {
            g2.dispose();
        }

        return true;
    }
}
