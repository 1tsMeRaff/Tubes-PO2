package utilitytools;

/**
 * Type-safe enumeration for enemy classifications in Tubes-PO2.
 * Adheres to Effective Java (Item 34: Use enums instead of int constants).
 */
public enum EnemyType {
    SLIME(0, "Slime"),
    DEMON_BOSS(1, "Demon Boss"),
    BLUE_GOLEM(2, "Blue Golem"),
    BRINGER_OF_DEATH(3, "Bringer of Death"),
    SKULLWOLF(4, "Skullwolf");

    private final int id;
    private final String displayName;

    EnemyType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static EnemyType fromId(int id) {
        for (EnemyType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return SLIME; // Default fallback
    }
}
