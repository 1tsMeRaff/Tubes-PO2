package entity;

import static utilitytools.Konstanta.Directions.*;
import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.PlayStates;
import main.GameCore;

public class BlueGolem extends Boss {

    private Rectangle2D.Float attackBox;
    private int attackCooldown = 0;
    private boolean isActive = false;

    private boolean hp50Triggered = false;

    public BlueGolem(float x, float y) {
        super(x, y, BLUE_GOLEM_WIDTH, BLUE_GOLEM_HEIGHT, BLUE_GOLEM);
        
        // Inisialisasi Hitbox (sesuaikan ukurannya nanti jika kurang pas)
        initHitBox(x, y, BLUE_GOLEM_HITBOX_WIDTH, BLUE_GOLEM_HITBOX_HEIGHT);
        initAttackBox();

        walkSpeed = 0.55f * GameCore.SCALE; // Golem biasanya bergerak lebih lambat
        attackDistance = GameCore.TILES_SIZE * 1.5f;
        aniSpeed = 5;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (60 * GameCore.SCALE), (int) (50 * GameCore.SCALE));
    }

    private void updateAttackBox() {
        attackBox.y = hitBox.y + (5 * GameCore.SCALE);
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
                    newState(ATTACK);
                    attackCooldown = 120; // Cooldown serangan lebih lama dari Demon Boss
                } else {
                    newState(IDLE);
                }
            } else {
                move(tilesData);
            }
            break;
        case ATTACK:
            if (aniIndex == 0) {
                attackChecked = false;
            }
            // Sesuaikan frame keberapa pukulan golem mengenai player (misal frame ke-6)
            if (aniIndex == 6 && !attackChecked) {
                checkHitEnemy(attackBox, player);
            }
            break;
        case HURT:
            break;
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
        BufferedImage frame = spriteAtlas[enemyState][aniIndex];
        if (frame != null) {
            g.drawImage(frame, drawX(), drawY(), BLUE_GOLEM_WIDTH * flipW(), BLUE_GOLEM_HEIGHT, null);
        }
    }

    @Override
    public int getExpReward() { return 150; } // EXP lebih besar

    @Override
    protected void handleHurtEffects(PlayStates playStates) {
        float hpPercentage = ((float) currentHealth / maxHealth) * 100f;
        if (hpPercentage <= 50 && !hp50Triggered) {
            hp50Triggered = true;
            playStates.triggerHeavyHit(15, 20, 10);
        }
    }

    @Override
    protected void handleDeathEffects(PlayStates playStates) {
        playStates.triggerHeavyHit(50, 150, 10); // Efek mati bergetar kuat
    }

    private boolean isPlayerCloseEnoughForAttack(Player player) {
        float xDistance = (float) Math.abs(player.getHitBox().getCenterX() - hitBox.getCenterX());
        float yDistance = (float) Math.abs(player.getHitBox().getCenterY() - hitBox.getCenterY());
        return xDistance <= attackDistance && yDistance <= GameCore.TILES_SIZE * 2.5f;
    }

    public int flipX() {
        if (walkDir == RIGHT) {
            return BLUE_GOLEM_WIDTH;
        }
        return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT ) {
            return -1;
        }
        return 1;
    }

    public int drawX() {
        return (int) (hitBox.x - BLUE_GOLEM_DRAWOFFSET_X + flipX());
    }

    public int drawY() {
        return (int) (hitBox.y - BLUE_GOLEM_DRAWOFFSET_Y);
    }
}