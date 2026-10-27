package utilitytools;

/**
 * Type-safe enumeration for world interactive objects, potions, containers, and equipment.
 * Adheres to Effective Java (Item 34: Use enums instead of int constants).
 */
public enum ObjectType {
    RED_POTION_1(0, "Small Red Potion"),
    RED_POTION_2(1, "Medium Red Potion"),
    RED_POTION_3(2, "Large Red Potion"),
    BLUE_POTION_1(3, "Small Blue Potion"),
    BLUE_POTION_2(4, "Medium Blue Potion"),
    BLUE_POTION_3(5, "Large Blue Potion"),
    SIGN(5, "Information Sign"),
    BARREL(6, "Explosive Barrel"),
    BOX(7, "Wooden Box"),
    KANDANG(8, "Tiger Cage"),
    HELMET(10, "Helmet"),
    ARMOR(11, "Armor"),
    SHOES(12, "Shoes"),
    RING(13, "Ring"),
    SACK(14, "Sack"),
    GLOVES(15, "Gloves");

    private final int id;
    private final String displayName;

    ObjectType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ObjectType fromId(int id) {
        for (ObjectType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return RED_POTION_1;
    }

    public boolean isEquipment() {
        return id >= 10 && id <= 15;
    }

    public boolean isPotion() {
        return id >= 0 && id <= 5;
    }

    public boolean isContainer() {
        return id == 6 || id == 7;
    }
}
