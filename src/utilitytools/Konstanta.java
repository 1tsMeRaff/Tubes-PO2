package utilitytools;

import main.GameCore;

public class Konstanta {
	

	public static class Environment {
	    public static final int CLOUDS_01_WIDTH_DEFAULT = 448;
	    public static final int CLOUDS_01_HEIGHT_DEFAULT = 101;
	    public static final int CLOUDS_02_WIDTH_DEFAULT = 74;
	    public static final int CLOUDS_02_HEIGHT_DEFAULT = 24;
	    
	    public static final int CLOUDS_01_WIDTH = (int) (CLOUDS_01_WIDTH_DEFAULT * GameCore.SCALE);
	    public static final int CLOUDS_01_HEIGHT = (int) (CLOUDS_01_HEIGHT_DEFAULT * GameCore.SCALE);
	    public static final int CLOUDS_02_WIDTH = (int) (CLOUDS_01_WIDTH_DEFAULT * GameCore.SCALE);
	    public static final int CLOUDS_02_HEIGHT = (int) (CLOUDS_01_HEIGHT_DEFAULT * GameCore.SCALE);
	}
	

	

	public static class EnemyConstants{
		public static final int SLIME = 0;
		
		public static final int IDLE = 0;
		public static final int WALK = 1;
		public static final int ATTACK= 2;
		public static final int JUMP = 3;
		public static final int HURT = 4;
		public static final int MATI = 5;
		
		public static final int SLIME_WIDTH_DEFAULT = 80;
		public static final int SLIME_HEIGHT_DEFAULT = 80;
		
		public static final int SLIME_WIDTH = (int) (SLIME_WIDTH_DEFAULT * GameCore.SCALE);
		public static final int SLIME_HEIGHT = (int) (SLIME_HEIGHT_DEFAULT * GameCore.SCALE);
		
		public static final int SLIME_HITBOX_WIDTH = (int) (16 * GameCore.SCALE);
		public static final int SLIME_HITBOX_HEIGHT = (int) (9 * GameCore.SCALE);
		
		public static final int SLIME_DRAWOFFSET_X = (int) (16 * GameCore.SCALE);
		public static final int SLIME_DRAWOFFSET_Y = (int) (52 * GameCore.SCALE);
		
		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			
			switch(enemy_type) {
			case SLIME:
				switch(enemy_state) {
				case IDLE:
					return 6;
				case WALK:
					return 6;
				case ATTACK:
					return 9;
				case JUMP:
					return 8;
				case HURT:
					return 3;
				case MATI:
					return 3;
				}
			}
			return 0;
		}
		
		public static int getMaxHealth(int enemy_type) {
			switch(enemy_type) {
			case SLIME:
				return 10;
			default:
				return 1;
			}
		}
		
		public static int getEnemyAtt(int enemy_type) {
			switch(enemy_type) {
			case SLIME:
				return 10;
			default:
				return 0;
			}
		}
	}
	
	public static class UI{
		public static class Frames{
			public static final int B_WIDTH_DEFAULT = 233;
			public static final int B_HEIGHT_DEFAULT = 138;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * GameCore.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * GameCore.SCALE);
		}

		public static class PauseButtons{
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * GameCore.SCALE);

			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * GameCore.SCALE);

			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * GameCore.SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * GameCore.SCALE);

			public static final int SLIDER_DEFAULT_WIDTH = 215;
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * GameCore.SCALE);
		}
	}

	public static class Directions{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	public static class KonstantaPlayerRight{
		public static final int IDLE_ACTIVE = 0;
		public static final int IDLE_CHILL = 1;
		public static final int LARI = 3;
		public static final int LOMPAT = 4;
		public static final int JATUH = 19;
		public static final int ATTACK_1 = 11;
		public static final int CHARGE_ATTACK = 12;
		public static final int GUARD = 15;
		public static final int MATI = 23;
		
		public static int GetSpriteAmount(int player_action) {
			
			switch(player_action) {
			case MATI:
				return 7;
			case IDLE_CHILL:
				return 8;
			case IDLE_ACTIVE:
				return 8;
			case LARI:
				return 8;
			case LOMPAT:
				return 11;
			case JATUH:
				return 6;
			case ATTACK_1:
				return 8;
			case CHARGE_ATTACK:
				return 10;
			case GUARD:
				return 6;
			default:
				return 1;
			}
		}
	}
}
