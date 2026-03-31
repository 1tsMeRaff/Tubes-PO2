package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.GameCore;
import utilitytools.LoadSave;

public class LevelManager {

	private GameCore gc;
	private BufferedImage[] levelSprite;
	private level level_1;
	
	public LevelManager(GameCore gc) {
		this.gc = gc;
//		levelSprite = LoadSave.GetSpriteAtlas(LoadSave.WORLD_SPRITE);
		importOutsideSprites();
		level_1 = new level(LoadSave.GetTilesData());
		
	}
	
	private void importOutsideSprites() {
		
		int col = 13;
		int row = 12;
		BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.WORLD_SPRITE);
		levelSprite = new BufferedImage[200];
		for(int j = 0; j < row; j++) {
			for(int i = 0; i < col; i++) {
				int index = (j*col) + i;
				levelSprite[index] = image.getSubimage(i*32, j*32, 32, 32);
			}
		}
	}

	public void draw(Graphics g) {
		
		for(int j = 0; j < GameCore.TILES_IN_HEIGHT; j++) {
			for(int i = 0; i < GameCore.TILES_IN_WIDTH; i++) {
				int index = level_1.getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], GameCore.TILES_SIZE * i, GameCore.TILES_SIZE * j, 
							GameCore.TILES_SIZE, GameCore.TILES_SIZE, null);
			}
		}
//		g.drawImage(levelSprite[138], 0, 0, null);
	}
	
	public void update() {
		
	}
}
