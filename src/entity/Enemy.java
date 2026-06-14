package entity;

import main.GameCore;
import static utilitytools.HelpMethods.*;
import static utilitytools.Konstanta.Directions.*;
import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.geom.Rectangle2D;


public abstract class Enemy extends Entity {
	protected int aniIndex, enemyState, enemyType;
	protected int aniTick;
	protected int aniSpeed = 7;
	protected boolean firstUpdate = true;
	protected boolean inAir = false;
	protected float fallSpeed;
	protected float gravity = 0.444f * GameCore.SCALE;
	protected float walkSpeed = 1.16f * GameCore.SCALE;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = GameCore.TILES_SIZE;
	protected int maxHealth;
	protected int currentHealth;
	
	protected boolean active = true;
	protected boolean attackChecked;
	
	// I-Frames & Knockback Musuh
	protected boolean isInvincible = false;
	protected int invincibilityTick = 0;
	protected final int INVINCIBILITY_DURATION = 20;

	protected float knockbackSpeed = 0;
	protected int knockbackDir = 1;
	protected float knockbackFriction = 0.15f * GameCore.SCALE;

	public Enemy(float x, float y, int width, int height, int enemyType) {
	    super(x, y, width, height);
	    this.enemyType = enemyType;
	    initHitBox(x, y, width, height);
	    maxHealth = getMaxHealth(enemyType);
	    currentHealth = maxHealth;
	    this.tileY = (int) (y / GameCore.TILES_SIZE);
	}
	
	protected void firstUpdateCheck(int[][] tilesData) {
		if(!IsEntityOnFloor(hitBox, tilesData)) {
			inAir = true;
			firstUpdate = false;
		}
	}
	
	protected void updateInAir(int[][] tilesData) {
		if(canMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, tilesData)) {
			hitBox.y += fallSpeed;
			fallSpeed += gravity;
		}else {
			inAir = false;
			hitBox.y = GetEntityPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
			tileY = (int) (hitBox.y /GameCore.TILES_SIZE);
			fallSpeed = 0;
		}
	}

	protected void checkOnFloor(int[][] tilesData) {
		if (!inAir && !IsEntityOnFloor(hitBox, tilesData)) {
			inAir = true;
			fallSpeed = 0;
			firstUpdate = false;
		}
	}
	
	protected void move(int[][] tilesData) {
		float xSpeed = 0;
		
		if(walkDir == LEFT) {
			xSpeed = -walkSpeed;
		}else {
			xSpeed = walkSpeed;
		}
		if(canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, tilesData)) {
			if(isFloor(hitBox, xSpeed, tilesData)) {
				hitBox.x += xSpeed;
				return;
			}
		}
		changeWalkDir();
	}
	
	protected void turnToPlayer(Player player) {
		if(player.hitBox.x  > hitBox.x) {
			walkDir = RIGHT;
		}else {
			walkDir = LEFT;
		}
	}
	
	protected void updateEffects(int[][] tilesData) {
	    if (isInvincible) {
	        invincibilityTick++;
	        if (invincibilityTick >= INVINCIBILITY_DURATION) {
	            isInvincible = false;
	            invincibilityTick = 0;
	        }
	    }
	    if (knockbackSpeed > 0) {
	        float xSpeed = knockbackSpeed * knockbackDir;
	        if(utilitytools.HelpMethods.canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, tilesData)) {
	            hitBox.x += xSpeed;
	        } else {
	            knockbackSpeed = 0;
	        }
	        knockbackSpeed -= knockbackFriction;
	        if (knockbackSpeed < 0) knockbackSpeed = 0;
	    }
	}
	
	protected boolean canSeePlayer(int[][] tilesData, Player player) {
		int playerTileY = (int) (player.getHitBox().y / GameCore.TILES_SIZE);
		if(playerTileY == tileY) {
			if(isPlayerInRange(player)) {
				if(IsSightClear(tilesData, hitBox, player.hitBox, tileY)) {
					return true;
				}
			}
		}
		return false;
	}
	
	

	protected boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitBox.x - hitBox.getX());
		return absValue <= attackDistance * 5;
	}
	
	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int) Math.abs(player.hitBox.x - hitBox.getX());
		return absValue <= attackDistance;
	}

	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	public void hurt(int value, int kbDir, boolean applyKnockback) {
	    if (isInvincible) return;

	    currentHealth -= value;
	    isInvincible = true;
	    
	    if (applyKnockback) {
	        knockbackDir = kbDir;
	        knockbackSpeed = 2.0f * GameCore.SCALE;
	        if (!inAir) {
	            inAir = true;
	            fallSpeed = -1.5f * GameCore.SCALE;
	        }
	    }

	    if(currentHealth <= 0) {
	        newState(MATI);
	    } else {
	        newState(HURT);
	    }
	}

	public void hurt(int value, int kbDir) {
	    hurt(value, kbDir, true);
	}

	public void hurt(int value) {
	    hurt(value, 1, false); 
	}

	protected void checkHitEnemy(Rectangle2D.Float AttackBox, Player player) {
	    if(AttackBox.intersects(player.getHitbox())) {
	        player.changeHealth(-getEnemyAtt(enemyType));
	        
	        int kbDir = (player.getHitbox().x > hitBox.x) ? 1 : -1;
	        
	        boolean fromBoss = (this.enemyType == DEMON_BOSS);
	        
	        player.applyKnockback(kbDir, fromBoss);
	    }
	    attackChecked = true;
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
				
				switch(enemyState) {
				case ATTACK, HURT -> enemyState = IDLE;
				case MATI -> active = false;
				}
			}
		}
	} 

	protected void changeWalkDir() {
		if(walkDir == LEFT) {
			walkDir = RIGHT;
		}else {
			walkDir = LEFT;
		}
	}
	
	public void resetEnemy() {
		hitBox.x = x;
		hitBox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		fallSpeed = 0;
		
	}

	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getEnemyState() {
		return enemyState;
	}

	public boolean isActive() {
		return active;
	}
	
	public int getAniTick() {
	    return aniTick;
	}
}












