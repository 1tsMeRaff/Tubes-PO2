package gameStates;

import entity.EnemyManager; 
import entity.Player;
import java.awt.Graphics;
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
import world.WorldManager;
import static utilitytools.Konstanta.Environment.*;

public class PlayStates extends States implements StateMethods {

    private Player player;
    private WorldManager worldManager;
    private EnemyManager enemyManager; 
    private ObjectManager objectManager; 
    private PauseOverlay pauseOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameOverOverlay gameOverOverlay; 

    private boolean paused = false;
    private boolean lvlCompleted = false;
    private boolean gameOver = false; 
    
    private InventoryOverlay inventoryOverlay;  
    private boolean inventoryOpen = false;   
    
    // Variabel Kamera
    public int xLvlOffset;
    private int leftBorder = (int) (0.2 * GameCore.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * GameCore.GAME_WIDTH);
    private int maxLvlOffsetX;

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
        
        // ---> TAMBAHAN: Memuat objek otomatis dari data CSV map <---
        objectManager.loadObjectsFromMap(worldManager.getCurrentMap().getWorldData());
        
        player = new Player(200, 200, (int) (64 * GameCore.SCALE), (int) (40 * GameCore.SCALE), this);
        player.loadmapData(worldManager.getCurrentMap().getWorldData());
        
        pauseOverlay = new PauseOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameOverOverlay = new GameOverOverlay(this); 
        inventoryOverlay = new InventoryOverlay(this);
    }
    
    private void calcLvlOffset() {
        int mapWidth = worldManager.getCurrentMap().getWorldData()[0].length;
        maxLvlOffsetX = (mapWidth - GameCore.TILES_IN_WIDTH) * GameCore.TILES_SIZE;
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        } 
        else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (gameOver) {
            // Saat game over, layar akan freeze
        } else if (inventoryOpen) {
            // Pause pergerakan dunia saat menu inventory dibuka
        } else {
            worldManager.update();
            objectManager.update();
            player.update();
            enemyManager.update(worldManager.getCurrentMap().getWorldData(), player); 
            checkCloseToBorder();
            
            // Cek Transisi Level & Memicu Suara Menang (dari dev)
            int endOfMapX = (worldManager.getCurrentMap().getWorldData()[0].length * GameCore.TILES_SIZE) - 50;
            if (player.getHitbox().x >= endOfMapX) {
                setLevelCompleted(true);
                gc.getAudioPlayer().lvlCompleted();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
        
        drawClouds(g);      // awan tetap digambar (dari dev)
        
        worldManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset); 
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset); 
        
        if (paused) {
            pauseOverlay.draw(g);
        } else if (lvlCompleted) {
            levelCompletedOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g); 
        } else if (inventoryOpen) {
            inventoryOverlay.draw(g);        // dari dev-Rizal
        }
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3 ; i++) {
            g.drawImage(clouds_01, i * CLOUDS_01_WIDTH - (int)(xLvlOffset * 0.3), (int) (204 * GameCore.SCALE), CLOUDS_01_WIDTH, CLOUDS_01_HEIGHT, null);
        }
        
        for (int i = 0; i < clouds_02Pos.length; i++) {
            g.drawImage(clouds_02, CLOUDS_02_WIDTH * 4 * i - (int)(xLvlOffset * 0.7), clouds_02Pos[i], CLOUDS_02_WIDTH, CLOUDS_02_HEIGHT, null);
        }
    }

    public void loadNextLevel() {
        worldManager.loadNextWorld(); 
        int[][] newMapData = worldManager.getCurrentMap().getWorldData();
        
        float newX = 200;
        float newY = 200;

        resetAll(newX, newY); 
        player.loadmapData(newMapData); 
        
        // ---> TAMBAHAN: Memuat ulang objek berdasarkan CSV dari map baru <---
        objectManager.loadObjectsFromMap(newMapData);
        
        calcLvlOffset();
        
        // [PEMICU BGM] Ganti lagu ke Level 2 saat map baru dimuat (dari dev)
        gc.getAudioPlayer().playSong(audio.AudioPlayer.LEVEL_2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // tidak digunakan
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver) return; 

        // Kalau inventory terbuka, klik hanya untuk UI, bukan untuk menyerang (dari dev-Rizal)
        if (inventoryOpen) {
            inventoryOverlay.mousePressed(e);
            return; 
        }

        if (paused) {
            pauseOverlay.mousePressed(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mousePressed(e);
        } else if(e.getButton() == MouseEvent.BUTTON1) {
            player.setCharging(true);       // charge attack (dari dev)
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
            player.releaseAttack();         // lepas charge (dari dev)
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver) return;

        // mouse buat inventory (dari dev-Rizal)
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
        if (lvlCompleted) return;

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = !paused;
            return;
        }
        
        // Tombol F untuk buka/tutup inventory (dari dev-Rizal)
        if (e.getKeyCode() == KeyEvent.VK_F) {
            inventoryOpen = !inventoryOpen;
            if (inventoryOpen) {
                inventoryOverlay.resetMenu();
            }
            return;
        }

        // Jangan proses input game jika inventory terbuka
        if (inventoryOpen) {
            return; 
        }

        if (paused) return;

        switch(e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_Q:                  // dash (dari dev)
                player.setDash(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Tidak menerima input jika game di pause, level selesai, game over, atau inventory terbuka
        if (paused || lvlCompleted || gameOver || inventoryOpen) return;
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
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
        inventoryOpen = false;        // dari dev-Rizal
        xLvlOffset = 0; 
    }
    
    // [MODIFIKASI] Mematikan lagu dan memainkan efek suara saat mati (dari dev)
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        if (gameOver) {
            gc.getAudioPlayer().stopSong(); 
            gc.getAudioPlayer().playEffect(audio.AudioPlayer.GAMEOVER); 
        }
    }
    
    // Method dari dev (dengan damage)
    public void checkHitEnemy(Rectangle2D.Float AttackBox, int damage) {
        enemyManager.checkEnemyHit(AttackBox, damage);
    }
    
    // Untuk mendeteksi tebasan pedang ke barel/kotak (dari dev)
    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    // Untuk mendeteksi sentuhan badan ke potion (dari dev)
    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
    }

    public boolean isPaused() {
        return paused;
    }
    
    public Player getPlayer() {
        return player;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }
    
    public GameCore getGameCore() {
        return gc;
    }
}