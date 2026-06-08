package gameStates;

import entity.EnemyManager; // Ditambahkan agar tidak error
import entity.Player;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
<<<<<<< HEAD
=======
import java.awt.geom.Rectangle2D;
>>>>>>> 9f408b81c3467aca97365f4ee712ea5bb4217001
import java.util.Random;

import main.GameCore;
import ui.GameOverUI;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import ui.GameOverOverlay; 
import utilitytools.LoadSave;
import world.WorldManager;
import objects.ObjectManager; // Tambahan Import untuk ObjectManager
import static utilitytools.Konstanta.Environment.*;

public class PlayStates extends States implements StateMethods {

<<<<<<< HEAD
    private Player player;
    private WorldManager worldManager;
    private ObjectManager objectManager; // 1. Tambahan Variabel ObjectManager
    private PauseOverlay pauseOverlay;
    private boolean paused = false;
    
    // Variabel Kamera
    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * GameCore.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * GameCore.GAME_WIDTH);
    private int maxLvlOffsetX;

    private BufferedImage backgroundImg, clouds_01, clouds_02; 
    private int[] clouds_02Pos;
    private Random rnd = new Random(); 
    
    public PlayStates(GameCore gc) {
        super(gc);
        initClasses();
        pauseOverlay = new PauseOverlay(this);
        calcLvlOffset(); 
        
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAY_BACKGROUND_IMG);
        clouds_01 = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS_01);
        clouds_02 = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS_01); // Pastikan ini pakai gambar clouds_02 jika ada
        clouds_02Pos = new int[8];
        for(int i = 0; i < clouds_02Pos.length; i++) {
            clouds_02Pos[i] = (int)(90 * GameCore.SCALE) + rnd.nextInt((int)(100*GameCore.SCALE));
        }
    }
    
    private void initClasses() {
        worldManager = new WorldManager(gc);
        
        // 2. Inisialisasi ObjectManager dan jalankan item Uji Coba
        objectManager = new ObjectManager(this);
        objectManager.addTestObjects(); // Memunculkan item sementara untuk tes
        
        player = new Player(200, 200, (int) (64 * GameCore.SCALE), (int) (40 * GameCore.SCALE));
        player.loadmapData(worldManager.getCurrentMap().getWorldData());
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
            return;
        }
        worldManager.update();
        objectManager.update(); // 3. Update animasi item
        player.update();
        checkCloseToBorder();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
        
        drawClounds(g);
        
        worldManager.draw(g, xLvlOffset);
        
        objectManager.draw(g, xLvlOffset); // 4. Gambar item ke layar (sebelum player)
        
        player.render(g, xLvlOffset);
        
        if (paused) {
            pauseOverlay.draw(g);
        }
    }

    private void drawClounds(Graphics g) {
        for (int i = 0; i < 3 ; i++) {
            g.drawImage(clouds_01, i * CLOUDS_01_WIDTH - (int)(xLvlOffset * 0.3), (int) (204 * GameCore.SCALE), CLOUDS_01_WIDTH, CLOUDS_01_HEIGHT, null);
        }
        
        for (int i = 0; i < clouds_02Pos.length; i++) {
            g.drawImage(clouds_02, CLOUDS_02_WIDTH * 4 * i - (int)(xLvlOffset * 0.7), clouds_02Pos[i], CLOUDS_02_WIDTH, CLOUDS_02_HEIGHT, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (paused) {
            return;
        }
        if(e.getButton() == MouseEvent.BUTTON1) {
            player.setAttack(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = !paused;
            return;
        }
        if (paused) {
            return;
        }

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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (paused) {
            return;
        }
        
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

    public void resetAll() {
        initClasses();
        paused = false;
        xLvlOffset = 0; 
    }
=======
	private Player player;
	private WorldManager worldManager;
	private EnemyManager enemyManager; // Dikembalikan ke tempatnya
	private PauseOverlay pauseOverlay;
	private GameOverUI gameOverUI; 
	private LevelCompletedOverlay levelCompletedOverlay;
	private GameOverOverlay gameOverOverlay; 

	private boolean paused = false;
	private boolean lvlCompleted = false;
	private boolean gameOver = false; 
	
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
		
		// Menggunakan versi Rafi yang mengoper 'this' ke Player
		player = new Player(200, 200, (int) (64 * GameCore.SCALE), (int) (40 * GameCore.SCALE), this);
		player.loadmapData(worldManager.getCurrentMap().getWorldData());
		
		pauseOverlay = new PauseOverlay(this); 
		gameOverUI = new GameOverUI(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameOverOverlay = new GameOverOverlay(this); 
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
		} else {
			worldManager.update();
			player.update();
			
			// Menggunakan versi Rafi untuk update musuh
			enemyManager.update(worldManager.getCurrentMap().getWorldData(), player); 
			checkCloseToBorder();
			
			// Cek Transisi Level
			int endOfMapX = (worldManager.getCurrentMap().getWorldData()[0].length * GameCore.TILES_SIZE) - 50;
			if (player.getHitbox().x >= endOfMapX) {
				setLevelCompleted(true);
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
		
		drawClouds(g);
		
		worldManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset); 
		
		if (paused) {
			pauseOverlay.draw(g);
		} else if (lvlCompleted) {
			levelCompletedOverlay.draw(g);
		} else if (gameOver) {
			gameOverOverlay.draw(g); 
		}
	}

	// Memperbaiki penulisan drawClounds menjadi drawClouds
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
		calcLvlOffset();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (paused || lvlCompleted || gameOver) return;

		if(e.getButton() == MouseEvent.BUTTON1) {
			player.setAttack(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gameOver) return; 

		if (paused) {
			pauseOverlay.mousePressed(e);
		} else if (lvlCompleted) {
			levelCompletedOverlay.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameOver) return;

		if (paused) {
			pauseOverlay.mouseReleased(e);
		} else if (lvlCompleted) {
			levelCompletedOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gameOver) return;

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
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (paused || lvlCompleted || gameOver) return;
		
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
		xLvlOffset = 0; 
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public void checkHitEnemy(Rectangle2D.Float AttackBox) {
		enemyManager.checkEnemyHit(AttackBox);
	}
>>>>>>> 9f408b81c3467aca97365f4ee712ea5bb4217001

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

<<<<<<< HEAD
    public boolean isPaused() {
        return paused;
    }
    
    public Player getPlayer() {
        return player;
    }

    // 5. Tambahan Getter untuk ObjectManager
    public ObjectManager getObjectManager() {
        return objectManager;
    }
=======
	public void setLevelCompleted(boolean levelCompleted) {
		this.lvlCompleted = levelCompleted;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public Player getPlayer() {
		return player;
	}
>>>>>>> 9f408b81c3467aca97365f4ee712ea5bb4217001
}