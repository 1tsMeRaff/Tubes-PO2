package utilitytools;

import java.awt.geom.Rectangle2D;

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
	
	public static float GetEntityPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
		
		int currentTile = (int) (hitBox.x / GameCore.TILES_SIZE);
		if(xSpeed > 0) {
			//Kanan
			int tileXpos = currentTile * GameCore.TILES_SIZE;
			int xOffSet = (int) (GameCore.TILES_SIZE - hitBox.width);
			return tileXpos + xOffSet -1;
		}else {
			//Kiri
			return currentTile * GameCore.TILES_SIZE;
		}
	}
	
	public static float GetEntityPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
		int currentTile = (int) (hitBox.y / GameCore.TILES_SIZE);
		if(airSpeed > 0) {
			// Falling
			int tileYPos = currentTile * GameCore.TILES_SIZE;
			int yOffSet = (int) (GameCore.TILES_SIZE - hitBox.height);
			return tileYPos + yOffSet - 1;
		}else {
			// Jumping
			return currentTile * GameCore.TILES_SIZE;
		}
	}
	
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] lvlData) {
		if(!isSolid(hitBox.x, hitBox.y + hitBox.height + 1, lvlData)) {
			if(!isSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData)) {
				return false;
			}
		}
		return true;
	}
}









	


