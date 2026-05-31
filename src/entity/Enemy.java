package entity;

import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.geom.Rectangle2D.Float;

import static utilitytools.HelpMethods.*;
import static utilitytools.Konstanta.Directions.*;

import main.GameCore;


public abstract class Enemy extends Entity {
	protected int aniIndex, enemyState, enemyType;
	protected int aniTick, aniSpeed = 25;
	protected boolean firstUpdate = true;
	protected boolean inAir = false;
	protected float fallSpeed;
	protected float gravity = 0.04f * GameCore.SCALE;
	protected float walkSpeed = 0.35f * GameCore.SCALE;
	protected int walkDir = LEFT;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitBox(x, y, width, height);
		
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
	
	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
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

	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getEnemyState() {
		return enemyState;
	}

}












