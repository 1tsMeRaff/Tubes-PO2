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

    private BufferedImage backgroundImage;
    private int menuX, menuY, menuWidth, menuHeight;

    // Hitbox area tombol (tetap ada untuk deteksi klik, tapi tidak digambar)
    private Rectangle playBtn, optionsBtn;
    private boolean playHover, playPressed;
    private boolean optionsHover, optionsPressed;

    public MainMenu(GameCore gc) {
        super(gc);
        loadBackground();
        initButtonsVertically();
    }

    private void loadBackground() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImage.getWidth() * GameCore.SCALE);
        menuHeight = (int) (backgroundImage.getHeight() * GameCore.SCALE);
        menuX = GameCore.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (120 * GameCore.SCALE);
    }

    private void initButtonsVertically() {
        double frameLeftPercent = 0.30;   // dari kiri background
        double frameWidthPercent = 0.40;  // lebar frame
        double frameTopPercent = 0.20;    // dari atas background
        double frameHeightPercent = 0.60; // tinggi frame

        int frameX = menuX + (int) (menuWidth * frameLeftPercent);
        int frameY = menuY + (int) (menuHeight * frameTopPercent);
        int frameWidth = (int) (menuWidth * frameWidthPercent);
        int frameHeight = (int) (menuHeight * frameHeightPercent);

  
        int btnWidth = (int) (frameWidth * 0.6);
        int btnHeight = (int) (40 * GameCore.SCALE);

        // Posisi X tombol (tengah frame)
        int btnX = frameX + (frameWidth - btnWidth) / 2;

        // Y: Start di 1/3 tinggi frame, Options di 2/3 tinggi frame
        int startY = frameY + (frameHeight / 3) - (btnHeight / 2);
        int optionsY = frameY + (2 * frameHeight / 3) - (btnHeight / 2);

        playBtn = new Rectangle(btnX, startY, btnWidth, btnHeight);
        optionsBtn = new Rectangle(btnX, optionsY, btnWidth, btnHeight);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        // Gambar background
        g.drawImage(backgroundImage, menuX, menuY, menuWidth, menuHeight, null);

        // Gambar tombol dengan teks tanpa kotak (transparan)
        drawTransparentButton(g, playBtn, "START", playHover, playPressed);
        drawTransparentButton(g, optionsBtn, "OPTIONS", optionsHover, optionsPressed);
    }

    private void drawTransparentButton(Graphics g, Rectangle bounds, String text, boolean isHover, boolean isPressed) {

        // Warna teks berdasarkan status
        if (isPressed) {
            g.setColor(new Color(150, 50, 50));  // merah gelap saat ditekan
        } else if (isHover) {
            g.setColor(new Color(255, 215, 0));  // emas saat di-hover
        } else {
            g.setColor(Color.LIGHT_GRAY);        // normal
        }

        // Font
        g.setFont(new Font("Arial", Font.BOLD, (int)(24 * GameCore.SCALE)));

        // Gambar teks di tengah rectangle
        int stringWidth = g.getFontMetrics().stringWidth(text);
        int stringHeight = g.getFontMetrics().getHeight();
        g.drawString(text,
                     bounds.x + (bounds.width - stringWidth) / 2,
                     bounds.y + (bounds.height + stringHeight) / 2 - 5);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        playHover = playBtn.contains(e.getX(), e.getY());
        optionsHover = optionsBtn.contains(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (playHover) playPressed = true;
        if (optionsHover) optionsPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (playHover && playPressed) {
            GameStates.state = GameStates.PLAYING;
        } else if (optionsHover && optionsPressed) {
            GameStates.state = GameStates.OPTIONS;
        }
        resetButtons();
    }

    private void resetButtons() {
        playHover = false;
        playPressed = false;
        optionsHover = false;
        optionsPressed = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameStates.state = GameStates.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}