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

public class GameOptions extends States implements StateMethods {

    private BufferedImage backgroundImage;
    private int bgX, bgY, bgW, bgH;
    
    // Hitbox untuk tombol BACK
    private Rectangle backBtn;
    private boolean backHover, backPressed;

    private static final Color COLOR_DARK_BACKUP = new Color(30, 30, 30);
    private static final Color COLOR_BTN_PRESSED = new Color(150, 50, 50);
    private static final Color COLOR_BTN_HOVER = new Color(255, 215, 0);

    private final Font titleFont = new Font("Arial", Font.BOLD, (int)(36 * GameCore.SCALE));
    private final Font subtitleFont = new Font("Arial", Font.PLAIN, (int)(16 * GameCore.SCALE));
    private final Font buttonFont = new Font("Arial", Font.BOLD, (int)(24 * GameCore.SCALE));

    public GameOptions(GameCore gc) {
        super(gc);
        loadBackground();
        initButton();
    }

    private void loadBackground() {
        // Menggunakan background menu utama sebagai latar Options
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        bgW = GameCore.GAME_WIDTH;
        bgH = GameCore.GAME_HEIGHT;
        bgX = 0;
        bgY = 0;
    }

    private void initButton() {
        int btnWidth = (int) (150 * GameCore.SCALE);
        int btnHeight = (int) (40 * GameCore.SCALE);
        int btnX = GameCore.GAME_WIDTH / 2 - btnWidth / 2;
        int btnY = (int) (300 * GameCore.SCALE); // Posisi tombol di bagian bawah

        backBtn = new Rectangle(btnX, btnY, btnWidth, btnHeight);
    }

    @Override
    public void update() {
        // Tempat logika update jika nanti ada slider volume
    }

    @Override
    public void draw(Graphics g) {
        // 1. Gambar Background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, bgX, bgY, bgW, bgH, null);
        } else {
            g.setColor(COLOR_DARK_BACKUP); // Warna gelap cadangan
            g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
        }

        // 2. Teks Judul Layar
        g.setColor(Color.WHITE);
        g.setFont(titleFont);
        String title = "OPTIONS MENU";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, GameCore.GAME_WIDTH / 2 - titleWidth / 2, (int)(100 * GameCore.SCALE));

        // 3. Teks Keterangan (Sementara)
        g.setFont(subtitleFont);
        String subtitle = "Pengaturan Audio & Kontrol dalam tahap pengembangan.";
        int subWidth = g.getFontMetrics().stringWidth(subtitle);
        g.drawString(subtitle, GameCore.GAME_WIDTH / 2 - subWidth / 2, (int)(150 * GameCore.SCALE));

        // 4. Gambar Tombol BACK
        drawTransparentButton(g, backBtn, "BACK", backHover, backPressed);
    }

    private void drawTransparentButton(Graphics g, Rectangle bounds, String text, boolean isHover, boolean isPressed) {
        // Efek warna saat ditekan/di-hover
        if (isPressed) {
            g.setColor(COLOR_BTN_PRESSED);  // Merah gelap
        } else if (isHover) {
            g.setColor(COLOR_BTN_HOVER);    // Emas
        } else {
            g.setColor(Color.LIGHT_GRAY);   // Normal
        }

        g.setFont(buttonFont);
        int stringWidth = g.getFontMetrics().stringWidth(text);
        int stringHeight = g.getFontMetrics().getHeight();
        
        g.drawString(text,
                     bounds.x + (bounds.width - stringWidth) / 2,
                     bounds.y + (bounds.height + stringHeight) / 2 - 5);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        backHover = backBtn.contains(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (backHover) backPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (backHover && backPressed) {
            // [AKSI KEMBALI KE MENU]
            GameStates.state = GameStates.MENU;
        }
        resetButtons();
    }

    private void resetButtons() {
        backHover = false;
        backPressed = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // Tombol ESC juga bisa dipakai untuk kembali ke Menu
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameStates.state = GameStates.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}