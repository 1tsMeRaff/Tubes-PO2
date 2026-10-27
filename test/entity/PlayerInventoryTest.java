package entity;

import java.util.List;
import utilitytools.Konstanta.ObjectConstants;

/**
 * Headless unit verification test suite for PlayerInventory controller.
 * Verifies inventory capacity limits, equipment slots, defense bonus aggregation,
 * and defensive copying immutability.
 */
public class PlayerInventoryTest {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING PLAYER INVENTORY (SRP) UNIT TESTS ===");

        if (testAddItemAndCapacityLimit()) {
            System.out.println("[PASS] testAddItemAndCapacityLimit");
            passed++;
        } else {
            System.err.println("[FAIL] testAddItemAndCapacityLimit");
            failed++;
        }

        if (testEquipUnequipAndDefenseBonus()) {
            System.out.println("[PASS] testEquipUnequipAndDefenseBonus");
            passed++;
        } else {
            System.err.println("[FAIL] testEquipUnequipAndDefenseBonus");
            failed++;
        }

        if (testDefensiveCopyImmutability()) {
            System.out.println("[PASS] testDefensiveCopyImmutability");
            passed++;
        } else {
            System.err.println("[FAIL] testDefensiveCopyImmutability");
            failed++;
        }

        System.out.println("==================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testAddItemAndCapacityLimit() {
        PlayerInventory inv = new PlayerInventory(null);

        // Fill to maximum capacity (24)
        for (int i = 0; i < PlayerInventory.MAX_INVENTORY_SIZE; i++) {
            if (!inv.addItem(ObjectConstants.RED_POTION_1)) {
                System.err.println("Failed to add item at index: " + i);
                return false;
            }
        }

        if (inv.size() != PlayerInventory.MAX_INVENTORY_SIZE) {
            System.err.println("Expected size " + PlayerInventory.MAX_INVENTORY_SIZE + " but got: " + inv.size());
            return false;
        }

        // 25th item should be rejected
        boolean addedOverLimit = inv.addItem(ObjectConstants.RED_POTION_1);
        if (addedOverLimit) {
            System.err.println("Inventory allowed adding beyond MAX_INVENTORY_SIZE!");
            return false;
        }

        return true;
    }

    private static boolean testEquipUnequipAndDefenseBonus() {
        PlayerInventory inv = new PlayerInventory(null);

        inv.addItem(ObjectConstants.HELMET); // def +5
        inv.addItem(ObjectConstants.ARMOR);  // def +15
        inv.addItem(ObjectConstants.GLOVES); // def +3
        inv.addItem(ObjectConstants.SHOES);  // def +4

        // Equip Helmet (index 0)
        inv.equipItem(0, "head");
        if (inv.getEquippedHelmet() != ObjectConstants.HELMET) {
            System.err.println("Helmet not equipped correctly");
            return false;
        }

        // Equip Armor (index 0 now, after first item removed)
        inv.equipItem(0, "body");
        if (inv.getEquippedArmor() != ObjectConstants.ARMOR) {
            System.err.println("Armor not equipped correctly");
            return false;
        }

        // Equip Gloves (index 0 now)
        inv.equipItem(0, "hands");
        // Equip Shoes (index 0 now)
        inv.equipItem(0, "shoes");

        int expectedDefense = 5 + 15 + 3 + 4; // 27
        if (inv.calculateDefenseBonus() != expectedDefense) {
            System.err.println("Expected defense bonus " + expectedDefense + " but got: " + inv.calculateDefenseBonus());
            return false;
        }

        // Unequip Helmet (slot 0)
        inv.unequipItem(0);
        if (inv.getEquippedHelmet() != -1) {
            System.err.println("Helmet slot should be -1 after unequip");
            return false;
        }
        if (inv.calculateDefenseBonus() != (expectedDefense - 5)) {
            System.err.println("Defense bonus did not decrease after helmet unequip");
            return false;
        }

        return true;
    }

    private static boolean testDefensiveCopyImmutability() {
        PlayerInventory inv = new PlayerInventory(null);
        inv.addItem(ObjectConstants.BARREL);
        inv.addItem(ObjectConstants.BOX);

        List<Integer> copy = inv.getItemsCopy();
        if (copy.size() != 2) {
            System.err.println("Copy size incorrect");
            return false;
        }

        try {
            copy.add(99);
            System.err.println("Modifying defensive copy should throw UnsupportedOperationException!");
            return false;
        } catch (UnsupportedOperationException expected) {
            // Expected behavior
        }

        if (inv.size() != 2) {
            System.err.println("Underlying inventory was modified by external attempt!");
            return false;
        }

        return true;
    }
}
