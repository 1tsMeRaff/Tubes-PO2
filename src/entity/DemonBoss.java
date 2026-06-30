package entity;

import static utilitytools.Konstanta.Directions.*;
import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.PlayStates;
import main.GameCore;

public class DemonBoss extends Boss {

	private Rectangle2D.Float attackBox;
	private int attackCooldown = 0;
	private boolean Active = false;
	
	private boolean hp70Triggered = false;
    private boolean hp40Triggered = false;
    private boolean hp10Triggered = false;

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
    
    public boolean checkHpThresholdEffect() {
        float hpPercentage = ((float) currentHealth / maxHealth) * 100f;

        if (hpPercentage <= 70 && !hp70Triggered) {
            hp70Triggered = true;
            return true;
        }
        if (hpPercentage <= 40 && !hp40Triggered) {
            hp40Triggered = true;
            return true;
        }
        if (hpPercentage <= 10 && !hp10Triggered) {
            hp10Triggered = true;
            return true;
        }
        return false;
    }

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (70 * GameCore.SCALE), (int) (55 * GameCore.SCALE));
	}

//	public void update(int[][] tilesData, Player player) {
//		updateEffects(tilesData);
//		updateBehaviour(tilesData, player);
//		updateAnimationTick();
//		updateAttackBox();
//	}	

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
			if (isPlayerCloseEnoughForAttack(player) && attackCooldown > 0 && !Active) {
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
	        g.drawImage(frame, drawX(), drawY(), DEMON_BOSS_WIDTH * flipW(), DEMON_BOSS_HEIGHT, null);
	    }
	}

	@Override
	public int getExpReward() { return 100; }

	@Override
	protected void handleHurtEffects(PlayStates playStates) {
	    if (checkHpThresholdEffect()) {
	        playStates.triggerHeavyHit(20, 25, 12);
	    }
	}

	@Override
	protected void handleDeathEffects(PlayStates playStates) {
	    playStates.triggerHeavyHit(45, 120, 8);
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
	
	public void resetBoss() {
        hp70Triggered = false;
        hp40Triggered = false;
        hp10Triggered = false;
    }
}
