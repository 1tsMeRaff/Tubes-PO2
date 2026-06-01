package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.GameCore;
import utilitytools.LoadSave;

import static utilitytools.Konstanta.KonstantaPlayerRight.*;
import static utilitytools.HelpMethods.*;

public class Player extends Entity {

	private BufferedImage[][] animasi;
	private int aniTick, aniIndex, aniSpeed = 15;
	private int playerAction = IDLE_ACTIVE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down, jump;
	private float playerSpeed = 1.0f * GameCore.SCALE;
	private int[][] mapData;
	private float xDrawOffSet = 26 * GameCore.SCALE;
	private float yDrawOffSet = 10 * GameCore.SCALE;
	
	// Jumping
	private float airSpeed = 0f;
	private float gravity = 0.04f * GameCore.SCALE;
	private float jumpSpeed = -2.25f * GameCore.SCALE;
	private float fallSpeedAfterCollision = 0.5f * GameCore.SCALE;
	private boolean inAir = true;
	
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initHitBox(x, y, (int) (18 * GameCore.SCALE), (int) (23 * GameCore.SCALE));
	}

	public void update() {
		updatePos();
		setAnimation();
		updateAnimationTick();
	}
	
	public void render(Graphics g, int xLvlOffset) {
		g.drawImage(animasi[playerAction][aniIndex], 
					(int) (hitBox.x - xDrawOffSet) - xLvlOffset, 
					(int) (hitBox.y - yDrawOffSet), width, height, null);
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
		} else {
			playerAction = IDLE_ACTIVE;
		}
		
		if(inAir) {
			if(airSpeed < 0) {
				playerAction = LOMPAT;
			} else {
				playerAction = JATUH;
			}
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
	    if(jump) {
	        jump();
	    }
	    
	    // 1. EARLY RETURN (Biarkan di atas): Jika player diam di tanah, langsung keluar 
	    // agar terhindar dari bug getaran/pembulatan floating point koordinat.
	    if(!left && !right && !inAir) {
	        return;	
	    }
	    
	    float xSpeed = 0;
	    if (attacking) {
	        return; 
	    }

	    if(left) {
	        xSpeed -= playerSpeed;
	    }
	    if(right) {
	        xSpeed += playerSpeed;
	    }
	    
	    // 2. [PINDAHKAN KE SINI] Cek lantai dilakukan SAAT player memang sedang bergerak/berpindah
	    if(!inAir) {
	        if(!IsEntityOnFloor(hitBox, mapData)) {
	            inAir = true;
	        }
	    }
	    
	    if(inAir) {
	        if(canMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, mapData)){
	            hitBox.y += airSpeed;
	            airSpeed += gravity;
	            updateXPos(xSpeed);
	        } else {
	            hitBox.y = GetEntityPosUnderRoofOrAboveFloor(hitBox, airSpeed);
	            if(airSpeed > 0) {
					resetInAir();
	            } else {
	                airSpeed = fallSpeedAfterCollision;
	            }
	            updateXPos(xSpeed);
	        }
	    } else {
	        updateXPos(xSpeed);
	    }
	    
	    moving = true;
	}
	
	private void jump() {
		if(inAir) {
			return;
		}
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if(canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, mapData)) {
			hitBox.x += xSpeed;
		} else {
			hitBox.x = GetEntityPosNextToWall(hitBox, xSpeed);
		}
	}

	private void loadAnimations() {
		BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_SPRITE);
		animasi = new BufferedImage[22][16];

		for(int j = 0; j < animasi.length; j++) {
			for(int i = 0; i < animasi[j].length; i ++) {
				animasi[j][i] = image.getSubimage(i * 80, j * 64, 80, 64); 
			}
		}
	}
	
	public void loadmapData(int[][] mapData) {
		this.mapData = mapData;
		if(!IsEntityOnFloor(hitBox, mapData)) {
			inAir = true;
		}
	}

	public void resetDirBooleans() {
		left = false; right = false; up = false; down = false;
	}

	// [PERBAIKAN] Tambahkan parameter koordinat baru untuk target map selanjutnya
	public void resetAll(float newX, float newY) {
	    resetDirBooleans();
	    inAir = false;
	    moving = false;
	    attacking = false;
	    playerAction = IDLE_ACTIVE;
	    
	    // Perbarui koordinat dasar Entity dan Hitbox ke posisi map baru
	    this.x = newX;
	    this.y = newY;
	    hitBox.x = newX;
	    hitBox.y = newY;
	    
	    // Pastikan mapData sudah di-load terlebih dahulu sebelum mengecek ini
	    if (mapData != null && !IsEntityOnFloor(hitBox, mapData)) {
	        inAir = true;
	    }
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

	// Method ini ada di branch `dev` tapi hilang di branch `dev-Arya`
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	// [PERBAIKAN] Menggunakan hitBox (huruf B besar) sesuai deklarasi di Entity
	public java.awt.geom.Rectangle2D.Float getHitbox() {
		return hitBox;
	}
}