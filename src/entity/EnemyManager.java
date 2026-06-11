package entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.PlayStates;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.EnemyConstants.*;

public class EnemyManager {

	private PlayStates playStates;
	private BufferedImage[][] slimeImg;
	private ArrayList<Slime> Slimes = new ArrayList<Slime>();

	public EnemyManager(PlayStates playStates) {
		this.playStates = playStates;
		loadEnemyImages();
		addEnemies();
	}

	private void addEnemies() {
		Slimes = LoadSave.GetSlimes("/map_test.txt");
	}

	public void update(int[][] tilesData, Player player) {
		for (Slime s : Slimes) {
			if(s.isActive()) {
				s.update(tilesData, player);
			}
		}

	}

	public void draw(Graphics g, int xLvlOffset) {
		drawSlimes(g, xLvlOffset);
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
	
	public void checkEnemyHit(Rectangle2D.Float attackBox, int damage) {
		for (Slime s : Slimes) {
			if(s.isActive()) {
				if (attackBox.intersects(s.getHitBox())) {
					s.hurt(damage); 
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
	}

	public void resetAllEnemies() {
		for(Slime s : Slimes) {
			s.resetEnemy();
		}
		
	}
}
