package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import utilitytools.Konstanta.ObjectConstants;

/**
 * Controller extracting inventory management, equipment slots,
 * and gear defense values out of Player to adhere to SRP (Single Responsibility Principle).
 */
public class PlayerInventory {

    public static final int MAX_INVENTORY_SIZE = 24;

    private final Player player;
    private final ArrayList<Integer> items = new ArrayList<>();

    // Equipment Slots (-1 indicates unequipped)
    private int equippedHelmet = -1;
    private int equippedArmor = -1;
    private int equippedGloves = -1;
    private int equippedShoes = -1;
    private int equippedAcc1 = -1;
    private int equippedAcc2 = -1;

    public PlayerInventory(Player player) {
        this.player = player;
    }

    public boolean addItem(int objType) {
        if (items.size() < MAX_INVENTORY_SIZE) {
            items.add(objType);
            System.out.println("Item masuk tas. Tipe ID: " + objType);
            return true;
        } else {
            System.out.println("Penyimpanan Penuh! Tidak bisa mengambil barang.");
            return false;
        }
    }

    public void unequipItem(int equipSlotIndex) {
        if (items.size() >= MAX_INVENTORY_SIZE) {
            System.out.println("Tas penuh, tidak bisa melepas perlengkapan!");
            return;
        }

        int itemToReturn = -1;
        switch (equipSlotIndex) {
            case 0: itemToReturn = equippedHelmet; equippedHelmet = -1; break;
            case 1: itemToReturn = equippedArmor;  equippedArmor = -1;  break;
            case 2: itemToReturn = equippedShoes;  equippedShoes = -1;  break;
            case 3: itemToReturn = equippedAcc1;   equippedAcc1 = -1;   break;
            case 4: itemToReturn = equippedAcc2;   equippedAcc2 = -1;   break;
            case 5: itemToReturn = equippedGloves; equippedGloves = -1; break;
        }

        if (itemToReturn != -1) {
            items.add(itemToReturn);
            if (player != null) {
                player.calculateDefense();
            }
            System.out.println("Perlengkapan dilepas!");
        }
    }

    public void equipItem(int itemIndex, String equipmentType) {
        if (itemIndex < items.size()) {
            int itemToEquip = items.get(itemIndex);
            int oldItem = -1;

            switch (equipmentType.toLowerCase()) {
                case "head":       oldItem = equippedHelmet; equippedHelmet = itemToEquip; break;
                case "body":       oldItem = equippedArmor;  equippedArmor = itemToEquip;  break;
                case "hands":      oldItem = equippedGloves; equippedGloves = itemToEquip; break;
                case "shoes":      oldItem = equippedShoes;  equippedShoes = itemToEquip;  break;
                case "accessory1": oldItem = equippedAcc1;   equippedAcc1 = itemToEquip;   break;
                case "accessory2": oldItem = equippedAcc2;   equippedAcc2 = itemToEquip;   break;
            }

            items.remove(itemIndex);
            if (oldItem != -1) {
                items.add(oldItem);
            }
            if (player != null) {
                player.calculateDefense();
            }
        }
    }

    public int calculateDefenseBonus() {
        int bonus = 0;
        bonus += getEquipmentDefenseValue(equippedHelmet);
        bonus += getEquipmentDefenseValue(equippedArmor);
        bonus += getEquipmentDefenseValue(equippedGloves);
        bonus += getEquipmentDefenseValue(equippedShoes);
        bonus += getEquipmentDefenseValue(equippedAcc1);
        bonus += getEquipmentDefenseValue(equippedAcc2);
        return bonus;
    }

    public static int getEquipmentDefenseValue(int itemType) {
        if (itemType == -1) return 0;
        switch (itemType) {
            case ObjectConstants.HELMET: return 5;
            case ObjectConstants.ARMOR:  return 15;
            case ObjectConstants.GLOVES: return 3;
            case ObjectConstants.SHOES:  return 4;
            case ObjectConstants.RING:   return 2;
            case ObjectConstants.SACK:   return 4;
            default: return 0;
        }
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public List<Integer> getItemsCopy() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    public void clear() {
        items.clear();
    }

    public int size() {
        return items.size();
    }

    // Equipment Getters & Setters
    public int getEquippedHelmet() { return equippedHelmet; }
    public void setEquippedHelmet(int id) { this.equippedHelmet = id; }

    public int getEquippedArmor() { return equippedArmor; }
    public void setEquippedArmor(int id) { this.equippedArmor = id; }

    public int getEquippedGloves() { return equippedGloves; }
    public void setEquippedGloves(int id) { this.equippedGloves = id; }

    public int getEquippedShoes() { return equippedShoes; }
    public void setEquippedShoes(int id) { this.equippedShoes = id; }

    public int getEquippedAcc1() { return equippedAcc1; }
    public void setEquippedAcc1(int id) { this.equippedAcc1 = id; }

    public int getEquippedAcc2() { return equippedAcc2; }
    public void setEquippedAcc2(int id) { this.equippedAcc2 = id; }
}
