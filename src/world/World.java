package world;

import java.util.ArrayList;
import entity.Enemy;

/**
 * World representation holding tile matrix, entity spawns, and environment metadata.
 */
public class World {

	private final int[][] tilesData;
	private final ArrayList<Enemy> enemies;
	private final String mapFilePath;
	private final String tilesetName;
	private final String backgroundName;

	public World(int[][] tilesData) {
		this(tilesData, new ArrayList<Enemy>(), "", "outside_sprites.png", "playing_bg_img.png");
	}

	public World(int[][] tilesData, ArrayList<Enemy> enemies, String mapFilePath, String tilesetName, String backgroundName) {
		this.tilesData = tilesData;
		this.enemies = enemies != null ? enemies : new ArrayList<Enemy>();
		this.mapFilePath = mapFilePath;
		this.tilesetName = tilesetName;
		this.backgroundName = backgroundName;
	}

	public int getSpriteIndex(int x, int y) {
		if (y < 0 || y >= tilesData.length || x < 0 || x >= tilesData[0].length) {
			return 0;
		}
		return tilesData[y][x];
	}

	public int[][] getWorldData() {
		return tilesData;
	}

	/**
	 * Returns a deep defensive copy of the tile matrix to protect internal state.
	 */
	public int[][] getClonedWorldData() {
		if (tilesData == null) return new int[0][0];
		int[][] clone = new int[tilesData.length][];
		for (int i = 0; i < tilesData.length; i++) {
			clone[i] = tilesData[i].clone();
		}
		return clone;
	}

	public int getWidthInTiles() {
		return (tilesData != null && tilesData.length > 0) ? tilesData[0].length : 0;
	}

	public int getHeightInTiles() {
		return (tilesData != null) ? tilesData.length : 0;
	}

	public int getTileAt(int col, int row) {
		if (row < 0 || row >= tilesData.length || col < 0 || col >= tilesData[0].length) {
			return 0;
		}
		return tilesData[row][col];
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public String getMapFilePath() {
		return mapFilePath;
	}

	public String getTilesetName() {
		return tilesetName;
	}

	public String getBackgroundName() {
		return backgroundName;
	}
}
