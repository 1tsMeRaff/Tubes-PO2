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
    
    // Variabel Ukuran & Posisi
    private int panelX, panelY, panelW, panelH;

    public InventoryOverlay(PlayStates playing) {
        this.playing = playing;
        loadImages();
        initLayout();
    }

    private void loadImages() {
        // Memanggil gambar UI barumu
        bgInventory = LoadSave.GetSpriteAtlas(LoadSave.INVENTORY_BG);
    }

    private void initLayout() {
        // --- KITA TAMBAHKAN SKALA KHUSUS DI SINI ---
        // 0.80f artinya kita menyusutkan gambar menjadi 80% dari ukuran sebelumnya.
        // Jika masih kurang kecil/kepotong, ubah jadi 0.75f atau 0.7f. 
        // Jika terlalu kecil, naikkan ke 0.9f.
        float customScale = 0.80f; 

        // Rasio asli 466 x 535 tetap terjaga, hanya ukurannya yang disesuaikan
        panelW = (int) (490 * GameCore.SCALE * customScale); 
        panelH = (int) (560 * GameCore.SCALE * customScale); 
        
        // Posisikan tepat di tengah layar
        panelX = GameCore.GAME_WIDTH / 2 - panelW / 2;
        panelY = GameCore.GAME_HEIGHT / 2 - panelH / 2;
    }

    public void update() {
        // Dikosongkan sementara
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // 1. Latar Belakang Gelap
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

        // 2. Gambar Papan Inventory
        if (bgInventory != null) {
            g2.drawImage(bgInventory, panelX, panelY, panelW, panelH, null);
            
         // --- 3. MENGGAMBAR TEKS ANGKA STATUS (RATA KANAN) ---
            g2.setColor(new Color(230, 230, 230)); // Warna teks putih terang
            float customScale = 0.80f; 
            g2.setFont(new Font("Monospaced", Font.BOLD, (int)(14 * GameCore.SCALE * customScale)));

            // Koordinat yang SUDAH DIKOREKSI:
            // textX ditambah nilainya agar geser lebih ke kanan menjauhi teks
            int textX = panelX + (int)(225 * GameCore.SCALE * customScale); 
            // startY dikurangi 18 agar naik persis 1 baris ke atas sejajar dengan "Level"
            int startY = panelY + (int)(125 * GameCore.SCALE * customScale); 
            // gapY tetap, karena jarak antar barisnya sudah pas
            int gapY = (int)(18 * GameCore.SCALE * customScale); 

            entity.Player p = playing.getPlayer();
            java.awt.FontMetrics fm = g2.getFontMetrics(); // Untuk fitur rata kanan

            // Baris 1: Level
            String lvlTxt = String.valueOf(p.getLevel());
            g2.drawString(lvlTxt, textX - fm.stringWidth(lvlTxt), startY);

            // Baris 2: EXP
            String expTxt = p.getExp() + " / " + p.getMaxExp();
            g2.drawString(expTxt, textX - fm.stringWidth(expTxt), startY + gapY);

            // Baris 3: HP
            String hpTxt = p.getCurrentHealth() + " / " + p.getMaxHealth();
            g2.drawString(hpTxt, textX - fm.stringWidth(hpTxt), startY + gapY * 2);

            // Baris 4: MP
            String mpTxt = p.getCurrentMana() + " / " + p.getMaxMana();
            g2.drawString(mpTxt, textX - fm.stringWidth(mpTxt), startY + gapY * 3);

            // Baris 5: Defense (Hanya menampilkan total defense saja)
            String defTxt = String.valueOf(p.getTotalDefense());
            g2.drawString(defTxt, textX - fm.stringWidth(defTxt), startY + gapY * 4);

            // Baris 6: DPS (Dibatasi 1 angka di belakang koma)
            String dpsTxt = String.format("%.1f", p.getDps());
            g2.drawString(dpsTxt, textX - fm.stringWidth(dpsTxt), startY + gapY * 5);
        }
    }

    public void mouseMoved(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void resetMenu() {}
}