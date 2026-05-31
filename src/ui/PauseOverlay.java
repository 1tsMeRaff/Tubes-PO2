package ui;

import gameStates.GameStates;
import gameStates.PlayStates;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import main.GameCore;
import static utilitytools.Konstanta.UI.PauseButtons.*;

public class PauseOverlay {
    private PlayStates playing;
    private int panelX;
    private int panelY;
    private int panelW;
    private int panelH;

    private SoundButton musicButton;
    private SoundButton sfxButton;
    private UrmButton menuButton;
    private UrmButton replayButton;
    private UrmButton unpauseButton;
    private VolumeButton volumeButton;

    public PauseOverlay(PlayStates playing) {
        this.playing = playing;
        initLayout();
    }

    private void initLayout() {
        int tile = GameCore.TILES_SIZE;
        
        // 1. PERBESAR PANEL (Lebar dan Tinggi ditambah secara signifikan)
        panelW = tile * 12; // Cukup lebar untuk menampung teks + panjang slider Kaarin
        panelH = tile * 11; // Cukup tinggi agar teks tombol bawah tidak tumpah
        panelX = GameCore.GAME_WIDTH / 2 - panelW / 2;
        panelY = GameCore.GAME_HEIGHT / 2 - panelH / 2;

        // X posisi untuk kolom tombol di sisi kanan
        int rightColX = panelX + panelW - (int)(tile * 1.8) - SOUND_SIZE;
        
        int startY = panelY + (int) (tile * 2.5);
        int itemDy = SOUND_SIZE + (int)(tile * 0.5); 

        // Baris 1: Music
        musicButton = new SoundButton(rightColX, startY, SOUND_SIZE, SOUND_SIZE);
        
        // Baris 2: SE (Sound Effects)
        int sfxY = startY + itemDy;
        sfxButton = new SoundButton(rightColX, sfxY, SOUND_SIZE, SOUND_SIZE);

        // Baris 3: Volume Slider
        int volumeY = sfxY + itemDy;

		int customSliderWidth = (int)(tile * 4.5);
        // Pastikan ujung kanan slider sejajar dengan sisi kanan tombol Music/SE
        int sliderX = rightColX + SOUND_SIZE - customSliderWidth; 
        
        volumeButton = new VolumeButton(sliderX, volumeY, VOLUME_WIDTH, VOLUME_HEIGHT, customSliderWidth, VOLUME_HEIGHT);
		
        // Baris 4: Tombol URM
        int gap = (int) (tile * 0.8);
        int totalW = URM_SIZE * 3 + gap * 2;
        int urmX = panelX + (panelW - totalW) / 2;
        
        // Posisikan tombol URM ditarik sedikit ke ATAS agar teks di bawahnya tidak keluar bingkai
        int urmY = volumeY + VOLUME_HEIGHT + (int)(tile * 0.5); // Sebelumnya tile * 1.0

        unpauseButton = new UrmButton(urmX, urmY, URM_SIZE, URM_SIZE, 0);
        replayButton = new UrmButton(urmX + URM_SIZE + gap, urmY, URM_SIZE, URM_SIZE, 1);
        menuButton = new UrmButton(urmX + (URM_SIZE + gap) * 2, urmY, URM_SIZE, URM_SIZE, 2);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        unpauseButton.update();
        replayButton.update();
        menuButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background transparan gelap
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

        // Gambar kotak window
        drawSubWindow(g2, panelX, panelY, panelW, panelH);

        // Setup Font 
        g2.setFont(new Font("Monospaced", Font.PLAIN, (int)(28 * GameCore.SCALE)));
        g2.setColor(Color.WHITE);

        // Judul Options
        String title = "Options";
        int titleX = getXforCenteredText(g2, title);
        int titleY = panelY + (int)(GameCore.TILES_SIZE * 1.5);
        g2.drawString(title, titleX, titleY);

        // Kordinat Teks Kiri
        int textX = panelX + (int)(GameCore.TILES_SIZE * 1.5); // Memberi jarak margin kiri
        int textOffsetY = (int) (SOUND_SIZE / 1.3); // Penyesuaian ke tengah tombol

        // Gambar Teks Baris Atas
        g2.drawString("Music", textX, musicButton.getY() + textOffsetY);
        g2.drawString("SE", textX, sfxButton.getY() + textOffsetY);
        
        // PENTING: Gambar Teks Volume disejajarkan dengan posisi Y slider
        int volumeTextY = volumeButton.getY() + (int)(VOLUME_HEIGHT / 1.5);
        g2.drawString("Volume", textX, volumeTextY);

        // Nyalakan antialiasing agar gambar tombol Kaarin tidak pecah
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        musicButton.draw(g2);
        sfxButton.draw(g2);
        volumeButton.draw(g2);
        unpauseButton.draw(g2);
        replayButton.draw(g2);
        menuButton.draw(g2);

        // Gambar teks tombol bawah
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) (14 * GameCore.SCALE)));
        drawButtonLabel(g2, unpauseButton, "Resume");
        drawButtonLabel(g2, replayButton, "Restart");
        drawButtonLabel(g2, menuButton, "Menu");

        g2.dispose();
    }

    // MENGGUNAKAN KODE drawSubWindow MILIK RYISNOW SAMA PERSIS
    private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        c = new Color(139, 69, 19);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    private void drawButtonLabel(Graphics2D g2, PauseButton button, String label) {
        int textW = g2.getFontMetrics().stringWidth(label);
        int tx = button.getX() + (button.getWidth() - textW) / 2;
        int ty = button.getY() + button.getHeight() + (int) (GameCore.TILES_SIZE * 0.6);
        g2.drawString(label, tx, ty);
    }

    private int getXforCenteredText(Graphics2D g2, String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return GameCore.GAME_WIDTH / 2 - length / 2;
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton)) sfxButton.setMousePressed(true);
        else if (isIn(e, unpauseButton)) unpauseButton.setMousePressed(true);
        else if (isIn(e, replayButton)) replayButton.setMousePressed(true);
        else if (isIn(e, menuButton)) menuButton.setMousePressed(true);
        else if (isIn(e, volumeButton)) volumeButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton) && musicButton.isMousePressed()) {
            musicButton.setMuted(!musicButton.isMuted());
        } else if (isIn(e, sfxButton) && sfxButton.isMousePressed()) {
            sfxButton.setMuted(!sfxButton.isMuted());
        } else if (isIn(e, unpauseButton) && unpauseButton.isMousePressed()) {
            playing.setPaused(false);
        } else if (isIn(e, replayButton) && replayButton.isMousePressed()) {
            playing.resetAll();
        } else if (isIn(e, menuButton) && menuButton.isMousePressed()) {
            GameStates.state = GameStates.MENU;
            playing.setPaused(false);
        }

        musicButton.resetBools();
        sfxButton.resetBools();
        unpauseButton.resetBools();
        replayButton.resetBools();
        menuButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        menuButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton)) musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton)) sfxButton.setMouseOver(true);
        else if (isIn(e, unpauseButton)) unpauseButton.setMouseOver(true);
        else if (isIn(e, replayButton)) replayButton.setMouseOver(true);
        else if (isIn(e, menuButton)) menuButton.setMouseOver(true);
        else if (isIn(e, volumeButton)) volumeButton.setMouseOver(true);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }
    }
}