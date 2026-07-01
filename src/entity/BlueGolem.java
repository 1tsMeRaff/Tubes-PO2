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

    // --- PENGATURAN SKALA UKURAN ---
    private float golemScale = 1.8f; 

    private int scaledWidth = (int) (BLUE_GOLEM_WIDTH * golemScale);
    private int scaledHeight = (int) (BLUE_GOLEM_HEIGHT * golemScale);
    private int scaledHitboxW = (int) (BLUE_GOLEM_HITBOX_WIDTH * golemScale);
    private int scaledHitboxH = (int) (BLUE_GOLEM_HITBOX_HEIGHT * golemScale);
    private int scaledOffsetX = (int) (BLUE_GOLEM_DRAWOFFSET_X * golemScale);
    private int scaledOffsetY = (int) (BLUE_GOLEM_DRAWOFFSET_Y * golemScale);

    public BlueGolem(float x, float y) {
        super(x, y, BLUE_GOLEM_WIDTH, BLUE_GOLEM_HEIGHT, BLUE_GOLEM);
        
        float adjustedY = y - (scaledHitboxH - BLUE_GOLEM_HITBOX_HEIGHT);
        initHitBox(x, adjustedY, scaledHitboxW, scaledHitboxH);
        initAttackBox();

        walkSpeed = 0.55f * GameCore.SCALE;
        attackDistance = (int) (GameCore.TILES_SIZE * 1.5f);
        aniSpeed = 5;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * GameCore.SCALE * golemScale), (int) (50 * GameCore.SCALE * golemScale));
    }
    
    // PERBAIKAN: Menambahkan parameter xLvlOffset agar kotak debugging 
    // tetap berada di posisi yang benar saat kamera bergerak
    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
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
                    attackCooldown = 120;
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
            if (aniIndex == 6 && !attackChecked) {
                checkHitEnemy(attackBox, player);
                attackChecked = true; // PERBAIKAN: Set ke true agar tidak Multi-Hit
            }
            
            // PERBAIKAN LOGIKA: Kembali ke IDLE setelah animasi serangan selesai
            if (aniIndex >= GetSpriteAmount(BLUE_GOLEM, enemyState) - 1) {
                newState(IDLE);
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
        // PERBAIKAN VISUAL 1: Validasi Baris State
        if (enemyState < 0 || enemyState >= spriteAtlas.length) return;
        BufferedImage[] frames = spriteAtlas[enemyState];
        
        // PERBAIKAN VISUAL 2: Cegah aniIndex out of bounds (Mencegah Menghilang)
        if (aniIndex < 0) {
            aniIndex = 0;
        }
        if (aniIndex >= frames.length) {
            aniIndex = frames.length - 1; 
        }
        
        // Gambar Sprite
        BufferedImage frame = frames[aniIndex];
        if (frame != null) {
            // PERBAIKAN VISUAL 3: Tambahkan ` - xLvlOffset` pada drawX()
            g.drawImage(frame, drawX() - xLvlOffset, drawY(), scaledWidth * flipW(), scaledHeight, null);
        }

        drawHitbox(g); 
        drawAttackBox(g, xLvlOffset); // Diperbarui untuk memakai xLvlOffset
    }

    @Override
    public int getExpReward() { return 150; } 

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
        playStates.triggerHeavyHit(50, 150, 10);
    }

    private boolean isPlayerCloseEnoughForAttack(Player player) {
        float xDistance = (float) Math.abs(player.getHitBox().getCenterX() - hitBox.getCenterX());
        float yDistance = (float) Math.abs(player.getHitBox().getCenterY() - hitBox.getCenterY());
        return xDistance <= attackDistance && yDistance <= GameCore.TILES_SIZE * 2.5f;
    }

    public int flipX() {
        if (walkDir == LEFT) {
            return (scaledOffsetX * 2) + scaledHitboxW;
        }
        return 0;
    }

    public int flipW() {
        if (walkDir == LEFT) {
            return -1; 
        }
        return 1;
    }

    public int drawX() {
        return (int) (hitBox.x - scaledOffsetX + flipX());
    }

    public int drawY() {
        return (int) (hitBox.y - scaledOffsetY);
    }
}