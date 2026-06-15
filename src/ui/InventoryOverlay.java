package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import gameStates.PlayStates;
import main.GameCore;
import utilitytools.Konstanta.ObjectConstants;

public class InventoryOverlay {

    private PlayStates playing;
    
    // State Navigasi
    private int currentTab = -1; 
    private String[] tabNames = {"Karakter", "Inventory", "Skill", ""};
    private int hoveredTab = -1;
    private int hoveredSlot = -1;
    private int selectedSlot = -1;

    // Rectangle untuk interaksi kotak Equipment di tab Karakter
    private Rectangle helmetSlot, armorSlot, glovesSlot, shoesSlot, acc1Slot, acc2Slot;
    
    // Array Rectangle untuk 20 kotak di Inventory Tab
    private Rectangle[] inventoryGrid = new Rectangle[20];

    public InventoryOverlay(PlayStates playing) {
        this.playing = playing;
        initLayout();
    }

    private void initLayout() {
        // Init Rectangle Layout untuk Karakter / Equipments
        int contentX = (int) (50 * GameCore.SCALE) + (int) (140 * GameCore.SCALE) + (int)(15 * GameCore.SCALE);
        int contentY = (int) (100 * GameCore.SCALE);
        int slotSize = (int) (45 * GameCore.SCALE);

        int eqSlotX = contentX + (int)(30 * GameCore.SCALE);
        helmetSlot = new Rectangle(eqSlotX, contentY + (int)(50 * GameCore.SCALE), slotSize, slotSize);
        armorSlot  = new Rectangle(eqSlotX, contentY + (int)(50 * GameCore.SCALE) + (slotSize + 10), slotSize, slotSize);
        shoesSlot  = new Rectangle(eqSlotX, contentY + (int)(50 * GameCore.SCALE) + ((slotSize + 10) * 2), slotSize, slotSize);
        
        int rightEqSlotX = eqSlotX + slotSize + (int)(40 * GameCore.SCALE);
        glovesSlot   = new Rectangle(rightEqSlotX, contentY + (int)(50 * GameCore.SCALE), slotSize, slotSize);
        acc1Slot     = new Rectangle(rightEqSlotX, contentY + (int)(50 * GameCore.SCALE) + (slotSize + 10), slotSize, slotSize);
        acc2Slot     = new Rectangle(rightEqSlotX, contentY + (int)(50 * GameCore.SCALE) + ((slotSize + 10) * 2), slotSize, slotSize);

        // Init Rectangle Layout untuk Inventory (4 Baris x 5 Kolom)
        int slotXStart = contentX + (int)(20 * GameCore.SCALE);
        int slotYStart = contentY + (int)(20 * GameCore.SCALE);
        
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                inventoryGrid[index] = new Rectangle(
                    slotXStart + (slotSize * j), 
                    slotYStart + (slotSize * i), 
                    slotSize, slotSize
                );
                index++;
            }
        }
    }

    public void resetMenu() {
        currentTab = -1;
        selectedSlot = -1;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // --- 1. GAMBAR SIDEBAR ---
        int sidebarX = (int) (50 * GameCore.SCALE);
        int sidebarY = (int) (100 * GameCore.SCALE);
        int sidebarW = (int) (140 * GameCore.SCALE);
        int sidebarH = (int) (220 * GameCore.SCALE);

        g2.setColor(Color.BLACK);
        g2.fillRect(sidebarX, sidebarY, sidebarW, sidebarH);
        g2.setColor(Color.WHITE);
        g2.drawRect(sidebarX, sidebarY, sidebarW, sidebarH);

        // Teks Menu Sidebar
        g2.setFont(new Font("Arial", Font.BOLD, (int)(18 * GameCore.SCALE)));
        for (int i = 0; i < tabNames.length; i++) {
            int tabY = sidebarY + (int)(20 * GameCore.SCALE) + (i * (int)(50 * GameCore.SCALE));
            g2.setColor(i == currentTab ? Color.YELLOW : Color.WHITE);
            g2.drawString(tabNames[i], sidebarX + (int)(30 * GameCore.SCALE), tabY + (int)(25 * GameCore.SCALE));
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, (int)(14 * GameCore.SCALE)));
        g2.drawString("Keluar tekan F", sidebarX + (int)(10 * GameCore.SCALE), sidebarY + sidebarH + (int)(25 * GameCore.SCALE));

        // --- 2. GAMBAR PANEL KONTEN ---
        if (currentTab != -1) {
            int contentX = sidebarX + sidebarW + (int)(15 * GameCore.SCALE);
            int contentY = sidebarY;
            int contentW = (int) (320 * GameCore.SCALE);
            int contentH = (int) (220 * GameCore.SCALE);

            g2.setColor(Color.BLACK);
            g2.fillRect(contentX, contentY, contentW, contentH);
            g2.setColor(Color.WHITE);
            g2.drawRect(contentX, contentY, contentW, contentH);

            if (currentTab == 0) {
                drawKarakterTab(g2, contentX, contentY);
            } else if (currentTab == 1) {
                drawInventory(g2, contentX, contentY);
            }
        }
    }

    private void drawKarakterTab(Graphics2D g2, int contentX, int contentY) {
        g2.setFont(new Font("Arial", Font.BOLD, (int)(14 * GameCore.SCALE)));
        g2.setColor(Color.WHITE);
        
        // Menampilkan Total Defense Player
        g2.drawString("EQUIPMENT & STATUS", contentX + (int)(20 * GameCore.SCALE), contentY + (int)(30 * GameCore.SCALE));
        g2.setColor(Color.GREEN);
        g2.drawString("Defense Saat ini : " + playing.getPlayer().getTotalDefense(), contentX + (int)(20 * GameCore.SCALE), contentY + (int)(50 * GameCore.SCALE));

        // Gambar Kotak + Labelnya
        drawSingleEquipmentBox(g2, helmetSlot, "Head", playing.getPlayer().getEquippedHelmet());
        drawSingleEquipmentBox(g2, armorSlot, "Body", playing.getPlayer().getEquippedArmor());
        
        // --- INI BAGIAN YANG DIPERBAIKI ---
        drawSingleEquipmentBox(g2, shoesSlot, "Feet", playing.getPlayer().getEquippedShoes()); 
        
        drawSingleEquipmentBox(g2, glovesSlot, "Hands", playing.getPlayer().getEquippedGloves());
        drawSingleEquipmentBox(g2, acc1Slot, "Accessory 1", playing.getPlayer().getEquippedAcc1());
        drawSingleEquipmentBox(g2, acc2Slot, "Accessory 2", playing.getPlayer().getEquippedAcc2());
    }

    private void drawSingleEquipmentBox(Graphics2D g2, Rectangle slotBox, String label, int itemTypeID) {
        // Kotak Dasar
        g2.setColor(Color.GRAY);
        g2.drawRect(slotBox.x, slotBox.y, slotBox.width, slotBox.height);
        
        // Teks Label
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, (int)(10 * GameCore.SCALE)));
        g2.drawString(label, slotBox.x, slotBox.y - 2);

        // Gambar Jika Dipakai
        if (itemTypeID != -1) {
            BufferedImage img = playing.getObjectManager().getItemImg(itemTypeID);
            if (img != null) {
                g2.drawImage(img, slotBox.x + 5, slotBox.y + 5, slotBox.width - 10, slotBox.height - 10, null);
            }
        }
    }

    private void drawInventory(Graphics2D g2, int contentX, int contentY) {
        int slotSize = (int) (45 * GameCore.SCALE);
        int slotXStart = contentX + (int)(20 * GameCore.SCALE);
        int slotYStart = contentY + (int)(20 * GameCore.SCALE);

        for (int i = 0; i < playing.getPlayer().inventory.size(); i++) {
            Rectangle box = inventoryGrid[i];
            
            // Background Slot
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(box.x, box.y, box.width, box.height);
            g2.setColor(Color.GRAY);
            g2.drawRect(box.x, box.y, box.width, box.height);

            int objType = playing.getPlayer().inventory.get(i);
            BufferedImage img = playing.getObjectManager().getItemImg(objType);
            if (img != null) {
                g2.drawImage(img, box.x + 5, box.y + 5, box.width - 10, box.height - 10, null);
            }

            if (selectedSlot == i) {
                int btnY = box.y + slotSize + 2;
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.setColor(Color.GREEN);
                g2.drawRect(box.x, btnY, 20, 12);
                g2.drawString("Use", box.x + 2, btnY + 10);
                
                g2.setColor(Color.RED);
                g2.drawRect(box.x + 25, btnY, 20, 12);
                g2.drawString("X", box.x + 30, btnY + 10);
            }
        }

        // Kursor Kuning Terkunci pada Target
        int targetSlot = (selectedSlot != -1) ? selectedSlot : hoveredSlot;
        if (targetSlot != -1 && targetSlot < playing.getPlayer().inventory.size()) {
            Rectangle hlBox = inventoryGrid[targetSlot];
            g2.setColor(Color.YELLOW);
            g2.drawRect(hlBox.x, hlBox.y, hlBox.width, hlBox.height);
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        if (selectedSlot != -1) return;

        int mx = e.getX();
        int my = e.getY();
        hoveredTab = -1;
        hoveredSlot = -1;

        int sidebarX = (int) (50 * GameCore.SCALE);
        int sidebarY = (int) (100 * GameCore.SCALE);
        int sidebarW = (int) (140 * GameCore.SCALE);

        // Hover Tab Selection
        for (int i = 0; i < tabNames.length; i++) {
            int tabY = sidebarY + (int)(20 * GameCore.SCALE) + (i * (int)(50 * GameCore.SCALE));
            if (mx >= sidebarX && mx <= sidebarX + sidebarW && my >= tabY && my <= tabY + (int)(40 * GameCore.SCALE)) {
                hoveredTab = i;
            }
        }

        // Hover Item Slots
        if (currentTab == 1) {
            for (int i = 0; i < playing.getPlayer().maxInventorySize; i++) {
                if (inventoryGrid[i].contains(mx, my)) {
                    hoveredSlot = i;
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int mx = e.getX();
            int my = e.getY();

            // Pindah Tab Menu
            if (hoveredTab != -1) {
                currentTab = (currentTab == hoveredTab) ? -1 : hoveredTab;
                selectedSlot = -1;
            } 
            // Konfirmasi Use atau Batalkan Item
            else if (currentTab == 1 && selectedSlot != -1) {
                int slotSize = (int) (45 * GameCore.SCALE);
                Rectangle box = inventoryGrid[selectedSlot];
                int btnY = box.y + slotSize + 2;

                // Tombol hijau "Use/Equip"
                if (mx >= box.x && mx <= box.x + 20 && my >= btnY && my <= btnY + 12) {
                    
                    int itemID = playing.getPlayer().inventory.get(selectedSlot);
                    
                    // Filter: Potion Atau Armor?
                    if (itemID >= 0 && itemID <= 5) {
                        playing.getPlayer().useItem(selectedSlot); // Heal Player
                    } else {
                        // Memanggil konstanta database asli (RING dan SACK)
                        String slotTarget = "";
                        switch(itemID) {
                            case ObjectConstants.HELMET: slotTarget = "head"; break;
                            case ObjectConstants.ARMOR: slotTarget = "body"; break;
                            case ObjectConstants.GLOVES: slotTarget = "hands"; break;
                            case ObjectConstants.SHOES: slotTarget = "shoes"; break;
                            case ObjectConstants.RING: slotTarget = "accessory1"; break;
                            case ObjectConstants.SACK: slotTarget = "accessory2"; break;
                        }
                        if (!slotTarget.isEmpty()) {
                            playing.equipPlayerItem(selectedSlot, slotTarget);
                        }
                    }
                    selectedSlot = -1;
                    
                } 
                // Tombol Merah Batal "X"
                else if (mx >= box.x + 25 && mx <= box.x + 45 && my >= btnY && my <= btnY + 12) {
                    selectedSlot = -1;
                }
            } 
            // Pilih Kotak Item untuk dipakai
            else if (currentTab == 1 && hoveredSlot != -1 && hoveredSlot < playing.getPlayer().inventory.size()) {
                selectedSlot = hoveredSlot;
            }
        }
    }
}