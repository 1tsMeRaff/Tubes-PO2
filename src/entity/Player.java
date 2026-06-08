package entity;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;

import static utilitytools.Konstanta.KonstantaPlayerRight.*;
import static utilitytools.HelpMethods.*;

import objects.GameContainer;

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
	
	//status
	private BufferedImage statusBarImg;
	
	private int statusBarWidth = (int) (192 * GameCore.SCALE);
	private int statusBarHeight = (int) (58 * GameCore.SCALE);
	private int statusBarX = (int) (10 * GameCore.SCALE);
	private int statusBarY = (int) (10 * GameCore.SCALE);

	private int healthBarWidth = (int) (150 * GameCore.SCALE);
	private int healthBarHeight = (int) (9 * GameCore.SCALE);
	private int healthBarXStart = (int) (34 * GameCore.SCALE);
	private int healthBarYStart = (int) (14 * GameCore.SCALE);
	
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
        updateManaBar(); // Tambahan untuk update mana

        if(currentHealth <= 0) {
            playStates.setGameOver(true);
            return;
        }

        updateAttackBox();
        updatePos();
        if(attacking) {
            checkAttack();
        }
        setAnimation();
        updateAnimationTick();
    }

    private void updateManaBar() {
        manaWidth = (int) ((currentMana / (float) maxMana) * manaBarWidth);
    }



    public void addItemToInventory(int objType) {
        if (inventory.size() < maxInventorySize) {
            inventory.add(objType);
            System.out.println("Item masuk tas. Tipe ID: " + objType);
        } else {
            System.out.println("Penyimpanan Penuh!");
        }
    }

    public void useItem(int itemIndex) {
        if (itemIndex < inventory.size()) {
            int potionType = inventory.get(itemIndex);
            int value = 0;
            boolean isRed = potionType <= 2;
            
            switch (potionType) {
                case utilitytools.Konstanta.ObjectConstants.RED_POTION_1: value = utilitytools.Konstanta.ObjectConstants.RED_VAL_1; break;
                case utilitytools.Konstanta.ObjectConstants.RED_POTION_2: value = utilitytools.Konstanta.ObjectConstants.RED_VAL_2; break;
                case utilitytools.Konstanta.ObjectConstants.RED_POTION_3: value = utilitytools.Konstanta.ObjectConstants.RED_VAL_3; break;
                case utilitytools.Konstanta.ObjectConstants.BLUE_POTION_1: value = utilitytools.Konstanta.ObjectConstants.BLUE_VAL_1; break;
                case utilitytools.Konstanta.ObjectConstants.BLUE_POTION_2: value = utilitytools.Konstanta.ObjectConstants.BLUE_VAL_2; break;
                case utilitytools.Konstanta.ObjectConstants.BLUE_POTION_3: value = utilitytools.Konstanta.ObjectConstants.BLUE_VAL_3; break;
            }
            
            if (isRed) {
                changeHealth(value);
                System.out.println("Berhasil minum Ramuan Merah! Darah sekarang: " + currentHealth);
            } else {
                changeMana(value); 
                System.out.println("Berhasil minum Ramuan Biru! Mana sekarang: " + currentMana);
            }
            
   
            updateHealthBar();
            updateManaBar();
            
            // Hapus item dari tas
            inventory.remove(itemIndex); 
        } else {
            System.out.println("Slot ini kosong!");
        }
    }
    public void changeMana(int value) {
        currentMana += value;
        if (currentMana < 0) currentMana = 0;
        else if (currentMana > maxMana) currentMana = maxMana;
    }
	
	private void checkAttack() {
		if(attackCheck) {
			return;
		}
		attackCheck = true;
		playStates.checkHitEnemy(AttackBox);
	}
	
	private void updateAttackBox() {
	    if (flipW == 1) {
	        AttackBox.x = hitBox.x + hitBox.width + (int) (GameCore.SCALE * 5);
	    } else if (flipW == -1) {
	        AttackBox.x = hitBox.x - AttackBox.width - (int) (GameCore.SCALE * 5);
	    }
	    AttackBox.y = hitBox.y + (GameCore.SCALE * 2); 
	}

	
	
	
	// Variabel Status Mana & Inventory
    private int maxMana = 100;
    private int currentMana = 50; 
    private int manaBarWidth = (int) (150 * GameCore.SCALE);
    private int manaBarHeight = (int) (9 * GameCore.SCALE);
    private int manaBarYStart = (int) (25 * GameCore.SCALE); 
    private int manaWidth = manaBarWidth;

    public ArrayList<Integer> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    
    
    
	
//	private void updateAttackBox() {
//		
//		if (right) {
//			AttackBox.x = hitBox.x + hitBox.width + (int) (GameCore.SCALE * 10);
//		}else if (left) {
//			AttackBox.x = hitBox.x - hitBox.width - (int) (GameCore.SCALE * 10);
//		}
//		AttackBox.y = hitBox.y + (GameCore.SCALE * 10);
//	}

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
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        
        // Render Health Bar (Merah)
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX + GameCore.TILES_SIZE, healthBarYStart + statusBarY,
                    healthWidth - GameCore.TILES_SIZE, healthBarHeight);
                    
        // Render Mana Bar (Biru)
        g.setColor(Color.blue);
        g.fillRect(healthBarXStart + statusBarX + GameCore.TILES_SIZE, manaBarYStart + statusBarY,
                    manaWidth - GameCore.TILES_SIZE, manaBarHeight);
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
					attackCheck = false;
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
			if(startAni != ATTACK_1) {
				aniIndex= 1;
				aniTick = 0;
				return;
			}
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

	    if(!left && !right && !inAir) {
	        return;
	    }

	    float xSpeed = 0;
	    if (attacking) {
	        return;
	    }

	    if(left) {
	        xSpeed -= playerSpeed;
	        flipX = width;
	        flipW = -1;
	    }
	    if(right) {
	        xSpeed += playerSpeed;
	        flipX = 0;
	        flipW = 1;
	    }

	    // Cek apakah pemain sedang berdiri di atas kotak/barrel
	    if(!inAir) {
	        if(!IsEntityOnFloor(hitBox, mapData)) {
	            Rectangle2D.Float boxUnderneath = new Rectangle2D.Float(hitBox.x, hitBox.y + 1, hitBox.width, hitBox.height);
	            if (playStates.getObjectManager().getIntersectingContainer(boxUnderneath) == null) {
	                inAir = true; // Jatuh jika tidak ada lantai dan tidak ada kotak di bawahnya
	            }
	        }
	    }

	    if(inAir) {
	        // Fisika Vertikal (Lompat / Jatuh)
	        Rectangle2D.Float nextYHitbox = new Rectangle2D.Float(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height);
	        GameContainer gcY = playStates.getObjectManager().getIntersectingContainer(nextYHitbox);

	        if(canMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, mapData) && gcY == null){
	            hitBox.y += airSpeed;
	            airSpeed += gravity;
	            updateXPos(xSpeed);
	        } else {
	            if (gcY != null) {
	                if (airSpeed > 0) { // Jatuh menimpa kotak
	                    hitBox.y = gcY.getHitbox().y - hitBox.height - 1f;
	                    resetInAir();
	                } else { // Lompat nabrak bawah kotak
	                    hitBox.y = gcY.getHitbox().y + gcY.getHitbox().height + 1f;
	                    airSpeed = fallSpeedAfterCollision;
	                }
	            } else {
	                hitBox.y = GetEntityPosUnderRoofOrAboveFloor(hitBox, airSpeed);
	                if(airSpeed > 0) {
	                    resetInAir();
	                } else {
	                    airSpeed = fallSpeedAfterCollision;
	                }
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
	    // Fisika Horizontal (Kiri / Kanan)
	    Rectangle2D.Float nextXHitbox = new Rectangle2D.Float(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height);
	    GameContainer gcX = playStates.getObjectManager().getIntersectingContainer(nextXHitbox);

	    if(canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, mapData) && gcX == null) {
	        hitBox.x += xSpeed;
	    } else {
	        if (gcX != null) {
	            if (xSpeed > 0) { // Berjalan ke kanan nabrak sisi kiri kotak
	                hitBox.x = gcX.getHitbox().x - hitBox.width - 1f;
	            } else if (xSpeed < 0) { // Berjalan ke kiri nabrak sisi kanan kotak
	                hitBox.x = gcX.getHitbox().x + gcX.getHitbox().width + 1f;
	            }
	        } else {
	            hitBox.x = GetEntityPosNextToWall(hitBox, xSpeed);
	        }
	    }
	}
	
	public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0) {
			currentHealth = 0;
			// gameOver();
		}else if (currentHealth >= maxHealth) {
			currentHealth = maxHealth;
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
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
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
	    currentHealth = maxHealth;
	    
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