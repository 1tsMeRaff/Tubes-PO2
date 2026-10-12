package inputs;

import gameStates.StateMethods;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Polymorphic input dispatcher.
 * Eliminates redundant switch-case branching across Keyboard and Mouse listeners
 * by routing events directly to the active GameState adhering to SRP, OCP, and DIP.
 */
public class InputDispatcher {

    private final StateProvider stateProvider;

    public InputDispatcher(StateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }

    public void dispatchKeyPressed(KeyEvent e) {
        StateMethods state = getActiveState();
        if (state != null) {
            state.keyPressed(e);
        }
    }

    public void dispatchKeyReleased(KeyEvent e) {
        StateMethods state = getActiveState();
        if (state != null) {
            state.keyReleased(e);
        }
    }

    public void dispatchMousePressed(MouseEvent e) {
        StateMethods state = getActiveState();
        if (state != null) {
            state.mousePressed(e);
        }
    }

    public void dispatchMouseReleased(MouseEvent e) {
        StateMethods state = getActiveState();
        if (state != null) {
            state.mouseReleased(e);
        }
    }

    public void dispatchMouseMoved(MouseEvent e) {
        StateMethods state = getActiveState();
        if (state != null) {
            state.mouseMoved(e);
        }
    }

    public void dispatchMouseClicked(MouseEvent e) {
        StateMethods state = getActiveState();
        if (state != null) {
            state.mouseClicked(e);
        }
    }

    public void dispatchMouseDragged(MouseEvent e) {
        StateMethods state = getActiveState();
        if (state != null) {
            state.mouseDragged(e);
        }
    }

    private StateMethods getActiveState() {
        return (stateProvider != null) ? stateProvider.getActiveState() : null;
    }
}
