package inputs;

import gameStates.StateMethods;

/**
 * Contract providing the active GameState to input dispatchers.
 * Decouples input listeners from the concrete GameCore class (DIP).
 */
public interface StateProvider {
    StateMethods getActiveState();
}
