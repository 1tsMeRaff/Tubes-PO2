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
		Slimes = LoadSave.GetSlimes("/map_test.txt");
	}

	public void update(int[][] tilesData, Player player) {
		for (Slime s : Slimes) {
			s.update(tilesData, player);
		}

	}

	public void draw(Graphics g, int xLvlOffset) {
		drawSlimes(g, xLvlOffset);
	}

	private void drawSlimes(Graphics g, int xLvlOffset) {
		Graphics g2 = g.create();
		g2.translate(-xLvlOffset, 0);
		for (Slime s : Slimes) {
			g2.drawImage(slimeImg[s.getEnemyState()][s.getAniIndex()],
					(int) (s.getHitBox().x - SLIME_DRAWOFFSET_X + s.flipX()),
					(int) (s.getHitBox().y - SLIME_DRAWOFFSET_Y),
					SLIME_WIDTH * s.flipW(), SLIME_HEIGHT, null);

			// (OPSIONAL) Untuk debugging melihat kotak hitbox merah:
			s.drawHitbox(g2);
		}
		g2.dispose();
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
}
