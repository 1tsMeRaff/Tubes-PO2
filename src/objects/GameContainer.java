package objects;

import main.GameCore;
import utilitytools.Konstanta.ObjectConstants;
import objects.GameObject;

public class GameContainer extends GameObject {

    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() {
        if (objType == ObjectConstants.BOX) {
            initHitbox(25, 18);
            xDrawOffset = (int)(7 * GameCore.SCALE);
            yDrawOffset = (int)(12 * GameCore.SCALE);
        } else { // BARREL
            initHitbox(23, 25);
            xDrawOffset = (int)(8 * GameCore.SCALE);
            yDrawOffset = (int)(5 * GameCore.SCALE);
        }

        // Penyesuaian kordinat Hitbox agar pas di tengah gambar
        hitbox.y += yDrawOffset + (int)(2 * GameCore.SCALE);
        hitbox.x += xDrawOffset / 2;
    }

    public void update() {
        if (doAnimation) {
            updateAnimationTick();
        }
    }
}