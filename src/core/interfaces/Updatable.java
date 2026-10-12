package core.interfaces;

/**
 * Interface for any game entity, state, or manager that updates per tick.
 * Part of ISP (Interface Segregation Principle) modernization.
 */
public interface Updatable {
    void update();
}
