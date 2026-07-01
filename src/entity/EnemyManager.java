package entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.EnemyConstants.*;

public class EnemyManager {

    private PlayStates playStates;
    
    // Menggunakan Map untuk menyimpan sprite atlas semua musuh secara dinamis
    private Map<Integer, BufferedImage[][]> enemySpriteMap = new HashMap<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public EnemyManager(PlayStates playStates) {
        this.playStates = playStates;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        enemies.clear();
        enemies.addAll(LoadSave.GetSlimes("/map_tutorial_fix.txt"));
        enemies.addAll(LoadSave.GetDemonBosses("/map_tutorial_fix.txt"));
    }

    public void update(int[][] tilesData, Player player) {
        for (Enemy e : enemies) {
            if (e.isActive()) {
                // Memanggil update() dari masing-masing subclass musuh tanpa instanceof
                e.update(tilesData, player); 
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        Graphics g2 = g.create();
        g2.translate(-xLvlOffset, 0);
        
        for (Enemy e : enemies) {
            if (e.isActive()) {
                BufferedImage[][] atlas = enemySpriteMap.get(e.getEnemyType());
                if (atlas != null) {
                    // Tiap musuh bertanggung jawab menggambar dirinya sendiri
                    e.draw(g2, xLvlOffset, atlas); 
                }
            }
        } 
        g2.dispose();
        
        drawBossUI(g);
    }
    
    private void drawBossUI(Graphics g) {
        for (Enemy e : enemies) {
            // Cukup cek tipe menggunakan method bawaan Enemy, tidak perlu instanceof
            if (e.getEnemyType() == DEMON_BOSS && e.isActive()) {
                int maxWidth = (int) (400 * GameCore.SCALE); 
                int height = (int) (20 * GameCore.SCALE);
                int xPos = (GameCore.GAME_WIDTH / 2) - (maxWidth / 2);
                int yPos = (int) (GameCore.GAME_HEIGHT - (40 * GameCore.SCALE));
                
                float distance = Math.abs(playStates.getPlayer().getHitbox().x - e.getHitBox().x);
                float healthPercentage = (float) e.getCurrentHealth() / e.getMaxHealth(); // Pastikan getMaxHealth() public di Enemy
                int currentWidth = (int) (maxWidth * healthPercentage);

                if (currentWidth < 0) currentWidth = 0;

                if (distance < GameCore.GAME_WIDTH) {
                    g.setColor(new java.awt.Color(50, 50, 50, 200));
                    g.fillRect(xPos, yPos, maxWidth, height);

                    g.setColor(new java.awt.Color(200, 50, 50));
                    g.fillRect(xPos, yPos, currentWidth, height);

                    g.setColor(java.awt.Color.WHITE);
                    g.drawRect(xPos, yPos, maxWidth, height);

                    g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, (int)(16 * GameCore.SCALE)));
                    g.drawString("DEMON BOSS", xPos, yPos - (int)(5 * GameCore.SCALE));
                    
                    break; // Hanya render UI untuk satu Boss yang aktif
                }
            }
        }
    }
    
    public void checkEnemyHit(Rectangle2D.Float attackBox, int damage, Player player) {
        for (Enemy e : enemies) {
            if (e.isActive() && attackBox.intersects(e.getHitBox())) {
                
                // 1. Menggunakan Unified Hit System dari branch dev-Rafi
                e.hit(damage, player, playStates);

                // 2. Menyelamatkan fitur spesifik dari branch dev (Loot, EXP, Screen Shake)
                if (e.getEnemyType() == DEMON_BOSS) {
                    DemonBoss demonBoss = (DemonBoss) e;

                    // Logika ketika Boss mati
                    if (demonBoss.getCurrentHealth() <= 0 || demonBoss.isDead()) {
                        playStates.getPlayer().gainExp(100);
                        
                        // Spawn item equipment
                        playStates.getObjectManager().spawnEquipment(
                            (int) demonBoss.getHitBox().x, 
                            (int) demonBoss.getHitBox().y
                        );
                        
                        // Guncangan besar saat mati
                        playStates.triggerHeavyHit(45, 120, 8); 
                    } 
                    // Logika ketika HP Boss rendah
                    else if (demonBoss.checkHpThresholdEffect()) {
                        playStates.triggerHeavyHit(20, 25, 12); 
                    }
                }
                
                return; // Keluar dari loop setelah berhasil mengenai 1 musuh
            }
        }
    }
    
    private void loadEnemyImages() {
        // Load Slime
        BufferedImage[][] slimeImg = new BufferedImage[5][9];
        BufferedImage tempSlime = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
        for (int j = 0; j < slimeImg.length; j++) {
            for (int i = 0; i < slimeImg[j].length; i++) {
                slimeImg[j][i] = tempSlime.getSubimage(i * SLIME_WIDTH_DEFAULT, j * SLIME_HEIGHT_DEFAULT,
                        SLIME_WIDTH_DEFAULT, SLIME_HEIGHT_DEFAULT);
            }
        }
        enemySpriteMap.put(SLIME, slimeImg);
        
        // Load Demon Boss
        BufferedImage[][] demonBossImg = new BufferedImage[6][DEMON_BOSS_SPRITE_COLUMNS];
        BufferedImage demonBossSheet = LoadSave.GetSpriteAtlas(LoadSave.DEMON_BOSS_SPRITE);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, IDLE, 0);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, WALK, 1);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, ATTACK, 2);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, HURT, 3);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, MATI, 4);
        enemySpriteMap.put(DEMON_BOSS, demonBossImg);
    }
    
    private void loadDemonBossAnimation(BufferedImage[][] targetArray, BufferedImage sheet, int targetState, int sourceRow) {
        for (int i = 0; i < GetSpriteAmount(DEMON_BOSS, targetState); i++) {
            targetArray[targetState][i] = sheet.getSubimage(i * DEMON_BOSS_WIDTH_DEFAULT,
                    sourceRow * DEMON_BOSS_HEIGHT_DEFAULT, DEMON_BOSS_WIDTH_DEFAULT, DEMON_BOSS_HEIGHT_DEFAULT);
        }
    }

    public void resetAllEnemies() {
        for (Enemy e : enemies) {
            e.resetEnemy(); 
        }
    }
}