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

        // Dinamis: 432 / 16 = 27 kolom | 320 / 16 = 20 baris
        int cols = image.getWidth() / tileWidth;
        int rows = image.getHeight() / tileHeight;

        // Total array sekarang akan berisi 540 elemen
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

        // === OPTIMASI FRUSTUM CULLING ===
        int startX = Math.max(0, xLvlOffset / tileSize);
        int endX = Math.min(mapWidth, startX + GameCore.TILES_IN_WIDTH + 2);

        for (int j = 0; j < mapHeight; j++) {
            for (int i = startX; i < endX; i++) {
                int tiledValue = worldData[j][i];

                // 1. Filter Udara: Lewati jika nilainya -1 (kosong)
                if (tiledValue != -1) {
                    
                    // 2. Koreksi Pergeseran Tiled (1-based) ke Java (0-based)
                    int spriteIndex = tiledValue;

                    // 3. Validasi Keamanan menggunakan fungsi Helper
                    if (isValidTile(spriteIndex)) {
                        int xPos = (int) (i * tileSize - xLvlOffset);
                        int yPos = j * tileSize;
                        
                        g.drawImage(mapSprite[spriteIndex], xPos, yPos, tileSize, tileSize, null);
                    }
                }
            }
        }
    }

    // Fungsi Helper untuk memvalidasi indeks ubin sebelum dirender
    private boolean isValidTile(int index) {
        // Mencegah error OutOfBounds jika Tiled mengirim angka di luar total atlas (540 ubin)
        // Ini otomatis memblokir ID Entitas bernilai besar (seperti 2000+) agar tidak digambar sebagai tanah
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