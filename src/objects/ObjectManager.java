package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.PlayStates;
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
                applyEffectToPlayer(p);
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
            if (gc.isActive() && gc.getHitbox().intersects(attackbox)) {
                gc.setAnimation(true);
                
                int droppedPotionType = RED_POTION_1;
                if (gc.getObjType() == BOX) {
                    droppedPotionType = BLUE_POTION_1;
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
            }
        }
    }

    public void resetAllObjects() {
        for (Potion p : potions) p.reset();
        for (GameContainer gc : containers) gc.reset();
    }

    public void addTestObjects() {
        // Angka 580 adalah posisi Y agar mereka mendarat di lantai
        potions.add(new Potion(650, 800, 0)); 
        potions.add(new Potion(700, 800, 3)); 
        containers.add(new GameContainer(580, 800, 7)); 
    }
}