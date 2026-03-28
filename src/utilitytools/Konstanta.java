package utilitytools;

public class Konstanta {

	public static class KonstantaPlayerRight{
		public static final int IDLE_CHILL = 0;
		public static final int IDLE_ACTIVE = 1;
		public static final int LARI = 2;
		public static final int LOMPAT = 3;
		public static final int JATUH = 4;
		public static final int ATTACK_1 = 5;
		public static final int CHARGE_ATTACK = 6;
		public static final int GUARD = 7;
		
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
				return 16;
			case GUARD:
				return 6;
			default:
				return 1;
			}
		}
	}
}
