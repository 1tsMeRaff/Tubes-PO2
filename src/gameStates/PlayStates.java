package gameStates;

import entity.DemonBoss;
import entity.EnemyManager;
import entity.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.GameCore;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import ui.InventoryOverlay;
import utilitytools.LoadSave;
import world.World;
import world.WorldManager;
import effects.ParticlePool;
import effects.FloatingTextPool;
import static utilitytools.Konstanta.Environment.*;

public class PlayStates extends States implements StateMethods {

    private Player player;
    private WorldManager worldManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager; 
    private PauseOverlay pauseOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameOverOverlay gameOverOverlay; 

    private final ParticlePool particlePool = new ParticlePool();
    private final FloatingTextPool floatingTextPool = new FloatingTextPool();
    private final effects.CurseProjectilePool cursePool = new effects.CurseProjectilePool();
    private float smoothLvlOffsetX = 0f;

    private boolean paused = false;
    private boolean lvlCompleted = false;
    private boolean gameOver = false; 
    
    private InventoryOverlay inventoryOverlay;  
    private boolean inventoryOpen = false;

    public int xLvlOffset;
    private int leftBorder = (int) (0.2 * GameCore.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * GameCore.GAME_WIDTH);
    private int maxLvlOffsetX;
    
    private int hitStopDuration = 0;
    private int shakeDuration = 0;
    private int shakeIntensity = 0;
    
    private boolean isBossEncountered = false;

    private BufferedImage backgroundImg, clouds_01, clouds_02; 
    private int[] clouds_02Pos;
    private Random rnd = new Random(); 
    
    public PlayStates(GameCore gc) {
        super(gc);
        initClasses();
        calcLvlOffset();
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAY_BACKGROUND_IMG);
        clouds_01 = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS_01);
        clouds_02 = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS_01);
        clouds_02Pos = new int[8];
        for(int i = 0; i < clouds_02Pos.length; i++)
            clouds_02Pos[i] = (int)(90 * GameCore.SCALE) +  rnd.nextInt((int)(100*GameCore.SCALE));
    }
    
    private void initClasses() {
        worldManager = new WorldManager(gc);
        enemyManager = new EnemyManager(this); 
        
        objectManager = new ObjectManager(this);
        objectManager.loadObjectsFromMap(worldManager.getCurrentMap().getWorldData());
        
        player = new Player(200, 200, (int) (64 * GameCore.SCALE), (int) (40 * GameCore.SCALE), this);
        player.loadmapData(worldManager.getCurrentMap().getWorldData());
        
        pauseOverlay = new PauseOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameOverOverlay = new GameOverOverlay(this); 
        inventoryOverlay = new InventoryOverlay(this);
    }

    public void loadSelectedLevel(String mapFilePath, String tilesetName) {
        worldManager.loadSpecificWorld(mapFilePath, tilesetName);
        World currentWorld = worldManager.getCurrentMap();
        int[][] newMapData = currentWorld.getWorldData();
        
        player.loadmapData(newMapData);
        objectManager = new ObjectManager(this); 
        objectManager.loadObjectsFromMap(newMapData);
        enemyManager.loadEnemies(currentWorld.getEnemies());
        
        calcLvlOffset();
        lvlCompleted = false;
        
        // PASTIKAN PEMAIN JATUH DARI ATAS AGAR TIDAK NYANGKUT
        resetAll(200, 400); 

        // --- LOGIKA BACKGROUND BARU ---
        if(tilesetName.equals("main_tileset.png")) {
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.CASTLE_BACKGROUND_IMG);
        } else {
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAY_BACKGROUND_IMG);
        }
    }
    
    private void calcLvlOffset() {
        int mapWidth = worldManager.getCurrentMap().getWorldData()[0].length;
        maxLvlOffsetX = (mapWidth - GameCore.TILES_IN_WIDTH) * GameCore.TILES_SIZE;
        if (maxLvlOffsetX < 0) {
            maxLvlOffsetX = 0; 
        }
    }

    private void checkCloseToBorder() {
        float playerCenterX = player.getHitbox().x + (player.getHitbox().width / 2.0f);
        float targetOffset = playerCenterX - (GameCore.GAME_WIDTH * 0.45f);

        if (targetOffset < 0) {
            targetOffset = 0;
        } else if (targetOffset > maxLvlOffsetX) {
            targetOffset = maxLvlOffsetX;
        }

        smoothLvlOffsetX += (targetOffset - smoothLvlOffsetX) * 0.08f;
        xLvlOffset = Math.round(smoothLvlOffsetX);

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        }
        if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }
    
    private void drawClouds(Graphics g) {
        // Dikosongkan sementara
    }
    
    public void triggerHeavyHit(int freezeFrames, int shakeFrames, int intensity) {
        this.hitStopDuration = freezeFrames;
        this.shakeDuration = shakeFrames;
        this.shakeIntensity = intensity;
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (gameOver) {
            
        } else if (inventoryOpen) {
            
        } else {
            if (hitStopDuration > 0) {
                hitStopDuration--;
                if (shakeDuration > 0) shakeDuration--;
                return;
            }

            if (shakeDuration > 0) shakeDuration--;

            worldManager.update();
            objectManager.update();
            player.update();
            enemyManager.update(worldManager.getCurrentMap().getWorldData(), player);
            cursePool.update(this, player);
            particlePool.update();
            floatingTextPool.update();
            checkCloseToBorder();
            
            int endOfMapX = (worldManager.getCurrentMap().getWorldData()[0].length * GameCore.TILES_SIZE) - 50;
            if (player.getHitbox().x >= endOfMapX) {
                setLevelCompleted(true);
                gc.getAudioPlayer().lvlCompleted();
            }
        }
    }

    private static final Color CASTLE_BG_COLOR = new Color(15, 15, 25);

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int shakeX = 0;
        int shakeY = 0;

        if (shakeDuration > 0) {
            shakeX = rnd.nextInt(shakeIntensity * 2) - shakeIntensity;
            shakeY = rnd.nextInt(shakeIntensity * 2) - shakeIntensity;
        }

        g2.translate(shakeX, shakeY);

        // --- MENGGAMBAR BACKGROUND DINAMIS ---
        if (backgroundImg != null) {
            float geserX = 0.25f;
            int bgX = (int)(-xLvlOffset * geserX) % GameCore.GAME_WIDTH;
            g.drawImage(backgroundImg, bgX, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
            g.drawImage(backgroundImg, bgX + GameCore.GAME_WIDTH, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
            drawClouds(g);
        } else {
            // Jika masuk Kastil, background jadi warna Biru Dongker Gelap
            g.setColor(CASTLE_BG_COLOR);
            g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
        }
        
        worldManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        cursePool.draw(g, xLvlOffset);
        particlePool.draw(g, xLvlOffset);
        floatingTextPool.draw(g, xLvlOffset);

        g2.translate(-shakeX, -shakeY);

        if (paused) {
            pauseOverlay.draw(g);
        } else if (lvlCompleted) {
            levelCompletedOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (inventoryOpen) {
            inventoryOverlay.draw(g);
        }
    }

    public void loadNextLevel() {
        worldManager.loadNextWorld();
        World currentWorld = worldManager.getCurrentMap();
        int[][] newMapData = currentWorld.getWorldData();
        float newX = 200;
        float newY = 200;
        resetAll(newX, newY); 
        player.loadmapData(newMapData); 
        objectManager = new ObjectManager(this);
        objectManager.loadObjectsFromMap(newMapData);
        enemyManager.loadEnemies(currentWorld.getEnemies());
        calcLvlOffset();
        lvlCompleted = false;

        if ("main_tileset.png".equals(currentWorld.getTilesetName())) {
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.CASTLE_BACKGROUND_IMG);
            gc.getAudioPlayer().playSong(audio.AudioPlayer.LEVEL_2);
        } else {
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAY_BACKGROUND_IMG);
            gc.getAudioPlayer().playSong(audio.AudioPlayer.LEVEL_1);
        }
    }

    public void equipPlayerItem(int itemIndex, String slotType) {
        player.equipItem(itemIndex, slotType);
    }

    @Override public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver) return; 
        if (inventoryOpen) {
            inventoryOverlay.mousePressed(e);
            return;
        }
        if (paused) {
            pauseOverlay.mousePressed(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mousePressed(e);
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            player.setCharging(true);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            player.castWitchCurse();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver) return;
        if (paused) {
            pauseOverlay.mouseReleased(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mouseReleased(e);
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            player.releaseAttack();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver) return;
        if (inventoryOpen) {
            inventoryOverlay.mouseMoved(e);
            return;
        }
        if (paused) {
            pauseOverlay.mouseMoved(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (gameOver) return;
        if (paused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
            return;
        }

        int code = e.getKeyCode();

        // 1. Level completed navigation via keyboard
        if (lvlCompleted) {
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                loadNextLevel();
            } else if (code == KeyEvent.VK_ESCAPE) {
                resetAll(200, 200);
                gameStates.GameStates.state = gameStates.GameStates.MENU;
                gc.getAudioPlayer().playSong(audio.AudioPlayer.MENU_1);
            }
            return;
        }

        // 2. Fullscreen Toggle
        if (code == KeyEvent.VK_F11) {
            gc.getGameFrame().toggleFullScreen();
            return;
        }

        // 3. Quick Save
        if (code == KeyEvent.VK_F5) {
            utilitytools.SaveLoadManager.saveGame(this);
            return;
        }

        // 4. Inventory Toggle (I = Universal RPG standard, Tab = Overlay standard, E = Interaction, F = Legacy)
        if (code == KeyEvent.VK_I || code == KeyEvent.VK_TAB || code == KeyEvent.VK_E || code == KeyEvent.VK_F) {
            if (paused) return;
            inventoryOpen = !inventoryOpen;
            if (inventoryOpen) {
                player.resetDirBooleans();
                inventoryOverlay.resetMenu();
            }
            return;
        }

        // 5. ESC priority: Close Inventory first if open, else toggle Pause
        if (code == KeyEvent.VK_ESCAPE) {
            if (inventoryOpen) {
                inventoryOpen = false;
                return;
            }
            paused = !paused;
            if (paused) {
                player.resetDirBooleans();
            }
            return;
        }

        // 6. Pause alias 'P'
        if (code == KeyEvent.VK_P) {
            if (inventoryOpen) return;
            paused = !paused;
            if (paused) {
                player.resetDirBooleans();
            }
            return;
        }

        // Block gameplay inputs if menus are open
        if (inventoryOpen || paused) return;

        // 7. Gameplay Controls
        switch (code) {
            // Movement Left: A or Left Arrow
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                player.setLeft(true);
                break;

            // Movement Right: D or Right Arrow
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                player.setRight(true);
                break;

            // Jump / Double Jump: Space, W, Up Arrow, or K (Arcade)
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_K:
                player.setJump(true);
                break;

            // Dash: Shift (Universal), L (Arcade), Q (Legacy), C (Retro)
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_L:
            case KeyEvent.VK_Q:
            case KeyEvent.VK_C:
                player.setDash(true);
                break;

            // Attack / Charge: J (Arcade) or X (Retro PC)
            case KeyEvent.VK_J:
            case KeyEvent.VK_X:
                player.setCharging(true);
                break;

            // Witch Curse Soul Ability: U (Arcade upper row) or H
            case KeyEvent.VK_U:
            case KeyEvent.VK_H:
                player.castWitchCurse();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (paused || lvlCompleted || gameOver || inventoryOpen) {
            player.resetDirBooleans();
            return;
        }

        int code = e.getKeyCode();
        switch (code) {
            // Movement Left Release
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                player.setLeft(false);
                break;

            // Movement Right Release
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                player.setRight(false);
                break;

            // Jump Release
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_K:
                player.setJump(false);
                break;

            // Attack Release (Triggers Slash or Heavy Charge Attack)
            case KeyEvent.VK_J:
            case KeyEvent.VK_X:
                player.releaseAttack();
                break;
        }
    }
    
    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public void resetAll(float targetX, float targetY) {
        enemyManager.resetAllEnemies();
        player.resetAll(targetX, targetY); 
        paused = false;
        lvlCompleted = false;
        gameOver = false; 
        inventoryOpen = false;
        particlePool.clear();
        floatingTextPool.clear();
        cursePool.reset();
        smoothLvlOffsetX = targetX - (GameCore.GAME_WIDTH * 0.45f);
        if (smoothLvlOffsetX < 0) smoothLvlOffsetX = 0;
        xLvlOffset = Math.round(smoothLvlOffsetX);
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        if (gameOver) {
            gc.getAudioPlayer().stopSong(); 
            gc.getAudioPlayer().playEffect(audio.AudioPlayer.GAMEOVER);
        }
    }

    public void checkHitEnemy(Rectangle2D.Float AttackBox, int damage) {
        enemyManager.checkEnemyHit(AttackBox, damage, player);
    }
    
    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }
    
    public boolean isBossEncountered() {
        return isBossEncountered;
    }
    
    public WorldManager getWorldManager() { return worldManager; }

    public void loadWorldByIndex(int index) {
        worldManager.setWorldIndex(index);
        World currentWorld = worldManager.getCurrentMap();
        int[][] currentMapData = currentWorld.getWorldData();
        
        player.loadmapData(currentMapData);
        objectManager = new ObjectManager(this);
        objectManager.loadObjectsFromMap(currentMapData);
        enemyManager.loadEnemies(currentWorld.getEnemies());
        
        calcLvlOffset();
        lvlCompleted = false;
        resetAll(200, 200);

        if ("main_tileset.png".equals(currentWorld.getTilesetName())) {
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.CASTLE_BACKGROUND_IMG);
            gc.getAudioPlayer().playSong(audio.AudioPlayer.LEVEL_2);
        } else {
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAY_BACKGROUND_IMG);
            gc.getAudioPlayer().playSong(audio.AudioPlayer.LEVEL_1);
        }
    }

    public void setPaused(boolean paused) { this.paused = paused; }
    public void setLevelCompleted(boolean levelCompleted) { this.lvlCompleted = levelCompleted; }
    public boolean isPaused() { return paused; }
    public Player getPlayer() { return player; }
    public EnemyManager getEnemyManager() { return enemyManager; }
    public ObjectManager getObjectManager() { return objectManager; }
    public GameCore getGameCore() { return gc; }
    public ParticlePool getParticlePool() { return particlePool; }
    public FloatingTextPool getFloatingTextPool() { return floatingTextPool; }
    public effects.CurseProjectilePool getCursePool() { return cursePool; }
    public void shakeScreen(int frames, int intensity) {
        this.shakeDuration = Math.max(this.shakeDuration, frames);
        this.shakeIntensity = Math.max(this.shakeIntensity, intensity);
    }
}