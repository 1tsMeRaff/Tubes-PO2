package entity;

import java.awt.Graphics;
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
		Slimes = LoadSave.GetSlimes("/untitled1.csv");
//		System.out.print("Size of Crabs : " + Slimes.size);
	}

	public void update() {
		for(Slime s : Slimes) {
			s.update();
		}
		
	}
	
	public void draw(Graphics g) {
		drawSlimes(g);
	}

	private void drawSlimes(Graphics g) {
		for(Slime c : Slimes) {
			g.drawImage(slimeImg[c.getEnemyState()][c.getAniIndex()], 
					(int)(c.getHitBox().x), (int)(c.getHitBox().y), 
					SLIME_WIDTH, SLIME_HEIGHT, null);
		}
		
	}

	private void loadEnemyImages() {
		slimeImg = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
		for(int j = 0; j < slimeImg.length; j++) {
			for(int i = 0; i < slimeImg[j].length; i++) {
				slimeImg[j][i] = temp.getSubimage(i * SLIME_WIDTH_DEFAULT, j * SLIME_HEIGHT_DEFAULT, SLIME_WIDTH_DEFAULT, SLIME_HEIGHT_DEFAULT);
			}
		}
	}
}













