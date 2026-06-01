package entity;

import static utilitytools.Konstanta.EnemyConstants.*;

public class Slime extends Enemy {

	public Slime(float x, float y) {
		super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
		initHitBox(x, y, SLIME_HITBOX_WIDTH, SLIME_HITBOX_HEIGHT);
	}
	
	public void update(int[][] tilesData, Player player) {
		updateMove(tilesData, player);
		updateAnimationTick();
		
	}
	
	private void updateMove(int[][] tilesData, Player player) {
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
			}
		}
	}

}
