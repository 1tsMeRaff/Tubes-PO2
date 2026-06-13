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
    private boolean canDoubleJump = true;
    private boolean inAir = true;

    // Dash
    private boolean dashing = false;
    private boolean canDash = true;
    private int dashDuration = 100;
    private int dashTick = 0;
    private int dashCooldown = 100;
    private int dashCooldownTick = 0;
    private float dashSpeed = 1.5f * GameCore.SCALE;

    // Save point (untuk respawn jurang)
    private float lastSafeX;
    private float lastSafeY;
    
    // Charge Attack
    private boolean charging = false;
    private int chargeTick = 0;
    private final int CHARGE_DURATION_NEEDED = 100;
    private boolean isExecutingChargeAttack = false;

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

    private int maxMana = 100;
    private int currentMana = 50;
    private int manaBarWidth = (int) (150 * GameCore.SCALE);
    private int manaBarHeight = (int) (9 * GameCore.SCALE);
    private int manaBarYStart = (int) (25 * GameCore.SCALE);
    private int manaWidth = manaBarWidth;

    // Inventory (dari dev-Rizal)
    public ArrayList<Integer> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    // AttackBox
    private Rectangle2D.Float AttackBox;

    // FlipImage
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
        updateManaBar(); // dari dev-Rizal

        if (currentHealth <= 0) {
            playStates.setGameOver(true);
            return;
        }

        // Cek apakah jatuh ke jurang
        checkChasm();

        // Logika dari dev (dash/charge) + potion
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

 // Method utama untuk mendeteksi jatuh ke jurang
    private void checkChasm() {
        if (hitBox.y + hitBox.height >= main.GameCore.GAME_HEIGHT - 5) {
            
            //changeHealth(-20); 
            
            if (currentHealth > 0) {
                hitBox.x = lastSafeX;
                hitBox.y = lastSafeY - 15; 
                
    
                inAir = true; 
                airSpeed = 0;
                moving = false;
                jump = false;
            }
        }
    }

    private void updateManaBar() {
        manaWidth = (int) ((currentMana / (float) maxMana) * manaBarWidth);
    }

    // Inventory methods (dari dev-Rizal)
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

    // Sensor potion (dari dev)
    private void checkPotionTouched() {
        playStates.checkPotionTouched(hitBox);
    }

    private void checkAttack() {
        if (attackCheck) {
            return;
        }

        int hitFrame;
        if (isExecutingChargeAttack) {
            hitFrame = 9;
        } else {
            hitFrame = 3;
        }

        if (aniIndex == hitFrame) {
            attackCheck = true;
            int damageToDeal;
            if (isExecutingChargeAttack) {
                damageToDeal = 25;
            } else {
                damageToDeal = 10;
            }
            playStates.checkHitEnemy(AttackBox, damageToDeal);
            playStates.checkObjectHit(AttackBox);
            playStates.getGameCore().getAudioPlayer().playAttackSound();
        }
    }

    private void updateAttackBox() {
        AttackBox.y = hitBox.y + (GameCore.SCALE * 2);
        int currentAttackWidth;

        if (isExecutingChargeAttack) {
            currentAttackWidth = (int) (30 * GameCore.SCALE);
        } else {
            currentAttackWidth = (int) (20 * GameCore.SCALE);
        }

        AttackBox.width = currentAttackWidth;

        if (flipW == 1) {
            AttackBox.x = hitBox.x + hitBox.width;
        } else {
            AttackBox.x = hitBox.x - AttackBox.width;
        }
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
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Health bar (merah)
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX + GameCore.TILES_SIZE, healthBarYStart + statusBarY,
                healthWidth - GameCore.TILES_SIZE, healthBarHeight);

        // Mana bar (biru) dari dev-Rizal
        g.setColor(Color.blue);
        g.fillRect(healthBarXStart + statusBarX + GameCore.TILES_SIZE, manaBarYStart + statusBarY,
                manaWidth - GameCore.TILES_SIZE, manaBarHeight);
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

        if (dashing) {
            playerAction = DASH;
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
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;

        if (jump && !charging) {
            jump();
        }

        // Dash logic (dari dev)
        if (dashing) {
            dashTick++;
            if (dashTick >= dashDuration) {
                dashing = false;
            } else {
                float xSpeed = (flipW == 1) ? dashSpeed : -dashSpeed;
                if (canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, mapData)) {
                    hitBox.x += xSpeed;
                } else {
                    hitBox.x = GetEntityPosNextToWall(hitBox, xSpeed);
                    dashing = false;
                }
                return;
            }
        }

        float xSpeed = 0;

        // Gerakan horizontal (bisa di udara walaupun attacking, tapi tidak saat charging di tanah)
        if (!attacking || inAir) {
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
        }

        // Cek di tanah atau tidak (dengan map)
        if (!inAir) {
            
            // ---> REKAM POSISI AMAN (DIPERBARUI) <---
            // Hanya rekam posisi aman jika Feline BUKAN berada di ujung bawah layar
            if (hitBox.y + hitBox.height < main.GameCore.GAME_HEIGHT - 32) {
                lastSafeX = hitBox.x;
                lastSafeY = hitBox.y;
            }

            if (!IsEntityOnFloor(hitBox, mapData)) {
                // Cek tambahan: mungkin berdiri di atas kontainer
                Rectangle2D.Float boxUnderneath = new Rectangle2D.Float(hitBox.x, hitBox.y + 1, hitBox.width, hitBox.height);
                if (playStates.getObjectManager().getIntersectingContainer(boxUnderneath) == null) {
                    inAir = true;
                }
            }
        }

        if (inAir) {
            // Fisika vertikal dengan kontainer (dari dev-Rizal)
            Rectangle2D.Float nextYHitbox = new Rectangle2D.Float(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height);
            GameContainer gcY = playStates.getObjectManager().getIntersectingContainer(nextYHitbox);

            if (canMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, mapData) && gcY == null) {
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
                    if (airSpeed > 0) {
                        resetInAir();
                    } else {
                        airSpeed = fallSpeedAfterCollision;
                    }
                }
                updateXPos(xSpeed);
            }
        } else {
            // Di tanah, hanya bisa bergerak jika tidak attacking dan tidak charging
            if (!attacking && !charging) {
                updateXPos(xSpeed);
            }
        }

        if (xSpeed != 0 && !attacking && !charging) {
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
        // Fisika horizontal dengan kontainer (dari dev-Rizal)
        Rectangle2D.Float nextXHitbox = new Rectangle2D.Float(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height);
        GameContainer gcX = playStates.getObjectManager().getIntersectingContainer(nextXHitbox);

        if (canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, mapData) && gcX == null) {
            hitBox.x += xSpeed;
        } else {
            if (gcX != null) {
                if (xSpeed > 0) { // Kanan, nabrak sisi kiri kontainer
                    hitBox.x = gcX.getHitbox().x - hitBox.width - 1f;
                } else if (xSpeed < 0) { // Kiri, nabrak sisi kanan kontainer
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
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    private void loadAnimations() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_SPRITE);
        animasi = new BufferedImage[22][16];

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