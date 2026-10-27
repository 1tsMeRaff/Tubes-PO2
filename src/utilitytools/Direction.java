package utilitytools;

/**
 * Type-safe enumeration for entity movement directions.
 */
public enum Direction {
    LEFT(0, -1, 0),
    UP(1, 0, -1),
    RIGHT(2, 1, 0),
    DOWN(3, 0, 1);

    private final int index;
    private final int dx;
    private final int dy;

    Direction(int index, int dx, int dy) {
        this.index = index;
        this.dx = dx;
        this.dy = dy;
    }

    public int getIndex() {
        return index;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public static Direction fromIndex(int index) {
        for (Direction dir : values()) {
            if (dir.index == index) {
                return dir;
            }
        }
        return RIGHT;
    }
}
