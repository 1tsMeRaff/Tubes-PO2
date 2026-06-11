package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.PlayStates;
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
	
	// Status
	private BufferedImage statusBarImg;
	
	private int statusBarWidth = (int) (192 * GameCore.SCALE);
	private int statusBarHeight = (int) (58 * GameCore.SCALE);
	private int statusBarX = (int) (10 * GameCore.SCALE);
	private int statusBarY = (int) (10 * GameCore.SCALE);

	private int healthBarWidth = (int) (105 * GameCore.SCALE);
	private int healthBarHeight = (int) (5 * GameCore.SCALE);
	private int healthBarXStart = (int) (65 * GameCore.SCALE);
	private int healthBarYStart = (int) (15 * GameCore.SCALE);
	
	private int maxHealth = 100;
	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;
	
	// AttackBox
	private Rectangle2D.Float AttackBox;
	
	private int flipX = 0;
	private int flipW = 1;
	
	private boolean attackCheck;
	private PlayStates playStates;
	
	public Player(float x, float y, int width, int height, PlayStates playStates) {
		super(x, y, width, height);
		this.playStates = playStates;
		loadAnimations();
		initHitBox(x, y, (int) (18 * GameCore.SCALE), (int) (23 * GameCore.SCALE));
		initAttackBox();
	}

	private void initAttackBox() {
		AttackBox = new Rectangle2D.Float(x, y, (int) (20 * GameCore.SCALE), (int) (20 * GameCore.SCALE));
	}

	public void update() {
		updateHealthBar();
		
		if (currentHealth <= 0) {
			playStates.setGameOver(true);
		} else {
			updateAttackBox();
			updatePos();
			
			// [SENSOR POTION DITAMBAHKAN DI SINI]
			checkPotionTouched();
			
			if (attacking) {
				checkAttack();
			}
		}
		
		setAnimation();
		updateAnimationTick();
	}
	
	// [METHOD SENSOR POTION]
	private void checkPotionTouched() {
		playStates.checkPotionTouched(hitBox);
	}
	
	private void checkAttack() {
		if (attackCheck) {
			return;
		}
		attackCheck = true;
		
		// [SENSOR PEDANG KE MUSUH]
		playStates.checkHitEnemy(AttackBox);
		
		// [SENSOR PEDANG KE BARREL DITAMBAHKAN DI SINI]
		playStates.checkObjectHit(AttackBox);
		
		playStates.getGameCore().getAudioPlayer().playAttackSound();
	}
	
	private void updateAttackBox() {
		if (flipW == 1) {
			AttackBox.x = hitBox.x + hitBox.width + (int) (GameCore.SCALE * 5);
		} else if (flipW == -1) {
			AttackBox.x = hitBox.x - AttackBox.width - (int) (GameCore.SCALE * 5);
		}
		AttackBox.y = hitBox.y + (GameCore.SCALE * 2);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
	}

	public void render(Graphics g, int xLvlOffset) {
		g.drawImage(animasi[playerAction][aniIndex], 
					(int) (hitBox.x - xDrawOffSet) - xLvlOffset + flipX, 
					(int) (hitBox.y - yDrawOffSet), 
					width * flipW, height, null);
		drawAttackBox(g, xLvlOffset);
		drawUI(g);
	}
	
	private void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (AttackBox.x) - xLvlOffset, (int) AttackBox.y, (int) (AttackBox.width), (int) (AttackBox.height));
	}
	
	private void drawUI(Graphics g) {
		// Gambar bingkai UI-nya
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		
		// Gambar darah merahnya (tanpa rumus yang aneh-aneh)
		g.setColor(Color.red);
		g.fillRect(statusBarX + healthBarXStart, statusBarY + healthBarYStart, healthWidth, healthBarHeight);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				if (playerAction == ATTACK_1) {
					attacking = false;
					attackCheck = false;
				}
			}
		}
	}
	
	private void setAnimation() {
		int startAni = playerAction;
		
		if (moving) {
			playerAction = LARI;
		} else {
			playerAction = IDLE_ACTIVE;
		}
		
		if (inAir) {
			if (airSpeed < 0) {
				playerAction = LOMPAT;
			} else {
				playerAction = JATUH;
			}
		}
		
		if (attacking) {
			playerAction = ATTACK_1;
			if (startAni != ATTACK_1) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		
		if (startAni != playerAction) {
			resetAniTick();
		}
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		if (jump) {
			jump();
		}
		
		if (!left && !right && !inAir) {
			return;	
		}
		
		float xSpeed = 0;
		if (attacking) {
			return; 
		}

		if (left) {
			xSpeed -= playerSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right) {
			xSpeed += playerSpeed;
			flipX = 0;
			flipW = 1;
		}
		
		if (!inAir) {
			if (!IsEntityOnFloor(hitBox, mapData)) {
				inAir = true;
			}
		}
		
		if (inAir) {
			if (canMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, mapData)){
				hitBox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitBox.y = GetEntityPosUnderRoofOrAboveFloor(hitBox, airSpeed);
				if (airSpeed > 0) {
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
		if (inAir) {
			return;
		}
		inAir = true;
		airSpeed = jumpSpeed;
		playStates.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.JUMP);
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, mapData)) {
			hitBox.x += xSpeed;
		} else {
			hitBox.x = GetEntityPosNextToWall(hitBox, xSpeed);
		}
	}
	
	public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0) {
			currentHealth = 0;
		} else if (currentHealth >= maxHealth) {
			currentHealth = maxHealth;
		}	
	}

	private void loadAnimations() {
		BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_SPRITE);
		animasi = new BufferedImage[22][16];

		for (int j = 0; j < animasi.length; j++) {
			for (int i = 0; i < animasi[j].length; i ++) {
				animasi[j][i] = image.getSubimage(i * 80, j * 64, 80, 64); 
			}
		}
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}
	
	public void loadmapData(int[][] mapData) {
		this.mapData = mapData;
		if (!IsEntityOnFloor(hitBox, mapData)) {
			inAir = true;
		}
	}

	public void resetDirBooleans() {
		left = false; right = false; up = false; down = false;
	}

	public void resetAll(float newX, float newY) {
		resetDirBooleans();
		inAir = false;
		moving = false;
		attacking = false;
		playerAction = IDLE_ACTIVE;
		currentHealth = maxHealth;
		
		this.x = newX;
		this.y = newY;
		hitBox.x = newX;
		hitBox.y = newY;
		
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

	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	public java.awt.geom.Rectangle2D.Float getHitbox() {
		return hitBox;
	}
}