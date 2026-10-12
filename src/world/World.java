package world;

public class World {

	private int[][] tilesData;
	
	public World(int[][] tilesData) {
		this.tilesData = tilesData;
	}
	
	public int getSpriteIndex(int x, int y) {
		if (y < 0 || y >= tilesData.length || x < 0 || x >= tilesData[0].length) {
			return 0;
		}
		return tilesData[y][x];
	}
	
	public int[][] getWorldData(){
		return tilesData;
	}

	public int getTileAt(int col, int row) {
		if (row < 0 || row >= tilesData.length || col < 0 || col >= tilesData[0].length) {
			return 0;
		}
		return tilesData[row][col];
	}
}
