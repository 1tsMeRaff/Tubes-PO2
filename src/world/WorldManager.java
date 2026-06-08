package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GameCore;
import utilitytools.LoadSave;

public class WorldManager {
	
	private GameCore gc;
	private BufferedImage[] mapSprite;
	private ArrayList<World> worlds;
	private int worldIndex = 0;
	
	public WorldManager(GameCore gc) {
		this.gc = gc;
		importOutsideSprites();
		worlds = new ArrayList<>();
		buildAllWorlds();
	}
	
	private void buildAllWorlds() {
		// Tambahkan semua map yang sudah dibuat ke sini.
		// Pastikan file CSV/PNG map-nya ada di folder resources.
		worlds.add(new World(LoadSave.GetTilesData("/map_test.txt"))); 
//		worlds.add(new World(LoadSave.GetTilesData("/untitled.csv"))); // Contoh Map 2
		// worlds.add(new World(LoadSave.GetTilesData("/map_3.csv"))); // Tambahkan lagi nanti
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
	
	public void draw(Graphics g, int xLvlOffset) {
		
		World currentMap = worlds.get(worldIndex);
		int mapWidth = currentMap.getWorldData()[0].length;
		
		for(int j = 0; j < GameCore.TILES_IN_HEIGHT; j++) {
			for(int i = 0; i < mapWidth; i++) {
				
				int index = currentMap.getSpriteIndex(i, j);
				
				if (index >= 0) { 
					g.drawImage(mapSprite[index], 
								(GameCore.TILES_SIZE * i) - xLvlOffset, 
								GameCore.TILES_SIZE * j, 
								GameCore.TILES_SIZE, 
								GameCore.TILES_SIZE, 
								null);
				}
			}
		}
	}
	
	public void update() {
		// Logika update tambahan (animasi air/awan statis) bisa ditaruh di sini nanti
	}
	
	public void loadNextWorld() {
		worldIndex++;
		if (worldIndex >= worlds.size()) {
			worldIndex = 0; // Balik ke map 1 jika sudah tamat
			System.out.println("Game Tamat! Kembali ke Map 1.");
			gameStates.GameStates.state = gameStates.GameStates.MENU; // Opsional: Balik ke Main Menu
		}
	}

	public World getCurrentMap() {
		return worlds.get(worldIndex);
	}
}