package entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.EnemyConstants.*;

public class EnemyManager {

	private PlayStates playStates;
	private BufferedImage[][] slimeImg;
	private BufferedImage[][] demonBossImg;
	private ArrayList<Slime> Slimes = new ArrayList<Slime>();
	private ArrayList<DemonBoss> demonBosses = new ArrayList<DemonBoss>();

	public EnemyManager(PlayStates playStates) {
		this.playStates = playStates;
		loadEnemyImages();
		addEnemies();
	}

	private void addEnemies() {
		Slimes = LoadSave.GetSlimes("/map_test.txt");
		demonBosses = LoadSave.GetDemonBosses("/map_test.txt");
	}

	public void update(int[][] tilesData, Player player) {
		for (Slime s : Slimes) {
			if(s.isActive()) {
				s.update(tilesData, player);
			}
		}
		for (DemonBoss demonBoss : demonBosses) {
			if(demonBoss.isActive()) {
				demonBoss.update(tilesData, player);
			}
		}
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawSlimes(g, xLvlOffset);
		drawDemonBosses(g, xLvlOffset);
		
		drawBossUI(g);
	}
	
	private void drawBossUI(Graphics g) {
		for (DemonBoss db : demonBosses) {
			// Hanya gambar jika bos masih hidup/aktif
			if(db.isActive()) {
				// 1. Tentukan ukuran dan posisi Health Bar (Statis di tengah bawah layar)
				int maxWidth = (int) (400 * GameCore.SCALE); 
				int height = (int) (20 * GameCore.SCALE);
				int xPos = (GameCore.GAME_WIDTH / 2) - (maxWidth / 2);
				int yPos = (int) (GameCore.GAME_HEIGHT - (40 * GameCore.SCALE)); // Posisi di bawah
				
				float distance = Math.abs(playStates.getPlayer().getHitbox().x - db.getHitBox().x);

				// 2. Kalkulasi persentase darah saat ini
				float healthPercentage = (float) db.getCurrentHealth() / db.getMaxHealth();
				int currentWidth = (int) (maxWidth * healthPercentage);

				// Pastikan lebar tidak negatif jika darah < 0
				if (currentWidth < 0) currentWidth = 0;

				if (distance < GameCore.GAME_WIDTH) {
					
					// 3. Gambar Background Bar (Warna Hitam/Abu-abu gelap)
					g.setColor(new java.awt.Color(50, 50, 50, 200));
					g.fillRect(xPos, yPos, maxWidth, height);

					// 4. Gambar Darah Boss (Warna Merah)
					g.setColor(new java.awt.Color(200, 50, 50));
					g.fillRect(xPos, yPos, currentWidth, height);

					// 5. Gambar Bingkai / Border (Warna Putih)
					g.setColor(java.awt.Color.WHITE);
					g.drawRect(xPos, yPos, maxWidth, height);

					// 6. Gambar Teks Nama Boss
					g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, (int)(16 * GameCore.SCALE)));
					g.drawString("DEMON BOSS", xPos, yPos - (int)(5 * GameCore.SCALE));
					
					break;
				}
			}
		}
	}
	
	private void drawSlimes(Graphics g, int xLvlOffset) {
	    Graphics g2 = g.create();
	    g2.translate(-xLvlOffset, 0);
	    
	    for (Slime s : Slimes) {
	        if(s.isActive()) {
	            int stateIndex = s.getEnemyState();
	            if (stateIndex == MATI) {
	                stateIndex = HURT; 
	            }
	            
	            if (s.getEnemyState() == MATI) {
	                if (s.getAniTick() % 8 < 4) {
	                    continue;
	                }
	            }
	            
	            g2.drawImage(slimeImg[stateIndex][s.getAniIndex()],
	                    (int) (s.getHitBox().x - SLIME_DRAWOFFSET_X + s.flipX()),
	                    (int) (s.getHitBox().y - SLIME_DRAWOFFSET_Y),
	                    SLIME_WIDTH * s.flipW(), SLIME_HEIGHT, null);

	             s.drawHitbox(g2); 
	             s.drawAttackBox(g2, xLvlOffset);
	        }
	    } 
	    g2.dispose();
	}
	
	private void drawDemonBosses(Graphics g, int xLvlOffset) {
	    Graphics g2 = g.create();
	    g2.translate(-xLvlOffset, 0);
	    
	    for (DemonBoss demonBoss : demonBosses) {
	        if(demonBoss.isActive()) {
	            BufferedImage frame = demonBossImg[demonBoss.getEnemyState()][demonBoss.getAniIndex()];
	            if (frame == null) {
	            	continue;
	            }
	            
	            g2.drawImage(frame,
	                    demonBoss.drawX(),
	                    demonBoss.drawY(),
	                    DEMON_BOSS_WIDTH * demonBoss.flipW(), DEMON_BOSS_HEIGHT, null);

	            demonBoss.drawHitbox(g2); 
	            demonBoss.drawAttackBox(g, xLvlOffset);
	        }
	    } 
	    g2.dispose();
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox, int damage, Player player) {
	    for (Slime s : Slimes) {
	        if(s.isActive()) {
	            if (attackBox.intersects(s.getHitBox())) {
	                int kbDir = (player.getHitbox().x < s.getHitBox().x) ? 1 : -1;
	                s.hurt(damage, kbDir, true); 
	                return;
	            }
	        }
	    }
	    
	    for (DemonBoss demonBoss : demonBosses) {
	        if(demonBoss.isActive()) {
	            if (attackBox.intersects(demonBoss.getHitBox())) {
	                int kbDir = (player.getHitbox().x < demonBoss.getHitBox().x) ? 1 : -1;
	                boolean isCharge = player.isChargeAttack();
	                demonBoss.hurt(damage, kbDir, isCharge); 
	                return;
	            }
	        }
	    }
	}
	
	private void loadEnemyImages() {
		slimeImg = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
		for (int j = 0; j < slimeImg.length; j++) {
			for (int i = 0; i < slimeImg[j].length; i++) {
				slimeImg[j][i] = temp.getSubimage(i * SLIME_WIDTH_DEFAULT, j * SLIME_HEIGHT_DEFAULT,
						SLIME_WIDTH_DEFAULT, SLIME_HEIGHT_DEFAULT);
			}
		}
		
		demonBossImg = new BufferedImage[6][DEMON_BOSS_SPRITE_COLUMNS];
		BufferedImage demonBossSheet = LoadSave.GetSpriteAtlas(LoadSave.DEMON_BOSS_SPRITE);
		loadDemonBossAnimation(demonBossSheet, IDLE, 0);
		loadDemonBossAnimation(demonBossSheet, WALK, 1);
		loadDemonBossAnimation(demonBossSheet, ATTACK, 2);
		loadDemonBossAnimation(demonBossSheet, HURT, 3);
		loadDemonBossAnimation(demonBossSheet, MATI, 4);
	}
	
	private void loadDemonBossAnimation(BufferedImage demonBossSheet, int targetState, int sourceRow) {
		for (int i = 0; i < GetSpriteAmount(DEMON_BOSS, targetState); i++) {
			demonBossImg[targetState][i] = demonBossSheet.getSubimage(i * DEMON_BOSS_WIDTH_DEFAULT,
					sourceRow * DEMON_BOSS_HEIGHT_DEFAULT, DEMON_BOSS_WIDTH_DEFAULT, DEMON_BOSS_HEIGHT_DEFAULT);
		}
	}

	public void resetAllEnemies() {
		for(Slime s : Slimes) {
			s.resetEnemy();
		}
		for(DemonBoss demonBoss : demonBosses) {
			demonBoss.resetEnemy();
		}
		
	}
}
