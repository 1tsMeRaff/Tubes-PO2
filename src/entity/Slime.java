package entity;

import static utilitytools.Konstanta.EnemyConstants.*;
import static utilitytools.HelpMethods.*;
import static utilitytools.Konstanta.Directions.*;

import java.awt.Color;
import java.awt.Graphics;

import main.GameCore;

public class Slime extends Enemy {

	public Slime(float x, float y) {
		super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
		initHitBox(x, y, SLIME_HITBOX_WIDTH, SLIME_HITBOX_HEIGHT);
	}
	
	public void update(int[][] tilesData) {
		updateMove(tilesData);
		updateAnimationTick();
		
	}
	
	private void updateMove(int[][] tilesData) {
		if(firstUpdate) {
			if(!IsEntityOnFloor(hitBox, tilesData)) {
				firstUpdateCheck(tilesData);
			}
		}
		if(inAir) {
			updateInAir(tilesData);
		}else {
			switch(enemyState) {
			case IDLE:
				newState(WALK);
				break;
			case WALK:
				move(tilesData);
				break;
			}
		}
	}

}
