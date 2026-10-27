package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;
import objects.GameContainer;

import core.interfaces.Damageable;
import ui.PlayerHUD;

import static utilitytools.Konstanta.KonstantaPlayerRight.*;
import static utilitytools.HelpMethods.*;

public class Player extends Entity implements Damageable {

    private final PlayerStats stats = new PlayerStats();
    private final PlayerHUD playerHUD = new PlayerHUD(); 
    private final PlayerPhysics physics = new PlayerPhysics(this);
    private final PlayerCombat combat = new PlayerCombat(this); 
    private final PlayerInventory playerInventory = new PlayerInventory(this);

    // Animasi & Render
    private BufferedImage[][] animasi;
    private int aniTick, aniIndex, aniSpeed = 4;
    private int playerAction = IDLE_ACTIVE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    
    private float playerScale = 1.5f;
    private float playerSpeed = 3.33f * GameCore.SCALE;
    private int[][] mapData;
    private float xDrawOffSet = 26 * GameCore.SCALE * playerScale;
    private float yDrawOffSet = 13 * GameCore.SCALE * playerScale;

    // Fisika & Gravitasi
    private float airSpeed = 0f;
    private float gravity = 0.444f * GameCore.SCALE;
    private float jumpSpeed = -7.5f * GameCore.SCALE;
    private float fallSpeedAfterCollision = 1.66f * GameCore.SCALE;
    private boolean canDoubleJump = true;
    private boolean inAir = true;

    // Dash
    private boolean dashing = false;
    private boolean canDash = true;
    private int dashDuration = 18; 
    private int dashTick = 0;
    private int dashCooldown = 30;
    private int dashCooldownTick = 0;
    private float dashSpeed = 5.0f * GameCore.SCALE;

    // Save Point
    private float lastSafeX;
    private float lastSafeY;
    
    // Charge Attack
    private boolean charging = false;
    private int chargeTick = 0;
    private final int CHARGE_DURATION_NEEDED = 30;
    private boolean isExecutingChargeAttack = false;
    private boolean attackCheck;
    private Rectangle2D.Float AttackBox;
    private final Rectangle2D.Float scratchHitbox = new Rectangle2D.Float();
    
    // I-Frames & Knockback
    private boolean isInvincible = false;
    private int invincibilityTick = 0;
    private final int INVINCIBILITY_DURATION = 45;
    private float knockbackSpeed = 0;
    private int knockbackDir = 1;
    private float knockbackFriction = 0.15f * GameCore.SCALE;
    private boolean isKnockedDown = false;

    // Witch Curse & Feline Soul Mechanics
    private boolean isGroundSlamming = false;
    private int curseCooldownTick = 0;
    public static final int CURSE_COOLDOWN = 30;
    public static final int CURSE_MANA_COST = 25;

    // Inventory & Equipment (SRP Delegation to PlayerInventory)
    public ArrayList<Integer> inventory = playerInventory.getItems();
    public final int maxInventorySize = PlayerInventory.MAX_INVENTORY_SIZE;

    private int flipX = 0;
    private int flipW = 1;
    private PlayStates playStates;

    public Player(float x, float y, int width, int height, PlayStates playStates) {
        super(x, y, width, height);
        this.playerScale = 1.5f;
        this.width = (int) (width * playerScale);
        this.height = (int) (height * playerScale);
        this.playStates = playStates;
        
        loadAnimations();
        initHitBox(x, y, (int) (18 * GameCore.SCALE * playerScale), (int) (18 * GameCore.SCALE * playerScale));
        initAttackBox();
    }

    private void initAttackBox() {
        AttackBox = new Rectangle2D.Float(x, y, (int) (1 * GameCore.SCALE * playerScale), (int) (15 * GameCore.SCALE * playerScale));
    }

    public void update() {
        if (isInvincible) {
            invincibilityTick++;
            if (invincibilityTick >= INVINCIBILITY_DURATION && !isKnockedDown) {
                isInvincible = false;
                invincibilityTick = 0;
            }
        }
        
        if (knockbackSpeed > 0) {
            knockbackSpeed -= knockbackFriction;
            if (knockbackSpeed < 0) {
                knockbackSpeed = 0;
            }
        }
        
        if (stats.getCurrentHealth() <= 0) {
            playStates.setGameOver(true);
            return;
        }

        checkChasm();
        updateAttackBox();
        updatePos();
        checkPotionTouched();
        
        if (attacking) {
            checkAttack();
            
        }
        if (charging) {
            chargeTick++;
        }
        if (!canDash && !dashing) {
            dashCooldownTick++;
            if (dashCooldownTick >= dashCooldown) {
                canDash = true;
                dashCooldownTick = 0;
            }
        }
        if (curseCooldownTick > 0) {
            curseCooldownTick--;
        }
        setAnimation();
        updateAnimationTick();
    }


    private void checkChasm() {
        if (hitBox.y + hitBox.height >= main.GameCore.GAME_HEIGHT - 5) {
            if (stats.getCurrentHealth() > 0) {
                changeHealth(-20);
                hitBox.x = lastSafeX;
                hitBox.y = lastSafeY - 15; 
                inAir = true; 
                airSpeed = 0;
                moving = false;
                jump = false;
            }
        }
    }

    public void calculateDefense() {
        stats.recalculateDefense(playerInventory.calculateDefenseBonus());
    }

    public boolean addItemToInventory(int objType) {
        return playerInventory.addItem(objType);
    }

    public void unequipItem(int equipSlotIndex) {
        playerInventory.unequipItem(equipSlotIndex);
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
                System.out.println("Berhasil minum Ramuan Merah! Darah sekarang: " + stats.getCurrentHealth());
            } else {
                changeMana(value);
                System.out.println("Berhasil minum Ramuan Biru! Mana sekarang: " + stats.getCurrentMana());
            }
            inventory.remove(itemIndex);
        } else {
            System.out.println("Slot ini kosong!");
        }
    }

    public void equipItem(int itemIndex, String equipmentType) {
        playerInventory.equipItem(itemIndex, equipmentType);
    }

    public void changeHealth(int value) {
        boolean damaged = stats.changeHealth(value, isInvincible);
        if (damaged) {
            isInvincible = true;
            if (playStates != null) {
                if (playStates.getFloatingTextPool() != null) {
                    playStates.getFloatingTextPool().spawnDamageTaken(hitBox.x + hitBox.width / 2, hitBox.y, -value);
                }
                if (playStates.getParticlePool() != null) {
                    playStates.getParticlePool().spawnBlood(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height / 2);
                }
            }
        } else if (value > 0 && playStates != null && playStates.getFloatingTextPool() != null) {
            playStates.getFloatingTextPool().spawnHeal(hitBox.x + hitBox.width / 2, hitBox.y, value);
        }
    }

    @Override
    public void hurt(int amount) {
        changeHealth(-amount);
    }

    @Override
    public boolean isAlive() {
        return stats.getCurrentHealth() > 0;
    }

    public void changeMana(int value) {
        stats.changeMana(value);
    }

    private void checkPotionTouched() {
        playStates.checkPotionTouched(hitBox);
    }

    private void checkAttack() {
        if (attackCheck) return;
        
        int hitFrame = isExecutingChargeAttack ? 9 : 3;
        
        if (aniIndex == hitFrame) {
            attackCheck = true;
            int damageToDeal = isExecutingChargeAttack ? (int)(stats.getDps() * 2.5) : (int)stats.getDps();
            

            int randomAttack = 4 + new java.util.Random().nextInt(3);
            playStates.getGameCore().getAudioPlayer().playEffect(randomAttack);
            



            playStates.checkHitEnemy(AttackBox, damageToDeal);
            playStates.checkObjectHit(AttackBox);
        }
    }

    private void updateAttackBox() {
        AttackBox.y = hitBox.y + (GameCore.SCALE * 2 * playerScale);
        int currentAttackWidth;
        int normalAttackWidth = 20;
        int chargeAttackWidht = 25;
        
        if (isExecutingChargeAttack) {
            currentAttackWidth = (int) (chargeAttackWidht * GameCore.SCALE * playerScale);
        } else {
            currentAttackWidth = (int) (normalAttackWidth * GameCore.SCALE * playerScale);
        }
        
        AttackBox.width = currentAttackWidth;
        if (flipW == 1) {
            AttackBox.x = hitBox.x + hitBox.width;
        } else {
            AttackBox.x = hitBox.x - AttackBox.width;
        }
    }

    public void render(Graphics g, int xLvlOffset) {
        if (isInvincible && invincibilityTick % 10 < 5) {
        	
        } else {
            g.drawImage(animasi[playerAction][aniIndex],
                    (int) (hitBox.x - xDrawOffSet) - xLvlOffset + flipX,
                    (int) (hitBox.y - yDrawOffSet),
                    width * flipW, height, null);
        }
        
        // drawAttackBox(g, xLvlOffset);
        // drawHitbox(g);
        drawUI(g);
    }
    
    private void drawUI(Graphics g) {
        playerHUD.draw(g, stats.getCurrentHealth(), stats.getMaxHealth(), stats.getCurrentMana(), stats.getMaxMana());
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                
                if (playerAction == ATTACK_1 || playerAction == CHARGE_ATTACK) {
                    attacking = false;
                    isExecutingChargeAttack = false;
                    attackCheck = false;
                } else if (playerAction == DOWN) {
                    if (!inAir) {
                        playerAction = ARISE;
                    } else {
                        aniIndex = GetSpriteAmount(DOWN) - 1;
                    }
                } else if (playerAction == ARISE) {
                    isKnockedDown = false;
                }
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;
        
        if (isKnockedDown) {
            return;
        }
        
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
        
        if (dashing) {
            if(!inAir) {
                playerAction = DASH;
            } else {
                playerAction = JATUH;
            }
        } else if (attacking) {
            if (isExecutingChargeAttack) {
                playerAction = CHARGE_ATTACK;
            } else {
                playerAction = ATTACK_1;
            }
            if (startAni != playerAction) {
                aniIndex = 0;
                aniTick = 0;
                attackCheck = false;
                return;
            }
        } else if (charging) {
            playerAction = GUARD;
        }
        
        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }
    
    public void setDash(boolean dash) {
        if (dash && canDash && !dashing && !attacking && !charging) {
            this.dashing = true;
            this.canDash = false;
            this.dashTick = 0;
        }
    }

    public void setCharging(boolean charging) {
        if (inAir) {
            if (charging && !attacking && !dashing) {
                if (down) {
                    executeGroundSlam();
                } else {
                    this.isExecutingChargeAttack = false;
                    this.attacking = true;
                }
            }
            return;
        }
        if (this.charging == charging) return;
        this.charging = charging;
        if (charging) {
            chargeTick = 0;
        }
    }

    public void executeGroundSlam() {
        if (!inAir) return;
        this.isGroundSlamming = true;
        this.attacking = true;
        this.airSpeed = 11.5f * main.GameCore.SCALE;
        if (playStates != null && playStates.getParticlePool() != null) {
            playStates.getParticlePool().spawnSoulTrail(hitBox.x + (hitBox.width / 2.0f), hitBox.y);
        }
    }

    public boolean castWitchCurse() {
        if (curseCooldownTick > 0) return false;

        if (stats.getCurrentMana() < CURSE_MANA_COST) {
            if (playStates != null && playStates.getFloatingTextPool() != null) {
                playStates.getFloatingTextPool().spawn(hitBox.x + (hitBox.width / 2.0f), hitBox.y - 12, "No Mana!", effects.FloatingTextPool.MANA_TEXT);
            }
            return false;
        }

        stats.changeMana(-CURSE_MANA_COST);
        curseCooldownTick = CURSE_COOLDOWN;

        int curseDamage = (int) (stats.getDps() * 2.5f) + (stats.getLevel() * 6);
        int dir = (flipW == 1) ? 1 : -1;
        float startX = hitBox.x + (dir == 1 ? hitBox.width : -20);
        float startY = hitBox.y + (hitBox.height / 3.0f);

        if (playStates != null && playStates.getCursePool() != null) {
            playStates.getCursePool().spawn(startX, startY, dir, curseDamage);
            playStates.getParticlePool().spawnSoulMagic(startX, startY, 12);
            playStates.shakeScreen(3, 2);
        }

        if (playStates != null && playStates.getGameCore() != null && playStates.getGameCore().getAudioPlayer() != null) {
            playStates.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.ATTACK_THREE);
        }

        return true;
    }

    public void releaseAttack() {
        if (!charging) return;
        this.charging = false;
        if (chargeTick >= CHARGE_DURATION_NEEDED) {
            isExecutingChargeAttack = true;
        } else {
            isExecutingChargeAttack = false;
        }
        attacking = true;
        chargeTick = 0;
        
        playStates.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.SLASH_SOUND);
    }

    private void updatePos() {
        moving = false;
        if (jump && !charging && !isKnockedDown) {
            jump();
        }

        float xSpeed = 0;

        if (dashing) {
            dashTick++;
            if (dashTick % 3 == 0 && playStates != null && playStates.getParticlePool() != null) {
                playStates.getParticlePool().spawnDust(hitBox.x + (flipW == 1 ? 0 : hitBox.width), hitBox.y + hitBox.height);
            }
            if (dashTick >= dashDuration) {
                dashing = false;
            }
        }

        if (knockbackSpeed > 0) {
            xSpeed = knockbackSpeed * knockbackDir;
            knockbackSpeed -= 0.15f * GameCore.SCALE;
            if (knockbackSpeed < 0) {
            	knockbackSpeed = 0;
            }
        } else if (dashing) {
            airSpeed = 0;
            if (flipW == 1) {
            	xSpeed = dashSpeed;
            }
            else {
            	xSpeed = -dashSpeed;
            }
        } else if (!isKnockedDown) {
            if (!attacking && !isExecutingChargeAttack && !charging) {
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
            } else {
                if (inAir && attacking) {
                    xSpeed = (flipW == 1) ? playerSpeed * 0.4f : -playerSpeed * 0.4f;
                }
            }
        }

        if (!inAir) {
            if (hitBox.y + hitBox.height < main.GameCore.GAME_HEIGHT - 32) {
                lastSafeX = hitBox.x;
                lastSafeY = hitBox.y;
            }

            if (!utilitytools.HelpMethods.IsEntityOnFloor(hitBox, mapData)) {
                scratchHitbox.setRect(hitBox.x, hitBox.y + 1, hitBox.width, hitBox.height);
                if (playStates.getObjectManager().getIntersectingContainer(scratchHitbox) == null) {
                    inAir = true;
                }
            }
        }
        
        if (inAir) {
            float maxFallSpeed = GameCore.TILES_SIZE - 1.0f; 
            if (airSpeed > maxFallSpeed) {
                airSpeed = maxFallSpeed;
            }
            
            scratchHitbox.setRect(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height);
            GameContainer gcY = playStates.getObjectManager().getIntersectingContainer(scratchHitbox);
            
            if (utilitytools.HelpMethods.canMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, mapData) && gcY == null) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                if (gcY != null) {
                    if (airSpeed > 0) {
                        hitBox.y = gcY.getHitbox().y - hitBox.height - 1f;
                        resetInAir();
                        if (isKnockedDown) {
                            playerAction = DOWN;
                            aniIndex = 0;
                        }
                    } else {
                        hitBox.y = gcY.getHitbox().y + gcY.getHitbox().height + 1f;
                        airSpeed = fallSpeedAfterCollision;
                    }
                } else {
                    hitBox.y = utilitytools.HelpMethods.GetEntityPosUnderRoofOrAboveFloor(hitBox, airSpeed);
                    if (airSpeed > 0) {
                        resetInAir();
                        if (isKnockedDown) {
                            playerAction = DOWN;
                            aniIndex = 0;
                        }
                    } else {
                        airSpeed = fallSpeedAfterCollision;
                    }
                }
                updateXPos(xSpeed);
            }
        } else {
            if (knockbackSpeed > 0 || dashing || (!attacking && !charging && !isKnockedDown)) {
                updateXPos(xSpeed);
            }
        }
        
        if (xSpeed != 0 && !attacking && !charging && !isKnockedDown && !dashing && knockbackSpeed <= 0) {
            moving = true;
        }
    }
    
    private void jump() {
        if (inAir) {
            if (canDoubleJump) {
                airSpeed = jumpSpeed;
                canDoubleJump = false;
                jump = false;
                playStates.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.JUMP);
                if (playStates.getParticlePool() != null) {
                    playStates.getParticlePool().spawnDust(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height);
                }
            }
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
        jump = false;
        playStates.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.JUMP);
        if (playStates.getParticlePool() != null) {
            playStates.getParticlePool().spawnDust(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height);
        }
    }

    private void resetInAir() {
        if (inAir && playStates != null && playStates.getParticlePool() != null) {
            playStates.getParticlePool().spawnDust(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height);
        }
        if (isGroundSlamming) {
            isGroundSlamming = false;
            if (playStates != null) {
                if (playStates.getParticlePool() != null) {
                    playStates.getParticlePool().spawnDust(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height, 14);
                    playStates.getParticlePool().spawnHitSparks(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height, 8);
                }
                playStates.shakeScreen(5, 3);
                if (playStates.getEnemyManager() != null) {
                    scratchHitbox.setRect(hitBox.x - 30, hitBox.y, hitBox.width + 60, hitBox.height);
                    int slamDamage = (int) (stats.getDps() * 2.2f);
                    playStates.getEnemyManager().checkEnemyHit(scratchHitbox, slamDamage, this);
                }
                if (playStates.getGameCore() != null && playStates.getGameCore().getAudioPlayer() != null) {
                    playStates.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.ATTACK_TWO);
                }
            }
        }
        inAir = false;
        airSpeed = 0;
        canDoubleJump = true;
    }

    private void updateXPos(float xSpeed) {
        Rectangle2D.Float nextXHitbox = new Rectangle2D.Float(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height);
        GameContainer gcX = playStates.getObjectManager().getIntersectingContainer(nextXHitbox);
        if (canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, mapData) && gcX == null) {
            hitBox.x += xSpeed;
        } else {
            if (gcX != null) {
                if (xSpeed > 0) {
                    hitBox.x = gcX.getHitbox().x - hitBox.width - 1f;
                } else if (xSpeed < 0) {
                    hitBox.x = gcX.getHitbox().x + gcX.getHitbox().width + 1f;
                }
            } else {
                hitBox.x = GetEntityPosNextToWall(hitBox, xSpeed);
            }
        }
    }

    public void applyKnockback(int dir, boolean fromBoss) {
        knockbackDir = dir;
        attacking = false;
        charging = false;
        isExecutingChargeAttack = false;

        if (fromBoss) {
            isKnockedDown = true;
            playerAction = DOWN;
            aniIndex = 0;
            aniTick = 0;
            
            knockbackSpeed = 5.0f * GameCore.SCALE;
            airSpeed = -3.5f * GameCore.SCALE;
            inAir = true;
        } else {
            knockbackSpeed = 3.5f * GameCore.SCALE;
            if (!inAir) {
                inAir = true;
                airSpeed = -2.0f * GameCore.SCALE;
            }
        }
    }

    private void loadAnimations() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_SPRITE);
        animasi = new BufferedImage[25][16];
        for (int j = 0; j < animasi.length; j++) {
            for (int i = 0; i < animasi[j].length; i++) {
                animasi[j][i] = image.getSubimage(i * 80, j * 64, 80, 64);
            }
        }
    }

    public void loadmapData(int[][] mapData) {
        this.mapData = mapData;
        if (!IsEntityOnFloor(hitBox, mapData)) {
            inAir = true;
        }
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void resetAll(float newX, float newY) {
        resetDirBooleans();
        inAir = false;
        canDoubleJump = true;
        moving = false;
        attacking = false;
        isKnockedDown = false;
        playerAction = IDLE_ACTIVE;
        stats.setCurrentHealth(stats.getMaxHealth());
        this.x = newX;
        this.y = newY;
        hitBox.x = newX;
        hitBox.y = newY;
        if (mapData != null && !IsEntityOnFloor(hitBox, mapData)) {
            inAir = true;
        }
    }

    public void gainExp(int amount) {
        stats.gainExp(amount);
        calculateDefense();
    }

    // GETTER & SETTER
    public boolean isChargeAttack() { return isExecutingChargeAttack; }
    public boolean isAttacking() { return attacking; }
    public boolean isDashing() { return dashing; }
    public boolean isCharging() { return charging; }
    public boolean isGroundSlamming() { return isGroundSlamming; }
    public int getCurseCooldownTick() { return curseCooldownTick; }
    public void setAttack(boolean attacking) { this.attacking = attacking; }
    public PlayStates getPlayStates() { return playStates; }
    public int getFlipW() { return flipW; }
    public boolean isLeft() { return left; }
    public void setLeft(boolean left) { this.left = left; }
    public boolean isUp() { return up; }
    public void setUp(boolean up) { this.up = up; }
    public boolean isRight() { return right; }
    public void setRight(boolean right) { this.right = right; }
    public boolean isDown() { return down; }
    public void setDown(boolean down) { this.down = down; }
    public void setJump(boolean jump) { this.jump = jump; }
    public boolean isJump() { return jump; }
    public java.awt.geom.Rectangle2D.Float getHitbox() { return hitBox; }

    // Equipment Getter untuk UI Inventory & Persistence (Delegated to PlayerInventory)
    public int getEquippedHelmet() { return playerInventory.getEquippedHelmet(); }
    public int getEquippedArmor() { return playerInventory.getEquippedArmor(); }
    public int getEquippedGloves() { return playerInventory.getEquippedGloves(); }
    public int getEquippedShoes() { return playerInventory.getEquippedShoes(); }
    public int getEquippedAcc1() { return playerInventory.getEquippedAcc1(); }
    public int getEquippedAcc2() { return playerInventory.getEquippedAcc2(); }
    public int getTotalDefense() { return stats.getTotalDefense(); }

    // Sub-komponen Getters (SRP Architecture)
    public PlayerStats getStats() { return stats; }
    public PlayerHUD getPlayerHUD() { return playerHUD; }
    public PlayerInventory getPlayerInventory() { return playerInventory; }

    // Tambahan Getter untuk UI Status & Backward Compatibility
    public int getLevel() { return stats.getLevel(); }
    public int getExp() { return stats.getExp(); }
    public int getMaxExp() { return stats.getMaxExp(); }
    public float getDps() { return stats.getDps(); }
    public int getCurrentHealth() { return stats.getCurrentHealth(); }
    public int getMaxHealth() { return stats.getMaxHealth(); }
    public int getCurrentMana() { return stats.getCurrentMana(); }
    public int getMaxMana() { return stats.getMaxMana(); }
    
    public void setLevel(int level) { stats.setLevel(level); }
    public void setExp(int exp) { stats.setExp(exp); }
    public void setMaxExp(int maxExp) { stats.setMaxExp(maxExp); }
    public void setCurrentHealth(int health) { stats.setCurrentHealth(health); }
    public void setCurrentMana(int mana) { stats.setCurrentMana(mana); }
    public void setEquippedHelmet(int id) { playerInventory.setEquippedHelmet(id); }
    public void setEquippedArmor(int id) { playerInventory.setEquippedArmor(id); }
    public void setEquippedGloves(int id) { playerInventory.setEquippedGloves(id); }
    public void setEquippedShoes(int id) { playerInventory.setEquippedShoes(id); }
    public void setEquippedAcc1(int id) { playerInventory.setEquippedAcc1(id); }
    public void setEquippedAcc2(int id) { playerInventory.setEquippedAcc2(id); }

    // Subcomponents & Accessors (Pillar 3 SRP Modernization)
    public PlayerPhysics getPhysics() { return physics; }
    public PlayerCombat getCombat() { return combat; }
    public float getPlayerScale() { return playerScale; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setMoving(boolean moving) { this.moving = moving; }
    public void setFlipX(int flipX) { this.flipX = flipX; }
    public void setFlipW(int flipW) { this.flipW = flipW; }
    public void setPlayerAction(int action) { this.playerAction = action; }
    public void setAniIndex(int index) { this.aniIndex = index; }
    public void setAttacking(boolean attacking) { this.attacking = attacking; }
}