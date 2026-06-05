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
        // Terapkan efek sesuai dengan ID potion
        switch (p.getObjType()) {
            case RED_POTION_1:
                // playStates.getPlayer().changeHealth(RED_VAL_1);
                break;
            case RED_POTION_2:
                // playStates.getPlayer().changeHealth(RED_VAL_2);
                break;
            case RED_POTION_3:
                // playStates.getPlayer().changeHealth(RED_VAL_3);
                break;
            case BLUE_POTION_1:
                // playStates.getPlayer().changePower(BLUE_VAL_1); 
                break;
            case BLUE_POTION_2:
                // playStates.getPlayer().changePower(BLUE_VAL_2);
                break;
            case BLUE_POTION_3:
                // playStates.getPlayer().changePower(BLUE_VAL_3);
                break;
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers) {
            if (gc.isActive() && gc.getHitbox().intersects(attackbox)) {
                gc.setAnimation(true);
                
                // Menentukan potion apa yang keluar saat box/barrel hancur
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
        // --- Memuat Potion ---
        // Pastikan nama filenya sudah sesuai dengan yang baru!
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas("potion_sprites.png"); 
        potionImgs = new BufferedImage[6][3]; 

        // Kordinat Y dari datamu: Baris 0 = 8, Baris 1 = 34
        int[] yKordinat = {8, 34}; 

        for (int baris = 0; baris < 2; baris++) { 
            for (int tipe = 0; tipe < 3; tipe++) { 
                for (int frame = 0; frame < 3; frame++) { 
                    
                    int idObjek = (baris * 3) + tipe; 
                    int kolomGambar = (tipe * 3) + frame; 
                    
                    // Menghitung titik X (Mulai dari 7, jarak antar frame 35)
                    int potongX = 7 + (kolomGambar * 35);
                    // Mengambil titik Y dari array (8 atau 34)
                    int potongY = yKordinat[baris];
                 
                    // Memotong dengan ukuran 16x16 sesuai datamu
                    potionImgs[idObjek][frame] = potionSprite.getSubimage(potongX, potongY, 16, 16);
                }
            }
        }

        // --- Memuat Kontainer (Barrel & Box) ---
        BufferedImage containerSprite = LoadSave.GetSpriteAtlas("objects_sprites.png"); 
        containerImgs = new BufferedImage[2][8];

        for (int j = 0; j < containerImgs.length; j++) {
            for (int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
            }
        }
    }

    public void update() {
        for (Potion p : potions) {
            if (p.isActive()) p.update();
        }
        for (GameContainer gc : containers) {
            if (gc.isActive()) gc.update();
        }
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
                    CONTAINER_WIDTH, 
                    CONTAINER_HEIGHT, null);
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
                    POTION_WIDTH, 
                    POTION_HEIGHT, null);
            }
        }
    }

    public void resetAllObjects() {
        for (Potion p : potions) p.reset();
        for (GameContainer gc : containers) gc.reset();
    }

    // Method uji coba untuk memunculkan item secara manual
    public void addTestObjects() {
        // Koordinat x dan y disesuaikan agar terlihat di layar
        potions.add(new Potion(300, 200, 0)); // Memunculkan Potion Merah Tipe 1
        potions.add(new Potion(350, 200, 3)); // Memunculkan Potion Biru Tipe 1
        
        containers.add(new GameContainer(450, 200, 7)); // Memunculkan Box
    }
}