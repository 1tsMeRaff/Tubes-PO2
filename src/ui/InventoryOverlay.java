package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gameStates.PlayStates;
import main.GameCore;

public class InventoryOverlay {
    private PlayStates playing;

    // State Navigasi
    private int currentTab = -1; 
    private String[] tabNames = {"Karakter", "Inventory", "Skill", ""};

    private int hoveredTab = -1;
    private int hoveredSlot = -1;
    private int selectedSlot = -1; // Slot yang menunggu konfirmasi (dikunci)

    public InventoryOverlay(PlayStates playing) {
        this.playing = playing;
    }

    public void resetMenu() {
        currentTab = -1;
        selectedSlot = -1;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // --- 1. GAMBAR SIDEBAR (HITAM PEKAT) ---
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

            if (currentTab == 1) {
                drawInventory(g2, contentX, contentY);
            }
        }
    }

    private void drawInventory(Graphics2D g2, int contentX, int contentY) {
        int slotSize = (int) (45 * GameCore.SCALE);
        int slotXStart = contentX + (int)(20 * GameCore.SCALE);
        int slotYStart = contentY + (int)(20 * GameCore.SCALE);

        for (int i = 0; i < playing.getPlayer().inventory.size(); i++) {
            int itemX = slotXStart + (slotSize * (i % 5));
            int itemY = slotYStart + (slotSize * (i / 5));

            int objType = playing.getPlayer().inventory.get(i);
            BufferedImage img = playing.getObjectManager().getPotionImg(objType);
            if (img != null) g2.drawImage(img, itemX + 5, itemY + 5, slotSize - 10, slotSize - 10, null);

            if (selectedSlot == i) {
                int btnY = itemY + slotSize + 2;
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.setColor(Color.GREEN);
                g2.drawRect(itemX, btnY, 20, 12);
                g2.drawString("Ya", itemX + 3, btnY + 10);
                g2.setColor(Color.RED);
                g2.drawRect(itemX + 25, btnY, 20, 12);
                g2.drawString("Tdk", itemX + 26, btnY + 10);
            }
        }

        // Kursor Kuning Terkunci pada Target (Pilih selectedSlot, jika tidak ada baru hoveredSlot)
        int targetSlot = (selectedSlot != -1) ? selectedSlot : hoveredSlot;
        if (targetSlot != -1) {
            int hX = slotXStart + (slotSize * (targetSlot % 5));
            int hY = slotYStart + (slotSize * (targetSlot / 5));
            g2.setColor(Color.YELLOW);
            g2.drawRect(hX, hY, slotSize, slotSize);
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        // Jika sudah dipilih, jangan ubah hoveredSlot agar kursor mengunci
        if (selectedSlot != -1) return;

        int mx = e.getX();
        int my = e.getY();
        hoveredTab = -1;
        hoveredSlot = -1;
        
        // ... (Logika deteksi hover tetap sama) ...
        int sidebarX = (int) (50 * GameCore.SCALE);
        int sidebarY = (int) (100 * GameCore.SCALE);
        int sidebarW = (int) (140 * GameCore.SCALE);
        for (int i = 0; i < tabNames.length; i++) {
            int tabY = sidebarY + (int)(20 * GameCore.SCALE) + (i * (int)(50 * GameCore.SCALE));
            if (mx >= sidebarX && mx <= sidebarX + sidebarW && my >= tabY && my <= tabY + (int)(40 * GameCore.SCALE)) hoveredTab = i;
        }

        if (currentTab == 1) {
            int slotSize = (int) (45 * GameCore.SCALE);
            int startX = sidebarX + (int)(155 * GameCore.SCALE) + (int)(20 * GameCore.SCALE);
            int startY = sidebarY + (int)(20 * GameCore.SCALE);
            for (int i = 0; i < playing.getPlayer().maxInventorySize; i++) {
                int ix = startX + (slotSize * (i % 5));
                int iy = startY + (slotSize * (i / 5));
                if (mx >= ix && mx <= ix + slotSize && my >= iy && my <= iy + slotSize) hoveredSlot = i;
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (hoveredTab != -1) {
                currentTab = (currentTab == hoveredTab) ? -1 : hoveredTab;
                selectedSlot = -1;
            } else if (currentTab == 1 && selectedSlot != -1) {
                int mx = e.getX();
                int my = e.getY();
                int slotSize = (int) (45 * GameCore.SCALE);
                int sx = (int)((50 + 140 + 15 + 20) * GameCore.SCALE) + (slotSize * (selectedSlot % 5));
                int sy = (int)((100 + 20) * GameCore.SCALE) + (slotSize * (selectedSlot / 5));
                int btnY = sy + slotSize + 2;

                if (mx >= sx && mx <= sx + 20 && my >= btnY && my <= btnY + 12) {
                    playing.getPlayer().useItem(selectedSlot);
                    selectedSlot = -1;
                } else if (mx >= sx + 25 && mx <= sx + 45 && my >= btnY && my <= btnY + 12) {
                    selectedSlot = -1;
                }
            } else if (currentTab == 1 && hoveredSlot != -1 && hoveredSlot < playing.getPlayer().inventory.size()) {
                selectedSlot = hoveredSlot;
            }
        }
    }
}