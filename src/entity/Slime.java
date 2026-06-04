package entity;

import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.GameCore;

import static utilitytools.Konstanta.Directions.*;

public class Slime extends Enemy {
	
	//AttackBox
	private Rectangle2D.Float AttackBox;
	private int attackBoxOffSetX;

	public Slime(float x, float y) {
		super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
		initHitBox(x, y, SLIME_HITBOX_WIDTH, SLIME_HITBOX_HEIGHT);
		initAttackBox();
	}
	
	private void initAttackBox() {
		AttackBox = new Rectangle2D.Float(x, y, (int) (60 * GameCore.SCALE), (int) (20 * GameCore.SCALE));
		attackBoxOffSetX = (int) (GameCore.SCALE * 30);
	}

	public void update(int[][] tilesData, Player player) {
		updateBehaviour(tilesData, player);
		updateAnimationTick();
		updateAttackBox();
	}
	
	private void updateAttackBox() {
		AttackBox.x = hitBox.x - attackBoxOffSetX;
		AttackBox.y = hitBox.y;
	}

	private void updateBehaviour(int[][] tilesData, Player player) {
		checkOnFloor(tilesData);
		if(inAir) {
			updateInAir(tilesData);
		}else {
			switch(enemyState) {
			case IDLE:
				newState(WALK);
				break;
			case WALK:
				if(canSeePlayer(tilesData, player)) {
					turnToPlayer(player);
				}
				if(isPlayerCloseForAttack(player)) {
					newState(ATTACK);
				}
				move(tilesData);
				break;
			case ATTACK:
				if(aniIndex == 0) {
					attackChecked = false;
				}
				
				if(aniIndex == 3 && !attackChecked) {
					checkHitEnemy(AttackBox ,player);
				}
				break;
			case HURT:
				break;
			}
		}
	}

	public int flipX() {
	    if (walkDir == LEFT) {
	        return (SLIME_DRAWOFFSET_X * 2) + SLIME_HITBOX_WIDTH; 
	    } else {
	        return 0;
	    }
	}
	
	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (AttackBox.x-xLvlOffset), (int) AttackBox.y, (int) AttackBox.width, (int) AttackBox.height);
	}
//	public int flipX() {
//		if (walkDir == LEFT) {
//			return width;
//		}else {
//			return 0;
//		}
//	}
	
	public int flipW() {
		if (walkDir == LEFT) {
			return -1;
		}else {
			return 1;
		}
	}
}
