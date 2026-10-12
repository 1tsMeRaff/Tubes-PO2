package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.GameCore;
import utilitytools.LoadSave;

/**
 * Dedicated HUD renderer for Player Health, Mana, and Status Bar.
 * Extracted from Player.java to enforce SRP (Single Responsibility Principle).
 */
public class PlayerHUD {

    private BufferedImage statusBarImg;
    private final int statusBarWidth = (int) (164 * GameCore.SCALE);
    private final int statusBarHeight = (int) (36 * GameCore.SCALE);
    private final int statusBarX = (int) (10 * GameCore.SCALE);
    private final int statusBarY = (int) (10 * GameCore.SCALE);

    private final int healthBarWidth = (int) (106 * GameCore.SCALE);
    private final int healthBarHeight = (int) (7 * GameCore.SCALE);
    private final int healthBarXStart = (int) (53 * GameCore.SCALE);
    private final int healthBarYStart = (int) (9 * GameCore.SCALE);

    private final int manaBarWidth = (int) (106 * GameCore.SCALE);
    private final int manaBarHeight = (int) (7 * GameCore.SCALE);
    private final int manaBarXStart = (int) (53 * GameCore.SCALE);
    private final int manaBarYStart = (int) (23 * GameCore.SCALE);

    public PlayerHUD() {
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void draw(Graphics g, int currentHealth, int maxHealth, int currentMana, int maxMana) {
        if (statusBarImg != null) {
            g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        }

        int healthBarX = statusBarX + healthBarXStart;
        int healthBarY = statusBarY + healthBarYStart;
        int manaBarX = statusBarX + manaBarXStart;
        int manaBarY = statusBarY + manaBarYStart;

        int healthWidth = (maxHealth > 0) ? (int) ((currentHealth / (float) maxHealth) * healthBarWidth) : 0;
        int manaWidth = (maxMana > 0) ? (int) ((currentMana / (float) maxMana) * manaBarWidth) : 0;

        if (healthWidth < 0) healthWidth = 0;
        if (healthWidth > healthBarWidth) healthWidth = healthBarWidth;
        if (manaWidth < 0) manaWidth = 0;
        if (manaWidth > manaBarWidth) manaWidth = manaBarWidth;

        g.setColor(Color.RED);
        g.fillRect(healthBarX, healthBarY, healthWidth, healthBarHeight);

        g.setColor(Color.BLUE);
        g.fillRect(manaBarX, manaBarY, manaWidth, manaBarHeight);
    }
}
