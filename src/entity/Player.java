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
    private int aniTick, aniIndex, aniSpeed = 4;
    private int playerAction = IDLE_ACTIVE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    
    private float playerScale = 1.5f;
    private float playerSpeed = 3.33f * GameCore.SCALE;
    private int[][] mapData;
    private float xDrawOffSet = 26 * GameCore.SCALE * playerScale;
    private float yDrawOffSet = 13 * GameCore.SCALE * playerScale;

    private float airSpeed = 0f;
    private float gravity = 0.444f * GameCore.SCALE;
    private float jumpSpeed = -7.5f * GameCore.SCALE;
    private float fallSpeedAfterCollision = 1.66f * GameCore.SCALE;
    private boolean canDoubleJump = true;
    private boolean inAir = true;

    private boolean dashing = false;
    private boolean canDash = true;
    private int dashDuration = 30;
    private int dashTick = 0;
    private int dashCooldown = 30;
    private int dashCooldownTick = 0;
    private float dashSpeed = 5.0f * GameCore.SCALE;

    private float lastSafeX;
    private float lastSafeY;
    
    private boolean charging = false;
    private int chargeTick = 0;
    private final int CHARGE_DURATION_NEEDED = 30;
    private boolean isExecutingChargeAttack = false;
    
    private boolean isInvincible = false;
    private int invincibilityTick = 0;
    private final int INVINCIBILITY_DURATION = 45;
    private float knockbackSpeed = 0;
    private int knockbackDir = 1;
    private float knockbackFriction = 0.15f * GameCore.SCALE;
    
    private boolean isKnockedDown = false;

    private BufferedImage statusBarImg;
    private int statusBarWidth = (int) (164 * GameCore.SCALE);
    private int statusBarHeight = (int) (36 * GameCore.SCALE);
    private int statusBarX = (int) (10 * GameCore.SCALE);
    private int statusBarY = (int) (10 * GameCore.SCALE);
    private int healthBarWidth = (int) (1000 * GameCore.SCALE);
    private int healthBarHeight = (int) (2 * GameCore.SCALE);
    private int healthBarXStart = (int) (63 * GameCore.SCALE);
    private int healthBarYStart = (int) (18 * GameCore.SCALE);
    private int healthWidth = healthBarWidth;
    
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    
    private int maxMana = 100;
    private int currentMana = maxMana;
    
    private int manaBarWidth = (int) (150 * GameCore.SCALE);
    private int manaBarHeight = (int) (6 * GameCore.SCALE);
    private int manaBarXStart = (int) (63 * GameCore.SCALE);
    private int manaBarYStart = (int) (33 * GameCore.SCALE);
    private int manaWidth = manaBarWidth;

    public ArrayList<Integer> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    // --- SISTEM DEFENSE & EQUIPMENT (Dari dev-Rizal) ---
    private int baseDefense = 5;
    private int totalDefense = baseDefense;

    private int equippedHelmet = -1; 
    private int equippedArmor = -1;  
    private int equippedGloves = -1; 
    private int equippedShoes = -1;  
    private int equippedAcc1 = -1;   
    private int equippedAcc2 = -1;   

    private Rectangle2D.Float AttackBox;
    private int flipX = 0;
    private int flipW = 1;
    private boolean attackCheck;
    private PlayStates playStates;

    public Player(float x, float y, int width, int height, PlayStates playStates) {
        super(x, y, width, height);
        this.playerScale = 1.5f;
        this.width = (int) (width * playerScale);
        this.height = (int) (height * playerScale);
        this.playStates = playStates;
        
        this.healthBarXStart = (int) (53 * GameCore.SCALE);
        this.healthBarYStart = (int) (9 * GameCore.SCALE);
        this.healthBarWidth = (int) (106 * GameCore.SCALE); 
        this.healthBarHeight = (int) (7 * GameCore.SCALE);
        this.manaBarXStart = (int) (53 * GameCore.SCALE);
        this.manaBarYStart = (int) (23 * GameCore.SCALE);
        this.manaBarWidth = (int) (106 * GameCore.SCALE); 
        this.manaBarHeight = (int) (7 * GameCore.SCALE);
        
        loadAnimations();
        initHitBox(x, y, (int) (18 * GameCore.SCALE * playerScale), (int) (18 * GameCore.SCALE * playerScale));
        initAttackBox();
    }

    private void initAttackBox() {
        AttackBox = new Rectangle2D.Float(x, y, (int) (1 * GameCore.SCALE * playerScale), (int) (15 * GameCore.SCALE * playerScale));
    }

    // --- LOGIKA SISTEM DEFENSE ---
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
        if (currentHealth <= 0) currentHealth = 0;
        else if (currentHealth >= maxHealth) currentHealth = maxHealth;
    }

    // ... (sisa method lainnya tetap sama) ...
    
    // Pastikan getter ini ditambahkan di paling bawah agar UI Inventory bisa mengambil data
    public int getEquippedHelmet() { return equippedHelmet; }
    public int getEquippedArmor() { return equippedArmor; }
    public int getEquippedGloves() { return equippedGloves; }
    public int getEquippedShoes() { return equippedShoes; }
    public int getEquippedAcc1() { return equippedAcc1; }
    public int getEquippedAcc2() { return equippedAcc2; }
    public int getTotalDefense() { return totalDefense; }
    
    // ... sisa method lainnya (update, draw, dll) ...
}