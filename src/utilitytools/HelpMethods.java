package utilitytools;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import main.GameCore;

public class HelpMethods {

//	public static boolean canMoveHere(float x, float y, float width, float height, int[][] tilesData) {
//		
//		if(!isSolid(x, y, tilesData)) {
//			if(!isSolid(x + width, y + height, tilesData)){
//				if(!isSolid(x + width, y, tilesData)) {
//					if(!isSolid(x , y + height, tilesData)) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}
	
	public static boolean canMoveHere(float x, float y, float width, float height, int[][] tilesData) {
		float leftX = x;
		float rightX = x + width - 1; 
		float topY = y;
		float bottomY = y + height - 1;

		int leftCol = (int) (leftX / GameCore.TILES_SIZE);
		int rightCol = (int) (rightX / GameCore.TILES_SIZE);
		int topRow = (int) (topY / GameCore.TILES_SIZE);
		int bottomRow = (int) (bottomY / GameCore.TILES_SIZE);

		for (int row = topRow; row <= bottomRow; row++) {
			for (int col = leftCol; col <= rightCol; col++) {

				if (row < 0 || row >= tilesData.length || col < 0 || col >= tilesData[0].length) {
					System.out.println("STUCK KARENA OUT OF BOUNDS! Baris: " + row + " Kolom: " + col);
					return false; 
				}

				int tileID = tilesData[row][col];

				if (tileID != -1 && tileID != 0) { 
					System.out.println("Nabrak Tile Solid ID: " + tileID);
					return false; 
				}
			}
		}
		return true; 
	}
	
	public static boolean isSolidTile(int tileID) {
		if (tileID == -1 || tileID == 0) { 
			return false;
		}
		return true;
	}
	
	public static boolean isSolid(float x, float y, int[][] lvlData) {
		int xIndex = (int) (x / GameCore.TILES_SIZE);
		int yIndex = (int) (y / GameCore.TILES_SIZE);

		if (yIndex < 0 || yIndex >= lvlData.length || xIndex < 0 || xIndex >= lvlData[0].length) {
			return true;
		}

		int value = lvlData[yIndex][xIndex];

		if (value == -1 || value == 0) {
			return false;
		}

		return true; 
	}
	
//	private static boolean isSolid(float x, float y, int[][] tilesData) {
//		if(x < 0 || x >= GameCore.GAME_WIDTH) {
//			return true;
//		}
//		if(y < 0 || y >= GameCore.GAME_HEIGHT) {
//			return true;
//		}
//		
//		float xIndex = x / GameCore.TILES_SIZE;
//		float yIndex = y / GameCore.TILES_SIZE;
//		
//		int value = tilesData[(int) yIndex][(int) xIndex];
//		
//		if(value >= 48 || value < 0 || value != 11) {
//			return true;
//		}
//		return false;
//	}
	
	public static float GetEntityPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
		int currentTile = (int) (hitBox.x / GameCore.TILES_SIZE);
		if(xSpeed > 0) {
			//Kanan
			int tileXpos = currentTile * GameCore.TILES_SIZE;
			int xOffSet = (int) (GameCore.TILES_SIZE - hitBox.width);
			return tileXpos + xOffSet -1;
		} else {
			return currentTile * GameCore.TILES_SIZE;
		}
	}
	
	public static float GetEntityPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
		int currentTile = (int) (hitBox.y / GameCore.TILES_SIZE);
		if(airSpeed > 0) {
			int tileYPos = currentTile * GameCore.TILES_SIZE;
			int yOffSet = (int) (GameCore.TILES_SIZE - hitBox.height);
			return tileYPos + yOffSet - 1;
		} else {
			return currentTile * GameCore.TILES_SIZE;
		}
	}
	
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
			if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isFloor(Rectangle2D.Float hitBox, float xSpeed, int[][] tilesData) {
		return isSolid(hitBox.x + xSpeed, hitBox.y + hitBox.height + 1, tilesData);
	}
}