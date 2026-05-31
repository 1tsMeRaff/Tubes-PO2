package entity;

import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.geom.Rectangle2D.Float;

import static utilitytools.HelpMethods.*;
import static utilitytools.Konstanta.Directions.*;

import main.GameCore;


public abstract class Enemy extends Entity {
	private int aniIndex, enemyState;
	protected static int enemyType;
	private int aniTick, aniSpeed = 25;
	private boolean firstUpdate = true;
	private boolean inAir = false;
	private float fallSpeed;
	private float gravity = 0.04f * GameCore.SCALE;
	private float walkSpeed = 0.35f * GameCore.SCALE;
	private int walkDir = LEFT;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitBox(x, y, width, height);
		
	}
	
	private void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
			}
		}
	}
	
	public void update(int[][] tilesData) {
		updateMove(tilesData);
		updateAnimationTick();
		
	}
	
	private void updateMove(int[][] tilesData) {
		if(firstUpdate) {
			if(!IsEntityOnFloor(hitBox, tilesData)) {
				inAir = true;
				firstUpdate = false;
			}
		}
		if(inAir) {
			if(canMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, tilesData)) {
				hitBox.y += fallSpeed;
				fallSpeed += gravity;
			}else {
				inAir = false;
				hitBox.y = GetEntityPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
			}
		}else {
			switch(enemyState) {
			case IDLE:
				enemyState = WALK;
				break;
			case WALK:
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
		}
	}

	private void changeWalkDir() {
		if(walkDir == LEFT) {
			walkDir = RIGHT;
		}else {
			walkDir = LEFT;
		}
		
	}

	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getEnemyState() {
		return enemyState;
	}

}












