package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.ObjectConstants.*;

public class ObjectManager {
    private PlayStates playStates;
    private BufferedImage[][] potionImgs, containerImgs;
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();

    public ObjectManager(PlayStates playStates) {
        this.playStates = playStates;
        loadImgs();
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions) {
            if (p.isActive() && hitbox.intersects(p.getHitbox())) {
                p.setActive(false);
                // Langsung masukkan ke penyimpanan
                playStates.getPlayer().addItemToInventory(p.getObjType());
            }
        }
    }

    public void applyEffectToPlayer(Potion p) {
        // Efek Potion ditambahkan ke Player
        switch (p.getObjType()) {
            case RED_POTION_1:
            case RED_POTION_2:
            case RED_POTION_3:
                playStates.getPlayer().changeHealth(20); // Tambah 20 HP
                break;
            case BLUE_POTION_1:
            case BLUE_POTION_2:
            case BLUE_POTION_3:
                playStates.getPlayer().changeMana(20); // Tambah 20 MP
                break;
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers) {
            // Tambahkan !gc.doAnimation agar tidak dipukul berkali-kali
            if (gc.isActive() && !gc.doAnimation && gc.getHitbox().intersects(attackbox)) {
                gc.setAnimation(true);

                int droppedPotionType = RED_POTION_1; // Barrel menjatuhkan ramuan merah
                if (gc.getObjType() == BOX) {
                    droppedPotionType = BLUE_POTION_1; // Box menjatuhkan ramuan biru
                }

                potions.add(new Potion(
                    (int) (gc.getHitbox().x + gc.getHitbox().width / 2),
                    (int) (gc.getHitbox().y - gc.getHitbox().height / 2),
                    droppedPotionType)
                );
                return;
            }
        }
    }

    public void loadObjects(ArrayList<Potion> p, ArrayList<GameContainer> c) {
        this.potions = p;
        this.containers = c;
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas("potion_sprites.png"); 
        potionImgs = new BufferedImage[6][3]; 
        int[] yKordinat = {8, 34}; 

        for (int baris = 0; baris < 2; baris++) { 
            for (int tipe = 0; tipe < 3; tipe++) { 
                for (int frame = 0; frame < 3; frame++) { 
                    int idObjek = (baris * 3) + tipe; 
                    int kolomGambar = (tipe * 3) + frame; 
                    int potongX = 7 + (kolomGambar * 35);
                    int potongY = yKordinat[baris];
                    potionImgs[idObjek][frame] = potionSprite.getSubimage(potongX, potongY, 16, 16);
                }
            }
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas("objects_sprites.png"); 
        containerImgs = new BufferedImage[2][8];
        for (int j = 0; j < containerImgs.length; j++) {
            for (int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
            }
        }
    }

    public void update() {
        for (Potion p : potions) { if (p.isActive()) p.update(); }
        for (GameContainer gc : containers) { if (gc.isActive()) gc.update(); }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers) {
            if (gc.isActive()) {
                int type = 0; 
                if (gc.getObjType() == BOX) type = 1; 
                g.drawImage(containerImgs[type][gc.getAniIndex()], 
                    (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), 
                    (int) (gc.getHitbox().y - gc.getyDrawOffset()), 
                    CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions) {
            if (p.isActive()) {
                int type = p.getObjType(); 
                g.drawImage(potionImgs[type][p.getAniIndex()], 
                    (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), 
                    (int) (p.getHitbox().y - p.getyDrawOffset()), 
                    POTION_WIDTH, POTION_HEIGHT, null);
                
                // Hilangkan tanda // pada dua baris di bawah ini jika ingin melihat hitbox merah (untuk debug)
                // g.setColor(java.awt.Color.RED);
                // g.drawRect((int) (p.getHitbox().x - xLvlOffset), (int) p.getHitbox().y, (int) p.getHitbox().width, (int) p.getHitbox().height);
            }
        }
    }

    public void resetAllObjects() {
        for (Potion p : potions) p.reset();
        for (GameContainer gc : containers) gc.reset();
    }

    // Method pembaca CSV
    public void loadObjectsFromMap(int[][] mapData) {
        // 1. Bersihkan arena dari objek map sebelumnya
        potions.clear();
        containers.clear();
        

        for (int j = 0; j < mapData.length; j++) {
            for (int i = 0; i < mapData[0].length; i++) {
                
                int id = mapData[j][i];
                int xPos = i * GameCore.TILES_SIZE;
                
                //  RAMUAN 
                if (id >= 300 && id <= 305) {
                    int yPosPotion = (j * GameCore.TILES_SIZE) + (int)(0 * GameCore.SCALE);
                    
                    if (id == 300) { potions.add(new Potion(xPos, yPosPotion, RED_POTION_1)); mapData[j][i] = 0; }
                    else if (id == 301) { potions.add(new Potion(xPos, yPosPotion, RED_POTION_2)); mapData[j][i] = 0; }
                    else if (id == 302) { potions.add(new Potion(xPos, yPosPotion, RED_POTION_3)); mapData[j][i] = 0; }
                    else if (id == 303) { potions.add(new Potion(xPos, yPosPotion, BLUE_POTION_1)); mapData[j][i] = 0; }
                    else if (id == 304) { potions.add(new Potion(xPos, yPosPotion, BLUE_POTION_2)); mapData[j][i] = 0; }
                    else if (id == 305) { potions.add(new Potion(xPos, yPosPotion, BLUE_POTION_3)); mapData[j][i] = 0; }
                }
                
           
                else if (id == 401 || id == 402) {
                    int yPosContainer = (j * GameCore.TILES_SIZE) - (int)(0 * GameCore.SCALE); 
                    
                    if (id == 401) { containers.add(new GameContainer(xPos, yPosContainer, BOX)); mapData[j][i] = 0; }
                    else if (id == 402) { containers.add(new GameContainer(xPos, yPosContainer, BARREL)); mapData[j][i] = 0; }
                }
            }
        }
    }
    
    public BufferedImage getPotionImg(int type) {
        if (potionImgs != null && type >= 0 && type < potionImgs.length) {
            return potionImgs[type][0];
        }
        return null;
    }
    
    // Method untuk mengecek tabrakan pemain dengan objek yang solid
    public GameContainer getIntersectingContainer(Rectangle2D.Float nextHitbox) {
        for (GameContainer gc : containers) {
            // Objek dianggap solid hanya jika dia masih aktif dan BUKAN sedang hancur
            if (gc.isActive() && !gc.doAnimation) {
                if (nextHitbox.intersects(gc.getHitbox())) {
                    return gc; // Kembalikan data kotak yang ditabrak
                }
            }
        }
        return null; // Tidak nabrak apa-apa
    }
}