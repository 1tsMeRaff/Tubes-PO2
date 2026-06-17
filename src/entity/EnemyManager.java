package entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.EnemyConstants.*;

public class EnemyManager {

    private PlayStates playStates;
    private BufferedImage[][] slimeImg;
    private BufferedImage[][] demonBossImg;
    
    // Menggunakan list tunggal untuk semua musuh
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
                if (e instanceof Slime) {
                    ((Slime) e).update(tilesData, player);
                } else if (e instanceof DemonBoss) {
                    ((DemonBoss) e).update(tilesData, player);
                }
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawEnemies(g, xLvlOffset);
        drawBossUI(g);
    }
    
    private void drawEnemies(Graphics g, int xLvlOffset) {
        Graphics g2 = g.create();
        g2.translate(-xLvlOffset, 0);
        
        for (Enemy e : enemies) {
            if (e.isActive()) {
                if (e instanceof Slime) {
                    Slime s = (Slime) e;
                    int stateIndex = s.getEnemyState();
                    if (stateIndex == MATI) {
                        stateIndex = HURT;
                    }
                    
                    if (s.getEnemyState() == MATI && s.getAniTick() % 8 < 4) {
                        continue;
                    }
                    
                    g2.drawImage(slimeImg[stateIndex][s.getAniIndex()],
                            (int) (s.getHitBox().x - SLIME_DRAWOFFSET_X + s.flipX()),
                            (int) (s.getHitBox().y - SLIME_DRAWOFFSET_Y),
                            SLIME_WIDTH * s.flipW(), SLIME_HEIGHT, null);
                            
                } else if (e instanceof DemonBoss) {
                    DemonBoss demonBoss = (DemonBoss) e;
                    BufferedImage frame = demonBossImg[demonBoss.getEnemyState()][demonBoss.getAniIndex()];
                    if (frame != null) {
                        g2.drawImage(frame,
                                demonBoss.drawX(),
                                demonBoss.drawY(),
                                DEMON_BOSS_WIDTH * demonBoss.flipW(), 
                                DEMON_BOSS_HEIGHT, null);
                    }
                }
            }
        } 
        g2.dispose();
    }
    
    private void drawBossUI(Graphics g) {
        for (Enemy e : enemies) {
            // Kita harus melakukan pengecekan instanceof karena kita sekarang menggunakan list umum 'enemies'
            if (e instanceof DemonBoss && e.isActive()) {
                DemonBoss db = (DemonBoss) e;
                int maxWidth = (int) (400 * GameCore.SCALE); 
                int height = (int) (20 * GameCore.SCALE);
                int xPos = (GameCore.GAME_WIDTH / 2) - (maxWidth / 2);
                int yPos = (int) (GameCore.GAME_HEIGHT - (40 * GameCore.SCALE));
                
                float distance = Math.abs(playStates.getPlayer().getHitbox().x - db.getHitBox().x);
                float healthPercentage = (float) db.getCurrentHealth() / db.getMaxHealth();
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
                
                if (e instanceof Slime) {
                    Slime s = (Slime) e;
                    s.hurt(damage); 
                    if (s.getCurrentHealth() <= 0) {
                        playStates.getPlayer().gainExp(20);
                    }
                    
                } else if (e instanceof DemonBoss) {
                    DemonBoss demonBoss = (DemonBoss) e;
                    int kbDir = (player != null && player.getHitbox().x < demonBoss.getHitBox().x) ? 1 : -1;
                    boolean isCharge = (player != null) && player.isChargeAttack();
                    
                    demonBoss.hurt(damage, kbDir, isCharge);
                    
                    if (demonBoss.getCurrentHealth() <= 0 || demonBoss.isDead()) {
                        playStates.getPlayer().gainExp(100);
                    }
                    
                    if (demonBoss.isDead() || demonBoss.getCurrentHealth() <= 0) {
                        playStates.triggerHeavyHit(45, 120, 8); 
                    } else if (demonBoss.checkHpThresholdEffect()) {
                        playStates.triggerHeavyHit(20, 25, 12); 
                    }
                }
                return;
            }
        }
    }
    
    private void loadEnemyImages() {
        slimeImg = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
        for (int j = 0; j < slimeImg.length; j++) {
            for (int i = 0; i < slimeImg[j].length; i++) {
                slimeImg[j][i] = temp.getSubimage(i * SLIME_WIDTH_DEFAULT, j * SLIME_HEIGHT_DEFAULT,
                        SLIME_WIDTH_DEFAULT, SLIME_HEIGHT_DEFAULT);
            }
        }
        
        demonBossImg = new BufferedImage[6][DEMON_BOSS_SPRITE_COLUMNS];
        BufferedImage demonBossSheet = LoadSave.GetSpriteAtlas(LoadSave.DEMON_BOSS_SPRITE);
        loadDemonBossAnimation(demonBossSheet, IDLE, 0);
        loadDemonBossAnimation(demonBossSheet, WALK, 1);
        loadDemonBossAnimation(demonBossSheet, ATTACK, 2);
        loadDemonBossAnimation(demonBossSheet, HURT, 3);
        loadDemonBossAnimation(demonBossSheet, MATI, 4);
    }
    
    private void loadDemonBossAnimation(BufferedImage demonBossSheet, int targetState, int sourceRow) {
        for (int i = 0; i < GetSpriteAmount(DEMON_BOSS, targetState); i++) {
            demonBossImg[targetState][i] = demonBossSheet.getSubimage(i * DEMON_BOSS_WIDTH_DEFAULT,
                    sourceRow * DEMON_BOSS_HEIGHT_DEFAULT, DEMON_BOSS_WIDTH_DEFAULT, DEMON_BOSS_HEIGHT_DEFAULT);
        }
    }

    public void resetAllEnemies() {
        for (Enemy e : enemies) {
            e.resetEnemy();
            if (e instanceof DemonBoss) {
                ((DemonBoss) e).resetBoss();
            }
        }
    }
}