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
    private ArrayList<Slime> Slimes = new ArrayList<Slime>();
    private ArrayList<DemonBoss> demonBosses = new ArrayList<DemonBoss>();

    public EnemyManager(PlayStates playStates) {
        this.playStates = playStates;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        // Menggunakan map_tutorial_fix.txt dari branch dev-Rafi
        Slimes = LoadSave.GetSlimes("/map_tutorial_fix.txt");
        demonBosses = LoadSave.GetDemonBosses("/map_tutorial_fix.txt");
    }

    public void update(int[][] tilesData, Player player) {
        for (Slime s : Slimes) {
            if(s.isActive()) {
                s.update(tilesData, player);
            }
        }
        for (DemonBoss demonBoss : demonBosses) {
            if(demonBoss.isActive()) {
                demonBoss.update(tilesData, player);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawSlimes(g, xLvlOffset);
        drawDemonBosses(g, xLvlOffset);
        drawBossUI(g);
    }
    
    public void drawBossUI(Graphics g) {
        for (DemonBoss db : demonBosses) {
            if(db.isActive()) {
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
                    
                    break;
                }
            }
        }
    }
    
    private void drawSlimes(Graphics g, int xLvlOffset) {
        Graphics g2 = g.create();
        g2.translate(-xLvlOffset, 0);
        for (Slime s : Slimes) {
            if(s.isActive()) {
                int stateIndex = s.getEnemyState();
                if (stateIndex == MATI) {
                    stateIndex = HURT;
                }
                
                if (s.getEnemyState() == MATI) {
                    if (s.getAniTick() % 8 < 4) {
                        continue;
                    }
                }
                
                g2.drawImage(slimeImg[stateIndex][s.getAniIndex()],
                        (int) (s.getHitBox().x - SLIME_DRAWOFFSET_X + s.flipX()),
                        (int) (s.getHitBox().y - SLIME_DRAWOFFSET_Y),
                        SLIME_WIDTH * s.flipW(), SLIME_HEIGHT, null);
                
                // Debugging Hitbox - Un-comment untuk melihat garis batas serangan/tubuh
                // s.drawHitbox(g2); 
                // s.drawAttackBox(g2, xLvlOffset);
            }
        } 
        g2.dispose();
    }
    
    private void drawDemonBosses(Graphics g, int xLvlOffset) {
        Graphics g2 = g.create();
        g2.translate(-xLvlOffset, 0);
        for (DemonBoss demonBoss : demonBosses) {
            if(demonBoss.isActive()) {
                BufferedImage frame = demonBossImg[demonBoss.getEnemyState()][demonBoss.getAniIndex()];
                if (frame == null) {
                    continue;
                }
                
                g2.drawImage(frame,
                        demonBoss.drawX(),
                        demonBoss.drawY(),
                        DEMON_BOSS_WIDTH * demonBoss.flipW(), 
                        DEMON_BOSS_HEIGHT, null);
                
                // Debugging Hitbox - Un-comment untuk melihat garis batas serangan/tubuh
                // demonBoss.drawHitbox(g2); 
                // demonBoss.drawAttackBox(g2, xLvlOffset);
            }
        } 
        g2.dispose();
    }
    
    // Logika tergabung: Hit, Knockback, Screen Shake (dari dev-Rafi) + EXP System (dari dev)
    public void checkEnemyHit(Rectangle2D.Float attackBox, int damage, Player player) {
        // 1. Cek Slime
        for (Slime s : Slimes) {
            if (s.isActive() && attackBox.intersects(s.getHitBox())) {
                s.hurt(damage); 
                
                // Logika EXP ketika Slime mati
                if (s.getCurrentHealth() <= 0) {
                    playStates.getPlayer().gainExp(20);
                }
                return;
            }
        }
        
        // 2. Cek DemonBoss
        for (DemonBoss demonBoss : demonBosses) {
            if (demonBoss.isActive() && attackBox.intersects(demonBoss.getHitBox())) {
                
                // Logika Knockback & Charge
                int kbDir = (player != null && player.getHitbox().x < demonBoss.getHitBox().x) ? 1 : -1;
                boolean isCharge = (player != null) && player.isChargeAttack();
                
                demonBoss.hurt(damage, kbDir, isCharge);
                
                // Logika EXP ketika Boss mati
                if (demonBoss.getCurrentHealth() <= 0 || demonBoss.isDead()) {
                    playStates.getPlayer().gainExp(100);
                }
                
                // Logika HeavyHit (Screen Shake)
                if (demonBoss.isDead() || demonBoss.getCurrentHealth() <= 0) {
                    playStates.triggerHeavyHit(45, 120, 8); // Guncangan besar saat mati
                } else if (demonBoss.checkHpThresholdEffect()) {
                    playStates.triggerHeavyHit(20, 25, 12); // Guncangan saat HP rendah
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
        for(Slime s : Slimes) {
            s.resetEnemy();
        }
        for(DemonBoss demonBoss : demonBosses) {
            demonBoss.resetEnemy();
        }
    }
}