package entity;

import static utilitytools.Konstanta.Directions.*;
import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import main.GameCore;

public class DemonBoss extends Boss {

	private Rectangle2D.Float attackBox;
	private int attackCooldown = 0;

    public DemonBoss(float x, float y) {
        super(x, y, DEMON_BOSS_WIDTH, DEMON_BOSS_HEIGHT, DEMON_BOSS); 
        initHitBox(x, y, DEMON_BOSS_HITBOX_WIDTH, DEMON_BOSS_HITBOX_HEIGHT);
        initAttackBox();
//      walkSpeed = 0.22f * GameCore.SCALE;
        walkSpeed = 0.73f * GameCore.SCALE;
        attackDistance = GameCore.TILES_SIZE * 2f;
//      aniSpeed = 12;
        aniSpeed = 4;
    }

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (70 * GameCore.SCALE), (int) (55 * GameCore.SCALE));
	}

	public void update(int[][] tilesData, Player player) {
		updateEffects(tilesData);
		updateBehaviour(tilesData, player);
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateAttackBox() {
		attackBox.y = hitBox.y + (10 * GameCore.SCALE);
		if (walkDir == LEFT) {
			attackBox.x = hitBox.x - attackBox.width;
		} else {
			attackBox.x = hitBox.x + hitBox.width;
		}
	}
	
	@Override
	public void hurt(int value, int kbDir, boolean applyKnockback) {
	    super.hurt(value, kbDir, applyKnockback);
	    checkPhaseTransition();
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
			if (isPlayerCloseEnoughForAttack(player) && attackCooldown > 0) {
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
					attackCooldown = 90;
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
			if (aniIndex == 10 && !attackChecked) {
				checkHitEnemy(attackBox, player);
			}
			break;
		case HURT:
			break;
		}
	}

	private boolean isPlayerCloseEnoughForAttack(Player player) {
		float xDistance = (float) Math.abs(player.getHitBox().getCenterX() - hitBox.getCenterX());
		float yDistance = (float) Math.abs(player.getHitBox().getCenterY() - hitBox.getCenterY());
		return xDistance <= attackDistance && yDistance <= GameCore.TILES_SIZE * 2.5f;
	}

	public int flipX() {
	    if (walkDir == RIGHT) { 
	        return DEMON_BOSS_WIDTH;
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
		return (int) (hitBox.x - DEMON_BOSS_DRAWOFFSET_X + flipX());
	}

	public int drawY() {
		return (int) (hitBox.y - DEMON_BOSS_DRAWOFFSET_Y);
	}

	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, 
				   (int) attackBox.width, (int) attackBox.height);
	}
}
