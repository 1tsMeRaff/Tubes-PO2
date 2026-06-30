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
    private BufferedImage kandangImg;
    private BufferedImage kandangBwImg;
    private BufferedImage[] equipmentImgs;
    
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Sign> signs = new ArrayList<>();
    private ArrayList<Cage> cages = new ArrayList<>();
    private ArrayList<DroppedEquipment> droppedEquipments = new ArrayList<>();

    public static double BOSS_ARMOR_DROP_CHANCE = 0.75; // 75% Drop Boss

    public ObjectManager(PlayStates playStates) {
        this.playStates = playStates;
        loadImgs();
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        // Cek Sentuhan Potion
        for (Potion p : potions) {
            if (p.isActive() && hitbox.intersects(p.getHitbox())) {
                if (playStates.getPlayer().addItemToInventory(p.getObjType())) {
                    p.setActive(false);
                }
            }
        }

        // Cek Sentuhan Equipment Jatuh
        for (int i = 0; i < droppedEquipments.size(); i++) {
            DroppedEquipment de = droppedEquipments.get(i);
            if (de.isActive() && hitbox.intersects(de.getHitbox())) {
                if (playStates.getPlayer().addItemToInventory(de.getItemType())) {
                    de.setActive(false);
                }
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
        
        for (Cage c : cages) {
            if (c.isActive() && !c.isBlinking() && c.getHitbox().intersects(attackbox)) {
                c.setHit();
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
        kandangImg = LoadSave.GetSpriteAtlas("kandang .png");
        kandangBwImg = new BufferedImage(kandangImg.getWidth(), kandangImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < kandangImg.getHeight(); y++) {
            for (int x = 0; x < kandangImg.getWidth(); x++) {
                int p = kandangImg.getRGB(x, y);
                int a = (p >> 24) & 0xff; 
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;  
                int b = p & 0xff;
                int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                kandangBwImg.setRGB(x, y, p);
            }
        }

        equipmentImgs = new BufferedImage[6];
        try {
            equipmentImgs[0] = LoadSave.GetSpriteAtlas("helmet.png");
            equipmentImgs[1] = LoadSave.GetSpriteAtlas("armor.png");
            equipmentImgs[2] = LoadSave.GetSpriteAtlas("Legs.png");
            equipmentImgs[3] = LoadSave.GetSpriteAtlas("ring.png");
            equipmentImgs[4] = LoadSave.GetSpriteAtlas("necklace.png");
            equipmentImgs[5] = LoadSave.GetSpriteAtlas("gloves.png");
        } catch (Exception e) {
            System.out.println("Warning: Ada gambar equipment yang gagal dimuat.");
        }
    }

    public void update() {
        for (Potion p : potions) { if (p.isActive()) p.update(); }
        for (GameContainer gc : containers) { if (gc.isActive()) gc.update(); }
        for (Cage c : cages) { if (c.isActive()) c.update(); } 
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawSigns(g, xLvlOffset);
        drawCages(g, xLvlOffset); 
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        
        for (DroppedEquipment de : droppedEquipments) {
            if (de.isActive()) {
                de.draw(g, xLvlOffset);
            }
        }
    }

    public void spawnEquipment(int x, int y) {
        if (Math.random() <= BOSS_ARMOR_DROP_CHANCE) {
            int randomType = 10 + (int) (Math.random() * 6);
            int eqIndex = randomType - 10; 
            
            if (equipmentImgs != null && eqIndex >= 0 && eqIndex < equipmentImgs.length) {
                BufferedImage itemImg = equipmentImgs[eqIndex];
                droppedEquipments.add(new DroppedEquipment(x, y, randomType, itemImg));
                System.out.println("Boss Menjatuhkan Equipment! ID: " + randomType);
            }
        }
    }
    
    private void drawCages(Graphics g, int xLvlOffset) {
        for (Cage c : cages) {
            if (c.isActive()) {
                int drawX = (int) (c.getHitbox().x - c.getxDrawOffset() - xLvlOffset);
                int drawY = (int) (c.getHitbox().y - c.getyDrawOffset());

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
        droppedEquipments.clear();
    }

    public void loadObjectsFromMap(int[][] mapData) {
        potions.clear();
        containers.clear();
        signs.clear();
        cages.clear();
        droppedEquipments.clear(); 
        
        for (int j = 0; j < mapData.length; j++) {
            for (int i = 0; i < mapData[0].length; i++) {
                int id = mapData[j][i];
                int xPos = i * GameCore.TILES_SIZE;

                if (id >= 500 && id <= 505) {
                    int yPosPotion = (j * GameCore.TILES_SIZE);
                    potions.add(new Potion(xPos, yPosPotion, id - 500));
                    mapData[j][i] = -1;
                }
                else if (id == 601 || id == 602) {
                    int yPosContainer = (j * GameCore.TILES_SIZE);
                    containers.add(new GameContainer(xPos, yPosContainer, (id == 601) ? BOX : BARREL));
                    mapData[j][i] = -1; 
                }
                else if (id >= 700 && id <= 703) {
                    int yPosSign = (j * GameCore.TILES_SIZE) + (int)(15 * GameCore.SCALE);
                    int xPosSign = xPos + (int)(4 * GameCore.SCALE);
                    if (id == 700) signs.add(new Sign(xPosSign, yPosSign, SIGN, "E", "gunakan"));
                    else if (id == 701) signs.add(new Sign(xPosSign, yPosSign, SIGN, "SHOP", "WEAPONS"));
                    else if (id == 702) signs.add(new Sign(xPosSign, yPosSign, SIGN, "DANGER", "BOSS AREA"));
                    else if (id == 703) signs.add(new Sign(xPosSign, yPosSign, SIGN, "SAVE", "POINT"));
                    mapData[j][i] = -1; 
                }
                else if (id == 403) {
                    int yPosKandang = (j * GameCore.TILES_SIZE);
                    cages.add(new Cage(xPos, yPosKandang, KANDANG));
                    mapData[j][i] = -1; 
                }
                // --- MEMUNCULKAN ARMOR LANGSUNG DI MAP JIKA ADA KODE 910-915 ---
                else if (id >= 910 && id <= 915) {
                    int yPosEq = (j * GameCore.TILES_SIZE);
                    int eqType = id - 900; 
                    int eqIndex = eqType - 10;
                    if (equipmentImgs != null && eqIndex >= 0 && eqIndex < equipmentImgs.length) {
                        java.awt.image.BufferedImage itemImg = equipmentImgs[eqIndex];
                        int centerX = xPos + (GameCore.TILES_SIZE / 2);
                        int centerY = yPosEq + (GameCore.TILES_SIZE / 2);
                        droppedEquipments.add(new DroppedEquipment(centerX, centerY, eqType, itemImg));
                    }
                    mapData[j][i] = -1; 
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

    public BufferedImage getItemImg(int type) {
        if (type >= 0 && type <= 5) {
            return getPotionImg(type);
        } 
        else if (type >= 10 && type <= 15) {
            int eqIndex = type - 10;
            if (equipmentImgs != null && eqIndex >= 0 && eqIndex < equipmentImgs.length) {
                return equipmentImgs[eqIndex];
            }
        }
        return null;
    }
    
    public GameContainer getIntersectingContainer(Rectangle2D.Float nextHitbox) {
        for (GameContainer gc : containers) {
            if (gc.isActive() && !gc.doAnimation && nextHitbox.intersects(gc.getHitbox())) {
                return gc;
            }
        }
        return null;
    }

    public Cage getIntersectingCage(Rectangle2D.Float nextHitbox) {
        for (Cage c : cages) {
            if (c.isActive() && !c.isBlinking() && nextHitbox.intersects(c.getHitbox())) {
                return c;
            }
        }
        return null; 
    }
}