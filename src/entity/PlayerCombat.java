package entity;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import gameStates.PlayStates;
import main.GameCore;
import audio.AudioPlayer;

/**
 * Controller extracting combat hitboxes, charge attacks, invincibility frames,
 * and equipment defense calculations out of Player to adhere to SRP.
 */
public class PlayerCombat {

    private final Player player;
    private final Rectangle2D.Float attackBox = new Rectangle2D.Float();
    private boolean attackCheck = false;

    // Charge Attack
    private boolean charging = false;
    private int chargeTick = 0;
    public static final int CHARGE_DURATION_NEEDED = 30;
    private boolean isExecutingChargeAttack = false;

    // I-Frames
    private boolean isInvincible = false;
    private int invincibilityTick = 0;
    public static final int INVINCIBILITY_DURATION = 45;

    // Sistem Equipment
    private int equippedHelmet = -1;
    private int equippedArmor = -1;
    private int equippedGloves = -1;
    private int equippedShoes = -1;
    private int equippedAcc1 = -1;
    private int equippedAcc2 = -1;

    private final Random rand = new Random();

    public PlayerCombat(Player player) {
        this.player = player;
    }

    public void update(PlayStates playStates) {
        if (charging) {
            chargeTick++;
        }

        if (isInvincible) {
            invincibilityTick++;
            if (invincibilityTick >= INVINCIBILITY_DURATION) {
                isInvincible = false;
                invincibilityTick = 0;
            }
        }

        updateAttackBox();
    }

    public void updateAttackBox() {
        Rectangle2D.Float hitBox = player.getHitBox();
        float playerScale = player.getPlayerScale();

        attackBox.y = hitBox.y + (GameCore.SCALE * 2 * playerScale);
        int currentAttackWidth;
        int normalAttackWidth = 20;
        int chargeAttackWidth = 25;

        if (isExecutingChargeAttack) {
            currentAttackWidth = (int) (chargeAttackWidth * GameCore.SCALE * playerScale);
        } else {
            currentAttackWidth = (int) (normalAttackWidth * GameCore.SCALE * playerScale);
        }

        attackBox.width = currentAttackWidth;
        attackBox.height = (int) (20 * GameCore.SCALE * playerScale);

        if (player.getFlipW() == 1) {
            attackBox.x = hitBox.x + hitBox.width;
        } else {
            attackBox.x = hitBox.x - attackBox.width;
        }
    }

    public void checkAttack(int aniIndex, PlayStates playStates, PlayerStats stats) {
        if (attackCheck) return;

        int hitFrame = isExecutingChargeAttack ? 9 : 3;

        if (aniIndex == hitFrame) {
            attackCheck = true;
            int damageToDeal = isExecutingChargeAttack ? (int) (stats.getDps() * 2.5) : (int) stats.getDps();

            int randomAttack = 4 + rand.nextInt(3);
            if (playStates != null && playStates.getGameCore() != null) {
                playStates.getGameCore().getAudioPlayer().playEffect(randomAttack);
                playStates.checkHitEnemy(attackBox, damageToDeal);
                playStates.checkObjectHit(attackBox);
            }
        }
    }

    public void setCharging(boolean charging, boolean inAir, boolean attacking, boolean dashing) {
        if (inAir) {
            if (charging && !attacking && !dashing) {
                this.isExecutingChargeAttack = false;
                player.setAttacking(true);
            }
            return;
        }
        if (this.charging == charging) return;
        this.charging = charging;
        if (charging) {
            chargeTick = 0;
        }
    }

    public void releaseAttack(PlayStates playStates) {
        if (!charging) return;
        this.charging = false;
        this.isExecutingChargeAttack = (chargeTick >= CHARGE_DURATION_NEEDED);
        player.setAttacking(true);
        chargeTick = 0;

        if (playStates != null && playStates.getGameCore() != null) {
            playStates.getGameCore().getAudioPlayer().playEffect(AudioPlayer.SLASH_SOUND);
        }
    }

    public void equipItem(int itemIndex, String equipmentType, ArrayList<Integer> inventory, PlayerStats stats) {
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
                default: break;
            }

            inventory.remove(itemIndex);
            if (oldItem != -1) inventory.add(oldItem);
            calculateDefense(stats);
        }
    }

    public void calculateDefense(PlayerStats stats) {
        int totalDefense = 0;
        int[] currentGear = {equippedHelmet, equippedArmor, equippedGloves, equippedShoes, equippedAcc1, equippedAcc2};

        for (int itemId : currentGear) {
            if (itemId != -1) {
                switch (itemId) {
                    case 10: totalDefense += 5; break;   // Helm
                    case 11: totalDefense += 15; break;  // Armor
                    case 12: totalDefense += 4; break;   // Gloves
                    case 13: totalDefense += 4; break;   // Shoes
                    case 14: totalDefense += 2; break;   // Acc 1
                    case 15: totalDefense += 2; break;   // Acc 2
                    default: break;
                }
            }
        }
        stats.recalculateDefense(totalDefense);
    }

    public void reset() {
        charging = false;
        chargeTick = 0;
        isExecutingChargeAttack = false;
        attackCheck = false;
        isInvincible = false;
        invincibilityTick = 0;
    }

    // Getters and Setters
    public Rectangle2D.Float getAttackBox() { return attackBox; }
    public boolean isCharging() { return charging; }
    public boolean isExecutingChargeAttack() { return isExecutingChargeAttack; }
    public void setExecutingChargeAttack(boolean val) { this.isExecutingChargeAttack = val; }
    public boolean isAttackCheck() { return attackCheck; }
    public void setAttackCheck(boolean check) { this.attackCheck = check; }
    public boolean isInvincible() { return isInvincible; }
    public void setInvincible(boolean val) { this.isInvincible = val; }
    public int getInvincibilityTick() { return invincibilityTick; }
    public int getChargeTick() { return chargeTick; }
}
