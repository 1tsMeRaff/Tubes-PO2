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
        worlds.add(new World(LoadSave.GetTilesData("/map_tutorial_fix.csv")));
    }

    private void importOutsideSprites() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.WORLD_SPRITE);

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
                    
                    int spriteIndex = tiledValue;

                    if (isValidTile(spriteIndex)) {
                        int xPos = (int) (i * tileSize - xLvlOffset);
                        int yPos = j * tileSize;
                        
                        g.drawImage(mapSprite[spriteIndex], xPos, yPos, tileSize, tileSize, null);
                    }
                }
            }
        }
    }

    private boolean isValidTile(int index) {
        if (index < 0 || index >= mapSprite.length) {
            return false;
        }
        return true;
    }

    public void update() {
    }

    public void loadNextWorld() {
        worldIndex++;
        if (worldIndex >= worlds.size()) {
            worldIndex = 0;
            System.out.println("Game Tamat! Kembali ke Map 1.");
            gameStates.GameStates.state = gameStates.GameStates.MENU;
        }
    }

    public World getCurrentMap() {
        return worlds.get(worldIndex);
    }
}