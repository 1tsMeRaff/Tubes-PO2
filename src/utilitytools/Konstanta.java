package utilitytools;

import main.GameCore;

public class Konstanta {
	
	public static class ObjectConstants {
	    // ID Potion Merah (3 Tipe)
	    public static final int RED_POTION_1 = 0;
	    public static final int RED_POTION_2 = 1;
	    public static final int RED_POTION_3 = 2;

	    // ID Potion Biru (3 Tipe)
	    public static final int BLUE_POTION_1 = 3;
	    public static final int BLUE_POTION_2 = 4;
	    public static final int BLUE_POTION_3 = 5;

	    // Papan tulis
	    public static final int SIGN = 5;

	    public static final int SIGN_WIDTH_DEFAULT = 17;
	    public static final int SIGN_HEIGHT_DEFAULT = 17;
	    public static final int SIGN_WIDTH = (int)(SIGN_WIDTH_DEFAULT * main.GameCore.SCALE);
	    public static final int SIGN_HEIGHT = (int)(SIGN_HEIGHT_DEFAULT * main.GameCore.SCALE);
	    
	    // Kandang macan
	    public static final int KANDANG = 8;
	    public static final int KANDANG_WIDTH_DEFAULT = 32; 
	    public static final int KANDANG_HEIGHT_DEFAULT = 32;
	    public static final int KANDANG_WIDTH = (int)(KANDANG_WIDTH_DEFAULT * main.GameCore.SCALE);
	    public static final int KANDANG_HEIGHT = (int)(KANDANG_HEIGHT_DEFAULT * main.GameCore.SCALE);

	    // ID Kontainer
	    public static final int BARREL = 6;
	    public static final int BOX = 7;
	    
	    // --- ID EQUIPMENT BARU ---
	    public static final int HELMET = 10;
	    public static final int ARMOR = 11;
	    public static final int SHOES = 12;
	    public static final int RING = 13;
	    public static final int SACK = 14;     // Karung
	    public static final int GLOVES = 15;   // Sarung Tangan

	    // Nilai Potion 
	    public static final int RED_VAL_1 = 5,  RED_VAL_2 = 10, RED_VAL_3 = 15;
	    public static final int BLUE_VAL_1 = 5, BLUE_VAL_2 = 10, BLUE_VAL_3 = 15;

	    // Ukuran Default
	    public static final int CONTAINER_WIDTH_DEFAULT = 40;
	    public static final int CONTAINER_HEIGHT_DEFAULT = 30;

	    // Ukuran Default Potion disesuaikan ke 16x16
	    public static final int POTION_WIDTH_DEFAULT = 16;
	    public static final int POTION_HEIGHT_DEFAULT = 16;

	    // Ukuran Ter-skala
	    public static final int CONTAINER_WIDTH = (int) (GameCore.SCALE * CONTAINER_WIDTH_DEFAULT);
	    public static final int CONTAINER_HEIGHT = (int) (GameCore.SCALE * CONTAINER_HEIGHT_DEFAULT);
	    public static final int POTION_WIDTH = (int) (GameCore.SCALE * POTION_WIDTH_DEFAULT);
	    public static final int POTION_HEIGHT = (int) (GameCore.SCALE * POTION_HEIGHT_DEFAULT);

	    public static int GetSpriteAmount(int object_type) {
	        switch (object_type) {
	            case RED_POTION_1: case RED_POTION_2: case RED_POTION_3:
	            case BLUE_POTION_1: case BLUE_POTION_2: case BLUE_POTION_3:
	                return 3;
	            case BARREL:
	            case BOX:
	                return 8;
	            default:
	                return 1;
	        }
	    }
	}
	
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
		public static final int DEMON_BOSS = 1;
		public static final int BLUE_GOLEM = 2;
		
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

		public static final int DEMON_BOSS_WIDTH_DEFAULT = 288;
		public static final int DEMON_BOSS_HEIGHT_DEFAULT = 160;
		public static final int DEMON_BOSS_SPRITE_COLUMNS = 22;
		
		public static final int DEMON_BOSS_WIDTH = (int) (DEMON_BOSS_WIDTH_DEFAULT * GameCore.SCALE);
		public static final int DEMON_BOSS_HEIGHT = (int) (DEMON_BOSS_HEIGHT_DEFAULT * GameCore.SCALE);
		
		public static final int DEMON_BOSS_HITBOX_WIDTH = (int) (46 * GameCore.SCALE);
		public static final int DEMON_BOSS_HITBOX_HEIGHT = (int) (72 * GameCore.SCALE);
		
		public static final int DEMON_BOSS_DRAWOFFSET_X = (int) (120 * GameCore.SCALE);
		public static final int DEMON_BOSS_DRAWOFFSET_Y = (int) (85 * GameCore.SCALE);
		
		// --- KONSTANTA BLUE GOLEM ---
	    public static final int BLUE_GOLEM_WIDTH_DEFAULT = 97;  // 1170 / 12 (Dibulatkan)
	    public static final int BLUE_GOLEM_HEIGHT_DEFAULT = 64; // 320 / 5
	    public static final int BLUE_GOLEM_SPRITE_COLUMNS = 12;

	    public static final int BLUE_GOLEM_WIDTH = (int) (BLUE_GOLEM_WIDTH_DEFAULT * GameCore.SCALE);
	    public static final int BLUE_GOLEM_HEIGHT = (int) (BLUE_GOLEM_HEIGHT_DEFAULT * GameCore.SCALE);

	    // Sesuaikan nilai hitbox dan offset ini setelah testing agar pas dengan visual
	    public static final int BLUE_GOLEM_HITBOX_WIDTH = (int) (40 * GameCore.SCALE);
	    public static final int BLUE_GOLEM_HITBOX_HEIGHT = (int) (55 * GameCore.SCALE);
	    public static final int BLUE_GOLEM_DRAWOFFSET_X = (int) (28 * GameCore.SCALE);
	    public static final int BLUE_GOLEM_DRAWOFFSET_Y = (int) (9 * GameCore.SCALE);

		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			switch(enemy_type) {
			case SLIME:
				switch(enemy_state) {
				case IDLE: return 6;
				case WALK: return 6;
				case ATTACK: return 9;
				case JUMP: return 8;
				case HURT: return 3;
				case MATI: return 3;
				}
			case DEMON_BOSS:
				switch(enemy_state) {
				case IDLE: return 6;
				case WALK: return 12;
				case ATTACK: return 15;
				case HURT: return 5;
				case MATI: return 22;
				}
			case BLUE_GOLEM:
	            switch(enemy_state) {
	            case IDLE: return 1;
	            case WALK: return 1;   
	            case ATTACK: return 1;
	            case HURT: return 1;
	            case MATI: return 1;
	            }
			}
			return 0;
		}
		
		public static int getMaxHealth(int enemy_type) {
			switch(enemy_type) {
			case SLIME: return 10;
			case DEMON_BOSS: return 80;
			case BLUE_GOLEM: return 150;
			default: return 1;
			}
		}
		
		public static int getEnemyAtt(int enemy_type) {
			switch(enemy_type) {
			case SLIME: return 10;
			case DEMON_BOSS: return 25;
			case BLUE_GOLEM: return 40;
			default: return 0;
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
		public static final int DASH = 16;
		public static final int JATUH = 19;
		public static final int ATTACK_1 = 11;
		public static final int CHARGE_ATTACK = 12;
		public static final int GUARD = 15;
		public static final int DOWN = 23;
		public static final int ARISE = 24;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case DOWN: return 7;
			case IDLE_CHILL: return 8;
			case ARISE: return 7;
			case IDLE_ACTIVE: return 8;
			case LARI: return 8;
			case LOMPAT: return 11;
			case JATUH: return 6;
			case ATTACK_1: return 8;
			case CHARGE_ATTACK: return 15;
			case GUARD: return 6;
			case DASH: return 10;
			default: return 1;
			}
		}
	}
}