package utilitytools;

import main.GameCore;

public class Konstanta {
	
	public static class UI{
		public static class Frames{
			public static final int B_WIDTH_DEFAULT = 233;
			public static final int B_HEIGHT_DEFAULT = 138;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * GameCore.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * GameCore.SCALE);
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
		
		public static int GetSpriteAmount(int player_action) {
			
			switch(player_action) {
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
