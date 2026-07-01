package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.GameCore;
import ui.SelectLevelOverlay;
import utilitytools.LoadSave;

public class MainMenu extends States implements StateMethods {
    private SelectLevelOverlay selectLevelOverlay;
    private boolean isLevelSelectActive = false;

    private BufferedImage backgroundImage; 
    private BufferedImage panelImage; 
    
    private int panelX, panelY, panelWidth, panelHeight;
    private Rectangle startBtn, continueBtn, optionsBtn, exitBtn;
    
    private boolean startHover, startPressed;
    private boolean continueHover, continuePressed;
    private boolean optionsHover, optionsPressed;
    private boolean exitHover, exitPressed;

    public MainMenu(GameCore gc) {
        super(gc);
        loadAsetUI(); 
        initLayoutMenu(); 
        selectLevelOverlay = new SelectLevelOverlay(this);
    }

    private void loadAsetUI() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND); 
        panelImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_PANEL); 
    }

    private void initLayoutMenu() {
        panelWidth = (int) (500 * GameCore.SCALE); 
        panelHeight = (int) (300 * GameCore.SCALE); 
        panelX = GameCore.GAME_WIDTH / 2 - panelWidth / 2;
        panelY = (int) (160 * GameCore.SCALE); 
        
        int btnWidth = (int)(panelWidth * 0.30); 
        int btnHeight = (int) (30 * GameCore.SCALE); 
        int btnX = GameCore.GAME_WIDTH / 2 - btnWidth / 2;
        int startY = panelY + (int)(105 * GameCore.SCALE); 
        int spacing = (int)(31 * GameCore.SCALE); 

        startBtn = new Rectangle(btnX, startY, btnWidth, btnHeight);
        continueBtn = new Rectangle(btnX, startY + spacing, btnWidth, btnHeight);
        optionsBtn = new Rectangle(btnX, startY + (spacing * 2), btnWidth, btnHeight);
        exitBtn = new Rectangle(btnX, startY + (spacing * 3), btnWidth, btnHeight);
    }

    @Override
    public void update() {
        if (isLevelSelectActive) {
            selectLevelOverlay.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
        }

        if (panelImage != null) {
            g.drawImage(panelImage, panelX, panelY, panelWidth, panelHeight, null);
        } else {
            g.setColor(new Color(60, 40, 20)); 
            g.fillRect(panelX, panelY, panelWidth, panelHeight);
        }
        
        drawButtonHighlight(g, startBtn, startHover, startPressed);
        drawButtonHighlight(g, continueBtn, continueHover, continuePressed);
        drawButtonHighlight(g, optionsBtn, optionsHover, optionsPressed);
        drawButtonHighlight(g, exitBtn, exitHover, exitPressed);

        if (isLevelSelectActive) {
            selectLevelOverlay.draw(g);
        }
    }
        
    private void drawButtonHighlight(Graphics g, Rectangle bounds, boolean isHover, boolean isPressed) {
        if (isPressed) {
            g.setColor(new Color(0, 0, 0, 100)); 
            g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        } else if (isHover) {
            g.setColor(new Color(255, 255, 255, 50)); 
            g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isLevelSelectActive) {
            selectLevelOverlay.mouseMoved(e);
        } else {
            startHover = startBtn.contains(e.getX(), e.getY());
            continueHover = continueBtn.contains(e.getX(), e.getY());
            optionsHover = optionsBtn.contains(e.getX(), e.getY());
            exitHover = exitBtn.contains(e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isLevelSelectActive) {
            selectLevelOverlay.mousePressed(e);
        } else {
            if (startHover) startPressed = true;
            if (continueHover) continuePressed = true;
            if (optionsHover) optionsPressed = true;
            if (exitHover) exitPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isLevelSelectActive) {
            selectLevelOverlay.mouseReleased(e);
        } else {
            if (startHover && startPressed) {
                isLevelSelectActive = true; 
            } else if (continueHover && continuePressed) {
                if (utilitytools.SaveLoadManager.loadGame(gc)) {
                    GameStates.state = GameStates.PLAYING;
                    setLevelSelectActive(false);
                } else {
                    System.out.println("Gagal memuat save atau file tidak ditemukan!");
                }
            } else if (optionsHover && optionsPressed) {
            } else if (optionsHover && optionsPressed) {
                GameStates.state = GameStates.OPTIONS;
            } else if (exitHover && exitPressed) {
                System.exit(0); 
            }
            resetButtons();
        }
    }

    private void resetButtons() {
        startHover = continueHover = optionsHover = exitHover = false;
        startPressed = continuePressed = optionsPressed = exitPressed = false;
    }

    public void setLevelSelectActive(boolean active) {
        this.isLevelSelectActive = active;
    }

    public void startSelectedLevel(String mapFilePath, String tilesetName) {
        gc.getPlay().loadSelectedLevel(mapFilePath, tilesetName); 
        GameStates.state = GameStates.PLAYING;
        setLevelSelectActive(false); 
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_F11) gc.getGameFrame().toggleFullScreen(); }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void triggerHeavyHit(int freezeFrames, int shakeFrames, int intensity) {}
}