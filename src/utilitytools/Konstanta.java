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
