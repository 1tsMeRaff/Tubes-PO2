package entity;

import static utilitytools.Konstanta.Directions.*;
import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.PlayStates;
import main.GameCore;

public class BringerOfDeath extends Boss {

    private Rectangle2D.Float attackBox;
    private int attackCooldown = 0;
    private boolean isActive = false; 
    
    private boolean hp50Triggered = false;
    private boolean hp25Triggered = false;

    private static final float BOSS_SCALE = 1.5f; 
    
    private static final int SCALED_WIDTH = (int) (BRINGER_WIDTH_DEFAULT * GameCore.SCALE * BOSS_SCALE);
    private static final int SCALED_HEIGHT = (int) (BRINGER_HEIGHT_DEFAULT * GameCore.SCALE * BOSS_SCALE);
    
    private static final int SCALED_HITBOX_W = (int) (35 * GameCore.SCALE * BOSS_SCALE);
    private static final int SCALED_HITBOX_H = (int) (50 * GameCore.SCALE * BOSS_SCALE);
    
    private static final int SCALED_OFFSET_X = (int) (90 * GameCore.SCALE * BOSS_SCALE);
    private static final int SCALED_OFFSET_Y = (int) (40 * GameCore.SCALE * BOSS_SCALE);

    public BringerOfDeath(float x, float y) {
        super(x, y, SCALED_WIDTH, SCALED_HEIGHT, BRINGER_OF_DEATH);
        
        initHitBox(x, y, SCALED_HITBOX_W, SCALED_HITBOX_H);
        initAttackBox();

        walkSpeed = 0.65f * GameCore.SCALE;
        attackDistance = (int) (GameCore.TILES_SIZE * 2.0f);
        aniSpeed = 5; 
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (60 * GameCore.SCALE * BOSS_SCALE), (int) (50 * GameCore.SCALE * BOSS_SCALE));
    }
    
    protected void drawAttackBox(Graphics g) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void updateAttackBox() {
        attackBox.y = hitBox.y;
        if (walkDir == LEFT) {
            attackBox.x = hitBox.x - attackBox.width;
        } else {
            attackBox.x = hitBox.x + hitBox.width;
        }
    }

    private void updateBehaviour(int[][] tilesData, Player player) {
        checkOnFloor(tilesData);
        if (inAir) {
            updateInAir(tilesData);
            if (attackCooldown > 0) {
                attackCooldown--;
            }
            return;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        switch(enemyState) {
        case IDLE:
            if (isPlayerCloseEnoughForAttack(player) && attackCooldown > 0 && !isActive) {
                turnToPlayer(player);
            } else {
                newState(WALK);
            }
            break;
        case WALK:
            if (isPlayerInRange(player)) {
                turnToPlayer(player);
            }
            if (isPlayerCloseEnoughForAttack(player)) {
                if (attackCooldown <= 0) {
                    if (Math.random() < 0.3) {
                        newState(BRINGER_SPECIAL);
                        attackCooldown = 180;
                        attackChecked = false;
                    } else {
                        newState(ATTACK);
                        attackCooldown = 100;
                        attackChecked = false;
                    }
                } else {
                    newState(IDLE);
                }
            } else {
                move(tilesData);
            }
            break;
        case ATTACK:
            if (aniIndex == 4 && !attackChecked) {
                checkHitEnemy(attackBox, player);
                attackChecked = true;
            }
            
            if (aniIndex >= GetSpriteAmount(BRINGER_OF_DEATH, enemyState) - 1) {
                newState(IDLE);
            }
            break;

        case BRINGER_SPECIAL:
            if (aniIndex == 4 && !attackChecked) {
                checkHitEnemy(attackBox, player);
                attackChecked = true; 
            }
            
            if (aniIndex >= GetSpriteAmount(BRINGER_OF_DEATH, enemyState) - 1) {
                newState(IDLE);
            }
            break;
        case HURT:
            break;
        }
    }
    
    @Override
    protected void turnToPlayer(Player player) {
        float playerCenterX = player.getHitBox().x + (player.getHitBox().width / 2);
        float bossCenterX = hitBox.x + (hitBox.width / 2);
        
        if (Math.abs(playerCenterX - bossCenterX) > 10 * GameCore.SCALE) {
            if (playerCenterX > bossCenterX) {
                walkDir = RIGHT;
            } else {
                walkDir = LEFT;
            }
        }
    }

    @Override
    public void update(int[][] tilesData, Player player) {
        updateEffects(tilesData);
        updateBehaviour(tilesData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    @Override
    public void draw(Graphics g, int xLvlOffset, BufferedImage[][] spriteAtlas) {
        if (enemyState < 0 || enemyState >= spriteAtlas.length) return;
        BufferedImage[] frames = spriteAtlas[enemyState];
        
        if (aniIndex < 0) {
            aniIndex = 0;
        }
        if (aniIndex >= frames.length) {
            aniIndex = frames.length - 1; 
        }
        
        BufferedImage frame = frames[aniIndex];
        if (frame != null) {
            g.drawImage(frame, drawX() - xLvlOffset, drawY(), SCALED_WIDTH * flipW(), SCALED_HEIGHT, null);
        }

//        drawHitbox(g); 
//        drawAttackBox(g); 
    }

    @Override
    public int getExpReward() { return 300; } 

    @Override
    protected void handleHurtEffects(PlayStates playStates) {
        float hpPercentage = ((float) currentHealth / maxHealth) * 100f;
        if (hpPercentage <= 50 && !hp50Triggered) {
            hp50Triggered = true;
            playStates.triggerHeavyHit(20, 25, 12);
        }
        if (hpPercentage <= 25 && !hp25Triggered) {
            hp25Triggered = true;
            playStates.triggerHeavyHit(20, 30, 15);
            walkSpeed = 0.85f * GameCore.SCALE;
        }
    }

    @Override
    protected void handleDeathEffects(PlayStates playStates) {
        playStates.triggerHeavyHit(60, 180, 15);
    }

    private boolean isPlayerCloseEnoughForAttack(Player player) {
        float xDistance = (float) Math.abs(player.getHitBox().getCenterX() - hitBox.getCenterX());
        float yDistance = (float) Math.abs(player.getHitBox().getCenterY() - hitBox.getCenterY());
        return xDistance <= attackDistance && yDistance <= GameCore.TILES_SIZE * 3.0f;
    }

    public int flipX() {
        if (walkDir == RIGHT) {
            return (SCALED_OFFSET_X * 2) + SCALED_HITBOX_W;
        } else {
            return 0;
        }
    }

    public int flipW() {
        if (walkDir == RIGHT) {
            return -1;
        } else {
            return 1;
        }
    }

    public int drawX() {
        return (int) (hitBox.x - SCALED_OFFSET_X + flipX());
    }

    public int drawY() {
        return (int) (hitBox.y - SCALED_OFFSET_Y);
    }
}