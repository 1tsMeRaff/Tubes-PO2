package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

//import levels.level;
import main.GameCore;
import utilitytools.LoadSave;

public class WorldManager {
	
	private GameCore gc;
	private BufferedImage[] mapSprite;
	private World world_1;
	
	public WorldManager(GameCore gc) {
		this.gc = gc;
		importOutsideSprites();
		world_1 = new World(LoadSave.GetTilesData("/untitled1.csv"));
		
	}
	
	private void importOutsideSprites() {
	    BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.WORLD_SPRITE);
	    
	    int col = image.getWidth() / 32;
	    int row = image.getHeight() / 32;
	    
	    mapSprite = new BufferedImage[col * row]; 
	    
	    for(int j = 0; j < row; j++) {
	        for(int i = 0; i < col; i++) {
	            int index = (j * col) + i;
	            
	            mapSprite[index] = image.getSubimage(i * 32, j * 32, 32, 32);
	        }
	    }
	}
	
	public void draw(Graphics g) {
	    
	    for(int j = 0; j < GameCore.TILES_IN_HEIGHT; j++) {
	        for(int i = 0; i < GameCore.TILES_IN_WIDTH; i++) {
	            
	            int index = world_1.getSpriteIndex(i, j);
	            
	            if (index >= 0) { 
	                g.drawImage(mapSprite[index], 
	                            GameCore.TILES_SIZE * i, 
	                            GameCore.TILES_SIZE * j, 
	                            GameCore.TILES_SIZE, 
	                            GameCore.TILES_SIZE, 
	                            null);
	            }
	            
	        }
	    }
	}
	
	public void update() {
		
	}
	
	public World getCurrentMap() {
		return world_1;
	}

}
