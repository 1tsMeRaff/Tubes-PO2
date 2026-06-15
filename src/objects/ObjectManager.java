package objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
    private BufferedImage signImg; // Gambar untuk papan tanda (MediavelFree.png)
    
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Sign> signs = new ArrayList<>(); // Penyimpanan data papan tanda di map

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
        
     
        signImg = LoadSave.GetSpriteAtlas("MediavelFree.png");
    }

    public void update() {
        for (Potion p : potions) { if (p.isActive()) p.update(); }
        for (GameContainer gc : containers) { if (gc.isActive()) gc.update(); }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawSigns(g, xLvlOffset); 
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
    }
    
 // Fungsi Render Papan Tanda & Teks
    private void drawSigns(Graphics g, int xLvlOffset) {
        for (Sign s : signs) {
            int drawX = (int) (s.getHitbox().x - s.getxDrawOffset() - xLvlOffset);
            int drawY = (int) (s.getHitbox().y - s.getyDrawOffset());

            // 1. Selalu gambar papan kayunya
            g.drawImage(signImg, drawX, drawY, SIGN_WIDTH, SIGN_HEIGHT, null);

            // 2. Persiapkan ukuran Font untuk SEMUA teks
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(10 * GameCore.SCALE))); 
            FontMetrics fm = g.getFontMetrics();

            // 3. SELALU gambar Teks Tengah (Warna Hitam / Tulisan "E")
            if (s.getMiddleText() != null && !s.getMiddleText().isEmpty()) {
                g.setColor(java.awt.Color.BLACK);
                int mx = drawX + (SIGN_WIDTH / 2) - (fm.stringWidth(s.getMiddleText()) / 2);
                int my = drawY + (SIGN_HEIGHT / 2) + (fm.getAscent() / 3); 
                g.drawString(s.getMiddleText(), mx, my);
            }

            // 4. Hitung jarak karakter dengan papan
            java.awt.geom.Rectangle2D.Float pBox = playStates.getPlayer().getHitbox();
            java.awt.geom.Rectangle2D.Float sBox = s.getHitbox();

            float xDist = Math.abs((pBox.x + pBox.width / 2) - (sBox.x + sBox.width / 2));
            float yDist = Math.abs((pBox.y + pBox.height / 2) - (sBox.y + sBox.height / 2));

            float maxDist = 40 * GameCore.SCALE; 

            // 5. JIKA PEMAIN DEKAT, BARU GAMBAR TEKS ATAS (Warna Putih / Tulisan "gunakan")
            if (xDist < maxDist && yDist < maxDist) {
                if (s.getTopText() != null && !s.getTopText().isEmpty()) {
                    g.setColor(java.awt.Color.WHITE);
                    int tx = drawX + (SIGN_WIDTH / 2) - (fm.stringWidth(s.getTopText()) / 2);
                    int ty = drawY - (int)(8 * GameCore.SCALE); 
                    g.drawString(s.getTopText(), tx, ty);
                }
            }
        }
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

    //CSV Map 
    public void loadObjectsFromMap(int[][] mapData) {
        // 1. Bersihkan arena dari objek map sebelumnya
        potions.clear();
        containers.clear();
        signs.clear();

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
                
                // CONTAINER 
                else if (id == 401 || id == 402) {
                    int yPosContainer = (j * GameCore.TILES_SIZE) - (int)(0 * GameCore.SCALE); 
                    
                    if (id == 401) { containers.add(new GameContainer(xPos, yPosContainer, BOX)); mapData[j][i] = 0; }
                    else if (id == 402) { containers.add(new GameContainer(xPos, yPosContainer, BARREL)); mapData[j][i] = 0; }
                }
                
     
                
                //sign
                else if (id >= 500 && id <= 503) {
                	int yPosSign = (j * GameCore.TILES_SIZE) + (int)(15 * GameCore.SCALE);
                	
                	int xPosSign = xPos + (int)(4 * GameCore.SCALE);
                    
                	if (id == 500) { 
                	    signs.add(new Sign(xPosSign, yPosSign, SIGN, "E", "gunakan")); 
                    } 
             

                    
                    mapData[j][i] = 0; 
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
    
    // Metho objek olid
    public GameContainer getIntersectingContainer(Rectangle2D.Float nextHitbox) {
        for (GameContainer gc : containers) {
          
            if (gc.isActive() && !gc.doAnimation) {
                if (nextHitbox.intersects(gc.getHitbox())) {
                    return gc;
                }
            }
        }
        return null; 
    }
}