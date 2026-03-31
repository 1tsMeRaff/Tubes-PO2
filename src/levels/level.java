package levels;

public class level {
	
	private int[][] tilesData;
	
	public level(int[][] tilesData) {
		this.tilesData = tilesData;
	}
	
	public int getSpriteIndex(int x, int y) {
		if (y < 0 || y >= tilesData.length || x < 0 || x >= tilesData[0].length) {
			return 0;
		}
		return tilesData[y][x];
	}

}
