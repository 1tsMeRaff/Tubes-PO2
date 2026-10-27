package inputs;

import gameStates.StateMethods;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Headless unit verification test suite for InputDispatcher.
 * Tests polymorphic routing of key and mouse events with zero GUI thread allocations.
 */
public class InputDispatcherTest {

    private static class MockState implements StateMethods {
        boolean keyPressedCalled = false;
        boolean keyReleasedCalled = false;
        boolean mouseClickedCalled = false;
        boolean mousePressedCalled = false;
        boolean mouseReleasedCalled = false;
        boolean mouseMovedCalled = false;
        boolean mouseDraggedCalled = false;

        @Override public void update() {}
        @Override public void draw(Graphics g) {}
        @Override public void mouseClicked(MouseEvent e) { mouseClickedCalled = true; }
        @Override public void mousePressed(MouseEvent e) { mousePressedCalled = true; }
        @Override public void mouseReleased(MouseEvent e) { mouseReleasedCalled = true; }
        @Override public void mouseMoved(MouseEvent e) { mouseMovedCalled = true; }
        @Override public void keyPressed(KeyEvent e) { keyPressedCalled = true; }
        @Override public void keyReleased(KeyEvent e) { keyReleasedCalled = true; }
        @Override public void mouseDragged(MouseEvent e) { mouseDraggedCalled = true; }
    }

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING INPUT DISPATCHER TESTS ===");

        if (testPolymorphicRouting()) {
            System.out.println("[PASS] testPolymorphicRouting");
            passed++;
        } else {
            System.err.println("[FAIL] testPolymorphicRouting");
            failed++;
        }

        System.out.println("======================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testPolymorphicRouting() {
        MockState mockState = new MockState();
        InputDispatcher dispatcher = new InputDispatcher(() -> mockState);

        dispatcher.dispatchKeyPressed(null);
        dispatcher.dispatchKeyReleased(null);
        dispatcher.dispatchMousePressed(null);
        dispatcher.dispatchMouseReleased(null);
        dispatcher.dispatchMouseMoved(null);
        dispatcher.dispatchMouseClicked(null);
        dispatcher.dispatchMouseDragged(null);

        return mockState.keyPressedCalled && mockState.keyReleasedCalled &&
               mockState.mousePressedCalled && mockState.mouseReleasedCalled &&
               mockState.mouseMovedCalled && mockState.mouseClickedCalled &&
               mockState.mouseDraggedCalled;
    }
}
