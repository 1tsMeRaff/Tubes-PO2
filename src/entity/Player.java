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

import static utilitytools.Konstanta.KonstantaPlayerRight.*;
import static utilitytools.HelpMethods.*;

public class Player extends Entity {

    private int level = 1;
    private int exp = 0;
    private int maxExp = 100;
    private float dps = 5.0f; 

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
    
    // I-Frames & Knockback
    private boolean isInvincible = false;
    private int invincibilityTick = 0;
    private final int INVINCIBILITY_DURATION = 45;
    private float knockbackSpeed = 0;
    private int knockbackDir = 1;
    private float knockbackFriction = 0.15f * GameCore.SCALE;
    private boolean isKnockedDown = false;

    // UI Status Bar (Health & Mana)
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int) (164 * GameCore.SCALE);
    private int statusBarHeight = (int) (36 * GameCore.SCALE);
    private int statusBarX = (int) (10 * GameCore.SCALE);
    private int statusBarY = (int) (10 * GameCore.SCALE);
    
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthBarWidth = (int) (106 * GameCore.SCALE);
    private int healthBarHeight = (int) (7 * GameCore.SCALE);
    private int healthBarXStart = (int) (53 * GameCore.SCALE);
    private int healthBarYStart = (int) (9 * GameCore.SCALE);
    private int healthWidth = healthBarWidth;
    
    private int maxMana = 100;
    private int currentMana = maxMana;
    private int manaBarWidth = (int) (106 * GameCore.SCALE);
    private int manaBarHeight = (int) (7 * GameCore.SCALE);
    private int manaBarXStart = (int) (53 * GameCore.SCALE);
    private int manaBarYStart = (int) (23 * GameCore.SCALE);
    private int manaWidth = manaBarWidth;

    // Inventory
    public ArrayList<Integer> inventory = new ArrayList<>();
    public final int maxInventorySize = 24;
    

    // Sistem Defense & Equipment
    private int baseDefense = 5;
    private int totalDefense = baseDefense;
    private int equippedHelmet = -1; 
    private int equippedArmor = -1;  
    private int equippedGloves = -1; 
    private int equippedShoes = -1;  
    private int equippedAcc1 = -1;   
    private int equippedAcc2 = -1;   

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
        
        updateHealthBar();
        updateManaBar();
        if (currentHealth <= 0) {
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
        setAnimation();
        updateAnimationTick();
    }


    private void checkChasm() {
        if (hitBox.y + hitBox.height >= main.GameCore.GAME_HEIGHT - 5) {
            if (currentHealth > 0) {
                
   //damage yang di terima
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
    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updateManaBar() {
        manaWidth = (int) ((currentMana / (float) maxMana) * manaBarWidth);
    }

    public void calculateDefense() {
        totalDefense = baseDefense;
        totalDefense += getEquipmentDefenseValue(equippedHelmet);
        totalDefense += getEquipmentDefenseValue(equippedArmor);
        totalDefense += getEquipmentDefenseValue(equippedGloves);
        totalDefense += getEquipmentDefenseValue(equippedShoes);
        totalDefense += getEquipmentDefenseValue(equippedAcc1);
        totalDefense += getEquipmentDefenseValue(equippedAcc2);
    }

    private int getEquipmentDefenseValue(int itemType) {
        if (itemType == -1) return 0;
        switch (itemType) {
            case utilitytools.Konstanta.ObjectConstants.HELMET: return 5;
            case utilitytools.Konstanta.ObjectConstants.ARMOR: return 15;
            case utilitytools.Konstanta.ObjectConstants.GLOVES: return 3;
            case utilitytools.Konstanta.ObjectConstants.SHOES: return 4;
            case utilitytools.Konstanta.ObjectConstants.RING: return 2;
            case utilitytools.Konstanta.ObjectConstants.SACK: return 4;
            default: return 0;
        }
    }

    public boolean addItemToInventory(int objType) {
        if (inventory.size() < maxInventorySize) {
            inventory.add(objType);
            System.out.println("Item masuk tas. Tipe ID: " + objType);
            return true; // Berhasil masuk tas
        } else {
            System.out.println("Penyimpanan Penuh! Tidak bisa mengambil barang.");
            return false; // Tas penuh, gagal mengambil
        }
    }

    // melepaskan armor dan mengembalikannya ke tas
    public void unequipItem(int equipSlotIndex) {
        if (inventory.size() >= maxInventorySize) {
            System.out.println("Tas penuh, tidak bisa melepas perlengkapan!");
            return; // Batalkan jika tas penuh
        }
        
        int itemToReturn = -1;
        
        // Cek slot mana yang mau dilepas (0 sampai 5)
        switch (equipSlotIndex) {
            case 0: itemToReturn = equippedHelmet; equippedHelmet = -1; break;
            case 1: itemToReturn = equippedArmor; equippedArmor = -1; break;
            case 2: itemToReturn = equippedShoes; equippedShoes = -1; break;
            case 3: itemToReturn = equippedAcc1; equippedAcc1 = -1; break;
            case 4: itemToReturn = equippedAcc2; equippedAcc2 = -1; break;
            case 5: itemToReturn = equippedGloves; equippedGloves = -1; break;
        }
        
        if (itemToReturn != -1) {
            inventory.add(itemToReturn); // Masukkan lagi ke tas
            calculateDefense(); // Hitung ulang pertahanan 
            System.out.println("Perlengkapan dilepas!");
        }
    }
    
    // Memisahkan logika potion (useItem) dan equipment (equipItem) yang sebelumnya menyatu
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
            inventory.remove(itemIndex);
        } else {
            System.out.println("Slot ini kosong!");
        }
    }

    public void equipItem(int itemIndex, String equipmentType) {
        if (itemIndex < inventory.size()) {
            int itemToEquip = inventory.get(itemIndex);
            int oldItem = -1;
            
            switch (equipmentType.toLowerCase()) {
                case "head": oldItem = equippedHelmet; equippedHelmet = itemToEquip; break;
                case "body": oldItem = equippedArmor; equippedArmor = itemToEquip; break;
                case "hands": oldItem = equippedGloves; equippedGloves = itemToEquip; break;
                case "shoes": oldItem = equippedShoes; equippedShoes = itemToEquip; break;
                case "accessory1": oldItem = equippedAcc1; equippedAcc1 = itemToEquip; break;
                case "accessory2": oldItem = equippedAcc2; equippedAcc2 = itemToEquip; break;
            }
            
            inventory.remove(itemIndex);
            if (oldItem != -1) inventory.add(oldItem);
            calculateDefense();
        }
    }

    public void changeHealth(int value) {
        if (value < 0) {
            if (isInvincible) return;
            int incomingDamage = Math.abs(value);
            int finalDamage = Math.max(1, incomingDamage - totalDefense);
            currentHealth -= finalDamage;
            isInvincible = true;
        } else {
            currentHealth += value;
        }
        
        if (currentHealth <= 0) {
            currentHealth = 0;
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public void changeMana(int value) {
        currentMana += value;
        if (currentMana < 0) currentMana = 0;
        else if (currentMana > maxMana) currentMana = maxMana;
    }

    private void checkPotionTouched() {
        playStates.checkPotionTouched(hitBox);
    }

    private void checkAttack() {
        if (attackCheck) return;
        
        int hitFrame = isExecutingChargeAttack ? 9 : 3;
        
        if (aniIndex == hitFrame) {
            attackCheck = true;
            int damageToDeal = isExecutingChargeAttack ? (int)(dps * 2.5) : (int)dps;
            

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
        	// efek berkedip
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
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        int healthBarX = statusBarX + healthBarXStart;
        int healthBarY = statusBarY + healthBarYStart;
        int manaBarX = statusBarX + manaBarXStart;
        int manaBarY = statusBarY + manaBarYStart;

        g.setColor(Color.red);
        g.fillRect(healthBarX, healthBarY, healthWidth, healthBarHeight);

        g.setColor(Color.blue);
        g.fillRect(manaBarX, manaBarY, manaWidth, manaBarHeight);
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
                this.isExecutingChargeAttack = false;
                this.attacking = true;
            }
            return;
        }
        if (this.charging == charging) return;
        this.charging = charging;
        if (charging) {
            chargeTick = 0;
        }
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
                Rectangle2D.Float boxUnderneath = new Rectangle2D.Float(hitBox.x, hitBox.y + 1, hitBox.width, hitBox.height);
                if (playStates.getObjectManager().getIntersectingContainer(boxUnderneath) == null) {
                    inAir = true;
                }
            }
        }
        
        if (inAir) {
            float maxFallSpeed = GameCore.TILES_SIZE - 1.0f; 
            if (airSpeed > maxFallSpeed) {
                airSpeed = maxFallSpeed;
            }
            
            Rectangle2D.Float nextYHitbox = new Rectangle2D.Float(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height);
            GameContainer gcY = playStates.getObjectManager().getIntersectingContainer(nextYHitbox);
            
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
            }
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
        jump = false;
        playStates.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.JUMP);
    }

    private void resetInAir() {
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
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
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
        currentHealth = maxHealth;
        this.x = newX;
        this.y = newY;
        hitBox.x = newX;
        hitBox.y = newY;
        if (mapData != null && !IsEntityOnFloor(hitBox, mapData)) {
            inAir = true;
        }
        if (currentHealth <= 0) {
            currentHealth = 0;
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public void gainExp(int amount) {
        exp += amount;
        System.out.println("Mendapatkan " + amount + " EXP!");
        
        while (exp >= maxExp) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        exp -= maxExp;               
        maxExp = (int)(maxExp * 1.5); 
        
        maxHealth += 20;
        currentHealth = maxHealth;   
        maxMana += 10;
        currentMana = maxMana;
        baseDefense += 2;            
        dps += 1.5f;                 
        calculateDefense();          
        
        System.out.println("Level Up! Sekarang Level " + level);
    }

    // GETTER & SETTER
    public boolean isChargeAttack() { return isExecutingChargeAttack; }
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
    public java.awt.geom.Rectangle2D.Float getHitbox() { return hitBox; }

    // Equipment Getter untuk UI Inventory
    public int getEquippedHelmet() { return equippedHelmet; }
    public int getEquippedArmor() { return equippedArmor; }
    public int getEquippedGloves() { return equippedGloves; }
    public int getEquippedShoes() { return equippedShoes; }
    public int getEquippedAcc1() { return equippedAcc1; }
    public int getEquippedAcc2() { return equippedAcc2; }
    public int getTotalDefense() { return totalDefense; }

    // Tambahan Getter untuk UI Status
    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getMaxExp() { return maxExp; }
    public float getDps() { return dps; }
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    public int getCurrentMana() { return currentMana; }
    public int getMaxMana() { return maxMana; }
}