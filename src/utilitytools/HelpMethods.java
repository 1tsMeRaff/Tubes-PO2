package utilitytools;

import main.GameCore;

public class HelpMethods {

	public static boolean canMoveHere(float x, float y, float width, float height, int[][] tilesData) {
		
		if(!isSolid(x, y, tilesData)) {
			if(!isSolid(x + width, y + height, tilesData)){
				if(!isSolid(x + width, y, tilesData)) {
					if(!isSolid(x , y + height, tilesData)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private static boolean isSolid(float x, float y, int[][] tilesData) {
		if(x < 0 || x >= GameCore.GAME_WIDTH) {
			return true;
		}
		if(y < 0 || y >= GameCore.GAME_HEIGHT) {
			return true;
		}
		
		float xIndex = x / GameCore.TILES_SIZE;
		float yIndex = y / GameCore.TILES_SIZE;
		
		int value = tilesData[(int) yIndex][(int) xIndex];
		
		if(value >= 48 || value < 0 || value != 11) {
			return true;
		}
		return false;
	}
}
