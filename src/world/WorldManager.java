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
//		levelSprite = LoadSave.GetSpriteAtlas(LoadSave.WORLD_SPRITE);
		importOutsideSprites();
		world_1 = new World(LoadSave.GetTilesData("/untitled1.csv"));
		
	}
	
	private void importOutsideSprites() {
	    BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.WORLD_SPRITE);
	    
	    // Hitung jumlah kolom dan baris secara otomatis dari dimensi gambar
	    int col = image.getWidth() / 32;
	    int row = image.getHeight() / 32;
	    
	    // Hindari menggunakan angka ajaib (magic number) seperti 200.
	    // Buat ukuran array tepat sebanyak jumlah tile yang ada.
	    mapSprite = new BufferedImage[col * row]; 
	    
	    for(int j = 0; j < row; j++) {
	        for(int i = 0; i < col; i++) {
	            int index = (j * col) + i;
	            
	            // Proses pemotongan sekarang dijamin 100% aman di dalam batas gambar
	            mapSprite[index] = image.getSubimage(i * 32, j * 32, 32, 32);
	        }
	    }
	}
	
//	private void importOutsideSprites() {
//		
//		int col = 13;
//		int row = 12;
//		BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.WORLD_SPRITE);
//		mapSprite = new BufferedImage[200];
//		for(int j = 0; j < row; j++) {
//			for(int i = 0; i < col; i++) {
//				int index = (j*col) + i;
//				mapSprite[index] = image.getSubimage(i*32, j*32, 32, 32);
//			}
//		}
//	}
	
	public void draw(Graphics g) {
	    
	    for(int j = 0; j < GameCore.TILES_IN_HEIGHT; j++) {
	        for(int i = 0; i < GameCore.TILES_IN_WIDTH; i++) {
	            
	            // Mengambil ID tile dari objek world_1
	            int index = world_1.getSpriteIndex(i, j);
	            
	            // [TAMBAHKAN BLOK IF INI]
	            // Hanya gambar jika ID bukan -1 (udara/kosong)
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

//	public void draw(Graphics g) {
//		
//		for(int j = 0; j < GameCore.TILES_IN_HEIGHT; j++) {
//			for(int i = 0; i < GameCore.TILES_IN_WIDTH; i++) {
//				int index = world_1.getSpriteIndex(i, j);
//				g.drawImage(mapSprite[index], GameCore.TILES_SIZE * i, GameCore.TILES_SIZE * j, 
//							GameCore.TILES_SIZE, GameCore.TILES_SIZE, null);
//			}
//		}
////		g.drawImage(levelSprite[138], 0, 0, null);
//	}
	
	public void update() {
		
	}
	
	public World getCurrentMap() {
		return world_1;
	}

}
