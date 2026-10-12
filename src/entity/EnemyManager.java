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
    
    private Map<Integer, BufferedImage[][]> enemySpriteMap = new HashMap<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public EnemyManager(PlayStates playStates) {
        this.playStates = playStates;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        enemies.clear();
        if (playStates != null && playStates.getWorldManager() != null && playStates.getWorldManager().getCurrentMap() != null) {
            loadEnemies(playStates.getWorldManager().getCurrentMap().getEnemies());
        }
    }

    public void loadEnemies(ArrayList<Enemy> newEnemies) {
        enemies.clear();
        if (newEnemies != null) {
            for (Enemy e : newEnemies) {
                e.resetEnemy();
                enemies.add(e);
            }
        }
    }

    public void update(int[][] tilesData, Player player) {
        for (Enemy e : enemies) {
            if (e.isActive()) {
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
                    e.draw(g2, xLvlOffset, atlas); 
                }
            }
        } 
        g2.dispose();
        
        drawBossUI(g);
    }
    
    private static final java.awt.Color BOSS_HP_BG = new java.awt.Color(50, 50, 50, 200);
    private static final java.awt.Color BOSS_HP_BAR = new java.awt.Color(200, 50, 50);
    private final java.awt.Font bossFont = new java.awt.Font("Arial", java.awt.Font.BOLD, (int)(16 * GameCore.SCALE));

    private void drawBossUI(Graphics g) {
        for (Enemy e : enemies) {
            if (e instanceof Boss && e.isActive()) {
                Boss boss = (Boss) e;
                int maxWidth = (int) (400 * GameCore.SCALE);
                int height = (int) (20 * GameCore.SCALE);
                int xPos = (GameCore.GAME_WIDTH / 2) - (maxWidth / 2);
                int yPos = (int) (GameCore.GAME_HEIGHT - (40 * GameCore.SCALE));

                float distance = Math.abs(playStates.getPlayer().getHitbox().x - boss.getHitBox().x);
                float healthPercentage = (float) boss.getCurrentHealth() / boss.getMaxHealth();
                int currentWidth = (int) (maxWidth * healthPercentage);

                if (currentWidth < 0) currentWidth = 0;

                if (distance < GameCore.GAME_WIDTH) {
                    g.setColor(BOSS_HP_BG);
                    g.fillRect(xPos, yPos, maxWidth, height);

                    g.setColor(BOSS_HP_BAR);
                    g.fillRect(xPos, yPos, currentWidth, height);

                    g.setColor(java.awt.Color.WHITE);
                    g.drawRect(xPos, yPos, maxWidth, height);

                    g.setFont(bossFont);
                    g.drawString(boss.getBossTitle(), xPos, yPos - (int)(5 * GameCore.SCALE));

                    break;
                }
            }
        }
    }
    
    public void checkEnemyHit(Rectangle2D.Float attackBox, int damage, Player player) {
        for (Enemy e : enemies) {
            if (e.isActive() && attackBox.intersects(e.getHitBox())) {
                e.hit(damage, player, playStates);

                float hitX = e.getHitBox().x + (e.getHitBox().width / 2.0f);
                float hitY = e.getHitBox().y + (e.getHitBox().height / 2.0f);
                playStates.getParticlePool().spawnHitSparks(hitX, hitY, 10);
                playStates.getFloatingTextPool().spawnDamageDealt(hitX, e.getHitBox().y - 5, damage);

                if (e.getEnemyType() == DEMON_BOSS) {
                    DemonBoss demonBoss = (DemonBoss) e;

                    if (demonBoss.getCurrentHealth() <= 0 || demonBoss.isDead()) {
                        playStates.getPlayer().gainExp(100);
                        
                        playStates.getObjectManager().spawnEquipment(
                            (int) demonBoss.getHitBox().x, 
                            (int) demonBoss.getHitBox().y
                        );
                        
                        playStates.triggerHeavyHit(45, 120, 8); 
                    } 
                    else if (demonBoss.checkHpThresholdEffect()) {
                        playStates.triggerHeavyHit(20, 25, 12); 
                    }
                }
                
                return;
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
        
        BufferedImage[][] skullwolfImg = new BufferedImage[4][7];
        BufferedImage tempSkullwolf = LoadSave.GetSpriteAtlas(LoadSave.SKULLWOLF_SPRITE);
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 7; i++) {
                skullwolfImg[j][i] = tempSkullwolf.getSubimage(
                    i * SKULLWOLF_WIDTH_DEFAULT, 
                    j * SKULLWOLF_HEIGHT_DEFAULT,
                    SKULLWOLF_WIDTH_DEFAULT, 
                    SKULLWOLF_HEIGHT_DEFAULT
                );
            }
        }
        enemySpriteMap.put(SKULLWOLF, skullwolfImg);
        
        BufferedImage[][] bringerImg = new BufferedImage[8][BRINGER_SPRITE_COLUMNS]; 
        BufferedImage bringerSheet = LoadSave.GetSpriteAtlas("Bringer_of_Death.png");
        
        loadBringerAnimation(bringerImg, bringerSheet, IDLE, 0);
        loadBringerAnimation(bringerImg, bringerSheet, WALK, 1);
        loadBringerAnimation(bringerImg, bringerSheet, ATTACK, 2);
        loadBringerAnimation(bringerImg, bringerSheet, HURT, 3);
        loadBringerAnimation(bringerImg, bringerSheet, MATI, 4);
        enemySpriteMap.put(BRINGER_OF_DEATH, bringerImg);       
        
        BufferedImage[][] demonBossImg = new BufferedImage[6][DEMON_BOSS_SPRITE_COLUMNS];
        BufferedImage demonBossSheet = LoadSave.GetSpriteAtlas(LoadSave.DEMON_BOSS_SPRITE);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, IDLE, 0);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, WALK, 1);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, ATTACK, 2);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, HURT, 3);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, MATI, 4);
        enemySpriteMap.put(DEMON_BOSS, demonBossImg);
        
        BufferedImage[][] blueGolemImg = new BufferedImage[6][BLUE_GOLEM_SPRITE_COLUMNS];
        BufferedImage blueGolemSheet = LoadSave.GetSpriteAtlas(LoadSave.BLUE_GOLEM_SPRITE);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, IDLE, 3);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, WALK, 4);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, ATTACK, 0);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, HURT, 2);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, MATI, 1);
        enemySpriteMap.put(BLUE_GOLEM, blueGolemImg);
    }
    
    private void loadDemonBossAnimation(BufferedImage[][] targetArray, BufferedImage sheet, int targetState, int sourceRow) {
        for (int i = 0; i < GetSpriteAmount(DEMON_BOSS, targetState); i++) {
            targetArray[targetState][i] = sheet.getSubimage(i * DEMON_BOSS_WIDTH_DEFAULT,
                    sourceRow * DEMON_BOSS_HEIGHT_DEFAULT, DEMON_BOSS_WIDTH_DEFAULT, DEMON_BOSS_HEIGHT_DEFAULT);
        }
    }
    
    private void loadBlueGolemAnimation(BufferedImage[][] targetArray, BufferedImage sheet, int targetState, int sourceRow) {
        for (int i = 0; i < GetSpriteAmount(BLUE_GOLEM, targetState); i++) {
            targetArray[targetState][i] = sheet.getSubimage(
                i * BLUE_GOLEM_WIDTH_DEFAULT,
                sourceRow * BLUE_GOLEM_HEIGHT_DEFAULT, 
                BLUE_GOLEM_WIDTH_DEFAULT, 
                BLUE_GOLEM_HEIGHT_DEFAULT
            );
        }
    }
    
    private void loadBringerAnimation(BufferedImage[][] imgArr, BufferedImage sheet, int state, int row) {
        for (int i = 0; i < imgArr[state].length; i++) {
            imgArr[state][i] = sheet.getSubimage(
                i * BRINGER_WIDTH_DEFAULT, 
                row * BRINGER_HEIGHT_DEFAULT,
                BRINGER_WIDTH_DEFAULT, 
                BRINGER_HEIGHT_DEFAULT
            );
        }
    }

    public void resetAllEnemies() {
        for (Enemy e : enemies) {
            e.resetEnemy(); 
        }
    }
}