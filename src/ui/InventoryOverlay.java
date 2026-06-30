package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;

public class InventoryOverlay {

    private PlayStates playing;
    private BufferedImage bgInventory;
    
    private int panelX, panelY, panelW, panelH;

    // Variabel untuk Popup Konfirmasi
    private boolean showConfirmPopup = false;
    private int selectedItemIndex = -1;
    private boolean isEquipSlotSelected = false; // Penanda apakah yg diklik tas atau perlengkapan
    private int popupX, popupY;
    private java.awt.Rectangle btnGunakan, btnBatal;

    public InventoryOverlay(PlayStates playing) {
        this.playing = playing;
        loadImages();
        initLayout();
    }

    private void loadImages() {
        bgInventory = LoadSave.GetSpriteAtlas(LoadSave.INVENTORY_BG);
    }

    private void initLayout() {
        float customScale = 0.80f; 
        panelW = (int) (490 * GameCore.SCALE * customScale); 
        panelH = (int) (560 * GameCore.SCALE * customScale); 
        panelX = GameCore.GAME_WIDTH / 2 - panelW / 2;
        panelY = GameCore.GAME_HEIGHT / 2 - panelH / 2;
    }

    public void update() {}

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

        if (bgInventory != null) {
            g2.drawImage(bgInventory, panelX, panelY, panelW, panelH, null);
            
            // --- 1. MENGGAMBAR TEKS ANGKA STATUS ---
            g2.setColor(new Color(230, 230, 230)); 
            float customScale = 0.80f;
            g2.setFont(new Font("Monospaced", Font.BOLD, (int)(14 * GameCore.SCALE * customScale)));

            int textX = panelX + (int)(225 * GameCore.SCALE * customScale);
            int startY = panelY + (int)(125 * GameCore.SCALE * customScale);
            int gapY = (int)(18 * GameCore.SCALE * customScale);
            
            entity.Player p = playing.getPlayer();
            java.awt.FontMetrics fm = g2.getFontMetrics(); 

            g2.drawString(String.valueOf(p.getLevel()), textX - fm.stringWidth(String.valueOf(p.getLevel())), startY);
            g2.drawString(p.getExp() + " / " + p.getMaxExp(), textX - fm.stringWidth(p.getExp() + " / " + p.getMaxExp()), startY + gapY);
            g2.drawString(p.getCurrentHealth() + " / " + p.getMaxHealth(), textX - fm.stringWidth(p.getCurrentHealth() + " / " + p.getMaxHealth()), startY + gapY * 2);
            g2.drawString(p.getCurrentMana() + " / " + p.getMaxMana(), textX - fm.stringWidth(p.getCurrentMana() + " / " + p.getMaxMana()), startY + gapY * 3);
            g2.drawString(String.valueOf(p.getTotalDefense()), textX - fm.stringWidth(String.valueOf(p.getTotalDefense())), startY + gapY * 4);
            g2.drawString(String.format("%.1f", p.getDps()), textX - fm.stringWidth(String.format("%.1f", p.getDps())), startY + gapY * 5);

            objects.ObjectManager objManager = playing.getObjectManager();

            // ==========================================================
            // --- 2. MENGGAMBAR PERLENGKAPAN YANG DIPAKAI (6 KOTAK ATAS) ---
            // ==========================================================
            int eqCols = 3; 
            int eqGridX = panelX + (int)(272 * GameCore.SCALE * customScale); 
            int eqGridY = panelY + (int)(136 * GameCore.SCALE * customScale); 
            int eqDistX = (int)(33 * GameCore.SCALE * customScale); 
            int eqDistY = (int)(35 * GameCore.SCALE * customScale); 
            int eqItemSize = (int)(22 * GameCore.SCALE * customScale); 
            
            int offEqX = (eqDistX - eqItemSize) / 2;
            int offEqY = (eqDistY - eqItemSize) / 2;

            int[] equippedItems = {
                p.getEquippedHelmet(), p.getEquippedArmor(), p.getEquippedShoes(),
                p.getEquippedAcc1(), p.getEquippedAcc2(), p.getEquippedGloves()
            };

            for (int i = 0; i < 6; i++) {
                if (equippedItems[i] != -1) {
                    BufferedImage eqImg = objManager.getItemImg(equippedItems[i]);
                    if (eqImg != null) {
                        int drawX = eqGridX + ((i % eqCols) * eqDistX) + offEqX;
                        int drawY = eqGridY + ((i / eqCols) * eqDistY) + offEqY;
                        g2.drawImage(eqImg, drawX, drawY, eqItemSize, eqItemSize, null);
                    }
                }
            }

            // ==========================================================
            // --- 3. MENGGAMBAR ISI TAS INVENTARIS (24 KOTAK BAWAH) ---
            // ==========================================================
            java.util.ArrayList<Integer> inv = p.inventory;
            int cols = 6; 
            int startGridX = panelX + (int)(262 * GameCore.SCALE * customScale); 
            int startGridY = panelY + (int)(242 * GameCore.SCALE * customScale); 
            int slotDistanceX = (int)(33 * GameCore.SCALE * customScale); 
            int slotDistanceY = (int)(35 * GameCore.SCALE * customScale); 
            int itemSize = (int)(22 * GameCore.SCALE * customScale); 
            
            int offsetX = (slotDistanceX - itemSize) / 2;
            int offsetY = (slotDistanceY - itemSize) / 2;

            for (int i = 0; i < inv.size(); i++) {
                BufferedImage itemImg = objManager.getItemImg(inv.get(i));
                if (itemImg != null) {
                    int drawX = startGridX + ((i % cols) * slotDistanceX) + offsetX;
                    int drawY = startGridY + ((i / cols) * slotDistanceY) + offsetY;
                    g2.drawImage(itemImg, drawX, drawY, itemSize, itemSize, null);
                }
            }

            // ==========================================================
            // --- 4. POPUP KONFIRMASI (GUNAKAN / PAKAI / LEPAS) ---
            // ==========================================================
            if (showConfirmPopup) {
                int popW = (int)(65 * GameCore.SCALE * customScale);
                int popH = (int)(40 * GameCore.SCALE * customScale);
                int btnH = popH / 2;
                
                g2.setColor(new Color(50, 30, 20, 240)); 
                g2.fillRect(popupX, popupY, popW, popH);
                g2.setColor(new Color(240, 190, 90)); 
                g2.setStroke(new java.awt.BasicStroke(2));
                g2.drawRect(popupX, popupY, popW, popH);
                g2.setColor(new Color(150, 100, 50));
                g2.drawLine(popupX, popupY + btnH, popupX + popW, popupY + btnH);
                
                btnGunakan = new java.awt.Rectangle(popupX, popupY, popW, btnH);
                btnBatal = new java.awt.Rectangle(popupX, popupY + btnH, popW, btnH);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Monospaced", Font.BOLD, (int)(11 * GameCore.SCALE * customScale)));
                java.awt.FontMetrics fmPop = g2.getFontMetrics();
                
                String txt1 = "Gunakan";
                if (isEquipSlotSelected) {
                    txt1 = "Lepaskan"; 
                } else {
                    int typeId = p.inventory.get(selectedItemIndex);
                    if (typeId >= 10 && typeId <= 15) {
                        txt1 = "Pakai"; 
                    }
                }
                String txt2 = "Batal";
                
                g2.drawString(txt1, popupX + (popW - fmPop.stringWidth(txt1)) / 2, popupY + (btnH + fmPop.getAscent()) / 2 - 2);
                g2.drawString(txt2, popupX + (popW - fmPop.stringWidth(txt2)) / 2, popupY + btnH + (btnH + fmPop.getAscent()) / 2 - 2);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        
        // 1. CEK KLIK POPUP TERLEBIH DAHULU
        if (showConfirmPopup) {
            if (btnGunakan != null && btnGunakan.contains(mx, my)) {
                if (isEquipSlotSelected) {
                    playing.getPlayer().unequipItem(selectedItemIndex); // Melepas
                } else {
                    int itemType = playing.getPlayer().inventory.get(selectedItemIndex);
                    if (itemType >= 0 && itemType <= 5) {
                        playing.getPlayer().useItem(selectedItemIndex); // Minum Potion
                        
                        playing.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.POTION);
                    } else if (itemType >= 10 && itemType <= 15) {
                        String tipePart = "";
                        if (itemType == 10) tipePart = "head";
                        else if (itemType == 11) tipePart = "body";
                        else if (itemType == 12) tipePart = "shoes";
                        else if (itemType == 13) tipePart = "accessory1";
                        else if (itemType == 14) tipePart = "accessory2";
                        else if (itemType == 15) tipePart = "hands";
                        playing.getPlayer().equipItem(selectedItemIndex, tipePart); // Pakai Armor
                    }
                }
                playing.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.JUMP); 
                showConfirmPopup = false; 
            } else if (btnBatal != null && btnBatal.contains(mx, my)) {
                showConfirmPopup = false; 
            } else {
                showConfirmPopup = false; 
            }
            return; 
        }
        
        float customScale = 0.80f;
        int slotDistX = (int)(33 * GameCore.SCALE * customScale); 
        int slotDistY = (int)(35 * GameCore.SCALE * customScale); 
        int itemSize = (int)(22 * GameCore.SCALE * customScale); 
        int offsetX = (slotDistX - itemSize) / 2;
        int offsetY = (slotDistY - itemSize) / 2;
        entity.Player p = playing.getPlayer();

        // 2. CEK KLIK DI KOTAK PERLENGKAPAN
        int eqGridX = panelX + (int)(272 * GameCore.SCALE * customScale); 
        int eqGridY = panelY + (int)(136 * GameCore.SCALE * customScale); 
        int[] equippedItems = { p.getEquippedHelmet(), p.getEquippedArmor(), p.getEquippedShoes(), p.getEquippedAcc1(), p.getEquippedAcc2(), p.getEquippedGloves() };

        for (int i = 0; i < 6; i++) {
            int drawX = eqGridX + ((i % 3) * slotDistX) + offsetX;
            int drawY = eqGridY + ((i / 3) * slotDistY) + offsetY;
            java.awt.Rectangle slot = new java.awt.Rectangle(drawX, drawY, itemSize, itemSize);
            
            if (slot.contains(mx, my) && equippedItems[i] != -1) {
                selectedItemIndex = i;
                isEquipSlotSelected = true;
                showConfirmPopup = true;
                popupX = drawX + (itemSize / 2); 
                popupY = drawY + itemSize; 
                return;
            }
        }

        // 3. CEK KLIK DI KOTAK INVENTARIS
        int startGridX = panelX + (int)(262 * GameCore.SCALE * customScale); 
        int startGridY = panelY + (int)(242 * GameCore.SCALE * customScale); 
        
        for (int i = 0; i < 24; i++) {
            int drawX = startGridX + ((i % 6) * slotDistX) + offsetX;
            int drawY = startGridY + ((i / 6) * slotDistY) + offsetY;
            java.awt.Rectangle slot = new java.awt.Rectangle(drawX, drawY, itemSize, itemSize);
            
            if (slot.contains(mx, my) && i < p.inventory.size()) {
                selectedItemIndex = i;
                isEquipSlotSelected = false; 
                showConfirmPopup = true;
                popupX = drawX + (itemSize / 2); 
                popupY = drawY + itemSize; 
                return; 
            }
        }
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void resetMenu() {}
}