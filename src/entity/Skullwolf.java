package entity;

import static utilitytools.Konstanta.EnemyConstants.*;
import static utilitytools.Konstanta.Directions.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import main.GameCore;

public class Skullwolf extends Enemy {

    private Rectangle2D.Float attackBox;
    private int attackBoxOffSetX;

    public Skullwolf(float x, float y) {
        super(x, y, SKULLWOLF_WIDTH, SKULLWOLF_HEIGHT, SKULLWOLF);
        initHitBox(x, y, SKULLWOLF_HITBOX_WIDTH, SKULLWOLF_HITBOX_HEIGHT);
        initAttackBox();
        
        walkSpeed = 1.35f * GameCore.SCALE;
        attackDistance = (int) (GameCore.TILES_SIZE * 1.2f);
        aniSpeed = 6;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (35 * GameCore.SCALE), (int) (20 * GameCore.SCALE));
//        attackBoxOffSetX = (int) (GameCore.SCALE * 15);
    }

    private void updateAttackBox() {
        attackBox.y = hitBox.y + (10 * GameCore.SCALE);
        if (walkDir == LEFT) {
            attackBox.x = hitBox.x - attackBox.width - attackBoxOffSetX;
        } else {
            attackBox.x = hitBox.x + hitBox.width + attackBoxOffSetX;
        }
    }

    @Override
    public void update(int[][] tilesData, Player player) {
        updateEffects(tilesData);
        updateBehaviour(tilesData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateBehaviour(int[][] tilesData, Player player) {
        checkOnFloor(tilesData);
        if (inAir) {
            updateInAir(tilesData);
        } else {
            switch (enemyState) {
                case IDLE:
                    newState(WALK);
                    break;
                case WALK:
                    if (canSeePlayer(tilesData, player)) {
                        turnToPlayer(player);
                    }
                    if (isPlayerCloseForAttack(player)) {
                        newState(ATTACK);
                    }
                    move(tilesData);
                    break;
                case ATTACK:
                    if (aniIndex == 0) attackChecked = false;
                    
                    if (aniIndex == 4 && !attackChecked) {
                        checkHitEnemy(attackBox, player);
                    }
                    break;
                case HURT:
                    break;
            }
        }
    }

    @Override
    public void draw(Graphics g, int xLvlOffset, BufferedImage[][] spriteAtlas) {
        int stateIndex = enemyState;
        
        // Memetakan ulang 4 baris spritesheet (0: IDLE, 1: WALK, 2: ATTACK, 3: HURT/MATI)
        if (stateIndex == MATI || stateIndex == HURT) {
            stateIndex = 3; 
        } else if (stateIndex > 3) {
            stateIndex = 0; 
        }

        g.drawImage(spriteAtlas[stateIndex][aniIndex],
                (int) (hitBox.x - SKULLWOLF_DRAWOFFSET_X - xLvlOffset + flipX()),
                (int) (hitBox.y - SKULLWOLF_DRAWOFFSET_Y),
                SKULLWOLF_WIDTH * flipW(), SKULLWOLF_HEIGHT, null);
        
        drawHitbox(g); 
        drawAttackBox(g, xLvlOffset);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public int flipX() {
        if (walkDir == RIGHT) {
            return (SKULLWOLF_DRAWOFFSET_X * 2) + SKULLWOLF_HITBOX_WIDTH;
        }
        return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT) {
            return -1;
        }
        return 1;
    }

    @Override
    public int getExpReward() { return 35; }
}