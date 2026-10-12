package entity;

import java.awt.geom.Rectangle2D;
import gameStates.PlayStates;
import main.GameCore;
import objects.GameContainer;
import utilitytools.HelpMethods;
import static utilitytools.HelpMethods.*;
import static utilitytools.Konstanta.KonstantaPlayerRight.*;

/**
 * Controller extracting movement, gravity, floor detection, jumping, dashing,
 * and knockback physics out of Player to adhere to SRP.
 */
public class PlayerPhysics {

    private final Player player;
    private float playerSpeed = 3.33f * GameCore.SCALE;

    // Air & Gravity
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

    // Knockback
    private float knockbackSpeed = 0;
    private int knockbackDir = 1;
    private boolean isKnockedDown = false;

    private final Rectangle2D.Float scratchHitbox = new Rectangle2D.Float();

    public PlayerPhysics(Player player) {
        this.player = player;
    }

    public void update(int[][] mapData, PlayStates playStates, boolean left, boolean right, boolean jump, boolean attacking, boolean charging, boolean isExecutingChargeAttack) {
        player.setMoving(false);

        if (dashCooldownTick > 0) {
            dashCooldownTick--;
            if (dashCooldownTick <= 0) canDash = true;
        }

        if (jump && !charging && !isKnockedDown) {
            triggerJump(playStates);
        }

        float xSpeed = 0;

        if (dashing) {
            dashTick++;
            if (dashTick % 3 == 0 && playStates != null && playStates.getParticlePool() != null) {
                float dustX = player.getHitBox().x + (player.getFlipW() == 1 ? 0 : player.getHitBox().width);
                playStates.getParticlePool().spawnDust(dustX, player.getHitBox().y + player.getHitBox().height);
            }
            if (dashTick >= dashDuration) {
                dashing = false;
                dashCooldownTick = dashCooldown;
            }
        }

        if (knockbackSpeed > 0) {
            xSpeed = knockbackSpeed * knockbackDir;
            knockbackSpeed -= 0.15f * GameCore.SCALE;
            if (knockbackSpeed < 0) knockbackSpeed = 0;
        } else if (dashing) {
            airSpeed = 0;
            xSpeed = (player.getFlipW() == 1) ? dashSpeed : -dashSpeed;
        } else if (!isKnockedDown) {
            if (!attacking && !isExecutingChargeAttack && !charging) {
                if (left) {
                    xSpeed -= playerSpeed;
                    player.setFlipX(player.getWidth());
                    player.setFlipW(-1);
                }
                if (right) {
                    xSpeed += playerSpeed;
                    player.setFlipX(0);
                    player.setFlipW(1);
                }
            } else {
                if (inAir && attacking) {
                    xSpeed = (player.getFlipW() == 1) ? playerSpeed * 0.4f : -playerSpeed * 0.4f;
                }
            }
        }

        Rectangle2D.Float hitBox = player.getHitBox();

        if (!inAir) {
            if (hitBox.y + hitBox.height < GameCore.GAME_HEIGHT - 32) {
                lastSafeX = hitBox.x;
                lastSafeY = hitBox.y;
            }

            if (!HelpMethods.IsEntityOnFloor(hitBox, mapData)) {
                scratchHitbox.setRect(hitBox.x, hitBox.y + 1, hitBox.width, hitBox.height);
                if (playStates != null && playStates.getObjectManager() != null && playStates.getObjectManager().getIntersectingContainer(scratchHitbox) == null) {
                    inAir = true;
                }
            }
        }

        if (inAir) {
            float maxFallSpeed = GameCore.TILES_SIZE - 1.0f;
            if (airSpeed > maxFallSpeed) airSpeed = maxFallSpeed;

            scratchHitbox.setRect(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height);
            GameContainer gcY = (playStates != null && playStates.getObjectManager() != null) 
                    ? playStates.getObjectManager().getIntersectingContainer(scratchHitbox) : null;

            if (HelpMethods.canMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, mapData) && gcY == null) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed, mapData, playStates);
            } else {
                if (gcY != null) {
                    if (airSpeed > 0) {
                        hitBox.y = gcY.getHitbox().y - hitBox.height - 1f;
                        resetInAir(playStates);
                        if (isKnockedDown) {
                            player.setPlayerAction(DOWN);
                            player.setAniIndex(0);
                        }
                    } else {
                        hitBox.y = gcY.getHitbox().y + gcY.getHitbox().height + 1f;
                        airSpeed = fallSpeedAfterCollision;
                    }
                } else {
                    hitBox.y = HelpMethods.GetEntityPosUnderRoofOrAboveFloor(hitBox, airSpeed);
                    if (airSpeed > 0) {
                        resetInAir(playStates);
                        if (isKnockedDown) {
                            player.setPlayerAction(DOWN);
                            player.setAniIndex(0);
                        }
                    } else {
                        airSpeed = fallSpeedAfterCollision;
                    }
                }
                updateXPos(xSpeed, mapData, playStates);
            }
        } else {
            if (knockbackSpeed > 0 || dashing || (!attacking && !charging && !isKnockedDown)) {
                updateXPos(xSpeed, mapData, playStates);
            }
        }

        if (xSpeed != 0 && !attacking && !charging && !isKnockedDown && !dashing && knockbackSpeed <= 0) {
            player.setMoving(true);
        }
    }

    public void triggerJump(PlayStates playStates) {
        Rectangle2D.Float hitBox = player.getHitBox();
        if (inAir) {
            if (canDoubleJump) {
                airSpeed = jumpSpeed;
                canDoubleJump = false;
                player.setJump(false);
                if (playStates != null) {
                    playStates.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.JUMP);
                    if (playStates.getParticlePool() != null) {
                        playStates.getParticlePool().spawnDust(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height);
                    }
                }
            }
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
        player.setJump(false);
        if (playStates != null) {
            playStates.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.JUMP);
            if (playStates.getParticlePool() != null) {
                playStates.getParticlePool().spawnDust(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height);
            }
        }
    }

    public void resetInAir(PlayStates playStates) {
        if (inAir && playStates != null && playStates.getParticlePool() != null) {
            Rectangle2D.Float hitBox = player.getHitBox();
            playStates.getParticlePool().spawnDust(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height);
        }
        inAir = false;
        airSpeed = 0;
        canDoubleJump = true;
    }

    private void updateXPos(float xSpeed, int[][] mapData, PlayStates playStates) {
        Rectangle2D.Float hitBox = player.getHitBox();
        scratchHitbox.setRect(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height);
        GameContainer gcX = (playStates != null && playStates.getObjectManager() != null) 
                ? playStates.getObjectManager().getIntersectingContainer(scratchHitbox) : null;

        if (HelpMethods.canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, mapData) && gcX == null) {
            hitBox.x += xSpeed;
        } else {
            if (gcX != null) {
                if (xSpeed > 0) {
                    hitBox.x = gcX.getHitbox().x - hitBox.width - 1f;
                } else if (xSpeed < 0) {
                    hitBox.x = gcX.getHitbox().x + gcX.getHitbox().width + 1f;
                }
            } else {
                hitBox.x = HelpMethods.GetEntityPosNextToWall(hitBox, xSpeed);
            }
        }
    }

    public void applyKnockback(int dir, boolean fromBoss) {
        this.knockbackDir = dir;
        if (fromBoss) {
            this.isKnockedDown = true;
            this.knockbackSpeed = 4.5f * GameCore.SCALE;
        } else {
            this.knockbackSpeed = 2.5f * GameCore.SCALE;
        }
    }

    public void startDash() {
        if (canDash && !dashing) {
            this.dashing = true;
            this.canDash = false;
            this.dashTick = 0;
        }
    }

    public void reset(float x, float y) {
        inAir = false;
        airSpeed = 0;
        canDoubleJump = true;
        dashing = false;
        canDash = true;
        dashTick = 0;
        dashCooldownTick = 0;
        knockbackSpeed = 0;
        isKnockedDown = false;
        lastSafeX = x;
        lastSafeY = y;
    }

    // Getters and Setters
    public boolean isInAir() { return inAir; }
    public boolean isDashing() { return dashing; }
    public boolean isKnockedDown() { return isKnockedDown; }
    public void setKnockedDown(boolean val) { this.isKnockedDown = val; }
    public float getLastSafeX() { return lastSafeX; }
    public float getLastSafeY() { return lastSafeY; }
    public void setAirSpeed(float speed) { this.airSpeed = speed; }
}
