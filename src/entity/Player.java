package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import utilitytools.LoadSave;

import static utilitytools.Konstanta.KonstantaPlayerRight.*;

public class Player extends Entity {

	private BufferedImage[][] animasi;
	private int aniTick, aniIndex, aniSpeed = 15;
	private int playerAction = IDLE_ACTIVE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down;
	private float playerSpeed = 2.0f;
	private int[][] lvlData;
	
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
	}

	
	public void update() {
		
		updatePos();
		updateHitBox();
	    setAnimation();
	    updateAnimationTick();
	}
	
	public void render(Graphics g) {
		g.drawImage(animasi[playerAction][aniIndex], (int) x, (int) y, width, height, null);
		drawHitbox(g);
	}
	
	private void updateAnimationTick() {
		
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				if(playerAction == ATTACK_1) {
					attacking = false;
				}
			}
		}
		
	}
	
	private void setAnimation() {
		
		int startAni = playerAction;
		
		if(moving) {
			playerAction = LARI;
		}else {
			playerAction = IDLE_ACTIVE;
		}
		
		if(attacking) {
			playerAction = ATTACK_1;
		}
		
		if(startAni != playerAction) {
			resetAniTick();
		}
		
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
		
	}


	private void updatePos() {
		
		moving = false;
		
		if (attacking) {
	        return; 
	    }
		
		if(left && !right) {
			x -= playerSpeed;
			moving = true;
		}else if(right && ! left) {
			x += playerSpeed;
			moving = true;
		}
		
		if(up && !down) {
			y -= playerSpeed;
			moving = true;
		}else if (down && !up) {
			y += playerSpeed;
			moving = true;
		}
		
	}
	
	private void loadAnimations() {
		
			BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_SPRITE);
			
			animasi = new BufferedImage[15][16];
			
			for(int j = 0; j < animasi.length; j++) {
				for(int i = 0; i < animasi[j].length; i ++) {
					animasi[j][i] = image.getSubimage(i * 80, j * 64, 80, 64); 
				}
			}
	}
	
	public void loadLvlData() {
		this.lvlData = lvlData;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setAttack(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}


	public void setLeft(boolean left) {
		this.left = left;
	}


	public boolean isUp() {
		return up;
	}


	public void setUp(boolean up) {
		this.up = up;
	}


	public boolean isRight() {
		return right;
	}


	public void setRight(boolean right) {
		this.right = right;
	}


	public boolean isDown() {
		return down;
	}


	public void setDown(boolean down) {
		this.down = down;
	}
}
