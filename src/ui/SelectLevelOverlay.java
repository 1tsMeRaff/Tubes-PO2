package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import gameStates.MainMenu; 
import main.GameCore;
import utilitytools.LoadSave;

public class SelectLevelOverlay {

    private MainMenu menu; 
    private int panelX, panelY, panelW, panelH;
    private Rectangle btnLevel1, btnLevel2, btnLevel3, btnBack;
    private boolean isHoveringL1, isHoveringL2, isHoveringL3, isHoveringBack;

    public SelectLevelOverlay(MainMenu menu) {
        this.menu = menu;
        initLayout();
    }

    private void initLayout() {
        panelW = (int) (500 * GameCore.SCALE);
        panelH = (int) (350 * GameCore.SCALE);
        panelX = GameCore.GAME_WIDTH / 2 - panelW / 2;
        panelY = GameCore.GAME_HEIGHT / 2 - panelH / 2;

        int btnSize = (int) (70 * GameCore.SCALE);
        int gap = (int) (30 * GameCore.SCALE);
        
        int totalBtnWidth = (btnSize * 3) + (gap * 2);
        int startX = panelX + (panelW / 2) - (totalBtnWidth / 2);
        int startY = panelY + (int) (130 * GameCore.SCALE);

        btnLevel1 = new Rectangle(startX, startY, btnSize, btnSize);
        btnLevel2 = new Rectangle(startX + btnSize + gap, startY, btnSize, btnSize);
        btnLevel3 = new Rectangle(startX + (btnSize + gap) * 2, startY, btnSize, btnSize);
        btnBack = new Rectangle(panelX + (int)(15 * GameCore.SCALE), panelY + (int)(15 * GameCore.SCALE), (int)(60 * GameCore.SCALE), (int)(25 * GameCore.SCALE));
    }

    public void update() {}

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

        g2.setColor(new Color(50, 30, 15)); 
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 30, 30);
        g2.setColor(new Color(139, 69, 19)); 
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 30, 30);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, (int)(25 * GameCore.SCALE)));
        g2.drawString("PILIH LEVEL", panelX + panelW/2 - (int)(75 * GameCore.SCALE), panelY + (int)(70 * GameCore.SCALE));

        drawCustomButton(g2, btnLevel1, "1", isHoveringL1, new Color(34, 139, 34)); 
        drawCustomButton(g2, btnLevel2, "2", isHoveringL2, new Color(100, 100, 100)); 
        drawCustomButton(g2, btnLevel3, "3", isHoveringL3, new Color(105, 105, 105)); 
        drawCustomButton(g2, btnBack, "Back", isHoveringBack, new Color(200, 50, 50));
    }

    private void drawCustomButton(Graphics2D g2, Rectangle bounds, String text, boolean isHovering, Color baseColor) {
        if (isHovering) g2.setColor(baseColor.brighter()); 
        else g2.setColor(baseColor);
        
        g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        g2.setColor(Color.WHITE); 
        g2.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        
        g2.setFont(new Font("Arial", Font.BOLD, (int)(18 * GameCore.SCALE)));
        g2.drawString(text, bounds.x + (bounds.width/3), bounds.y + (bounds.height/2) + (int)(6 * GameCore.SCALE));
    }

    public void mouseMoved(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        isHoveringL1 = btnLevel1.contains(mx, my);
        isHoveringL2 = btnLevel2.contains(mx, my);
        isHoveringL3 = btnLevel3.contains(mx, my);
        isHoveringBack = btnBack.contains(mx, my);
    }
    
    // METHOD YANG HILANG (Penting agar tidak error)
    public void mousePressed(MouseEvent e) {
        // Kosongkan saja tidak apa-apa
    }

    public void mouseReleased(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (btnLevel1.contains(mx, my)) {
            menu.startSelectedLevel("/map_tutorial_fix.txt", LoadSave.WORLD_SPRITE); 
        } 
        else if (btnLevel2.contains(mx, my)) {
            menu.startSelectedLevel("/map_tutorial_fix.txt", LoadSave.WORLD_SPRITE); 
        }
        else if (btnLevel3.contains(mx, my)) {
            menu.startSelectedLevel("/main_tileset.txt", "main_tileset.png"); 
        } 
        else if (btnBack.contains(mx, my)) {
            menu.setLevelSelectActive(false); 
        }
    }
}