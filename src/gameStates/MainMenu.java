package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.GameCore;
import utilitytools.LoadSave;

public class MainMenu extends States implements StateMethods {

    // Aset Gambar Terpisah
    private BufferedImage backgroundImage; 
    private BufferedImage panelImage; 
    
    private int panelX, panelY, panelWidth, panelHeight;

    // Hitbox untuk 4 Tombol
    private Rectangle startBtn, continueBtn, optionsBtn, exitBtn;
    
    // Status Interaksi (Hover & Pressed)
    private boolean startHover, startPressed;
    private boolean continueHover, continuePressed;
    private boolean optionsHover, optionsPressed;
    private boolean exitHover, exitPressed;

    public MainMenu(GameCore gc) {
        super(gc);
        loadAsetUI(); 
        initLayoutMenu(); 
    }

    private void loadAsetUI() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND); 
        // Menggunakan panel kayu yang baru
        panelImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_PANEL); 
    }

    private void initLayoutMenu() {
        // Ukuran panel kayumu yang baru
        panelWidth = (int) (500 * GameCore.SCALE); 
        panelHeight = (int) (300 * GameCore.SCALE); 
        panelX = GameCore.GAME_WIDTH / 2 - panelWidth / 2;
        panelY = (int) (160 * GameCore.SCALE); 

        // --- PERBAIKAN KOTAK HITIH (HITBOX) ---
        
        // 1. LEBAR & TINGGI KOTAK
        // Dikecilkan jadi 0.30 (30%) agar seukuran teks, tidak tumpah keluar kayu
        int btnWidth = (int)(panelWidth * 0.30); 
        int btnHeight = (int) (30 * GameCore.SCALE); // Tinggi kotak
        int btnX = GameCore.GAME_WIDTH / 2 - btnWidth / 2;

        // 2. POSISI AWAL & JARAK (SPACING)
        // Ubah angka 100 agar kotak pertama pas di teks "START"
        int startY = panelY + (int)(105 * GameCore.SCALE); 
        
        // Ubah angka 40 agar jarak jatuhnya pas ke teks "CONTINUE", "OPTIONS", dst
        int spacing = (int)(31 * GameCore.SCALE); 

        startBtn = new Rectangle(btnX, startY, btnWidth, btnHeight);
        continueBtn = new Rectangle(btnX, startY + spacing, btnWidth, btnHeight);
        optionsBtn = new Rectangle(btnX, startY + (spacing * 2), btnWidth, btnHeight);
        exitBtn = new Rectangle(btnX, startY + (spacing * 3), btnWidth, btnHeight);
    }

    @Override
    public void update() {
        // Logika animasi panel (jika ada) bisa ditaruh di sini nanti
    }

    @Override
    public void draw(Graphics g) {
        // 1. Gambar Latar Belakang (Paling Bawah)
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
        }

        // 2. Gambar Panel Kayu (Di Atas Latar)
        if (panelImage != null) {
            g.drawImage(panelImage, panelX, panelY, panelWidth, panelHeight, null);
        } else {
            g.setColor(new Color(60, 40, 20)); // Warna darurat jika gambar belum dimuat
            g.fillRect(panelX, panelY, panelWidth, panelHeight);
        }
        
        // 3. PANGGIL METHOD HIGHLIGHT HITBOX DI SINI
        drawButtonHighlight(g, startBtn, startHover, startPressed);
        drawButtonHighlight(g, continueBtn, continueHover, continuePressed);
        drawButtonHighlight(g, optionsBtn, optionsHover, optionsPressed);
        drawButtonHighlight(g, exitBtn, exitHover, exitPressed);
    }
        
    private void drawButtonHighlight(Graphics g, Rectangle bounds, boolean isHover, boolean isPressed) {
        // g.setColor(Color.RED);
        // g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Efek visual kotak terang/gelap dengan SUDUT MEMBULAT
        if (isPressed) {
            g.setColor(new Color(0, 0, 0, 100)); // Hitam transparan saat diklik
            // Menggunakan fillRoundRect dengan lengkungan 15 pixel
            g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        } else if (isHover) {
            g.setColor(new Color(255, 255, 255, 50)); // Putih transparan saat di-hover
            g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        }
    }

    // Method untuk menggambar teks tombol dibiarkan saja tapi tidak dipanggil agar tidak error
    private void drawMenuText(Graphics g, Rectangle bounds, String text, boolean isHover, boolean isPressed) {
        g.setFont(new Font("Arial", Font.BOLD, (int)(24 * GameCore.SCALE)));
        int stringWidth = g.getFontMetrics().stringWidth(text);
        int stringHeight = g.getFontMetrics().getHeight();
        int drawX = bounds.x + (bounds.width - stringWidth) / 2;
        int drawY = bounds.y + (bounds.height + stringHeight) / 2 - 5;

        if (isPressed) {
            g.setColor(new Color(150, 50, 50)); 
            g.drawString(text, drawX, drawY + 2); 
        } else if (isHover) {
            g.setColor(new Color(255, 215, 0)); 
            g.drawString(text, drawX, drawY);
            g.setColor(new Color(255, 255, 200, 100));
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height); 
        } else {
            g.setColor(new Color(200, 200, 200)); 
            g.drawString(text, drawX, drawY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        startHover = startBtn.contains(e.getX(), e.getY());
        continueHover = continueBtn.contains(e.getX(), e.getY());
        optionsHover = optionsBtn.contains(e.getX(), e.getY());
        exitHover = exitBtn.contains(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (startHover) startPressed = true;
        if (continueHover) continuePressed = true;
        if (optionsHover) optionsPressed = true;
        if (exitHover) exitPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (startHover && startPressed) {
            GameStates.state = GameStates.PLAYING;
            gc.getAudioPlayer().playSong(audio.AudioPlayer.LEVEL_1);
        } else if (continueHover && continuePressed) {
            System.out.println("Continue Game...");
        } else if (optionsHover && optionsPressed) {
            GameStates.state = GameStates.OPTIONS;
        } else if (exitHover && exitPressed) {
            System.exit(0); 
        }
        resetButtons();
    }

    private void resetButtons() {
        startHover = continueHover = optionsHover = exitHover = false;
        startPressed = continuePressed = optionsPressed = exitPressed = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

	@Override
	public void triggerHeavyHit(int freezeFrames, int shakeFrames, int intensity) {
		// TODO Auto-generated method stub
		
	}
}