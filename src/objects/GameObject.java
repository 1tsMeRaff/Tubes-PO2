package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.GameCore;
import utilitytools.Konstanta.ObjectConstants;

public class GameObject {
    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= 25) { // Kecepatan animasi
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= ObjectConstants.GetSpriteAmount(objType)) {
                aniIndex = 0;
                // Jika objek hancur (box/barrel), hentikan animasi dan nonaktifkan
                if (objType == ObjectConstants.BARREL || objType == ObjectConstants.BOX) {
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    public void reset() {
        aniIndex = 0;
        aniTick = 0;
        active = true;
        
        if (objType == ObjectConstants.BARREL || objType == ObjectConstants.BOX) {
            doAnimation = false;
        } else {
            doAnimation = true;
        }
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * GameCore.SCALE), (int) (height * GameCore.SCALE));
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    // --- GETTERS & SETTERS ---
    public int getObjType() { return objType; }
    public Rectangle2D.Float getHitbox() { return hitbox; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public void setAnimation(boolean doAnimation) { this.doAnimation = doAnimation; }
    public int getxDrawOffset() { return xDrawOffset; }
    public int getyDrawOffset() { return yDrawOffset; }
    public int getAniIndex() { return aniIndex; }
}