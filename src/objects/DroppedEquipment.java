package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import main.GameCore;

public class DroppedEquipment {
    private Rectangle2D.Float hitbox;
    private int itemType; // ID Tipe Item (10 - 15)
    private boolean active = true;
    private BufferedImage img;

    public DroppedEquipment(int x, int y, int itemType, BufferedImage img) {
        this.itemType = itemType;
        this.img = img;
        
        // Ukuran item di tanah (disesuaikan dengan skala game Anda, misal 22x22 px)
        int size = (int) (22 * GameCore.SCALE);
        // Posisikan tepat di tengah lokasi matinya monster
        this.hitbox = new Rectangle2D.Float(x - size / 2, y - size / 2, size, size);
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (active && img != null) {
            g.drawImage(img, 
                (int) (hitbox.x - xLvlOffset), 
                (int) hitbox.y, 
                (int) hitbox.width, 
                (int) hitbox.height, null);
        }
    }

    public Rectangle2D.Float getHitbox() { return hitbox; }
    public int getItemType() { return itemType; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}