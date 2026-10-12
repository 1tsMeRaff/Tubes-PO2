package ui;

import gameStates.GameStates;
import gameStates.PlayStates;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.GameCore;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.UI.PauseButtons.*;

public class PauseOverlay {
    private PlayStates playing;
    private int panelX, panelY, panelW, panelH;

    private BufferedImage titleImg, musicImg, seImg, volImg;
    private BufferedImage pawImg, resumeImg, restartImg, menuImg;

    private SoundButton musicButton, sfxButton;
    private UrmButton menuButton, replayButton, unpauseButton;
    private VolumeButton volumeButton; 

    public PauseOverlay(PlayStates playing) {
        this.playing = playing;
        loadImages(); 
        initLayout();
    }

    private void loadImages() {
        titleImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_TITLE);
        musicImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MUSIC_TEXT);
        seImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_SE_TEXT);
        volImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_VOL_TEXT);
        
        pawImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_PAW_BTN);
        resumeImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_RESUME_BTN);
        restartImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_RESTART_BTN);
        menuImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU_BTN);
    }

    private void initLayout() {
        int tile = GameCore.TILES_SIZE;
        panelW = tile * 12; 
        panelH = tile * 11; 
        panelX = GameCore.GAME_WIDTH / 2 - panelW / 2;
        panelY = GameCore.GAME_HEIGHT / 2 - panelH / 2;

        int btnSize = (int)(45 * GameCore.SCALE);
        

        // Ini adalah posisi X awal (di sebelah kanan)
        int defaultColX = panelX + panelW - btnSize - (int)(30 * GameCore.SCALE);
        
        // 1. Posisi X untuk Tombol Cakar (Geser ke kiri sebanyak 80 pixel)
        int soundHitboxX = defaultColX - 80; // <-- Ganti angka 80 jika kurang pas
        
        // 2. Posisi X untuk Slider Volume (Tetap di kanan agar tidak rusak)
        int volumeHitboxX = defaultColX;     
        // ==============================================================

        int startY = panelY + (int)(100 * GameCore.SCALE);
        int itemDy = btnSize + (int)(15 * GameCore.SCALE);

        // Buat tombol musik & SE menggunakan koordinat X yang baru (soundHitboxX)
        musicButton = new SoundButton(soundHitboxX, startY, btnSize, btnSize);
        int sfxY = startY + itemDy;
        sfxButton = new SoundButton(soundHitboxX, sfxY, btnSize, btnSize);

        int volumeY = sfxY + itemDy + (int)(10 * GameCore.SCALE);
        int customSliderWidth = (int)(150 * GameCore.SCALE);
        
        // Buat slider volume menggunakan koordinat X yang volume (volumeHitboxX)
        int sliderX = volumeHitboxX + btnSize - customSliderWidth; 
        volumeButton = new VolumeButton(sliderX, volumeY + (int)(15 * GameCore.SCALE), VOLUME_WIDTH, VOLUME_HEIGHT, customSliderWidth, VOLUME_HEIGHT);
        
        int gap = (int)(25 * GameCore.SCALE);
        int urmSize = (int)(60 * GameCore.SCALE); 
        int totalW = (urmSize * 3) + (gap * 2);
        int urmX = panelX + (panelW - totalW) / 2;
        int urmY = volumeY + (int)(75 * GameCore.SCALE);

        unpauseButton = new UrmButton(urmX, urmY, urmSize, urmSize, 0);
        replayButton = new UrmButton(urmX + urmSize + gap, urmY, urmSize, urmSize, 1);
        menuButton = new UrmButton(urmX + (urmSize + gap) * 2, urmY, urmSize, urmSize, 2);
    }

    public void update() {
        musicButton.update(); sfxButton.update(); unpauseButton.update();
        replayButton.update(); menuButton.update(); volumeButton.update();
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
        drawSubWindow(g2, panelX, panelY, panelW, panelH);

        int textX = panelX + (int)(GameCore.TILES_SIZE * 1.5); 

        // Teks "PAUSE" (Otomatis di tengah panel)
        int titleW = (int)(200 * GameCore.SCALE); 
        int titleH = (int)(65 * GameCore.SCALE);  
        int titleX = panelX + (panelW / 2) - (titleW / 2); 
        int titleY = panelY + (int)(GameCore.TILES_SIZE * 0.8);

        // Teks "MUSIC"
        int musicTextX = textX + 0;               // <--- Ubah ini untuk geser Kiri (-) / Kanan (+)
        int musicTextY = musicButton.getY() + 10; // <--- Ubah ini untuk geser Atas (-) / Bawah (+)
        int musicTextW = (int)(125 * GameCore.SCALE); 
        int musicTextH = (int)(50 * GameCore.SCALE); 

        // Teks "SE"
        int seTextX = textX + 0;                 // <--- Ubah ini untuk geser Kiri (-) / Kanan (+)
        int seTextY = sfxButton.getY() + 10;     // <--- Ubah ini untuk geser Atas (-) / Bawah (+)
        int seTextW = (int)(125 * GameCore.SCALE); 
        int seTextH = (int)(60 * GameCore.SCALE); 

        // Teks "VOLUME"
        int volTextX = textX + 10;                  // <--- Ubah ini untuk geser Kiri (-) / Kanan (+)
        int volTextY = volumeButton.getY() - 10;    // <--- Ubah ini untuk geser Atas (-) / Bawah (+)
        int volTextW = (int)(112 * GameCore.SCALE); 
        int volTextH = (int)(55 * GameCore.SCALE);  

   
     // Tombol Cakar Musik
        int pawMusicX = musicButton.getBounds().x + 0;  // <--- Kembalikan jadi + 0
        int pawMusicY = musicButton.getBounds().y + 10; 
        int pawMusicW = (int)(musicButton.getBounds().width);  
        int pawMusicH = (int)(musicButton.getBounds().height); 
        java.awt.Rectangle customMusicBounds = new java.awt.Rectangle(pawMusicX, pawMusicY, pawMusicW, pawMusicH);

        // Tombol Cakar SFX / SE
        int pawSfxX = sfxButton.getBounds().x + 0;    // <--- Kembalikan jadi + 0
        int pawSfxY = sfxButton.getBounds().y + 25;   
        int pawSfxW = (int)(sfxButton.getBounds().width);   
        int pawSfxH = (int)(sfxButton.getBounds().height);  
        java.awt.Rectangle customSfxBounds = new java.awt.Rectangle(pawSfxX, pawSfxY, pawSfxW, pawSfxH);


        if (titleImg != null) g2.drawImage(titleImg, titleX, titleY, titleW, titleH, null);
        if (musicImg != null) g2.drawImage(musicImg, musicTextX, musicTextY, musicTextW, musicTextH, null);
        if (seImg != null) g2.drawImage(seImg, seTextX, seTextY, seTextW, seTextH, null);
        if (volImg != null) g2.drawImage(volImg, volTextX, volTextY, volTextW, volTextH, null);

        // Menggambar tombol cakar berdasarkan koordinat kustom baru
        drawCustomButton(g2, pawImg, customMusicBounds, musicButton.isMouseOver(), musicButton.isMousePressed(), musicButton.isMuted());
        drawCustomButton(g2, pawImg, customSfxBounds, sfxButton.isMouseOver(), sfxButton.isMousePressed(), sfxButton.isMuted());
        
        // Tombol menu bagian bawah tetap menggunakan posisi aslinya
        drawCustomButton(g2, resumeImg, unpauseButton.getBounds(), unpauseButton.isMouseOver(), unpauseButton.isMousePressed(), false);
        drawCustomButton(g2, restartImg, replayButton.getBounds(), replayButton.isMouseOver(), replayButton.isMousePressed(), false);
        drawCustomButton(g2, menuImg, menuButton.getBounds(), menuButton.isMouseOver(), menuButton.isMousePressed(), false);

        volumeButton.draw(g2); 
        g2.dispose();
    }

    private void drawCustomButton(Graphics2D g2, BufferedImage img, Rectangle bounds, boolean isHover, boolean isPressed, boolean isMuted) {
        if (img != null) {
            g2.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height, null);
            if (isMuted || isPressed) { g2.setColor(new Color(0, 0, 0, 100)); g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10); }
            else if (isHover) { g2.setColor(new Color(255, 255, 255, 50)); g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10); }
        }
    }

    private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 200)); g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(new Color(139, 69, 19)); g2.setStroke(new BasicStroke(5)); g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    private boolean isIn(MouseEvent e, PauseButton b) { return b.getBounds().contains(e.getX(), e.getY()); }

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
            playing.GetGame().getAudioPlayer().toggleSongMute(); 
        } 
        else if (isIn(e, sfxButton) && sfxButton.isMousePressed()) { 
            sfxButton.setMuted(!sfxButton.isMuted()); 
        } 
        else if (isIn(e, unpauseButton) && unpauseButton.isMousePressed()) { 
            playing.setPaused(false); 
        } 
        else if (isIn(e, replayButton) && replayButton.isMousePressed()) { 
            playing.resetAll(200, 200); 
        } 
        else if (isIn(e, menuButton) && menuButton.isMousePressed()) { 
            GameStates.state = GameStates.MENU; 
            playing.setPaused(false); 
        }
        
        musicButton.resetBools(); sfxButton.resetBools(); unpauseButton.resetBools(); replayButton.resetBools(); menuButton.resetBools(); volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false); sfxButton.setMouseOver(false); unpauseButton.setMouseOver(false);
        replayButton.setMouseOver(false); menuButton.setMouseOver(false); volumeButton.setMouseOver(false);
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
            playing.GetGame().getAudioPlayer().setVolume(volumeButton.GetFloatValue());
        }
    }
}