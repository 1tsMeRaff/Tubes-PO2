package entity;

import static utilitytools.Konstanta.EnemyConstants.*;

public class Slime extends Enemy {

	public Slime(float x, float y) {
		super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
	}

}
