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
        worlds = new ArrayList<>();
        buildAllWorlds();
        loadCurrentWorldTileset();
    }

    private void buildAllWorlds() {
        worlds.clear();
        // Campaign Level 1: Tutorial
        worlds.add(SinglePassLevelParser.parse("/map_tutorial_fix.txt", "outside_sprites.png", LoadSave.PLAY_BACKGROUND_IMG));
        // Campaign Level 2: Jungle
        worlds.add(SinglePassLevelParser.parse("/jungle_map.txt", "map_jungle_fix.png", LoadSave.PLAY_BACKGROUND_IMG));
        // Campaign Level 3: Castle & Boss
        worlds.add(SinglePassLevelParser.parse("/main_tileset.txt", "main_tileset.png", LoadSave.CASTLE_BACKGROUND_IMG));
    }

    public void loadCurrentWorldTileset() {
        if (!worlds.isEmpty() && worldIndex < worlds.size()) {
            importOutsideSprites(worlds.get(worldIndex).getTilesetName());
        } else {
            importOutsideSprites(LoadSave.WORLD_SPRITE);
        }
    }

    public void loadSpecificWorld(String filePath, String tilesetName) {
        String bg = tilesetName.equals("main_tileset.png") ? LoadSave.CASTLE_BACKGROUND_IMG : LoadSave.PLAY_BACKGROUND_IMG;
        World customWorld = SinglePassLevelParser.parse(filePath, tilesetName, bg);
        worlds.clear();
        worlds.add(customWorld);
        worldIndex = 0;
        importOutsideSprites(tilesetName);
    }

    private void importOutsideSprites(String tilesetName) {
        BufferedImage image = LoadSave.GetSpriteAtlas(tilesetName);

        int tileWidth = 16;
        int tileHeight = 16;

        int cols = image.getWidth() / tileWidth;
        int rows = image.getHeight() / tileHeight;

        mapSprite = new BufferedImage[cols * rows];

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                int index = i + (j * cols);
                mapSprite[index] = image.getSubimage(i * tileWidth, j * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        World currentMap = worlds.get(worldIndex);
        int[][] worldData = currentMap.getWorldData();
        int mapHeight = worldData.length;
        int mapWidth = worldData[0].length;
        int tileSize = GameCore.TILES_SIZE;

        int startX = Math.max(0, xLvlOffset / tileSize);
        int endX = Math.min(mapWidth, startX + GameCore.TILES_IN_WIDTH + 2);

        for (int j = 0; j < mapHeight; j++) {
            for (int i = startX; i < endX; i++) {
                int tiledValue = worldData[j][i];

                if (tiledValue != -1) {
                    if (isValidTile(tiledValue)) {
                        int xPos = (int) (i * tileSize - xLvlOffset);
                        int yPos = j * tileSize;
                        
                        g.drawImage(mapSprite[tiledValue], xPos, yPos, tileSize, tileSize, null);
                    }
                }
            }
        }
    }

    private boolean isValidTile(int index) {
        return index >= 0 && mapSprite != null && index < mapSprite.length;
    }

    public void update() {
    }

    public void loadNextWorld() {
        worldIndex++;
        if (worldIndex >= worlds.size()) {
            worldIndex = 0;
            System.out.println("Game Tamat! Kembali ke Level 1.");
        }
        loadCurrentWorldTileset();
    }

    public World getCurrentMap() {
        return worlds.get(worldIndex);
    }
    
    public int getWorldIndex() { return worldIndex; }
    
    public void setWorldIndex(int index) { 
        this.worldIndex = Math.max(0, Math.min(index, worlds.size() - 1));
        loadCurrentWorldTileset();
    }

    public int getTotalWorlds() {
        return worlds.size();
    }
}