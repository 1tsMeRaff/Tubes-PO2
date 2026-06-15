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
    private BufferedImage signImg;
    
    // Variabel untuk Kandang
    private BufferedImage kandangImg;
    private BufferedImage kandangBwImg; // Kandang versi hitam putih transparan
    
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Sign> signs = new ArrayList<>();
    private ArrayList<Cage> cages = new ArrayList<>(); // Penyimpanan kandang

    public ObjectManager(PlayStates playStates) {
        this.playStates = playStates;
        loadImgs();
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions) {
            if (p.isActive() && hitbox.intersects(p.getHitbox())) {
                p.setActive(false);
                playStates.getPlayer().addItemToInventory(p.getObjType());
            }
        }
    }

    public void applyEffectToPlayer(Potion p) {
        switch (p.getObjType()) {
            case RED_POTION_1: case RED_POTION_2: case RED_POTION_3:
                playStates.getPlayer().changeHealth(20); 
                break;
            case BLUE_POTION_1: case BLUE_POTION_2: case BLUE_POTION_3:
                playStates.getPlayer().changeMana(20); 
                break;
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        // Cek tabrakan pedang dengan Box/Barrel
        for (GameContainer gc : containers) {
            if (gc.isActive() && !gc.doAnimation && gc.getHitbox().intersects(attackbox)) {
                gc.setAnimation(true);
                int droppedPotionType = (gc.getObjType() == BOX) ? BLUE_POTION_1 : RED_POTION_1;
                potions.add(new Potion(
                    (int) (gc.getHitbox().x + gc.getHitbox().width / 2),
                    (int) (gc.getHitbox().y - gc.getHitbox().height / 2),
                    droppedPotionType)
                );
                return;
            }
        }
        
        // Cek tabrakan pedang dengan Kandang
        for (Cage c : cages) {
            if (c.isActive() && !c.isBlinking() && c.getHitbox().intersects(attackbox)) {
                c.setHit(); // Mulai kedip hitam putih
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
                    potionImgs[idObjek][frame] = potionSprite.getSubimage(7 + (kolomGambar * 35), yKordinat[baris], 16, 16);
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
        
        // Load gambar kandang asli
        kandangImg = LoadSave.GetSpriteAtlas("kandang .png");
        
        // Buat versi hitam-putih TAPI tetap mempertahankan background transparan (ARGB)
        kandangBwImg = new BufferedImage(kandangImg.getWidth(), kandangImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        for (int y = 0; y < kandangImg.getHeight(); y++) {
            for (int x = 0; x < kandangImg.getWidth(); x++) {
                int p = kandangImg.getRGB(x, y);
                
                int a = (p >> 24) & 0xff; // Ambil nilai Transparan (Alpha)
                int r = (p >> 16) & 0xff; // Merah
                int g = (p >> 8) & 0xff;  // Hijau
                int b = p & 0xff;         // Biru
                
                // Hitung rata-rata warna untuk menjadikannya abu-abu
                int avg = (r + g + b) / 3;
                
                // Gabungkan kembali warnanya dengan Alpha (transparan) yang asli
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                kandangBwImg.setRGB(x, y, p);
            }
        }
    }

    public void update() {
        for (Potion p : potions) { if (p.isActive()) p.update(); }
        for (GameContainer gc : containers) { if (gc.isActive()) gc.update(); }
        for (Cage c : cages) { if (c.isActive()) c.update(); } // Update waktu kedip kandang
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawSigns(g, xLvlOffset); 
        drawCages(g, xLvlOffset); // Panggil gambar kandang
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
    }
    
    // Render Kandang
    private void drawCages(Graphics g, int xLvlOffset) {
        for (Cage c : cages) {
            if (c.isActive()) {
                int drawX = (int) (c.getHitbox().x - c.getxDrawOffset() - xLvlOffset);
                int drawY = (int) (c.getHitbox().y - c.getyDrawOffset());

                // Logika Berkedip (Tukar gambar normal & hitam-putih tiap 5 frame)
                if (c.isBlinking() && (c.getBlinkTick() / 5) % 2 == 0) {
                    g.drawImage(kandangBwImg, drawX, drawY, KANDANG_WIDTH, KANDANG_HEIGHT, null);
                } else {
                    g.drawImage(kandangImg, drawX, drawY, KANDANG_WIDTH, KANDANG_HEIGHT, null);
                }
            }
        }
    }

    private void drawSigns(Graphics g, int xLvlOffset) {
        for (Sign s : signs) {
            int drawX = (int) (s.getHitbox().x - s.getxDrawOffset() - xLvlOffset);
            int drawY = (int) (s.getHitbox().y - s.getyDrawOffset());

            g.drawImage(signImg, drawX, drawY, SIGN_WIDTH, SIGN_HEIGHT, null);
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(10 * GameCore.SCALE))); 
            FontMetrics fm = g.getFontMetrics();

            if (s.getMiddleText() != null && !s.getMiddleText().isEmpty()) {
                g.setColor(Color.BLACK);
                int mx = drawX + (SIGN_WIDTH / 2) - (fm.stringWidth(s.getMiddleText()) / 2);
                int my = drawY + (SIGN_HEIGHT / 2) + (fm.getAscent() / 3); 
                g.drawString(s.getMiddleText(), mx, my);
            }

            Rectangle2D.Float pBox = playStates.getPlayer().getHitbox();
            Rectangle2D.Float sBox = s.getHitbox();
            float xDist = Math.abs((pBox.x + pBox.width / 2) - (sBox.x + sBox.width / 2));
            float yDist = Math.abs((pBox.y + pBox.height / 2) - (sBox.y + sBox.height / 2));
            float maxDist = 40 * GameCore.SCALE; 

            if (xDist < maxDist && yDist < maxDist) {
                if (s.getTopText() != null && !s.getTopText().isEmpty()) {
                    g.setColor(Color.WHITE);
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
                int type = (gc.getObjType() == BOX) ? 1 : 0; 
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
                g.drawImage(potionImgs[p.getObjType()][p.getAniIndex()], 
                    (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), 
                    (int) (p.getHitbox().y - p.getyDrawOffset()), 
                    POTION_WIDTH, POTION_HEIGHT, null);
            }
        }
    }

    public void resetAllObjects() {
        for (Potion p : potions) p.reset();
        for (GameContainer gc : containers) gc.reset();
        for (Cage c : cages) c.reset();
    }

    public void loadObjectsFromMap(int[][] mapData) {
        potions.clear();
        containers.clear();
        signs.clear(); 
        cages.clear(); // Bersihkan memori kandang

        for (int j = 0; j < mapData.length; j++) {
            for (int i = 0; i < mapData[0].length; i++) {
                
                int id = mapData[j][i];
                int xPos = i * GameCore.TILES_SIZE;
                
                if (id >= 300 && id <= 305) {
                    int yPosPotion = (j * GameCore.TILES_SIZE);
                    potions.add(new Potion(xPos, yPosPotion, id - 300)); 
                    mapData[j][i] = 0; 
                }
                else if (id == 401 || id == 402) {
                    int yPosContainer = (j * GameCore.TILES_SIZE); 
                    containers.add(new GameContainer(xPos, yPosContainer, (id == 401) ? BOX : BARREL)); 
                    mapData[j][i] = 0; 
                }
                else if (id >= 500 && id <= 503) {
                    int yPosSign = (j * GameCore.TILES_SIZE) + (int)(15 * GameCore.SCALE);
                    int xPosSign = xPos + (int)(4 * GameCore.SCALE);
                    
                    if (id == 500) signs.add(new Sign(xPosSign, yPosSign, SIGN, "E", "gunakan")); 
                    else if (id == 501) signs.add(new Sign(xPosSign, yPosSign, SIGN, "SHOP", "WEAPONS")); 
                    else if (id == 502) signs.add(new Sign(xPosSign, yPosSign, SIGN, "DANGER", "BOSS AREA")); 
                    else if (id == 503) signs.add(new Sign(xPosSign, yPosSign, SIGN, "SAVE", "POINT")); 

                    mapData[j][i] = 0; 
                }
              
                // --- KANDANG (Menggunakan ID 403) ---
                else if (id == 403) {
                    int yPosKandang = (j * GameCore.TILES_SIZE);
                    cages.add(new Cage(xPos, yPosKandang, KANDANG));
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
    
    // METHOD UNTUK BARREL / BOX (Jangan dihapus agar error di Player hilang)
    public GameContainer getIntersectingContainer(Rectangle2D.Float nextHitbox) {
        for (GameContainer gc : containers) {
            if (gc.isActive() && !gc.doAnimation && nextHitbox.intersects(gc.getHitbox())) {
                return gc; 
            }
        }
        return null; 
    }

    // METHOD BARU KHUSUS UNTUK KANDANG (CAGE)
    public Cage getIntersectingCage(Rectangle2D.Float nextHitbox) {
        for (Cage c : cages) {
            if (c.isActive() && !c.isBlinking() && nextHitbox.intersects(c.getHitbox())) {
                return c; 
            }
        }
        return null; 
    }
}