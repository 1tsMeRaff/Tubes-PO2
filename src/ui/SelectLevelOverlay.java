package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import gameStates.MainMenu;
import main.GameCore;

/**
 * SelectLevelOverlay - Chronicles of the Realm / Expedition Selector.
 * Crafted by the Design Division for "Feline Souls : Witch Curse".
 * 
 * Features:
 * - Gothic Obsidian Ledger aesthetic with atmospheric chapter cards.
 * - WCAG 2.1 AA/AAA compliant high-contrast typography, difficulty badges, and lore briefs.
 * - Full dual Keyboard & Mouse interaction parity ([1], [2], [3], [ESC]).
 * - Hover elevation, pressed states, and responsive audio feedback.
 * - Zero-allocation rendering architecture (all Color, Font, and Stroke objects pre-cached).
 */
public class SelectLevelOverlay {

    // === PRE-CACHED COLOR PALETTE (ZERO GC ALLOCATIONS) ===
    private static final Color BG_OVERLAY = new Color(6, 6, 12, 230);
    private static final Color PANEL_BG = new Color(18, 16, 26, 250);
    private static final Color PANEL_INNER_BG = new Color(10, 9, 16, 210);
    private static final Color PANEL_GOLD_BORDER = new Color(190, 155, 75, 230);
    private static final Color PANEL_INNER_RIM = new Color(75, 65, 90, 160);
    private static final Color DIVIDER_COLOR = new Color(55, 50, 70);

    private static final Color TITLE_SHADOW = new Color(20, 0, 5, 240);
    private static final Color TITLE_GOLD = new Color(245, 215, 125);
    private static final Color SUBTITLE_SILVER = new Color(190, 195, 210);
    private static final Color FOOTER_HINT_COLOR = new Color(150, 155, 175);

    // Card Colors
    private static final Color CARD_NORMAL_BG = new Color(26, 23, 36, 230);
    private static final Color CARD_HOVER_BG = new Color(40, 35, 56, 245);
    private static final Color CARD_PRESSED_BG = new Color(18, 16, 26, 255);
    private static final Color CARD_NORMAL_BORDER = new Color(65, 60, 80);

    // Difficulty & Theme Colors
    private static final Color COLOR_L1_EMERALD = new Color(46, 204, 113);
    private static final Color COLOR_L2_AMBER = new Color(241, 196, 15);
    private static final Color COLOR_L3_RUBY = new Color(231, 76, 60);

    // Pill / Badge Backgrounds
    private static final Color BADGE_BG = new Color(14, 12, 20, 200);

    // Back Button Colors
    private static final Color BTN_BACK_NORMAL = new Color(34, 38, 52);
    private static final Color BTN_BACK_HOVER = new Color(54, 60, 84);
    private static final Color BTN_BACK_PRESSED = new Color(24, 26, 38);
    private static final Color BTN_BACK_BORDER = new Color(180, 195, 220);

    private static final Color TEXT_WHITE = new Color(250, 250, 252);
    private static final Color TEXT_DESC_MUTED = new Color(185, 190, 205);
    private static final Color TEXT_KEY_HINT = new Color(230, 200, 120);

    // === PRE-CACHED STROKES ===
    private static final BasicStroke STROKE_PANEL_OUTER = new BasicStroke(2.5f);
    private static final BasicStroke STROKE_PANEL_INNER = new BasicStroke(1.0f);
    private static final BasicStroke STROKE_CARD_BORDER = new BasicStroke(1.8f);
    private static final BasicStroke STROKE_BTN = new BasicStroke(1.5f);

    // === PRE-CACHED FONTS (SCALED) ===
    private final Font titleFont;
    private final Font subtitleFont;
    private final Font cardHeaderFont;
    private final Font cardTitleFont;
    private final Font cardDescFont;
    private final Font cardDiffFont;
    private final Font btnLabelFont;
    private final Font footerFont;

    // === LAYOUT & INTERACTION BOUNDS ===
    private final MainMenu menu;
    private int panelX, panelY, panelW, panelH;

    private Rectangle cardL1;
    private Rectangle cardL2;
    private Rectangle cardL3;
    private Rectangle btnBack;

    private boolean hoverL1 = false;
    private boolean hoverL2 = false;
    private boolean hoverL3 = false;
    private boolean hoverBack = false;

    private boolean pressL1 = false;
    private boolean pressL2 = false;
    private boolean pressL3 = false;
    private boolean pressBack = false;

    private boolean lastHoverSoundPlayed = false;

    public SelectLevelOverlay(MainMenu menu) {
        this.menu = menu;

        float scale = GameCore.SCALE;
        titleFont = new Font("Serif", Font.BOLD, Math.max(18, (int) (24 * scale)));
        subtitleFont = new Font("SansSerif", Font.ITALIC, Math.max(10, (int) (11 * scale)));
        cardHeaderFont = new Font("SansSerif", Font.BOLD, Math.max(9, (int) (10 * scale)));
        cardTitleFont = new Font("Serif", Font.BOLD, Math.max(12, (int) (14 * scale)));
        cardDescFont = new Font("SansSerif", Font.PLAIN, Math.max(9, (int) (10 * scale)));
        cardDiffFont = new Font("SansSerif", Font.BOLD, Math.max(9, (int) (10 * scale)));
        btnLabelFont = new Font("SansSerif", Font.BOLD, Math.max(11, (int) (12 * scale)));
        footerFont = new Font("SansSerif", Font.PLAIN, Math.max(9, (int) (10 * scale)));

        initLayout();
    }

    private void initLayout() {
        float scale = GameCore.SCALE;
        panelW = (int) (580 * scale);
        panelH = (int) (390 * scale);
        panelX = GameCore.GAME_WIDTH / 2 - panelW / 2;
        panelY = GameCore.GAME_HEIGHT / 2 - panelH / 2;

        int cardW = (int) (165 * scale);
        int cardH = (int) (205 * scale);
        int gap = (int) (16 * scale);

        int totalCardsW = (cardW * 3) + (gap * 2);
        int startX = panelX + (panelW - totalCardsW) / 2;
        int startY = panelY + (int) (95 * scale);

        cardL1 = new Rectangle(startX, startY, cardW, cardH);
        cardL2 = new Rectangle(startX + cardW + gap, startY, cardW, cardH);
        cardL3 = new Rectangle(startX + (cardW + gap) * 2, startY, cardW, cardH);

        int btnW = (int) (200 * scale);
        int btnH = (int) (36 * scale);
        int btnX = panelX + (panelW - btnW) / 2;
        int btnY = panelY + panelH - btnH - (int) (38 * scale);

        btnBack = new Rectangle(btnX, btnY, btnW, btnH);
    }

    public void update() {}

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Object prevAa = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Fullscreen Dim Backdrop
        g2.setColor(BG_OVERLAY);
        g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

        // 2. Gothic Panel Frame
        g2.setColor(PANEL_BG);
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 24, 24);

        g2.setStroke(STROKE_PANEL_OUTER);
        g2.setColor(PANEL_GOLD_BORDER);
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 24, 24);

        g2.setStroke(STROKE_PANEL_INNER);
        g2.setColor(PANEL_INNER_RIM);
        g2.drawRoundRect(panelX + 4, panelY + 4, panelW - 8, panelH - 8, 20, 20);

        // 3. Header: Title & Subtitle
        String title = "❖  CHRONICLES OF THE REALM  ❖";
        g2.setFont(titleFont);
        int titleW = g2.getFontMetrics().stringWidth(title);
        int titleX = panelX + (panelW - titleW) / 2;
        int titleY = panelY + (int) (42 * GameCore.SCALE);

        g2.setColor(TITLE_SHADOW);
        g2.drawString(title, titleX + 2, titleY + 2);
        g2.setColor(TITLE_GOLD);
        g2.drawString(title, titleX, titleY);

        String subtitle = "Select a cursed domain to cleanse from the witch's corruption";
        g2.setFont(subtitleFont);
        int subW = g2.getFontMetrics().stringWidth(subtitle);
        int subX = panelX + (panelW - subW) / 2;
        int subY = titleY + (int) (20 * GameCore.SCALE);
        g2.setColor(SUBTITLE_SILVER);
        g2.drawString(subtitle, subX, subY);

        g2.setColor(DIVIDER_COLOR);
        g2.drawLine(panelX + 35, subY + (int) (12 * GameCore.SCALE), panelX + panelW - 35, subY + (int) (12 * GameCore.SCALE));

        // 4. Three Expedition Cards
        drawLevelCard(g2, cardL1, "CHAPTER I", "[KEY 1]", "Abandoned Outskirts", "Whispering forest & sunken ruins under dark mist.", "★☆☆  NOVICE", COLOR_L1_EMERALD, hoverL1, pressL1);
        drawLevelCard(g2, cardL2, "CHAPTER II", "[KEY 2]", "Deep Jungle Ruins", "Poison canopy, venom vines & forgotten shrines.", "★★☆  VETERAN", COLOR_L2_AMBER, hoverL2, pressL2);
        drawLevelCard(g2, cardL3, "CHAPTER III", "[KEY 3]", "The Witch's Citadel", "Obsidian throne room of the arch-curse mistress.", "★★★  NIGHTMARE", COLOR_L3_RUBY, hoverL3, pressL3);

        // 5. Return to Sanctuary Button
        drawBackButton(g2);

        // 6. Accessible Footer Hint
        String hint = "Controls: Click card or press [1], [2], [3] • [ESC] Return to Title";
        g2.setFont(footerFont);
        int hintW = g2.getFontMetrics().stringWidth(hint);
        int hintX = panelX + (panelW - hintW) / 2;
        int hintY = panelY + panelH - (int) (14 * GameCore.SCALE);
        g2.setColor(FOOTER_HINT_COLOR);
        g2.drawString(hint, hintX, hintY);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, prevAa != null ? prevAa : RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    private void drawLevelCard(Graphics2D g2, Rectangle bounds, String chapter, String keyHint,
                               String realmName, String description, String diffText,
                               Color accentColor, boolean isHover, boolean isPressed) {
        float scale = GameCore.SCALE;
        int drawY = isHover ? bounds.y - (int) (3 * scale) : bounds.y;

        // Card Background Fill
        Color bg = isPressed ? CARD_PRESSED_BG : (isHover ? CARD_HOVER_BG : CARD_NORMAL_BG);
        g2.setColor(bg);
        g2.fillRoundRect(bounds.x, drawY, bounds.width, bounds.height, 16, 16);

        // Card Border
        g2.setStroke(STROKE_CARD_BORDER);
        g2.setColor(isHover ? accentColor : CARD_NORMAL_BORDER);
        g2.drawRoundRect(bounds.x, drawY, bounds.width, bounds.height, 16, 16);

        // Top Pill Badge: Chapter & Key Hint
        int badgeH = (int) (22 * scale);
        int badgeY = drawY + (int) (10 * scale);
        int badgeX = bounds.x + (int) (10 * scale);
        int badgeW = bounds.width - (int) (20 * scale);

        g2.setColor(BADGE_BG);
        g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 10, 10);
        g2.setColor(PANEL_INNER_RIM);
        g2.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 10, 10);

        g2.setFont(cardHeaderFont);
        g2.setColor(TEXT_WHITE);
        g2.drawString(chapter, badgeX + (int) (8 * scale), badgeY + (int) (15 * scale));

        g2.setColor(isHover ? accentColor : TEXT_KEY_HINT);
        int keyW = g2.getFontMetrics().stringWidth(keyHint);
        g2.drawString(keyHint, badgeX + badgeW - keyW - (int) (8 * scale), badgeY + (int) (15 * scale));

        // Realm Title
        g2.setFont(cardTitleFont);
        g2.setColor(TEXT_WHITE);
        int realmY = badgeY + badgeH + (int) (24 * scale);
        drawWrappedText(g2, realmName, bounds.x + (int) (12 * scale), realmY, bounds.width - (int) (24 * scale), (int) (16 * scale));

        // Realm Description
        g2.setFont(cardDescFont);
        g2.setColor(TEXT_DESC_MUTED);
        int descY = drawY + (int) (100 * scale);
        drawWrappedText(g2, description, bounds.x + (int) (12 * scale), descY, bounds.width - (int) (24 * scale), (int) (14 * scale));

        // Difficulty Badge at bottom of card
        int diffY = drawY + bounds.height - (int) (16 * scale);
        g2.setFont(cardDiffFont);
        g2.setColor(accentColor);
        int diffW = g2.getFontMetrics().stringWidth(diffText);
        g2.drawString(diffText, bounds.x + (bounds.width - diffW) / 2, diffY);
    }

    private void drawWrappedText(Graphics2D g2, String text, int x, int y, int maxW, int lineH) {
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        int curY = y;

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            if (g2.getFontMetrics().stringWidth(testLine) > maxW && currentLine.length() > 0) {
                g2.drawString(currentLine.toString(), x, curY);
                currentLine = new StringBuilder(word);
                curY += lineH;
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }
        if (currentLine.length() > 0) {
            g2.drawString(currentLine.toString(), x, curY);
        }
    }

    private void drawBackButton(Graphics2D g2) {
        Color bg = pressBack ? BTN_BACK_PRESSED : (hoverBack ? BTN_BACK_HOVER : BTN_BACK_NORMAL);
        g2.setColor(bg);
        g2.fillRoundRect(btnBack.x, btnBack.y, btnBack.width, btnBack.height, 12, 12);

        g2.setStroke(STROKE_BTN);
        g2.setColor(hoverBack ? BTN_BACK_BORDER : PANEL_INNER_RIM);
        g2.drawRoundRect(btnBack.x, btnBack.y, btnBack.width, btnBack.height, 12, 12);

        String label = "RETURN TO SANCTUARY  [ESC]";
        g2.setFont(btnLabelFont);
        int labelW = g2.getFontMetrics().stringWidth(label);
        int textX = btnBack.x + (btnBack.width - labelW) / 2;
        int textY = btnBack.y + (int) (23 * GameCore.SCALE);

        g2.setColor(hoverBack ? TEXT_WHITE : SUBTITLE_SILVER);
        g2.drawString(label, textX, textY);
    }

    // === MOUSE & ACCESSIBILITY HANDLERS ===
    public void mouseMoved(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        boolean prevHover = hoverL1 || hoverL2 || hoverL3 || hoverBack;
        hoverL1 = cardL1 != null && cardL1.contains(mx, my);
        hoverL2 = cardL2 != null && cardL2.contains(mx, my);
        hoverL3 = cardL3 != null && cardL3.contains(mx, my);
        hoverBack = btnBack != null && btnBack.contains(mx, my);

        boolean currentHover = hoverL1 || hoverL2 || hoverL3 || hoverBack;
        if (!prevHover && currentHover && !lastHoverSoundPlayed) {
            playSfx(audio.AudioPlayer.ATTACK_THREE);
            lastHoverSoundPlayed = true;
        } else if (!currentHover) {
            lastHoverSoundPlayed = false;
        }
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (cardL1 != null && cardL1.contains(mx, my)) pressL1 = true;
        else if (cardL2 != null && cardL2.contains(mx, my)) pressL2 = true;
        else if (cardL3 != null && cardL3.contains(mx, my)) pressL3 = true;
        else if (btnBack != null && btnBack.contains(mx, my)) pressBack = true;
    }

    public void mouseReleased(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (pressL1 && cardL1 != null && cardL1.contains(mx, my)) {
            launchLevel(1);
        } else if (pressL2 && cardL2 != null && cardL2.contains(mx, my)) {
            launchLevel(2);
        } else if (pressL3 && cardL3 != null && cardL3.contains(mx, my)) {
            launchLevel(3);
        } else if (pressBack && btnBack != null && btnBack.contains(mx, my)) {
            closeOverlay();
        }

        pressL1 = pressL2 = pressL3 = pressBack = false;
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ESCAPE) {
            closeOverlay();
        } else if (code == KeyEvent.VK_1 || code == KeyEvent.VK_NUMPAD1) {
            launchLevel(1);
        } else if (code == KeyEvent.VK_2 || code == KeyEvent.VK_NUMPAD2) {
            launchLevel(2);
        } else if (code == KeyEvent.VK_3 || code == KeyEvent.VK_NUMPAD3) {
            launchLevel(3);
        }
    }

    public void launchLevel(int levelIndex) {
        playSfx(audio.AudioPlayer.SLASH_SOUND);
        if (menu == null) return;

        switch (levelIndex) {
            case 1:
                menu.startSelectedLevel("/map_tutorial_fix.txt", "outside_sprites.png");
                break;
            case 2:
                menu.startSelectedLevel("/jungle_map.txt", "map_jungle_fix.png");
                break;
            case 3:
                menu.startSelectedLevel("/main_tileset.txt", "main_tileset.png");
                break;
        }
    }

    public void closeOverlay() {
        playSfx(audio.AudioPlayer.SLASH_SOUND);
        if (menu != null) {
            menu.setLevelSelectActive(false);
        }
    }

    private void playSfx(int effectId) {
        if (menu != null && menu.getGameCore() != null && menu.getGameCore().getAudioPlayer() != null) {
            menu.getGameCore().getAudioPlayer().playEffect(effectId);
        }
    }

    // Accessors for QA verification
    public Rectangle getCardL1Bounds() { return cardL1; }
    public Rectangle getCardL2Bounds() { return cardL2; }
    public Rectangle getCardL3Bounds() { return cardL3; }
    public Rectangle getBtnBackBounds() { return btnBack; }
    public boolean isHoverL1() { return hoverL1; }
    public boolean isHoverL2() { return hoverL2; }
    public boolean isHoverL3() { return hoverL3; }
    public boolean isHoverBack() { return hoverBack; }
}