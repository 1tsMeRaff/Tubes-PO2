This file is a merged representation of the entire codebase, combined into a single document by Repomix.
The content has been processed where comments have been removed.

# File Summary

## Purpose
This file contains a packed representation of the entire repository's contents.
It is designed to be easily consumable by AI systems for analysis, code review,
or other automated processes.

## File Format
The content is organized as follows:
1. This summary section
2. Repository information
3. Directory structure
4. Repository files (if enabled)
5. Multiple file entries, each consisting of:
  a. A header with the file path (## File: path/to/file)
  b. The full contents of the file in a code block

## Usage Guidelines
- This file should be treated as read-only. Any changes should be made to the
  original repository files, not this packed version.
- When processing this file, use the file path to distinguish
  between different files in the repository.
- Be aware that this file may contain sensitive information. Handle it with
  the same level of security as you would the original repository.

## Notes
- Some files may have been excluded based on .gitignore rules and Repomix's configuration
- Binary files are not included in this packed representation. Please refer to the Repository Structure section for a complete list of file paths, including binary files
- Files matching patterns in .gitignore are excluded
- Files matching default ignore patterns are excluded
- Code comments have been removed from supported file types
- Files are sorted by Git change count (files with more changes are at the bottom)

# Directory Structure
```
.gitignore
resources/armor-removebg-preview.png
resources/armor.png
resources/attack1.wav
resources/attack2.wav
resources/attack3.wav
resources/audio/Theme.wav
resources/awan_01.png
resources/Background_0.png
resources/Background_jungle.png
resources/Background_jungle2.png
resources/bg_feline.png
resources/Bringer_of_Death.png
resources/castle_map.png
resources/demon_boss_spritesheet.png
resources/enemy_slime.png
resources/Frames_baru.png
resources/gloves-removebg-preview.png
resources/gloves.png
resources/golem_blue.png
resources/helmet-removebg-preview.png
resources/helmet.png
resources/inventory_skill_karakter_panel.png
resources/kandang .png
resources/Legs-removebg-preview.png
resources/Legs.png
resources/main_tileset.png
resources/main_tileset.txt
resources/mainn_menu.jpeg
resources/map_jungle_fix.png
resources/map_jungle.png
resources/map_test.txt
resources/map_tutorial_fix.csv
resources/map_tutorial_fix.txt
resources/map_tutorial.csv
resources/map_tutorial.txt
resources/MediavelFree.png
resources/menu_button.png
resources/Menu_Frames.png
resources/menu_panel.png
resources/mushroom_red.png
resources/music.png
resources/necklace-removebg-preview.png
resources/necklace.png
resources/objects_sprites.png
resources/pause.png
resources/paw_button.png
resources/player_right.png
resources/potion_sprites.png
resources/potion.wav
resources/restart_button.png
resources/resume_button.png
resources/ring-removebg-preview.png
resources/ring.png
resources/SE.png
resources/skullwolf.png
resources/slash.wav
resources/statusbar.png
resources/volume.png
savegame.txt
src/audio/AudioPlayer.java
src/entity/BlueGolem.java
src/entity/Boss.java
src/entity/BringerOfDeath.java
src/entity/DemonBoss.java
src/entity/Enemy.java
src/entity/EnemyManager.java
src/entity/Entity.java
src/entity/Player.java
src/entity/Skullwolf.java
src/entity/Slime.java
src/gameStates/GameOptions.java
src/gameStates/GameStates.java
src/gameStates/MainMenu.java
src/gameStates/PlayStates.java
src/gameStates/StateMethods.java
src/gameStates/States.java
src/inputs/Keyboard.java
src/inputs/Mouse.java
src/main/GameCore.java
src/main/GameFrame.java
src/main/GamePanel.java
src/main/Main.java
src/objects/Cage.java
src/objects/DroppedEquipment.java
src/objects/GameContainer.java
src/objects/GameObject.java
src/objects/ObjectManager.java
src/objects/Potion.java
src/objects/Sign.java
src/ui/GameOverOverlay.java
src/ui/InventoryOverlay.java
src/ui/LevelCompletedOverlay.java
src/ui/MainMenuButton.java
src/ui/PauseButton.java
src/ui/PauseOverlay.java
src/ui/SelectLevelOverlay.java
src/ui/SoundButton.java
src/ui/UrmButton.java
src/ui/VolumeButton.java
src/utilitytools/HelpMethods.java
src/utilitytools/ImportObject.java
src/utilitytools/Konstanta.java
src/utilitytools/LoadSave.java
src/utilitytools/SaveLoadManager.java
src/world/World.java
src/world/WorldManager.java
```

# Files

## File: savegame.txt
```
level=1
exp=0
maxExp=100
currentHealth=100
currentMana=100
x=896.9195
y=343.99
equippedHelmet=-1
equippedArmor=-1
equippedGloves=-1
equippedShoes=-1
equippedAcc1=-1
equippedAcc2=-1
inventory=10,1
worldIndex=0
```

## File: src/gameStates/GameStates.java
```java
package gameStates;

public enum GameStates {

	MENU, PLAYING, OPTIONS, QUIT;

	public static GameStates state = MENU;

}
```

## File: src/gameStates/States.java
```java
package gameStates;

import java.awt.event.MouseEvent;

import main.GameCore;
import ui.MainMenuButton;

public class States {

	protected GameCore gc;

	public States(GameCore gc) {
		this.gc = gc;
	}

	public boolean isIn(MouseEvent e, MainMenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}

	public GameCore GetGame() {
		return gc;
	}
}
```

## File: src/main/Main.java
```java
package main;

public class Main {

	public static void main(String[] args) {
		new GameCore();
	}
}
```

## File: src/ui/MainMenuButton.java
```java
package ui;

import gameStates.GameStates;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import static utilitytools.Konstanta.UI.Frames.*;

public class MainMenuButton {

	private int posX, posY, rowIndex, index;
	private int xOffSetCenter = B_WIDTH / 2;
	private boolean mouseOver, mousePressed;
	private GameStates states;
	private Rectangle bounds;

	public MainMenuButton(int posX, int posY, int rowIndex, GameStates states) {
		this.posX = posX;
		this.posY = posY;
		this.rowIndex = rowIndex;
		this.states = states;
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(posX - xOffSetCenter, posY, B_WIDTH, B_HEIGHT);

	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int x = posX - xOffSetCenter;
		int y = posY;
		Color base = new Color(34, 34, 36);
		Color hover = new Color(60, 72, 90);
		Color pressed = new Color(90, 76, 52);
		Color fill = base;
		if (index == 1) {
			fill = hover;
		} else if (index == 2) {
			fill = pressed;
		}
		int arc = Math.max(10, B_HEIGHT / 2);
		g2.setColor(fill);
		g2.fillRoundRect(x, y, B_WIDTH, B_HEIGHT, arc, arc);
		g2.setColor(new Color(210, 210, 210));
		g2.drawRoundRect(x, y, B_WIDTH, B_HEIGHT, arc, arc);

		g2.setFont(new Font("SansSerif", Font.BOLD, (int) (20 * main.GameCore.SCALE)));
		g2.setColor(new Color(230, 230, 230));
		String label = getLabel();
		int textW = g2.getFontMetrics().stringWidth(label);
		int textH = g2.getFontMetrics().getHeight();
		g2.drawString(label, x + (B_WIDTH - textW) / 2, y + (B_HEIGHT + textH) / 2 - (int) (4 * main.GameCore.SCALE));
		g2.dispose();
	}

	public void update() {
		index = 0;
		if(mouseOver) {
			index = 1;
		}
		if(mousePressed) {
			index = 2;
		}
	}

	private String getLabel() {
		switch (rowIndex) {
		case 0:
			return "PLAY";
		case 1:
			return "OPTIONS";
		case 2:
			return "QUIT";
		default:
			return "BUTTON";
		}
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void applyGameStates() {
		GameStates.state = states;
	}

	public void resetBoolean() {
		mouseOver = false;
		mousePressed = false;
	}

}
```

## File: src/ui/PauseButton.java
```java
package ui;

import java.awt.Rectangle;

public class PauseButton {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected Rectangle bounds;

	public PauseButton(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		createBounds();
	}

	protected void createBounds() {
		bounds = new Rectangle(x, y, width, height);
	}

	private void updateBounds() {
		if (bounds != null) {
			bounds.setBounds(x, y, width, height);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		updateBounds();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		updateBounds();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		updateBounds();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		updateBounds();
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
}
```

## File: src/ui/SoundButton.java
```java
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

public class SoundButton extends PauseButton {
    private boolean mouseOver;
    private boolean mousePressed;
    private boolean muted;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void update() {
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();


        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        Color fill = new Color(0, 0, 0, 200);
        Color border = Color.WHITE;
        Color icon = Color.WHITE;


        if (mouseOver) {
            border = new Color(240, 190, 90);
        }

        if (mousePressed) {
            fill = new Color(240, 190, 90);
            border = Color.WHITE;
            icon = Color.BLACK;
        }


        int arc = 15;


        g2.setColor(fill);
        g2.fillRoundRect(x, y, width, height, arc, arc);


        g2.setColor(border);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(x, y, width, height, arc, arc);


        int spW = width / 4;
        int spH = height / 3;
        int spX = x + width / 4;
        int spY = y + height / 2 - spH / 2;

        g2.setColor(icon);
        g2.fillRect(spX, spY, spW, spH);

        Polygon tri = new Polygon();
        tri.addPoint(spX + spW, spY - (spH/4));
        tri.addPoint(spX + spW + spW, spY + (spH/2));
        tri.addPoint(spX + spW, spY + spH + (spH/4));
        g2.fillPolygon(tri);


        if (muted) {
            g2.setColor(new Color(255, 50, 50));
            g2.setStroke(new BasicStroke(4f));

            g2.drawLine(x + width / 5, y + height / 5, x + width - width / 5, y + height - height / 5);
        }

        g2.dispose();
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() { return mouseOver; }
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
    public boolean isMousePressed() { return mousePressed; }
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }
}
```

## File: src/ui/UrmButton.java
```java
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

public class UrmButton extends PauseButton {
	private boolean mouseOver;
	private boolean mousePressed;
	private int rowIndex;

	public UrmButton(int x, int y, int width, int height, int rowIndex) {
		super(x, y, width, height);
		this.rowIndex = rowIndex;
	}

	public void update() {
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Color fill = new Color(0, 0, 0, 200);
		Color border = Color.white;
		Color icon = Color.white;
		if (mouseOver) {
			border = new Color(240, 190, 90);
		}
		if (mousePressed) {
			fill = new Color(240, 190, 90);
			border = Color.white;
			icon = Color.black;
		}

		int arc = Math.max(10, width / 3);
		g2.setColor(fill);
		g2.fillRoundRect(x, y, width, height, arc, arc);
		g2.setColor(border);
		g2.setStroke(new BasicStroke(Math.max(2f, width / 12f)));
		g2.drawRoundRect(x, y, width, height, arc, arc);

		drawIcon(g2, icon);
		g2.dispose();
	}

	private void drawIcon(Graphics2D g2, Color icon) {
		int pad = Math.max(6, width / 5);
		int ix = x + pad;
		int iy = y + pad;
		int iw = width - pad * 2;
		int ih = height - pad * 2;
		g2.setColor(icon);
		g2.setStroke(new BasicStroke(Math.max(2f, width / 10f)));

		switch (rowIndex) {
		case 0:
			Polygon play = new Polygon();
			play.addPoint(ix, iy);
			play.addPoint(ix, iy + ih);
			play.addPoint(ix + iw, iy + ih / 2);
			g2.fillPolygon(play);
			break;
		case 1:
			g2.drawArc(ix, iy, iw, ih, 45, 270);
			Polygon arrow = new Polygon();
			arrow.addPoint(ix + iw, iy + ih / 2);
			arrow.addPoint(ix + iw - pad / 2, iy + ih / 2 - pad);
			arrow.addPoint(ix + iw - pad / 2, iy + ih / 2 + pad);
			g2.fillPolygon(arrow);
			break;
		case 2:
			int lineH = Math.max(2, ih / 6);
			int gap = lineH;
			g2.fillRect(ix, iy, iw, lineH);
			g2.fillRect(ix, iy + lineH + gap, iw, lineH);
			g2.fillRect(ix, iy + (lineH + gap) * 2, iw, lineH);
			break;
		default:
			g2.drawRect(ix, iy, iw, ih);
			break;
		}
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public int getRowIndex() {
		return rowIndex;
	}
}
```

## File: src/utilitytools/SaveLoadManager.java
```java
package utilitytools;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import main.GameCore;
import entity.Player;
import gameStates.PlayStates;

public class SaveLoadManager {
    private static final String SAVE_FILE = "savegame.txt";

    public static void saveGame(PlayStates playStates) {
        try (PrintWriter writer = new PrintWriter(new File(SAVE_FILE))) {
            Player p = playStates.getPlayer();

            writer.println("level=" + p.getLevel());
            writer.println("exp=" + p.getExp());
            writer.println("maxExp=" + p.getMaxExp());
            writer.println("currentHealth=" + p.getCurrentHealth());
            writer.println("currentMana=" + p.getCurrentMana());
            writer.println("x=" + p.getHitbox().x);
            writer.println("y=" + p.getHitbox().y);

            writer.println("equippedHelmet=" + p.getEquippedHelmet());
            writer.println("equippedArmor=" + p.getEquippedArmor());
            writer.println("equippedGloves=" + p.getEquippedGloves());
            writer.println("equippedShoes=" + p.getEquippedShoes());
            writer.println("equippedAcc1=" + p.getEquippedAcc1());
            writer.println("equippedAcc2=" + p.getEquippedAcc2());

            StringBuilder invStr = new StringBuilder("inventory=");
            for (int i = 0; i < p.inventory.size(); i++) {
                invStr.append(p.inventory.get(i));
                if (i < p.inventory.size() - 1) invStr.append(",");
            }
            writer.println(invStr.toString());

            writer.println("worldIndex=" + playStates.getWorldManager().getWorldIndex());

            System.out.println("Game saved successfully to " + SAVE_FILE);
        } catch (Exception e) {
            System.out.println("Failed to save game!");
            e.printStackTrace();
        }
    }

    public static boolean loadGame(GameCore gc) {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No save file found!");
            return false;
        }

        try (Scanner scanner = new Scanner(file)) {
            gc.getPlay().resetAll(200, 200);
            Player p = gc.getPlay().getPlayer();
            int wIndex = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.contains("=")) continue;

                String[] parts = line.split("=");
                String key = parts[0];
                String value = parts.length > 1 ? parts[1] : "";

                switch (key) {
                    case "level": p.setLevel(Integer.parseInt(value)); break;
                    case "exp": p.setExp(Integer.parseInt(value)); break;
                    case "maxExp": p.setMaxExp(Integer.parseInt(value)); break;
                    case "currentHealth": p.setCurrentHealth(Integer.parseInt(value)); break;
                    case "currentMana": p.setCurrentMana(Integer.parseInt(value)); break;
                    case "x": p.getHitbox().x = Float.parseFloat(value); break;
                    case "y": p.getHitbox().y = Float.parseFloat(value); break;

                    case "equippedHelmet": p.setEquippedHelmet(Integer.parseInt(value)); break;
                    case "equippedArmor": p.setEquippedArmor(Integer.parseInt(value)); break;
                    case "equippedGloves": p.setEquippedGloves(Integer.parseInt(value)); break;
                    case "equippedShoes": p.setEquippedShoes(Integer.parseInt(value)); break;
                    case "equippedAcc1": p.setEquippedAcc1(Integer.parseInt(value)); break;
                    case "equippedAcc2": p.setEquippedAcc2(Integer.parseInt(value)); break;

                    case "inventory":
                        p.inventory.clear();
                        if (!value.isEmpty()) {
                            String[] items = value.split(",");
                            for (String item : items) {
                                p.inventory.add(Integer.parseInt(item));
                            }
                        }
                        break;

                    case "worldIndex":
                        wIndex = Integer.parseInt(value);
                        break;
                }
            }
            p.calculateDefense();
            gc.getPlay().loadWorldByIndex(wIndex);

            System.out.println("Game loaded successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Failed to load game!");
            e.printStackTrace();
            return false;
        }
    }
}
```

## File: resources/main_tileset.txt
```
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,108,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,152,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,108,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,131,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,128,129,130,131,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,131,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,131,-1,-1,-1,-1,150,151,152,153,150,151,152,153,150,151,152,153,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,131,-1,-1,-1,-1,-1,-1,-1,130,131,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,151,152,153,150,151,152,153,150,151,152,153,150,151,152,153,150,151,152,153,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,108,109,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,128,129,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,59,59,60,60,60,59,59,59,59,59,59,60,-1,130,131,-1,-1,-1,-1,-1,-1,130,131,-1,-1,-1,-1,130,131,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,59,60,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,60,60,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,151,152,153,150,151,152,153,150,151,152,153,-1,-1,-1,-1,-1,-1,-1,59,59,59,59,59,59,60,60,60,60,60,60,-1,-1,-1,-1,-1,130,131,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,59,60,59,59,60,60,60,60,60,60,60,60,59,59,59,59,60,60,59,60,-1,-1,-1,-1,153,150,151,152,153,150,151,152,153,150,151,152,153,-1,-1,-1,-1,-1,-1,-1,128,129,130,131,128,129,130,128,129,130,131
150,151,152,153,150,151,152,153,150,151,152,153,-1,-1,-1,-1,-1,-1,-1,-1,150,151,152,153,150,151,152,153,150,151,152,153,150,151,152,153,-1,-1,-1,-1,-1,-1,59,59,59,59,59,59,59,59,59,59,59,60,-1,-1,-1,-1,-1,-1,-1,59,59,59,59,59,60,59,59,60,59,60,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,151,152,153,150,151,152,153,150,151,152,153,-1,-1,-1,-1,-1,-1,-1,130,131,-1,130,131,-1,-1,-1,-1,130,131,-1,150,151,152,153,150,151,152,153,150,151,152,153,150,151,152,153,59,60,59,59,59,59,59,59,59,59,59,59,59,60,60,60,60,59,60,60,-1,-1,-1,-1,59,59,59,59,59,59,59,59,59,59,59,59,60,-1,-1,-1,-1,-1,-1,-1,59,59,59,59,59,59,59,59,59,59,60
59,60,59,60,60,60,60,60,60,60,60,60,-1,-1,-1,-1,-1,-1,-1,-1,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,60,-1,130,131,130,131,-1,59,59,59,59,59,59,59,59,59,60,59,60,-1,-1,-1,-1,-1,-1,-1,59,60,59,60,60,60,60,60,60,59,59,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,59,59,59,59,59,59,59,59,59,59,59,60,-1,130,131,-1,130,131,-1,-1,-1,-1,-1,-1,-1,130,131,-1,-1,-1,-1,59,59,59,59,59,59,59,59,59,59,59,60,59,59,59,60,59,60,59,59,60,60,60,60,60,60,60,59,60,59,60,60,60,60,60,60,-1,-1,-1,-1,60,60,60,60,60,60,60,60,60,60,59,59,60,-1,-1,-1,-1,-1,-1,-1,59,59,59,59,59,59,60,60,60,59,60
59,60,59,59,59,59,59,59,59,60,59,60,-1,-1,-1,-1,-1,-1,-1,-1,59,59,59,59,59,59,59,59,59,59,59,59,59,60,59,60,-1,-1,-1,-1,-1,-1,59,60,59,60,60,60,60,60,60,60,59,60,-1,-1,-1,-1,-1,-1,-1,59,60,59,60,60,60,60,60,60,59,59,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,59,59,59,59,59,59,59,59,59,60,59,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,59,59,59,59,59,59,59,59,59,60,59,59,59,59,59,60,59,60,59,60,60,60,60,60,60,60,59,60,60,60,60,60,60,60,59,60,-1,-1,-1,-1,60,60,60,60,60,60,60,60,60,60,59,60,60,-1,-1,-1,-1,-1,-1,-1,59,59,60,60,60,60,60,59,60,59,60
59,59,59,60,59,59,59,59,59,59,59,60,-1,-1,-1,-1,-1,-1,-1,-1,59,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,-1,-1,-1,-1,-1,-1,59,60,60,60,60,60,60,60,60,60,60,60,-1,-1,-1,-1,-1,-1,-1,59,60,60,60,60,60,60,60,60,59,59,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,59,60,60,60,60,60,60,60,60,60,60,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,59,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,60,-1,-1,-1,-1,59,59,59,59,59,59,59,59,60,60,60,60,60,-1,-1,-1,-1,-1,-1,-1,59,60,60,60,60,60,60,60,60,60,60
```

## File: resources/map_tutorial.csv
```
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,135,135,-1,-1,-1,-1,-1,-1,-1,204,205,-1,135,135,-1,-1,204,205,-1,270,270,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,135,135,-1,-1,-1,-1,-1,-1,-1,204,205,-1,135,135,-1,-1,204,205,-1,270,270,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,229,230,230,231,205,136,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,135,135,-1,-1,-1,-1,-1,-1,-1,329,205,-1,135,135,-1,-1,329,205,-1,270,270,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,136,136,136,-1,-1,-1,-1,-1,-1,-1,-1,256,257,257,258,205,136,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,135,135,135,-1,-1,-1,-1,-1,-1,329,205,-1,135,135,-1,-1,204,205,-1,270,270,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,285,205,-1,136,136,-1,-1,-1,-1,-1,5,277,277,6,136,136,136,285,205,136,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,285,205,-1,135,135,135,-1,-1,-1,-1,-1,-1,285,205,-1,135,135,-1,-1,329,205,-1,270,270,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,136,136,-1,-1,-1,-1,-1,32,304,304,33,136,136,136,312,313,136,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,135,135,-1,-1,-1,-1,-1,-1,-1,312,313,-1,135,135,-1,-1,329,205,-1,270,270,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,297,297,297,297,297,-1,-1,-1,-1,-1,-1,-1,5,277,277,6,136,136,136,-1,-1,-1,-1,-1,-1,-1,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,135,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,135,135,-1,-1,329,205,-1,270,270,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300,300,297,297,297,297,167,167,167,167,-1,-1,-1,-1,32,304,304,33,136,136,136,-1,-1,-1,-1,-1,-1,-1,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,135,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,135,-1,-1,-1,312,313,-1,270,270,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,355,-1,-1,-1,-1,5,277,277,6,300,138,5,277,277,6,297,297,297,297,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,136,-1,5,277,277,6,273,273,5,277,277,6,217,217,217,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,135,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
2,335,336,337,338,339,2,3,4,355,355,355,355,-1,-1,-1,-1,32,304,304,33,-1,138,32,304,304,33,297,297,221,221,221,221,221,221,221,221,221,221,221,221,221,221,221,221,221,-1,-1,136,136,32,304,304,33,273,273,32,304,304,33,217,217,217,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,135,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
28,362,363,364,365,366,29,30,232,2,337,338,339,340,3,4,355,355,355,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,335,336,337,338,339,340,341,342,343,342,343,2,337,338,339,340,3,4,136,136,136,-1,-1,-1,-1,-1,273,-1,-1,-1,-1,-1,0,335,336,337,338,339,340,341,3,3,3,3,3,4,135,135,135,271,0,335,336,337,338,339,340,341,342,343,342,342,343,342,3,3,3,3,3,4
55,56,56,56,56,56,56,371,29,29,364,365,366,367,30,31,355,355,355,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,27,28,363,364,365,366,367,368,369,370,369,370,29,364,365,366,367,30,31,136,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,27,28,363,364,365,366,367,368,369,370,369,367,30,31,135,135,135,271,27,28,363,364,365,366,367,368,369,370,369,369,370,369,369,370,369,367,30,31
55,56,56,56,56,56,56,56,56,56,56,56,56,56,57,58,355,355,355,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,81,55,56,56,56,56,56,56,56,56,56,56,56,56,56,56,56,57,58,136,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,81,55,56,56,56,56,56,56,56,56,56,56,57,58,135,135,135,271,81,55,56,56,56,56,56,56,56,56,56,56,56,56,56,56,56,56,57,58
82,83,83,83,83,83,83,83,83,83,83,83,83,83,84,85,355,355,355,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,54,82,83,83,83,83,83,83,83,83,83,83,83,83,83,83,83,84,85,136,136,136,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,54,82,83,83,83,83,83,83,83,83,83,83,84,85,135,135,135,271,54,82,83,83,83,83,83,83,83,83,83,83,83,83,83,83,83,83,84,85
```

## File: resources/map_tutorial.txt
```
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,163,164,165,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,502,503,504,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,548,549,550,551,552,553,554,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,346,347,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,346,347,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1,-1,-1,-1,-1,-1,-1,360,361,361,362,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,360,361,361,362,-1,360,361,361,362,-1,-1,-1,-1,-1,-1,-1,-1,490,491,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,538,539,-1,-1,-1,-1,-1,-1,-1,-1,-1,460,461,461,462,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,346,347,-1,-1,-1,-1,360,361,361,362,-1,-1,460,461,461,462,-1,460,461,461,462,-1,-1,360,361,361,362,-1,-1,346,347,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,360,361,361,362,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,360,361,361,362,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,538,539,-1,-1,-1,-1,460,461,461,462,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,460,461,461,462,-1,-1,538,539,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,460,461,461,462,-1,-1,-1,-1,-1,-1,-1,360,361,361,362,-1,-1,360,361,361,362,-1,-1,-1,-1,-1,-1,-1,-1,460,461,461,462,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,460,461,461,462,-1,-1,460,461,461,462,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,360,361,362,341,360,361,362,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
610,611,612,613,614,615,616,617,618,316,402,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,460,461,462,341,460,461,462,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
51,661,662,663,664,665,666,667,53,72,396,674,675,676,677,678,679,680,681,104,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,100,673,674,675,676,677,678,679,680,681,682,101,102,103,101,102,103,676,677,678,679,104,-1,-1,-1,-1,-1,-1,-1,-1,-1,100,673,674,675,676,677,678,679,680,681,682,101,102,103,673,674,675,676,677,678,679,680,681,682,101,102,103,101,102,103,676,677,678,679,103
101,102,102,102,102,102,102,102,670,52,721,722,723,724,725,726,727,728,53,152,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,148,149,722,723,724,725,726,727,728,729,730,150,150,150,150,150,150,150,150,150,151,152,-1,-1,-1,-1,-1,-1,-1,-1,-1,148,149,722,723,724,725,726,727,728,729,730,150,726,727,728,729,730,150,725,726,727,728,729,730,150,150,150,150,150,150,150,150,150,150,151
101,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,103,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,196,197,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,199,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,196,197,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,198,199
151,152,152,152,152,152,152,152,152,152,152,152,152,152,152,152,152,152,153,248,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,244,245,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,247,248,-1,-1,-1,-1,-1,-1,-1,-1,-1,244,245,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,246,247
```

## File: src/entity/Boss.java
```java
package entity;

public abstract class Boss extends Enemy {

    protected int currentPhase = 1;

    public Boss(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height, enemyType);
    }

    @Override
    public void hurt(int value) {
        super.hurt(value);
        checkPhaseTransition();
    }

    protected void checkPhaseTransition() {
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }
}
```

## File: src/entity/Entity.java
```java
package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitBox;

	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	protected void drawHitbox(Graphics g) {

		g.setColor(Color.pink);
		g.drawRect((int) hitBox.x, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
	}

	protected void initHitBox(float x, float y, int widht, int height) {

		hitBox = new Rectangle2D.Float(x, y, widht, height);
	}

	public Rectangle2D.Float getHitBox() {
		return hitBox;
	}
}
```

## File: src/entity/Skullwolf.java
```java
package entity;

import static utilitytools.Konstanta.EnemyConstants.*;
import static utilitytools.Konstanta.Directions.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import main.GameCore;

public class Skullwolf extends Enemy {

    private Rectangle2D.Float attackBox;
    private int attackBoxOffSetX;

    public Skullwolf(float x, float y) {
        super(x, y, SKULLWOLF_WIDTH, SKULLWOLF_HEIGHT, SKULLWOLF);
        initHitBox(x, y, SKULLWOLF_HITBOX_WIDTH, SKULLWOLF_HITBOX_HEIGHT);
        initAttackBox();

        walkSpeed = 1.35f * GameCore.SCALE;
        attackDistance = (int) (GameCore.TILES_SIZE * 1.2f);
        aniSpeed = 6;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (35 * GameCore.SCALE), (int) (20 * GameCore.SCALE));

    }

    private void updateAttackBox() {
        attackBox.y = hitBox.y + (10 * GameCore.SCALE);
        if (walkDir == LEFT) {
            attackBox.x = hitBox.x - attackBox.width - attackBoxOffSetX;
        } else {
            attackBox.x = hitBox.x + hitBox.width + attackBoxOffSetX;
        }
    }

    @Override
    public void update(int[][] tilesData, Player player) {
        updateEffects(tilesData);
        updateBehaviour(tilesData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateBehaviour(int[][] tilesData, Player player) {
        checkOnFloor(tilesData);
        if (inAir) {
            updateInAir(tilesData);
        } else {
            switch (enemyState) {
                case IDLE:
                    newState(WALK);
                    break;
                case WALK:
                    if (canSeePlayer(tilesData, player)) {
                        turnToPlayer(player);
                    }
                    if (isPlayerCloseForAttack(player)) {
                        newState(ATTACK);
                    }
                    move(tilesData);
                    break;
                case ATTACK:
                    if (aniIndex == 0) attackChecked = false;

                    if (aniIndex == 4 && !attackChecked) {
                        checkHitEnemy(attackBox, player);
                    }
                    break;
                case HURT:
                    break;
            }
        }
    }

    @Override
    public void draw(Graphics g, int xLvlOffset, BufferedImage[][] spriteAtlas) {
        int stateIndex = enemyState;


        if (stateIndex == MATI || stateIndex == HURT) {
            stateIndex = 3;
        } else if (stateIndex > 3) {
            stateIndex = 0;
        }

        g.drawImage(spriteAtlas[stateIndex][aniIndex],
                (int) (hitBox.x - SKULLWOLF_DRAWOFFSET_X - xLvlOffset + flipX()),
                (int) (hitBox.y - SKULLWOLF_DRAWOFFSET_Y),
                SKULLWOLF_WIDTH * flipW(), SKULLWOLF_HEIGHT, null);

        drawHitbox(g);
        drawAttackBox(g, xLvlOffset);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public int flipX() {
        if (walkDir == RIGHT) {
            return (SKULLWOLF_DRAWOFFSET_X * 2) + SKULLWOLF_HITBOX_WIDTH;
        }
        return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT) {
            return -1;
        }
        return 1;
    }

    @Override
    public int getExpReward() { return 35; }
}
```

## File: src/gameStates/StateMethods.java
```java
package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface StateMethods {

	public void update();

	public void draw(Graphics g);

	public void mouseClicked(MouseEvent e);

	public void mousePressed(MouseEvent e);

	public void mouseReleased(MouseEvent e);

	public void mouseMoved(MouseEvent e);

	public void keyPressed(KeyEvent e);

	public void keyReleased(KeyEvent e);

	void triggerHeavyHit(int freezeFrames, int shakeFrames, int intensity);

}
```

## File: src/inputs/Mouse.java
```java
package inputs;

import gameStates.GameStates;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import main.GamePanel;

public class Mouse implements MouseListener, MouseMotionListener {

	private GamePanel gamePanel;

	public Mouse(GamePanel gamePanel) {
		this.gamePanel = gamePanel;

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		switch(GameStates.state) {
		case PLAYING:
			gamePanel.getGame().getPlay().mouseDragged(e);
			break;
		default:
			break;
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseMoved(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().mouseMoved(e);
			break;
		default:
			break;
		case OPTIONS:
	        gamePanel.getGame().getGameOptions().mouseMoved(e);
	        break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch(GameStates.state) {
		case PLAYING:
			gamePanel.getGame().getPlay().mouseClicked(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().mousePressed(e);
			break;
		default:
			break;
		case OPTIONS:
	        gamePanel.getGame().getGameOptions().mousePressed(e);
	        break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().mouseReleased(e);
			break;
		default:
			break;
		case OPTIONS:
	        gamePanel.getGame().getGameOptions().mouseReleased(e);
	        break;
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {


	}

	@Override
	public void mouseExited(MouseEvent e) {


	}

}
```

## File: src/main/GamePanel.java
```java
package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import static main.GameCore.GAME_WIDTH;
import static main.GameCore.GAME_HEIGHT;

import inputs.Keyboard;
import inputs.Mouse;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Mouse mouse;
	private GameCore gc;

	public GamePanel(GameCore gc) {
		mouse = new Mouse(this);
		this.gc = gc;

		setPanelSize();
		addKeyListener(new Keyboard(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		setFocusable(true);
		requestFocusInWindow();

	}

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}

	public void myUpdate() {

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;

		float scaleX = (float) getWidth() / GAME_WIDTH;
		float scaleY = (float) getHeight() / GAME_HEIGHT;

		g2.scale(scaleX, scaleY);

		gc.render(g2);
	}

	public GameCore getGame() {
		return gc;
	}
}
```

## File: src/objects/Cage.java
```java
package objects;

public class Cage extends GameObject {
    private int blinkTick = 0;
    private boolean blinking = false;

    public Cage(int x, int y, int objType) {
        super(x, y, objType);


        initHitbox(32, 32);
        xDrawOffset = 0;
        yDrawOffset = 0;
    }

    public void update() {
        if (blinking) {
            blinkTick++;

            if (blinkTick >= 40) {
                blinking = false;
                setActive(false);
            }
        }
    }


    public void setHit() {
        if (!blinking) {
            blinking = true;
        }
    }

    public boolean isBlinking() { return blinking; }
    public int getBlinkTick() { return blinkTick; }
}
```

## File: src/objects/DroppedEquipment.java
```java
package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import main.GameCore;

public class DroppedEquipment {
    private Rectangle2D.Float hitbox;
    private int itemType;
    private boolean active = true;
    private BufferedImage img;

    public DroppedEquipment(int x, int y, int itemType, BufferedImage img) {
        this.itemType = itemType;
        this.img = img;


        int size = (int) (22 * GameCore.SCALE);

        this.hitbox = new Rectangle2D.Float(x - size / 2, y - size / 2, size, size);
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (active && img != null) {
            g.drawImage(img,
                (int) (hitbox.x - xLvlOffset),
                (int) hitbox.y,
                (int) hitbox.width,
                (int) hitbox.height, null);
        }
    }

    public Rectangle2D.Float getHitbox() { return hitbox; }
    public int getItemType() { return itemType; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
```

## File: src/objects/GameObject.java
```java
package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.GameCore;
import utilitytools.Konstanta.ObjectConstants;

public class GameObject {
    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= 25) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= ObjectConstants.GetSpriteAmount(objType)) {
                aniIndex = 0;

                if (objType == ObjectConstants.BARREL || objType == ObjectConstants.BOX) {
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    public void reset() {
        aniIndex = 0;
        aniTick = 0;
        active = true;

        if (objType == ObjectConstants.BARREL || objType == ObjectConstants.BOX) {
            doAnimation = false;
        } else {
            doAnimation = true;
        }
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * GameCore.SCALE), (int) (height * GameCore.SCALE));
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }


    public int getObjType() { return objType; }
    public Rectangle2D.Float getHitbox() { return hitbox; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public void setAnimation(boolean doAnimation) { this.doAnimation = doAnimation; }
    public int getxDrawOffset() { return xDrawOffset; }
    public int getyDrawOffset() { return yDrawOffset; }
    public int getAniIndex() { return aniIndex; }
}
```

## File: src/objects/Potion.java
```java
package objects;

import main.GameCore;

public class Potion extends GameObject {
    private float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;

    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffset = (int)(3 * GameCore.SCALE);
        yDrawOffset = (int)(2 * GameCore.SCALE);

        maxHoverOffset = (int)(10 * GameCore.SCALE);
    }

    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.075f * GameCore.SCALE) * hoverDir;

        if (hoverOffset >= maxHoverOffset) {
            hoverDir = -1;
        } else if (hoverOffset < 0) {
            hoverDir = 1;
        }

        hitbox.y = y + hoverOffset;
    }
}
```

## File: src/objects/Sign.java
```java
package objects;

public class Sign extends GameObject {
    private String middleText;
    private String topText;

    public Sign(int x, int y, int objType, String middleText, String topText) {
        super(x, y, objType);
        this.middleText = middleText;
        this.topText = topText;


        initHitbox(17, 17);
        xDrawOffset = 0;
        yDrawOffset = 0;
    }

    public String getMiddleText() { return middleText; }
    public String getTopText() { return topText; }
}
```

## File: src/ui/LevelCompletedOverlay.java
```java
package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gameStates.GameStates;
import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;

import static utilitytools.Konstanta.UI.PauseButtons.*;

public class LevelCompletedOverlay {

	private PlayStates playStates;
	private UrmButton menu, next;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;

	public LevelCompletedOverlay(PlayStates playStates) {
		this.playStates = playStates;
		initImg();
		initButtons();
	}

	private void initImg() {


		img = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);

		bgW = (int) (img.getWidth() * GameCore.SCALE);
		bgH = (int) (img.getHeight() * GameCore.SCALE);
		bgX = GameCore.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (75 * GameCore.SCALE);
	}

	private void initButtons() {
		int menuX = (int) (330 * GameCore.SCALE);
		int nextX = (int) (445 * GameCore.SCALE);
		int y = (int) (195 * GameCore.SCALE);


		next = new UrmButton(nextX, y, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, 0);
		menu = new UrmButton(menuX, y, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, 2);
	}

	public void update() {
		next.update();
		menu.update();
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);


		g.drawImage(img, bgX, bgY, bgW, bgH, null);

		next.draw(g);
		menu.draw(g);
	}

	public void mouseMoved(MouseEvent e) {
		next.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e)) {
			menu.setMouseOver(true);
		} else if (isIn(next, e)) {
			next.setMouseOver(true);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {

				playStates.resetAll(200, 200);
				GameStates.state = GameStates.MENU;
			}
		} else if (isIn(next, e)) {
			if (next.isMousePressed()) {

				playStates.loadNextLevel();
				System.out.println("Tombol NEXT ditekan: Memuat Level Berikutnya!");
			}
		}

		menu.resetBools();
		next.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e)) {
			menu.setMousePressed(true);
		} else if (isIn(next, e)) {
			next.setMousePressed(true);
		}
	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
}
```

## File: src/ui/SelectLevelOverlay.java
```java
package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import gameStates.MainMenu;
import main.GameCore;
import utilitytools.LoadSave;

public class SelectLevelOverlay {

    private MainMenu menu;
    private int panelX, panelY, panelW, panelH;
    private Rectangle btnLevel1, btnLevel2, btnLevel3, btnBack;
    private boolean isHoveringL1, isHoveringL2, isHoveringL3, isHoveringBack;

    public SelectLevelOverlay(MainMenu menu) {
        this.menu = menu;
        initLayout();
    }

    private void initLayout() {
        panelW = (int) (500 * GameCore.SCALE);
        panelH = (int) (350 * GameCore.SCALE);
        panelX = GameCore.GAME_WIDTH / 2 - panelW / 2;
        panelY = GameCore.GAME_HEIGHT / 2 - panelH / 2;

        int btnSize = (int) (70 * GameCore.SCALE);
        int gap = (int) (30 * GameCore.SCALE);

        int totalBtnWidth = (btnSize * 3) + (gap * 2);
        int startX = panelX + (panelW / 2) - (totalBtnWidth / 2);
        int startY = panelY + (int) (130 * GameCore.SCALE);

        btnLevel1 = new Rectangle(startX, startY, btnSize, btnSize);
        btnLevel2 = new Rectangle(startX + btnSize + gap, startY, btnSize, btnSize);
        btnLevel3 = new Rectangle(startX + (btnSize + gap) * 2, startY, btnSize, btnSize);
        btnBack = new Rectangle(panelX + (int)(15 * GameCore.SCALE), panelY + (int)(15 * GameCore.SCALE), (int)(60 * GameCore.SCALE), (int)(25 * GameCore.SCALE));
    }

    public void update() {}

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

        g2.setColor(new Color(50, 30, 15));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 30, 30);
        g2.setColor(new Color(139, 69, 19));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 30, 30);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, (int)(25 * GameCore.SCALE)));
        g2.drawString("PILIH LEVEL", panelX + panelW/2 - (int)(75 * GameCore.SCALE), panelY + (int)(70 * GameCore.SCALE));

        drawCustomButton(g2, btnLevel1, "1", isHoveringL1, new Color(34, 139, 34));
        drawCustomButton(g2, btnLevel2, "2", isHoveringL2, new Color(100, 100, 100));
        drawCustomButton(g2, btnLevel3, "3", isHoveringL3, new Color(105, 105, 105));
        drawCustomButton(g2, btnBack, "Back", isHoveringBack, new Color(200, 50, 50));
    }

    private void drawCustomButton(Graphics2D g2, Rectangle bounds, String text, boolean isHovering, Color baseColor) {
        if (isHovering) g2.setColor(baseColor.brighter());
        else g2.setColor(baseColor);

        g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);

        g2.setFont(new Font("Arial", Font.BOLD, (int)(18 * GameCore.SCALE)));
        g2.drawString(text, bounds.x + (bounds.width/3), bounds.y + (bounds.height/2) + (int)(6 * GameCore.SCALE));
    }

    public void mouseMoved(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        isHoveringL1 = btnLevel1.contains(mx, my);
        isHoveringL2 = btnLevel2.contains(mx, my);
        isHoveringL3 = btnLevel3.contains(mx, my);
        isHoveringBack = btnBack.contains(mx, my);
    }


    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (btnLevel1.contains(mx, my)) {
            menu.startSelectedLevel("/map_tutorial_fix.txt", LoadSave.WORLD_SPRITE);
        }
        else if (btnLevel2.contains(mx, my)) {
            menu.startSelectedLevel("/map_tutorial_fix.txt", LoadSave.WORLD_SPRITE);
        }
        else if (btnLevel3.contains(mx, my)) {
            menu.startSelectedLevel("/main_tileset.txt", "main_tileset.png");
        }
        else if (btnBack.contains(mx, my)) {
            menu.setLevelSelectActive(false);
        }
    }
}
```

## File: src/ui/VolumeButton.java
```java
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class VolumeButton extends PauseButton {

    private int buttonX, minX, maxX;
    private int sliderWidth;


    private int knobWidth;
    private int knobHeight;

    private boolean mouseOver, mousePressed;

    public VolumeButton(int x, int y, int width, int height, int sliderWidth, int sliderHeight) {
        super(x, y, sliderWidth, height);
        this.sliderWidth = sliderWidth;



        this.knobWidth = width / 2;

        this.knobHeight = (int)(height * 0.6);

        this.minX = x;
        this.maxX = x + sliderWidth - knobWidth;
        this.buttonX = minX + (sliderWidth / 2) - (knobWidth / 2);
    }

    public void update() {
        bounds.x = buttonX;
        bounds.width = knobWidth;

        bounds.y = y + (height / 2) - (knobHeight / 2);
        bounds.height = knobHeight;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int trackY = y + (height / 2) - 4;
        int trackHeight = 8;


        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRect(x, trackY, sliderWidth, trackHeight);


        float volumeRatio = 0;
        if (maxX - minX > 0) {
            volumeRatio = (float) (buttonX - minX) / (maxX - minX);
        }
        int fillWidth = (int) (sliderWidth * volumeRatio);

        g2.setColor(new Color(240, 190, 90));
        g2.fillRect(x + 2, trackY + 2, Math.max(0, fillWidth - 4), trackHeight - 3);


        int knobY = y + (height / 2) - (knobHeight / 2);

        Color knobColor = mouseOver ? new Color(240, 190, 90) : Color.WHITE;
        if (mousePressed) {
            knobColor = new Color(200, 150, 50);
        }

        g2.setColor(knobColor);
        g2.fillRect(buttonX, knobY, knobWidth, knobHeight);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(buttonX, knobY, knobWidth, knobHeight);

        g2.dispose();
    }

    public void changeX(int xPos) {
        if (xPos < minX) {
            buttonX = minX;
        } else if (xPos > maxX) {
            buttonX = maxX;
        } else {
            buttonX = xPos;
        }
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }



    public float GetFloatValue() {
        if (maxX - minX > 0) {
            return (float) (buttonX - minX) / (maxX - minX);
        }
        return 0f;
    }



    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
```

## File: src/world/World.java
```java
package world;

public class World {

	private int[][] tilesData;

	public World(int[][] tilesData) {
		this.tilesData = tilesData;
	}

	public int getSpriteIndex(int x, int y) {
		if (y < 0 || y >= tilesData.length || x < 0 || x >= tilesData[0].length) {
			return 0;
		}
		return tilesData[y][x];
	}

	public int[][] getWorldData(){
		return tilesData;
	}
}
```

## File: .gitignore
```
# Eclipse
/bin/
.classpath
.project
.settings/
.metadata/

# Compiled class files  
*.class

# OS files
.DS_Store
Thumbs.db

# Build files
target/
ANALISIS_PROYEK.md
*.md
```

## File: resources/map_test.txt
```
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,150,150,150,150,150,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,150,150,150,150,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,150,150,150,150,150,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,303,-1,-1,-1,402,500,-1,403,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
-1,-1,-1,-1,-1,150,150,150,150,150,150,-1,-1,-1,-1,201,-1,-1,150,150,150,150,150,150,150,-1,-1,-1,-1,201,-1,150,150,150,150,150,150,-1,-1,-1,-1,-1,150,150,150,150,150,150,-1,-1
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
150,150,150,150,150,150,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,150,150,150,150,150
150,150,150,150,150,150,150,150,150,150,150,-1,-1,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150
150,150,150,150,150,150,150,150,150,150,150,-1,-1,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150
```

## File: resources/map_tutorial_fix.csv
```
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,229,230,230,231,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,256,257,257,258,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,285,205,-1,-1,-1,-1,-1,-1,-1,-1,5,277,277,6,-1,-1,-1,285,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,285,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,285,205,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,-1,-1,-1,-1,-1,-1,-1,32,304,304,33,-1,-1,-1,312,313,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,277,277,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,32,304,304,33,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,277,277,6,-1,-1,5,277,277,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,277,277,6,-1,-1,5,277,277,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
2,335,336,337,338,339,2,3,4,-1,-1,-1,-1,-1,-1,-1,-1,32,304,304,33,-1,-1,32,304,304,33,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,800,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,32,304,304,33,-1,-1,32,304,304,33,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,801,-1
28,362,363,364,365,366,29,30,232,2,337,338,339,340,3,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,335,336,337,338,339,340,341,342,343,342,343,2,337,338,339,340,3,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,335,336,337,338,339,340,341,3,3,3,3,3,4,-1,-1,-1,-1,0,335,336,337,338,339,340,341,342,343,342,342,343,342,3,3,3,3,3,4
55,56,56,56,56,56,56,371,29,29,364,365,366,367,30,31,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,27,28,363,364,365,366,367,368,369,370,369,370,29,364,365,366,367,30,31,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,27,28,363,364,365,366,367,368,369,370,369,367,30,31,-1,-1,-1,-1,27,28,363,364,365,366,367,368,369,370,369,369,370,369,369,370,369,367,30,31
55,56,56,56,56,56,56,56,56,56,56,56,56,56,57,58,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,81,55,56,56,56,56,56,56,56,56,56,56,56,56,56,56,56,57,58,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,81,55,56,56,56,56,56,56,56,56,56,56,57,58,-1,-1,-1,-1,81,55,56,56,56,56,56,56,56,56,56,56,56,56,56,56,56,56,57,58
82,83,83,83,83,83,83,83,83,83,83,83,83,83,84,85,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,54,82,83,83,83,83,83,83,83,83,83,83,83,83,83,83,83,84,85,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,54,82,83,83,83,83,83,83,83,83,83,83,84,85,-1,-1,-1,-1,54,82,83,83,83,83,83,83,83,83,83,83,83,83,83,83,83,83,84,85
```

## File: src/gameStates/GameOptions.java
```java
package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.GameCore;
import utilitytools.LoadSave;

public class GameOptions extends States implements StateMethods {

    private BufferedImage backgroundImage;
    private int bgX, bgY, bgW, bgH;


    private Rectangle backBtn;
    private boolean backHover, backPressed;

    public GameOptions(GameCore gc) {
        super(gc);
        loadBackground();
        initButton();
    }

    private void loadBackground() {

        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        bgW = GameCore.GAME_WIDTH;
        bgH = GameCore.GAME_HEIGHT;
        bgX = 0;
        bgY = 0;
    }

    private void initButton() {
        int btnWidth = (int) (150 * GameCore.SCALE);
        int btnHeight = (int) (40 * GameCore.SCALE);
        int btnX = GameCore.GAME_WIDTH / 2 - btnWidth / 2;
        int btnY = (int) (300 * GameCore.SCALE);

        backBtn = new Rectangle(btnX, btnY, btnWidth, btnHeight);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, bgX, bgY, bgW, bgH, null);
        } else {
            g.setColor(new Color(30, 30, 30));
            g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
        }


        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, (int)(36 * GameCore.SCALE)));
        String title = "OPTIONS MENU";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, GameCore.GAME_WIDTH / 2 - titleWidth / 2, (int)(100 * GameCore.SCALE));


        g.setFont(new Font("Arial", Font.PLAIN, (int)(16 * GameCore.SCALE)));
        String subtitle = "Pengaturan Audio & Kontrol dalam tahap pengembangan.";
        int subWidth = g.getFontMetrics().stringWidth(subtitle);
        g.drawString(subtitle, GameCore.GAME_WIDTH / 2 - subWidth / 2, (int)(150 * GameCore.SCALE));


        drawTransparentButton(g, backBtn, "BACK", backHover, backPressed);
    }

    private void drawTransparentButton(Graphics g, Rectangle bounds, String text, boolean isHover, boolean isPressed) {

        if (isPressed) {
            g.setColor(new Color(150, 50, 50));
        } else if (isHover) {
            g.setColor(new Color(255, 215, 0));
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }

        g.setFont(new Font("Arial", Font.BOLD, (int)(24 * GameCore.SCALE)));
        int stringWidth = g.getFontMetrics().stringWidth(text);
        int stringHeight = g.getFontMetrics().getHeight();

        g.drawString(text,
                     bounds.x + (bounds.width - stringWidth) / 2,
                     bounds.y + (bounds.height + stringHeight) / 2 - 5);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        backHover = backBtn.contains(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (backHover) backPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (backHover && backPressed) {

            GameStates.state = GameStates.MENU;
        }
        resetButtons();
    }

    private void resetButtons() {
        backHover = false;
        backPressed = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameStates.state = GameStates.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

	@Override
	public void triggerHeavyHit(int freezeFrames, int shakeFrames, int intensity) {


	}
}
```

## File: src/inputs/Keyboard.java
```java
package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gameStates.GameStates;
import main.GamePanel;

import static utilitytools.Konstanta.Directions.*;

public class Keyboard implements KeyListener {

	private GamePanel gamePanel;
	public Keyboard(GamePanel gamePanel) {
		this.gamePanel = gamePanel;

	}

	@Override
	public void keyTyped(KeyEvent e) {


	}

	@Override
	public void keyReleased(KeyEvent e) {

		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().keyReleased(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch(GameStates.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlay().keyPressed(e);
			break;
		case OPTIONS:
	        gamePanel.getGame().getGameOptions().keyPressed(e);
	        break;
		default:
			break;
		}

	}
}
```

## File: src/main/GameFrame.java
```java
package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

public class GameFrame {
	private JFrame jFrame;
	private boolean isFullScreen = false;

	public GameFrame(GamePanel gamePanel) {

		jFrame = new JFrame();

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.add(gamePanel);
		jFrame.setResizable(false);
		jFrame.setTitle("Feline Souls : Witch Curse");

		jFrame.pack();

		jFrame.setLocationRelativeTo(null);

		jFrame.setVisible(true);
		jFrame.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();

			}

			@Override
			public void windowGainedFocus(WindowEvent e) {

			}
		});
	}

	public void toggleFullScreen() {
		isFullScreen = !isFullScreen;

		jFrame.dispose();

		jFrame.setUndecorated(isFullScreen);

		if (isFullScreen) {
			jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			jFrame.setExtendedState(JFrame.NORMAL);
			jFrame.pack();
			jFrame.setLocationRelativeTo(null);
		}

		jFrame.setVisible(true);
	}
}
```

## File: src/objects/GameContainer.java
```java
package objects;

import main.GameCore;
import utilitytools.Konstanta.ObjectConstants;
import objects.GameObject;

public class GameContainer extends GameObject {

    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() {
        if (objType == ObjectConstants.BOX) {
            initHitbox(25, 18);
            xDrawOffset = (int)(7 * GameCore.SCALE);
            yDrawOffset = (int)(12 * GameCore.SCALE);
        } else {
            initHitbox(23, 25);
            xDrawOffset = (int)(8 * GameCore.SCALE);
            yDrawOffset = (int)(5 * GameCore.SCALE);
        }


        hitbox.y += yDrawOffset + (int)(2 * GameCore.SCALE);
        hitbox.x += xDrawOffset / 2;
    }

    public void update() {
        if (doAnimation) {
            updateAnimationTick();
        }
    }
}
```

## File: src/ui/PauseOverlay.java
```java
package ui;

import gameStates.GameStates;
import gameStates.PlayStates;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.GameCore;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.UI.PauseButtons.*;

public class PauseOverlay {
    private PlayStates playing;
    private int panelX, panelY, panelW, panelH;

    private BufferedImage titleImg, musicImg, seImg, volImg;
    private BufferedImage pawImg, resumeImg, restartImg, menuImg;

    private SoundButton musicButton, sfxButton;
    private UrmButton menuButton, replayButton, unpauseButton;
    private VolumeButton volumeButton;

    public PauseOverlay(PlayStates playing) {
        this.playing = playing;
        loadImages();
        initLayout();
    }

    private void loadImages() {
        titleImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_TITLE);
        musicImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MUSIC_TEXT);
        seImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_SE_TEXT);
        volImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_VOL_TEXT);

        pawImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_PAW_BTN);
        resumeImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_RESUME_BTN);
        restartImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_RESTART_BTN);
        menuImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU_BTN);
    }

    private void initLayout() {
        int tile = GameCore.TILES_SIZE;
        panelW = tile * 12;
        panelH = tile * 11;
        panelX = GameCore.GAME_WIDTH / 2 - panelW / 2;
        panelY = GameCore.GAME_HEIGHT / 2 - panelH / 2;

        int btnSize = (int)(45 * GameCore.SCALE);



        int defaultColX = panelX + panelW - btnSize - (int)(30 * GameCore.SCALE);


        int soundHitboxX = defaultColX - 80;


        int volumeHitboxX = defaultColX;


        int startY = panelY + (int)(100 * GameCore.SCALE);
        int itemDy = btnSize + (int)(15 * GameCore.SCALE);


        musicButton = new SoundButton(soundHitboxX, startY, btnSize, btnSize);
        int sfxY = startY + itemDy;
        sfxButton = new SoundButton(soundHitboxX, sfxY, btnSize, btnSize);

        int volumeY = sfxY + itemDy + (int)(10 * GameCore.SCALE);
        int customSliderWidth = (int)(150 * GameCore.SCALE);


        int sliderX = volumeHitboxX + btnSize - customSliderWidth;
        volumeButton = new VolumeButton(sliderX, volumeY + (int)(15 * GameCore.SCALE), VOLUME_WIDTH, VOLUME_HEIGHT, customSliderWidth, VOLUME_HEIGHT);

        int gap = (int)(25 * GameCore.SCALE);
        int urmSize = (int)(60 * GameCore.SCALE);
        int totalW = (urmSize * 3) + (gap * 2);
        int urmX = panelX + (panelW - totalW) / 2;
        int urmY = volumeY + (int)(75 * GameCore.SCALE);

        unpauseButton = new UrmButton(urmX, urmY, urmSize, urmSize, 0);
        replayButton = new UrmButton(urmX + urmSize + gap, urmY, urmSize, urmSize, 1);
        menuButton = new UrmButton(urmX + (urmSize + gap) * 2, urmY, urmSize, urmSize, 2);
    }

    public void update() {
        musicButton.update(); sfxButton.update(); unpauseButton.update();
        replayButton.update(); menuButton.update(); volumeButton.update();
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
        drawSubWindow(g2, panelX, panelY, panelW, panelH);

        int textX = panelX + (int)(GameCore.TILES_SIZE * 1.5);


        int titleW = (int)(200 * GameCore.SCALE);
        int titleH = (int)(65 * GameCore.SCALE);
        int titleX = panelX + (panelW / 2) - (titleW / 2);
        int titleY = panelY + (int)(GameCore.TILES_SIZE * 0.8);


        int musicTextX = textX + 0;
        int musicTextY = musicButton.getY() + 10;
        int musicTextW = (int)(125 * GameCore.SCALE);
        int musicTextH = (int)(50 * GameCore.SCALE);


        int seTextX = textX + 0;
        int seTextY = sfxButton.getY() + 10;
        int seTextW = (int)(125 * GameCore.SCALE);
        int seTextH = (int)(60 * GameCore.SCALE);


        int volTextX = textX + 10;
        int volTextY = volumeButton.getY() - 10;
        int volTextW = (int)(112 * GameCore.SCALE);
        int volTextH = (int)(55 * GameCore.SCALE);



        int pawMusicX = musicButton.getBounds().x + 0;
        int pawMusicY = musicButton.getBounds().y + 10;
        int pawMusicW = (int)(musicButton.getBounds().width);
        int pawMusicH = (int)(musicButton.getBounds().height);
        java.awt.Rectangle customMusicBounds = new java.awt.Rectangle(pawMusicX, pawMusicY, pawMusicW, pawMusicH);


        int pawSfxX = sfxButton.getBounds().x + 0;
        int pawSfxY = sfxButton.getBounds().y + 25;
        int pawSfxW = (int)(sfxButton.getBounds().width);
        int pawSfxH = (int)(sfxButton.getBounds().height);
        java.awt.Rectangle customSfxBounds = new java.awt.Rectangle(pawSfxX, pawSfxY, pawSfxW, pawSfxH);


        if (titleImg != null) g2.drawImage(titleImg, titleX, titleY, titleW, titleH, null);
        if (musicImg != null) g2.drawImage(musicImg, musicTextX, musicTextY, musicTextW, musicTextH, null);
        if (seImg != null) g2.drawImage(seImg, seTextX, seTextY, seTextW, seTextH, null);
        if (volImg != null) g2.drawImage(volImg, volTextX, volTextY, volTextW, volTextH, null);


        drawCustomButton(g2, pawImg, customMusicBounds, musicButton.isMouseOver(), musicButton.isMousePressed(), musicButton.isMuted());
        drawCustomButton(g2, pawImg, customSfxBounds, sfxButton.isMouseOver(), sfxButton.isMousePressed(), sfxButton.isMuted());


        drawCustomButton(g2, resumeImg, unpauseButton.getBounds(), unpauseButton.isMouseOver(), unpauseButton.isMousePressed(), false);
        drawCustomButton(g2, restartImg, replayButton.getBounds(), replayButton.isMouseOver(), replayButton.isMousePressed(), false);
        drawCustomButton(g2, menuImg, menuButton.getBounds(), menuButton.isMouseOver(), menuButton.isMousePressed(), false);

        volumeButton.draw(g2);
        g2.dispose();
    }

    private void drawCustomButton(Graphics2D g2, BufferedImage img, Rectangle bounds, boolean isHover, boolean isPressed, boolean isMuted) {
        if (img != null) {
            g2.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height, null);
            if (isMuted || isPressed) { g2.setColor(new Color(0, 0, 0, 100)); g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10); }
            else if (isHover) { g2.setColor(new Color(255, 255, 255, 50)); g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10); }
        }
    }

    private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 200)); g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(new Color(139, 69, 19)); g2.setStroke(new BasicStroke(5)); g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    private boolean isIn(MouseEvent e, PauseButton b) { return b.getBounds().contains(e.getX(), e.getY()); }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton)) sfxButton.setMousePressed(true);
        else if (isIn(e, unpauseButton)) unpauseButton.setMousePressed(true);
        else if (isIn(e, replayButton)) replayButton.setMousePressed(true);
        else if (isIn(e, menuButton)) menuButton.setMousePressed(true);
        else if (isIn(e, volumeButton)) volumeButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {

        if (isIn(e, musicButton) && musicButton.isMousePressed()) {
            musicButton.setMuted(!musicButton.isMuted());
            playing.GetGame().getAudioPlayer().toggleSongMute();
        }
        else if (isIn(e, sfxButton) && sfxButton.isMousePressed()) {
            sfxButton.setMuted(!sfxButton.isMuted());
        }
        else if (isIn(e, unpauseButton) && unpauseButton.isMousePressed()) {
            playing.setPaused(false);
        }
        else if (isIn(e, replayButton) && replayButton.isMousePressed()) {
            playing.resetAll(200, 200);
        }
        else if (isIn(e, menuButton) && menuButton.isMousePressed()) {
            GameStates.state = GameStates.MENU;
            playing.setPaused(false);
        }

        musicButton.resetBools(); sfxButton.resetBools(); unpauseButton.resetBools(); replayButton.resetBools(); menuButton.resetBools(); volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false); sfxButton.setMouseOver(false); unpauseButton.setMouseOver(false);
        replayButton.setMouseOver(false); menuButton.setMouseOver(false); volumeButton.setMouseOver(false);
        if (isIn(e, musicButton)) musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton)) sfxButton.setMouseOver(true);
        else if (isIn(e, unpauseButton)) unpauseButton.setMouseOver(true);
        else if (isIn(e, replayButton)) replayButton.setMouseOver(true);
        else if (isIn(e, menuButton)) menuButton.setMouseOver(true);
        else if (isIn(e, volumeButton)) volumeButton.setMouseOver(true);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
            playing.GetGame().getAudioPlayer().setVolume(volumeButton.GetFloatValue());
        }
    }
}
```

## File: src/utilitytools/ImportObject.java
```java
package utilitytools;

import java.util.ArrayList;
import main.GameCore;
import objects.GameContainer;
import objects.Potion;
import static utilitytools.Konstanta.ObjectConstants.*;

public class ImportObject {

    public static void GetObjectsFromMap(int[][] mapData, ArrayList<Potion> potions, ArrayList<GameContainer> containers) {

        potions.clear();
        containers.clear();

        for (int j = 0; j < mapData.length; j++) {
            for (int i = 0; i < mapData[0].length; i++) {

                int id = mapData[j][i];
                int xPos = i * GameCore.TILES_SIZE;
                int yPos = j * GameCore.TILES_SIZE;

                if (id == 500) { potions.add(new Potion(xPos, yPos, RED_POTION_1)); mapData[j][i] = 11; }
                else if (id == 501) { potions.add(new Potion(xPos, yPos, RED_POTION_2)); mapData[j][i] = 11; }
                else if (id == 502) { potions.add(new Potion(xPos, yPos, RED_POTION_3)); mapData[j][i] = 11; }

                else if (id == 503) { potions.add(new Potion(xPos, yPos, BLUE_POTION_1)); mapData[j][i] = 11; }
                else if (id == 504) { potions.add(new Potion(xPos, yPos, BLUE_POTION_2)); mapData[j][i] = 11; }
                else if (id == 505) { potions.add(new Potion(xPos, yPos, BLUE_POTION_3)); mapData[j][i] = 11; }

                else if (id == 601) { containers.add(new GameContainer(xPos, yPos, BOX)); mapData[j][i] = 11; }
                else if (id == 602) { containers.add(new GameContainer(xPos, yPos, BARREL)); mapData[j][i] = 11; }
            }
        }
    }
}
```

## File: src/entity/BringerOfDeath.java
```java
package entity;

import static utilitytools.Konstanta.Directions.*;
import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.PlayStates;
import main.GameCore;

public class BringerOfDeath extends Boss {

    private Rectangle2D.Float attackBox;
    private int attackCooldown = 0;
    private boolean isActive = false;

    private boolean hp50Triggered = false;
    private boolean hp25Triggered = false;

    private static final float BOSS_SCALE = 1.5f;

    private static final int SCALED_WIDTH = (int) (BRINGER_WIDTH_DEFAULT * GameCore.SCALE * BOSS_SCALE);
    private static final int SCALED_HEIGHT = (int) (BRINGER_HEIGHT_DEFAULT * GameCore.SCALE * BOSS_SCALE);

    private static final int SCALED_HITBOX_W = (int) (35 * GameCore.SCALE * BOSS_SCALE);
    private static final int SCALED_HITBOX_H = (int) (50 * GameCore.SCALE * BOSS_SCALE);

    private static final int SCALED_OFFSET_X = (int) (90 * GameCore.SCALE * BOSS_SCALE);
    private static final int SCALED_OFFSET_Y = (int) (40 * GameCore.SCALE * BOSS_SCALE);

    public BringerOfDeath(float x, float y) {
        super(x, y, SCALED_WIDTH, SCALED_HEIGHT, BRINGER_OF_DEATH);

        initHitBox(x, y, SCALED_HITBOX_W, SCALED_HITBOX_H);
        initAttackBox();

        walkSpeed = 0.65f * GameCore.SCALE;
        attackDistance = (int) (GameCore.TILES_SIZE * 2.0f);
        aniSpeed = 5;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (60 * GameCore.SCALE * BOSS_SCALE), (int) (50 * GameCore.SCALE * BOSS_SCALE));
    }

    protected void drawAttackBox(Graphics g) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void updateAttackBox() {
        attackBox.y = hitBox.y;
        if (walkDir == LEFT) {
            attackBox.x = hitBox.x - attackBox.width;
        } else {
            attackBox.x = hitBox.x + hitBox.width;
        }
    }

    private void updateBehaviour(int[][] tilesData, Player player) {
        checkOnFloor(tilesData);
        if (inAir) {
            updateInAir(tilesData);
            if (attackCooldown > 0) {
                attackCooldown--;
            }
            return;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        switch(enemyState) {
        case IDLE:
            if (isPlayerCloseEnoughForAttack(player) && attackCooldown > 0 && !isActive) {
                turnToPlayer(player);
            } else {
                newState(WALK);
            }
            break;
        case WALK:
            if (isPlayerInRange(player)) {
                turnToPlayer(player);
            }
            if (isPlayerCloseEnoughForAttack(player)) {
                if (attackCooldown <= 0) {
                    if (Math.random() < 0.3) {
                        newState(BRINGER_SPECIAL);
                        attackCooldown = 180;
                        attackChecked = false;
                    } else {
                        newState(ATTACK);
                        attackCooldown = 100;
                        attackChecked = false;
                    }
                } else {
                    newState(IDLE);
                }
            } else {
                move(tilesData);
            }
            break;
        case ATTACK:
            if (aniIndex == 4 && !attackChecked) {
                checkHitEnemy(attackBox, player);
                attackChecked = true;
            }

            if (aniIndex >= GetSpriteAmount(BRINGER_OF_DEATH, enemyState) - 1) {
                newState(IDLE);
            }
            break;

        case BRINGER_SPECIAL:
            if (aniIndex == 4 && !attackChecked) {
                checkHitEnemy(attackBox, player);
                attackChecked = true;
            }

            if (aniIndex >= GetSpriteAmount(BRINGER_OF_DEATH, enemyState) - 1) {
                newState(IDLE);
            }
            break;
        case HURT:
            break;
        }
    }

    @Override
    protected void turnToPlayer(Player player) {
        float playerCenterX = player.getHitBox().x + (player.getHitBox().width / 2);
        float bossCenterX = hitBox.x + (hitBox.width / 2);

        if (Math.abs(playerCenterX - bossCenterX) > 10 * GameCore.SCALE) {
            if (playerCenterX > bossCenterX) {
                walkDir = RIGHT;
            } else {
                walkDir = LEFT;
            }
        }
    }

    @Override
    public void update(int[][] tilesData, Player player) {
        updateEffects(tilesData);
        updateBehaviour(tilesData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    @Override
    public void draw(Graphics g, int xLvlOffset, BufferedImage[][] spriteAtlas) {
        if (enemyState < 0 || enemyState >= spriteAtlas.length) return;
        BufferedImage[] frames = spriteAtlas[enemyState];

        if (aniIndex < 0) {
            aniIndex = 0;
        }
        if (aniIndex >= frames.length) {
            aniIndex = frames.length - 1;
        }

        BufferedImage frame = frames[aniIndex];
        if (frame != null) {
            g.drawImage(frame, drawX() - xLvlOffset, drawY(), SCALED_WIDTH * flipW(), SCALED_HEIGHT, null);
        }



    }

    @Override
    public int getExpReward() { return 300; }

    @Override
    protected void handleHurtEffects(PlayStates playStates) {
        float hpPercentage = ((float) currentHealth / maxHealth) * 100f;
        if (hpPercentage <= 50 && !hp50Triggered) {
            hp50Triggered = true;
            playStates.triggerHeavyHit(20, 25, 12);
        }
        if (hpPercentage <= 25 && !hp25Triggered) {
            hp25Triggered = true;
            playStates.triggerHeavyHit(20, 30, 15);
            walkSpeed = 0.85f * GameCore.SCALE;
        }
    }

    @Override
    protected void handleDeathEffects(PlayStates playStates) {
        playStates.triggerHeavyHit(60, 180, 15);
    }

    private boolean isPlayerCloseEnoughForAttack(Player player) {
        float xDistance = (float) Math.abs(player.getHitBox().getCenterX() - hitBox.getCenterX());
        float yDistance = (float) Math.abs(player.getHitBox().getCenterY() - hitBox.getCenterY());
        return xDistance <= attackDistance && yDistance <= GameCore.TILES_SIZE * 3.0f;
    }

    public int flipX() {
        if (walkDir == RIGHT) {
            return (SCALED_OFFSET_X * 2) + SCALED_HITBOX_W;
        } else {
            return 0;
        }
    }

    public int flipW() {
        if (walkDir == RIGHT) {
            return -1;
        } else {
            return 1;
        }
    }

    public int drawX() {
        return (int) (hitBox.x - SCALED_OFFSET_X + flipX());
    }

    public int drawY() {
        return (int) (hitBox.y - SCALED_OFFSET_Y);
    }
}
```

## File: src/utilitytools/HelpMethods.java
```java
package utilitytools;

import java.awt.geom.Rectangle2D;
import main.GameCore;

public class HelpMethods {

    public static boolean isSolidTile(int tileID) {
        return tileID != -1;
    }

    public static boolean isSolid(float x, float y, int[][] lvlData) {
        int xIndex = (int) (x / GameCore.TILES_SIZE);
        int yIndex = (int) (y / GameCore.TILES_SIZE);

        if (yIndex < 0 || yIndex >= lvlData.length ||
            xIndex < 0 || xIndex >= lvlData[yIndex].length) {
            return true;
        }

        return isSolidTile(lvlData[yIndex][xIndex]);
    }

    public static boolean isSolidTile(int xTile, int yTile, int[][] lvlData) {
        if (yTile < 0 || yTile >= lvlData.length ||
            xTile < 0 || xTile >= lvlData[yTile].length) {
            return true;
        }
        return isSolidTile(lvlData[yTile][xTile]);
    }

    public static boolean canMoveHere(float x, float y, float width, float height, int[][] tilesData) {
        float leftX = x;
        float rightX = x + width - 0.01f;
        float topY = y;
        float bottomY = y + height - 0.01f;

        int leftCol = (int) (leftX / GameCore.TILES_SIZE);
        int rightCol = (int) (rightX / GameCore.TILES_SIZE);
        int topRow = (int) (topY / GameCore.TILES_SIZE);
        int bottomRow = (int) (bottomY / GameCore.TILES_SIZE);

        for (int row = topRow; row <= bottomRow; row++) {
            for (int col = leftCol; col <= rightCol; col++) {
                if (row < 0 || row >= tilesData.length ||
                    col < 0 || col >= tilesData[row].length) {
                    return false;
                }
                int tileID = tilesData[row][col];
                if (isSolidTile(tileID)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static float GetEntityPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        if (xSpeed > 0) {


            int currentTileRight = (int) ((hitBox.x + hitBox.width) / GameCore.TILES_SIZE);
            int tileRightEdge = (currentTileRight + 1) * GameCore.TILES_SIZE;


            return tileRightEdge - hitBox.width - 0.01f;
        } else {

            int currentTileLeft = (int) (hitBox.x / GameCore.TILES_SIZE);
            return currentTileLeft * GameCore.TILES_SIZE;
        }
    }














    public static float GetEntityPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
        float yAfterMove = hitBox.y + airSpeed;
        if (airSpeed > 0) {

            int currentTileBottom = (int) ((yAfterMove + hitBox.height) / GameCore.TILES_SIZE);
            int tileYPos = currentTileBottom * GameCore.TILES_SIZE;
            return tileYPos - hitBox.height - 0.01f;
        } else {

            int currentTileTop = (int) (yAfterMove / GameCore.TILES_SIZE);
            int tileYPos = currentTileTop * GameCore.TILES_SIZE;
            return tileYPos + GameCore.TILES_SIZE;
        }
    }
















    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {


        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData) &&
            !isSolid(hitbox.x + hitbox.width - 0.01f, hitbox.y + hitbox.height + 1, lvlData)) {
            return false;
        }
        return true;
    }










    public static boolean IsSightClear(int[][] tilesData,
                                       Rectangle2D.Float firstHitBox,
                                       Rectangle2D.Float secondHitBox,
                                       int tileY) {
        int firstXTile = (int) (firstHitBox.x / GameCore.TILES_SIZE);
        int secondXTile = (int) (secondHitBox.x / GameCore.TILES_SIZE);

        int leftXTile = Math.min(firstXTile, secondXTile);
        int rightXTile = Math.max(firstXTile, secondXTile);


        for (int x = leftXTile + 1; x < rightXTile; x++) {
            if (isSolidTile(x, tileY, tilesData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFloor(Rectangle2D.Float hitBox, float xSpeed, int[][] tilesData) {
        if (xSpeed > 0) {

            return isSolid(hitBox.x + hitBox.width + xSpeed, hitBox.y + hitBox.height + 1, tilesData);
        } else {

            return isSolid(hitBox.x + xSpeed, hitBox.y + hitBox.height + 1, tilesData);
        }
    }
}
```

## File: src/entity/BlueGolem.java
```java
package entity;

import static utilitytools.Konstanta.Directions.*;
import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.PlayStates;
import main.GameCore;

public class BlueGolem extends Boss {

    private Rectangle2D.Float attackBox;
    private int attackCooldown = 0;
    private boolean isActive = false;
    private boolean hp50Triggered = false;


    private float golemScale = 1.8f;

    private int scaledWidth = (int) (BLUE_GOLEM_WIDTH * golemScale);
    private int scaledHeight = (int) (BLUE_GOLEM_HEIGHT * golemScale);
    private int scaledHitboxW = (int) (BLUE_GOLEM_HITBOX_WIDTH * golemScale);
    private int scaledHitboxH = (int) (BLUE_GOLEM_HITBOX_HEIGHT * golemScale);
    private int scaledOffsetX = (int) (BLUE_GOLEM_DRAWOFFSET_X * golemScale);
    private int scaledOffsetY = (int) (BLUE_GOLEM_DRAWOFFSET_Y * golemScale);

    public BlueGolem(float x, float y) {
        super(x, y, BLUE_GOLEM_WIDTH, BLUE_GOLEM_HEIGHT, BLUE_GOLEM);

        float adjustedY = y - (scaledHitboxH - BLUE_GOLEM_HITBOX_HEIGHT);
        initHitBox(x, adjustedY, scaledHitboxW, scaledHitboxH);
        initAttackBox();

        walkSpeed = 0.55f * GameCore.SCALE;
        attackDistance = (int) (GameCore.TILES_SIZE * 1.5f);
        aniSpeed = 5;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * GameCore.SCALE * golemScale), (int) (50 * GameCore.SCALE * golemScale));
    }

    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void updateAttackBox() {
        attackBox.y = hitBox.y + (5 * GameCore.SCALE);
        if (walkDir == LEFT) {
            attackBox.x = hitBox.x - attackBox.width;
        } else {
            attackBox.x = hitBox.x + hitBox.width;
        }
    }

    private void updateBehaviour(int[][] tilesData, Player player) {
        checkOnFloor(tilesData);
        if (inAir) {
            updateInAir(tilesData);
            return;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        switch(enemyState) {
        case IDLE:
            if (isPlayerCloseEnoughForAttack(player) && attackCooldown > 0 && !isActive) {
                turnToPlayer(player);
            } else {
                newState(WALK);
            }
            break;
        case WALK:
            if (isPlayerInRange(player)) {
                turnToPlayer(player);
            }
            if (isPlayerCloseEnoughForAttack(player)) {
                if (attackCooldown <= 0) {
                    newState(ATTACK);
                    attackCooldown = 120;
                } else {
                    newState(IDLE);
                }
            } else {
                move(tilesData);
            }
            break;
        case ATTACK:
            if (aniIndex == 0) {
                attackChecked = false;
            }
            if (aniIndex == 6 && !attackChecked) {
                checkHitEnemy(attackBox, player);
                attackChecked = true;
            }

            if (aniIndex >= GetSpriteAmount(BLUE_GOLEM, enemyState) - 1) {
                newState(IDLE);
            }
            break;
        case HURT:
            break;
        }
    }

    @Override
    public void update(int[][] tilesData, Player player) {
        updateEffects(tilesData);
        updateBehaviour(tilesData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    @Override
    public void draw(Graphics g, int xLvlOffset, BufferedImage[][] spriteAtlas) {
        if (enemyState < 0 || enemyState >= spriteAtlas.length) return;
        BufferedImage[] frames = spriteAtlas[enemyState];

        if (aniIndex < 0) {
            aniIndex = 0;
        }
        if (aniIndex >= frames.length) {
            aniIndex = frames.length - 1;
        }


        BufferedImage frame = frames[aniIndex];
        if (frame != null) {
            g.drawImage(frame, drawX() - xLvlOffset, drawY(), scaledWidth * flipW(), scaledHeight, null);
        }



    }

    @Override
    public int getExpReward() { return 150; }

    @Override
    protected void handleHurtEffects(PlayStates playStates) {
        float hpPercentage = ((float) currentHealth / maxHealth) * 100f;
        if (hpPercentage <= 50 && !hp50Triggered) {
            hp50Triggered = true;
            playStates.triggerHeavyHit(15, 20, 10);
        }
    }

    @Override
    protected void handleDeathEffects(PlayStates playStates) {
        playStates.triggerHeavyHit(50, 150, 10);
    }

    private boolean isPlayerCloseEnoughForAttack(Player player) {
        float xDistance = (float) Math.abs(player.getHitBox().getCenterX() - hitBox.getCenterX());
        float yDistance = (float) Math.abs(player.getHitBox().getCenterY() - hitBox.getCenterY());
        return xDistance <= attackDistance && yDistance <= GameCore.TILES_SIZE * 2.5f;
    }

    public int flipX() {
        if (walkDir == LEFT) {
            return (scaledOffsetX * 2) + scaledHitboxW;
        }
        return 0;
    }

    public int flipW() {
        if (walkDir == LEFT) {
            return -1;
        }
        return 1;
    }

    public int drawX() {
        return (int) (hitBox.x - scaledOffsetX + flipX());
    }

    public int drawY() {
        return (int) (hitBox.y - scaledOffsetY);
    }
}
```

## File: src/ui/GameOverOverlay.java
```java
package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gameStates.GameStates;
import gameStates.PlayStates;
import main.GameCore;

public class GameOverOverlay {

	private PlayStates playStates;

	public GameOverOverlay(PlayStates playStates) {
		this.playStates = playStates;
	}

	public void draw(Graphics g) {

		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);


		g.setColor(Color.WHITE);
		g.drawString("GAME OVER", GameCore.GAME_WIDTH / 2 - 35, (int)(150 * GameCore.SCALE));
		g.drawString("Tekan ESC untuk kembali ke Menu Utama", GameCore.GAME_WIDTH / 2 - 110, (int)(200 * GameCore.SCALE));
		g.drawString("Tekan R untuk Main Lagi (Restart)", GameCore.GAME_WIDTH / 2 - 95, (int)(250 * GameCore.SCALE));
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playStates.resetAll(200, 200);
			GameStates.state = GameStates.MENU;

			playStates.getGameCore().getAudioPlayer().playSong(audio.AudioPlayer.MENU_1);

		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			playStates.resetAll(200, 200);
		}
	}
}
```

## File: resources/map_tutorial_fix.txt
```
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,229,230,230,231,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,256,257,257,258,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,204,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,204,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,285,205,-1,-1,-1,-1,-1,-1,-1,-1,5,277,277,6,-1,-1,-1,285,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,285,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,285,205,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,-1,-1,-1,-1,-1,-1,-1,32,304,304,33,-1,-1,-1,312,313,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,277,277,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,329,205,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,801,-1,-1,329,205
-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,32,304,304,33,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,312,313
-1,-1,-1,-1,-1,-1,910,501,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,277,277,6,-1,-1,5,277,277,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,5,277,277,6,-1,-1,5,277,277,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
2,335,336,337,338,339,2,3,4,-1,-1,-1,-1,-1,-1,-1,-1,32,304,304,33,-1,-1,32,304,304,33,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,800,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,32,304,304,33,-1,-1,32,304,304,33,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
28,362,363,364,365,366,29,30,232,2,337,338,339,340,3,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,335,336,337,338,339,340,341,342,343,342,343,2,337,338,339,340,3,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,335,336,337,338,339,340,341,3,3,3,3,3,4,-1,-1,-1,-1,0,335,336,337,338,339,340,341,342,343,342,342,343,342,3,3,3,3,3,4
55,56,56,56,56,56,56,371,29,29,364,365,366,367,30,31,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,27,28,363,364,365,366,367,368,369,370,369,370,29,364,365,366,367,30,31,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,27,28,363,364,365,366,367,368,369,370,369,367,30,31,-1,-1,-1,-1,27,28,363,364,365,366,367,368,369,370,369,369,370,369,369,370,369,367,30,31
55,56,56,56,56,56,56,56,56,56,56,56,56,56,57,58,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,81,55,56,56,56,56,56,56,56,56,56,56,56,56,56,56,56,57,58,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,81,55,56,56,56,56,56,56,56,56,56,56,57,58,-1,-1,-1,-1,81,55,56,56,56,56,56,56,56,56,56,56,56,56,56,56,56,56,57,58
82,83,83,83,83,83,83,83,83,83,83,83,83,83,84,85,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,54,82,83,83,83,83,83,83,83,83,83,83,83,83,83,83,83,84,85,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,54,82,83,83,83,83,83,83,83,83,83,83,84,85,-1,-1,-1,-1,54,82,83,83,83,83,83,83,83,83,83,83,83,83,83,83,83,83,84,85
```

## File: src/ui/InventoryOverlay.java
```java
package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;

public class InventoryOverlay {

    private PlayStates playing;
    private BufferedImage bgInventory;

    private int panelX, panelY, panelW, panelH;


    private boolean showConfirmPopup = false;
    private int selectedItemIndex = -1;
    private boolean isEquipSlotSelected = false;
    private int popupX, popupY;
    private java.awt.Rectangle btnGunakan, btnBatal;

    public InventoryOverlay(PlayStates playing) {
        this.playing = playing;
        loadImages();
        initLayout();
    }

    private void loadImages() {
        bgInventory = LoadSave.GetSpriteAtlas(LoadSave.INVENTORY_BG);
    }

    private void initLayout() {
        float customScale = 0.80f;
        panelW = (int) (490 * GameCore.SCALE * customScale);
        panelH = (int) (560 * GameCore.SCALE * customScale);
        panelX = GameCore.GAME_WIDTH / 2 - panelW / 2;
        panelY = GameCore.GAME_HEIGHT / 2 - panelH / 2;
    }

    public void update() {}

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);

        if (bgInventory != null) {
            g2.drawImage(bgInventory, panelX, panelY, panelW, panelH, null);


            g2.setColor(new Color(230, 230, 230));
            float customScale = 0.80f;
            g2.setFont(new Font("Monospaced", Font.BOLD, (int)(14 * GameCore.SCALE * customScale)));

            int textX = panelX + (int)(225 * GameCore.SCALE * customScale);
            int startY = panelY + (int)(125 * GameCore.SCALE * customScale);
            int gapY = (int)(18 * GameCore.SCALE * customScale);

            entity.Player p = playing.getPlayer();
            java.awt.FontMetrics fm = g2.getFontMetrics();

            g2.drawString(String.valueOf(p.getLevel()), textX - fm.stringWidth(String.valueOf(p.getLevel())), startY);
            g2.drawString(p.getExp() + " / " + p.getMaxExp(), textX - fm.stringWidth(p.getExp() + " / " + p.getMaxExp()), startY + gapY);
            g2.drawString(p.getCurrentHealth() + " / " + p.getMaxHealth(), textX - fm.stringWidth(p.getCurrentHealth() + " / " + p.getMaxHealth()), startY + gapY * 2);
            g2.drawString(p.getCurrentMana() + " / " + p.getMaxMana(), textX - fm.stringWidth(p.getCurrentMana() + " / " + p.getMaxMana()), startY + gapY * 3);
            g2.drawString(String.valueOf(p.getTotalDefense()), textX - fm.stringWidth(String.valueOf(p.getTotalDefense())), startY + gapY * 4);
            g2.drawString(String.format("%.1f", p.getDps()), textX - fm.stringWidth(String.format("%.1f", p.getDps())), startY + gapY * 5);

            objects.ObjectManager objManager = playing.getObjectManager();




            int eqCols = 3;
            int eqGridX = panelX + (int)(272 * GameCore.SCALE * customScale);
            int eqGridY = panelY + (int)(136 * GameCore.SCALE * customScale);
            int eqDistX = (int)(33 * GameCore.SCALE * customScale);
            int eqDistY = (int)(35 * GameCore.SCALE * customScale);
            int eqItemSize = (int)(22 * GameCore.SCALE * customScale);

            int offEqX = (eqDistX - eqItemSize) / 2;
            int offEqY = (eqDistY - eqItemSize) / 2;

            int[] equippedItems = {
                p.getEquippedHelmet(), p.getEquippedArmor(), p.getEquippedShoes(),
                p.getEquippedAcc1(), p.getEquippedAcc2(), p.getEquippedGloves()
            };

            for (int i = 0; i < 6; i++) {
                if (equippedItems[i] != -1) {
                    BufferedImage eqImg = objManager.getItemImg(equippedItems[i]);
                    if (eqImg != null) {
                        int drawX = eqGridX + ((i % eqCols) * eqDistX) + offEqX;
                        int drawY = eqGridY + ((i / eqCols) * eqDistY) + offEqY;
                        g2.drawImage(eqImg, drawX, drawY, eqItemSize, eqItemSize, null);
                    }
                }
            }




            java.util.ArrayList<Integer> inv = p.inventory;
            int cols = 6;
            int startGridX = panelX + (int)(262 * GameCore.SCALE * customScale);
            int startGridY = panelY + (int)(242 * GameCore.SCALE * customScale);
            int slotDistanceX = (int)(33 * GameCore.SCALE * customScale);
            int slotDistanceY = (int)(35 * GameCore.SCALE * customScale);
            int itemSize = (int)(22 * GameCore.SCALE * customScale);

            int offsetX = (slotDistanceX - itemSize) / 2;
            int offsetY = (slotDistanceY - itemSize) / 2;

            for (int i = 0; i < inv.size(); i++) {
                BufferedImage itemImg = objManager.getItemImg(inv.get(i));
                if (itemImg != null) {
                    int drawX = startGridX + ((i % cols) * slotDistanceX) + offsetX;
                    int drawY = startGridY + ((i / cols) * slotDistanceY) + offsetY;
                    g2.drawImage(itemImg, drawX, drawY, itemSize, itemSize, null);
                }
            }




            if (showConfirmPopup) {
                int popW = (int)(65 * GameCore.SCALE * customScale);
                int popH = (int)(40 * GameCore.SCALE * customScale);
                int btnH = popH / 2;

                g2.setColor(new Color(50, 30, 20, 240));
                g2.fillRect(popupX, popupY, popW, popH);
                g2.setColor(new Color(240, 190, 90));
                g2.setStroke(new java.awt.BasicStroke(2));
                g2.drawRect(popupX, popupY, popW, popH);
                g2.setColor(new Color(150, 100, 50));
                g2.drawLine(popupX, popupY + btnH, popupX + popW, popupY + btnH);

                btnGunakan = new java.awt.Rectangle(popupX, popupY, popW, btnH);
                btnBatal = new java.awt.Rectangle(popupX, popupY + btnH, popW, btnH);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Monospaced", Font.BOLD, (int)(11 * GameCore.SCALE * customScale)));
                java.awt.FontMetrics fmPop = g2.getFontMetrics();

                String txt1 = "Gunakan";
                if (isEquipSlotSelected) {
                    txt1 = "Lepaskan";
                } else {
                    int typeId = p.inventory.get(selectedItemIndex);
                    if (typeId >= 10 && typeId <= 15) {
                        txt1 = "Pakai";
                    }
                }
                String txt2 = "Batal";

                g2.drawString(txt1, popupX + (popW - fmPop.stringWidth(txt1)) / 2, popupY + (btnH + fmPop.getAscent()) / 2 - 2);
                g2.drawString(txt2, popupX + (popW - fmPop.stringWidth(txt2)) / 2, popupY + btnH + (btnH + fmPop.getAscent()) / 2 - 2);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();


        if (showConfirmPopup) {
            if (btnGunakan != null && btnGunakan.contains(mx, my)) {
                if (isEquipSlotSelected) {
                    playing.getPlayer().unequipItem(selectedItemIndex);
                } else {
                    int itemType = playing.getPlayer().inventory.get(selectedItemIndex);
                    if (itemType >= 0 && itemType <= 5) {
                        playing.getPlayer().useItem(selectedItemIndex);

                        playing.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.POTION);
                    } else if (itemType >= 10 && itemType <= 15) {
                        String tipePart = "";
                        if (itemType == 10) tipePart = "head";
                        else if (itemType == 11) tipePart = "body";
                        else if (itemType == 12) tipePart = "shoes";
                        else if (itemType == 13) tipePart = "accessory1";
                        else if (itemType == 14) tipePart = "accessory2";
                        else if (itemType == 15) tipePart = "hands";
                        playing.getPlayer().equipItem(selectedItemIndex, tipePart);
                    }
                }
                playing.getGameCore().getAudioPlayer().playEffect(audio.AudioPlayer.JUMP);
                showConfirmPopup = false;
            } else if (btnBatal != null && btnBatal.contains(mx, my)) {
                showConfirmPopup = false;
            } else {
                showConfirmPopup = false;
            }
            return;
        }

        float customScale = 0.80f;
        int slotDistX = (int)(33 * GameCore.SCALE * customScale);
        int slotDistY = (int)(35 * GameCore.SCALE * customScale);
        int itemSize = (int)(22 * GameCore.SCALE * customScale);
        int offsetX = (slotDistX - itemSize) / 2;
        int offsetY = (slotDistY - itemSize) / 2;
        entity.Player p = playing.getPlayer();


        int eqGridX = panelX + (int)(272 * GameCore.SCALE * customScale);
        int eqGridY = panelY + (int)(136 * GameCore.SCALE * customScale);
        int[] equippedItems = { p.getEquippedHelmet(), p.getEquippedArmor(), p.getEquippedShoes(), p.getEquippedAcc1(), p.getEquippedAcc2(), p.getEquippedGloves() };

        for (int i = 0; i < 6; i++) {
            int drawX = eqGridX + ((i % 3) * slotDistX) + offsetX;
            int drawY = eqGridY + ((i / 3) * slotDistY) + offsetY;
            java.awt.Rectangle slot = new java.awt.Rectangle(drawX, drawY, itemSize, itemSize);

            if (slot.contains(mx, my) && equippedItems[i] != -1) {
                selectedItemIndex = i;
                isEquipSlotSelected = true;
                showConfirmPopup = true;
                popupX = drawX + (itemSize / 2);
                popupY = drawY + itemSize;
                return;
            }
        }


        int startGridX = panelX + (int)(262 * GameCore.SCALE * customScale);
        int startGridY = panelY + (int)(242 * GameCore.SCALE * customScale);

        for (int i = 0; i < 24; i++) {
            int drawX = startGridX + ((i % 6) * slotDistX) + offsetX;
            int drawY = startGridY + ((i / 6) * slotDistY) + offsetY;
            java.awt.Rectangle slot = new java.awt.Rectangle(drawX, drawY, itemSize, itemSize);

            if (slot.contains(mx, my) && i < p.inventory.size()) {
                selectedItemIndex = i;
                isEquipSlotSelected = false;
                showConfirmPopup = true;
                popupX = drawX + (itemSize / 2);
                popupY = drawY + itemSize;
                return;
            }
        }
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void resetMenu() {}
}
```

## File: src/world/WorldManager.java
```java
package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GameCore;
import utilitytools.LoadSave;

public class WorldManager {

    private GameCore gc;
    private BufferedImage[] mapSprite;
    private ArrayList<World> worlds;
    private int worldIndex = 0;

    public WorldManager(GameCore gc) {
        this.gc = gc;

        importOutsideSprites(LoadSave.WORLD_SPRITE);
        worlds = new ArrayList<>();
        buildAllWorlds();
    }

    private void buildAllWorlds() {
        worlds.add(new World(LoadSave.GetTilesData("/map_tutorial_fix.txt")));
    }


    public void loadSpecificWorld(String filePath, String tilesetName) {
        worlds.clear();
        worlds.add(new World(LoadSave.GetTilesData(filePath)));
        worldIndex = 0;
        importOutsideSprites(tilesetName);
    }

    private void importOutsideSprites(String tilesetName) {
        BufferedImage image = LoadSave.GetSpriteAtlas(tilesetName);

        int tileWidth = 16;
        int tileHeight = 16;

        int cols = image.getWidth() / tileWidth;
        int rows = image.getHeight() / tileHeight;

        mapSprite = new BufferedImage[cols * rows];

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                int index = i + (j * cols);
                mapSprite[index] = image.getSubimage(i * tileWidth, j * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        World currentMap = worlds.get(worldIndex);
        int[][] worldData = currentMap.getWorldData();
        int mapHeight = worldData.length;
        int mapWidth = worldData[0].length;
        int tileSize = GameCore.TILES_SIZE;

        int startX = Math.max(0, xLvlOffset / tileSize);
        int endX = Math.min(mapWidth, startX + GameCore.TILES_IN_WIDTH + 2);

        for (int j = 0; j < mapHeight; j++) {
            for (int i = startX; i < endX; i++) {
                int tiledValue = worldData[j][i];

                if (tiledValue != -1) {
                    if (isValidTile(tiledValue)) {
                        int xPos = (int) (i * tileSize - xLvlOffset);
                        int yPos = j * tileSize;

                        g.drawImage(mapSprite[tiledValue], xPos, yPos, tileSize, tileSize, null);
                    }
                }
            }
        }
    }

    private boolean isValidTile(int index) {
        return index >= 0 && index < mapSprite.length;
    }

    public void update() {
    }

    public void loadNextWorld() {
        worldIndex++;
        if (worldIndex >= worlds.size()) {
            worldIndex = 0;
            System.out.println("Game Tamat! Kembali ke Menu.");
            gameStates.GameStates.state = gameStates.GameStates.MENU;
        }
    }

    public World getCurrentMap() {
        return worlds.get(worldIndex);
    }

    public int getWorldIndex() { return worldIndex; }
    public void setWorldIndex(int index) { this.worldIndex = index; }
}
```

## File: src/gameStates/MainMenu.java
```java
package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.GameCore;
import ui.SelectLevelOverlay;
import utilitytools.LoadSave;

public class MainMenu extends States implements StateMethods {
    private SelectLevelOverlay selectLevelOverlay;
    private boolean isLevelSelectActive = false;

    private BufferedImage backgroundImage;
    private BufferedImage panelImage;

    private int panelX, panelY, panelWidth, panelHeight;
    private Rectangle startBtn, continueBtn, optionsBtn, exitBtn;

    private boolean startHover, startPressed;
    private boolean continueHover, continuePressed;
    private boolean optionsHover, optionsPressed;
    private boolean exitHover, exitPressed;

    public MainMenu(GameCore gc) {
        super(gc);
        loadAsetUI();
        initLayoutMenu();
        selectLevelOverlay = new SelectLevelOverlay(this);
    }

    private void loadAsetUI() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        panelImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_PANEL);
    }

    private void initLayoutMenu() {
        panelWidth = (int) (500 * GameCore.SCALE);
        panelHeight = (int) (300 * GameCore.SCALE);
        panelX = GameCore.GAME_WIDTH / 2 - panelWidth / 2;
        panelY = (int) (160 * GameCore.SCALE);

        int btnWidth = (int)(panelWidth * 0.30);
        int btnHeight = (int) (30 * GameCore.SCALE);
        int btnX = GameCore.GAME_WIDTH / 2 - btnWidth / 2;
        int startY = panelY + (int)(105 * GameCore.SCALE);
        int spacing = (int)(31 * GameCore.SCALE);

        startBtn = new Rectangle(btnX, startY, btnWidth, btnHeight);
        continueBtn = new Rectangle(btnX, startY + spacing, btnWidth, btnHeight);
        optionsBtn = new Rectangle(btnX, startY + (spacing * 2), btnWidth, btnHeight);
        exitBtn = new Rectangle(btnX, startY + (spacing * 3), btnWidth, btnHeight);
    }

    @Override
    public void update() {
        if (isLevelSelectActive) {
            selectLevelOverlay.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
        }

        if (panelImage != null) {
            g.drawImage(panelImage, panelX, panelY, panelWidth, panelHeight, null);
        } else {
            g.setColor(new Color(60, 40, 20));
            g.fillRect(panelX, panelY, panelWidth, panelHeight);
        }

        drawButtonHighlight(g, startBtn, startHover, startPressed);
        drawButtonHighlight(g, continueBtn, continueHover, continuePressed);
        drawButtonHighlight(g, optionsBtn, optionsHover, optionsPressed);
        drawButtonHighlight(g, exitBtn, exitHover, exitPressed);

        if (isLevelSelectActive) {
            selectLevelOverlay.draw(g);
        }
    }

    private void drawButtonHighlight(Graphics g, Rectangle bounds, boolean isHover, boolean isPressed) {
        if (isPressed) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        } else if (isHover) {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isLevelSelectActive) {
            selectLevelOverlay.mouseMoved(e);
        } else {
            startHover = startBtn.contains(e.getX(), e.getY());
            continueHover = continueBtn.contains(e.getX(), e.getY());
            optionsHover = optionsBtn.contains(e.getX(), e.getY());
            exitHover = exitBtn.contains(e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isLevelSelectActive) {
            selectLevelOverlay.mousePressed(e);
        } else {
            if (startHover) startPressed = true;
            if (continueHover) continuePressed = true;
            if (optionsHover) optionsPressed = true;
            if (exitHover) exitPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isLevelSelectActive) {
            selectLevelOverlay.mouseReleased(e);
        } else {
            if (startHover && startPressed) {
                isLevelSelectActive = true;
            } else if (continueHover && continuePressed) {
                if (utilitytools.SaveLoadManager.loadGame(gc)) {
                    GameStates.state = GameStates.PLAYING;
                    setLevelSelectActive(false);
                } else {
                    System.out.println("Gagal memuat save atau file tidak ditemukan!");
                }
            } else if (optionsHover && optionsPressed) {
            } else if (optionsHover && optionsPressed) {
                GameStates.state = GameStates.OPTIONS;
            } else if (exitHover && exitPressed) {
                System.exit(0);
            }
            resetButtons();
        }
    }

    private void resetButtons() {
        startHover = continueHover = optionsHover = exitHover = false;
        startPressed = continuePressed = optionsPressed = exitPressed = false;
    }

    public void setLevelSelectActive(boolean active) {
        this.isLevelSelectActive = active;
    }

    public void startSelectedLevel(String mapFilePath, String tilesetName) {
        gc.getPlay().loadSelectedLevel(mapFilePath, tilesetName);
        GameStates.state = GameStates.PLAYING;
        setLevelSelectActive(false);
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_F11) gc.getGameFrame().toggleFullScreen(); }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void triggerHeavyHit(int freezeFrames, int shakeFrames, int intensity) {}
}
```

## File: src/audio/AudioPlayer.java
```java
package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

	public static int MENU_1 = 0;
	public static int LEVEL_1 = 1;
	public static int LEVEL_2 = 2;

	public static int DIE = 0;
	public static int JUMP = 1;
	public static int GAMEOVER = 2;
	public static int LVL_COMPLETED = 3;
	public static int ATTACK_ONE = 4;
	public static int ATTACK_TWO = 5;
	public static int ATTACK_THREE = 6;
	public static int POTION = 7;
	public static int SLASH_SOUND = 8;

	private Clip[] songs, effects;
	private int currentSongId;
	private float volume = 0.8f;
	private boolean songMute, effectMute;
	private Random rand = new Random();



	public static int ATTACK_SOUND = 4;
	public static int POTION_SOUND = 7;


	public AudioPlayer() {
		loadSongs();
		loadEffects();
		playSong(MENU_1);
	}

	private void loadSongs() {
		String[] names = { "Theme", "Theme", "Theme" };
		songs = new Clip[names.length];
		for (int i = 0; i < songs.length; i++) {
			songs[i] = getClip(names[i]);
		}
	}

	private void loadEffects() {
        String[] names = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3", "potion", "slash" };
        effects = new Clip[names.length];

        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(names[i]);
        }
        updateEffectsVolume();
	}

	private Clip getClip(String name) {
		String[] paths = {
			"/" + name + ".wav",
			"/audio/" + name + ".wav",
			"/resources/audio/" + name + ".wav"
		};

		for (String path : paths) {
			URL url = getClass().getResource(path);
			if (url != null) {
				try (AudioInputStream audio = AudioSystem.getAudioInputStream(url)) {
					Clip c = AudioSystem.getClip();
					c.open(audio);
					return c;
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
		updateEffectsVolume();
	}

	public void stopSong() {
		if (songs != null && songs[currentSongId] != null && songs[currentSongId].isActive()) {
			songs[currentSongId].stop();
		}
	}

	public void setLevelSong(int lvlIndex) {
		if (lvlIndex % 2 == 0) {
			playSong(LEVEL_1);
		} else {
			playSong(LEVEL_2);
		}
	}

	public void lvlCompleted() {
		stopSong();
		playEffect(LVL_COMPLETED);
	}

	public void playAttackSound() {
		int start = ATTACK_ONE + rand.nextInt(3);
		playEffect(start);
	}

	public void playEffect(int effect) {
		if (effects != null && effect >= 0 && effect < effects.length && effects[effect] != null) {
			effects[effect].setMicrosecondPosition(0);
			effects[effect].start();
		}
	}

	public void playSong(int song) {
		stopSong();
		currentSongId = song;
		updateSongVolume();

		if (songs != null && currentSongId >= 0 && currentSongId < songs.length && songs[currentSongId] != null) {
			songs[currentSongId].setMicrosecondPosition(0);
			songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	public void toggleSongMute() {
		this.songMute = !songMute;
		if (songs != null) {
			for (Clip c : songs) {
				if (c != null) {
					BooleanControl control = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
					control.setValue(songMute);
				}
			}
		}
	}

	public void toggleEffectMute() {
		this.effectMute = !effectMute;
		if (effects != null) {
			for (Clip c : effects) {
				if (c != null) {
					BooleanControl control = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
					control.setValue(effectMute);
				}
			}
		}
		if (!effectMute) {
			playEffect(JUMP);
		}
	}

	private void updateSongVolume() {
		if (songs != null && currentSongId >= 0 && currentSongId < songs.length && songs[currentSongId] != null) {
			FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);


			float safeVolume = Math.max(0.0001f, volume);


			float gain = 20f * (float) Math.log10(safeVolume);


			gain = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), gain));

			gainControl.setValue(gain);
		}
	}

	private void updateEffectsVolume() {
		if (effects != null) {
			for (Clip c : effects) {
				if (c != null) {
					FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);


					float safeVolume = Math.max(0.0001f, volume);
					float gain = 20f * (float) Math.log10(safeVolume);
					gain = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), gain));

					gainControl.setValue(gain);
				}
			}
		}
	}
}
```

## File: src/entity/DemonBoss.java
```java
package entity;

import static utilitytools.Konstanta.Directions.*;
import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.PlayStates;
import main.GameCore;

public class DemonBoss extends Boss {

	private Rectangle2D.Float attackBox;
	private int attackCooldown = 0;
	private boolean Active = false;

	private boolean hp70Triggered = false;
    private boolean hp40Triggered = false;
    private boolean hp10Triggered = false;

    public DemonBoss(float x, float y) {
        super(x, y, DEMON_BOSS_WIDTH, DEMON_BOSS_HEIGHT, DEMON_BOSS);
        initHitBox(x, y, DEMON_BOSS_HITBOX_WIDTH, DEMON_BOSS_HITBOX_HEIGHT);
        initAttackBox();

        walkSpeed = 0.73f * GameCore.SCALE;
        attackDistance = (int) (GameCore.TILES_SIZE * 2f);

        aniSpeed = 4;
    }

    public boolean checkHpThresholdEffect() {
        float hpPercentage = ((float) currentHealth / maxHealth) * 100f;

        if (hpPercentage <= 70 && !hp70Triggered) {
            hp70Triggered = true;
            return true;
        }
        if (hpPercentage <= 40 && !hp40Triggered) {
            hp40Triggered = true;
            return true;
        }
        if (hpPercentage <= 10 && !hp10Triggered) {
            hp10Triggered = true;
            return true;
        }
        return false;
    }

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (70 * GameCore.SCALE), (int) (55 * GameCore.SCALE));
	}

	private void updateAttackBox() {
		attackBox.y = hitBox.y + (10 * GameCore.SCALE);
		if (walkDir == LEFT) {
			attackBox.x = hitBox.x - attackBox.width;
		} else {
			attackBox.x = hitBox.x + hitBox.width;
		}
	}

	@Override
	public void hurt(int value, int kbDir, boolean applyKnockback) {
	    super.hurt(value, kbDir, applyKnockback);
	    checkPhaseTransition();
	}

	private void updateBehaviour(int[][] tilesData, Player player) {
		checkOnFloor(tilesData);
		if (inAir) {
			updateInAir(tilesData);
			return;
		}

		if (attackCooldown > 0) {
			attackCooldown--;
		}

		switch(enemyState) {
		case IDLE:
			if (isPlayerCloseEnoughForAttack(player) && attackCooldown > 0 && !Active) {
				turnToPlayer(player);
			} else {
				newState(WALK);
			}
			break;
		case WALK:
			if (isPlayerInRange(player)) {
				turnToPlayer(player);
			}
			if (isPlayerCloseEnoughForAttack(player)) {
				if (attackCooldown <= 0) {
					newState(ATTACK);
					attackCooldown = 90;
				} else {
					newState(IDLE);
				}
			} else {
				move(tilesData);
			}
			break;
		case ATTACK:
			if (aniIndex == 0) {
				attackChecked = false;
			}
			if (aniIndex == 10 && !attackChecked) {
				checkHitEnemy(attackBox, player);
			}
			break;
		case HURT:
			break;
		}
	}

	@Override
	public void update(int[][] tilesData, Player player) {
	    updateEffects(tilesData);
	    updateBehaviour(tilesData, player);
	    updateAnimationTick();
	    updateAttackBox();
	}

	@Override
	public void draw(Graphics g, int xLvlOffset, BufferedImage[][] spriteAtlas) {
	    BufferedImage frame = spriteAtlas[enemyState][aniIndex];
	    if (frame != null) {
	        g.drawImage(frame, drawX(), drawY(), DEMON_BOSS_WIDTH * flipW(), DEMON_BOSS_HEIGHT, null);
	    }
	}

	@Override
	public int getExpReward() { return 100; }

	@Override
	protected void handleHurtEffects(PlayStates playStates) {
	    if (checkHpThresholdEffect()) {
	        playStates.triggerHeavyHit(20, 25, 12);
	    }
	}

	@Override
	protected void handleDeathEffects(PlayStates playStates) {
	    playStates.triggerHeavyHit(45, 120, 8);
	}

	private boolean isPlayerCloseEnoughForAttack(Player player) {
		float xDistance = (float) Math.abs(player.getHitBox().getCenterX() - hitBox.getCenterX());
		float yDistance = (float) Math.abs(player.getHitBox().getCenterY() - hitBox.getCenterY());
		return xDistance <= attackDistance && yDistance <= GameCore.TILES_SIZE * 2.5f;
	}

	public int flipX() {
	    if (walkDir == RIGHT) {
	        return DEMON_BOSS_WIDTH;
	    }
	    return 0;
	}

	public int flipW() {
	    if (walkDir == RIGHT ) {
	        return -1;
	    }
	    return 1;
	}
	public int drawX() {
		return (int) (hitBox.x - DEMON_BOSS_DRAWOFFSET_X + flipX());
	}

	public int drawY() {
		return (int) (hitBox.y - DEMON_BOSS_DRAWOFFSET_Y);
	}

	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y,
				   (int) attackBox.width, (int) attackBox.height);
	}

	public void resetBoss() {
        hp70Triggered = false;
        hp40Triggered = false;
        hp10Triggered = false;
    }
}
```

## File: src/main/GameCore.java
```java
package main;

import audio.AudioPlayer;
import gameStates.GameStates;
import gameStates.MainMenu;
import gameStates.PlayStates;
import gameStates.GameOptions;
import java.awt.Graphics;

public class GameCore implements Runnable {

	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 60;
	private final int UPS_SET = 60;

	private PlayStates play;
	private MainMenu menu;
	private GameOptions gameOptions;
	private AudioPlayer audioPlayer;

	public final static int TILE_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.5f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILE_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	public GameCore() {
		initClasses();

		gamePanel = new GamePanel(this);
		gameFrame = new GameFrame(gamePanel);
		gamePanel.setRequestFocusEnabled(true);
		gamePanel.requestFocus();
		audioPlayer = new audio.AudioPlayer();

		startGameLoop();
	}

	private void initClasses() {
		menu = new MainMenu(this);
		play = new PlayStates(this);
		gameOptions = new GameOptions(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch(GameStates.state) {
			case MENU:
				menu.update();
				break;
			case PLAYING:
				play.update();
				break;
			case OPTIONS:
				gameOptions.update();
				break;
			case QUIT:
				System.exit(0);
				break;
			default:
				break;
		}
	}

	public void render(Graphics g) {
		switch(GameStates.state) {
			case MENU:
				menu.draw(g);
				break;
			case PLAYING:
				play.draw(g);
				break;
			case OPTIONS:
				gameOptions.draw(g);
				break;
			default:
				break;
		}
	}

	@Override
	public void run() {

	    final double TIME_PER_UPDATE = 1000000000.0 / UPS_SET;
	    final double TIME_PER_FRAME = 1000000000.0 / FPS_SET;

	    long previousTime = System.nanoTime();

	    double deltaU = 0;
	    double deltaF = 0;


	    long lastCheck = System.currentTimeMillis();
	    int updates = 0;
	    int frames = 0;

	    while (gameThread != null) {
	        long currentTime = System.nanoTime();

	        deltaU += (currentTime - previousTime) / TIME_PER_UPDATE;
	        deltaF += (currentTime - previousTime) / TIME_PER_FRAME;
	        previousTime = currentTime;

	        while (deltaU >= 1) {
	            update();
	            updates++;
	            deltaU--;
	        }

	        if (deltaF >= 1) {
	            gamePanel.repaint();
	            frames++;
	            deltaF--;
	        }

	        if (System.currentTimeMillis() - lastCheck >= 1000) {
	            lastCheck = System.currentTimeMillis();
	            System.out.println("FPS: " + frames + " | UPS: " + updates);
	            frames = 0;
	            updates = 0;
	        }
	    }
	}

	public void windowFocusLost() {
		if(GameStates.state == GameStates.PLAYING) {
			play.getPlayer().resetDirBooleans();
		}
	}

	public MainMenu getMenu() {
		return menu;
	}

	public PlayStates getPlay() {
		return play;
	}

	public GameOptions getGameOptions() {
		return gameOptions;
	}

	public audio.AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public GameFrame getGameFrame() {
		return gameFrame;
	}
}
```

## File: src/entity/Slime.java
```java
package entity;

import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GameCore;

import static utilitytools.Konstanta.Directions.*;

public class Slime extends Enemy {


	private Rectangle2D.Float AttackBox;
	private int attackBoxOffSetX;

	public Slime(float x, float y) {
		super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
		initHitBox(x, y, SLIME_HITBOX_WIDTH, SLIME_HITBOX_HEIGHT);
		initAttackBox();
	}

	@Override
	public void update(int[][] tilesData, Player player) {
	    updateEffects(tilesData);
	    updateBehaviour(tilesData, player);
	    updateAnimationTick();
	    updateAttackBox();
	}

	@Override
	public void draw(Graphics g, int xLvlOffset, BufferedImage[][] spriteAtlas) {
	    int stateIndex = enemyState;
	    if (stateIndex == MATI) stateIndex = HURT;
	    if (enemyState == MATI && aniTick % 8 < 4) return;

	    g.drawImage(spriteAtlas[stateIndex][aniIndex],
	            (int) (hitBox.x - SLIME_DRAWOFFSET_X + flipX()),
	            (int) (hitBox.y - SLIME_DRAWOFFSET_Y),
	            SLIME_WIDTH * flipW(), SLIME_HEIGHT, null);
	}

	@Override
	public int getExpReward() { return 20; }

	private void initAttackBox() {
		AttackBox = new Rectangle2D.Float(x, y, (int) (30 * GameCore.SCALE), (int) (15 * GameCore.SCALE));
		attackBoxOffSetX = (int) (GameCore.SCALE * 25);
	}

	private void updateAttackBox() {
		AttackBox.y = hitBox.y;
		if (walkDir == LEFT) {
			AttackBox.x = hitBox.x - AttackBox.width - attackBoxOffSetX;
		} else {
			AttackBox.x = hitBox.x + hitBox.width + attackBoxOffSetX;
		}
	}

	private void updateBehaviour(int[][] tilesData, Player player) {
		checkOnFloor(tilesData);
		if(inAir) {
			updateInAir(tilesData);
		}else {
			switch(enemyState) {
			case IDLE:
				newState(WALK);
				break;
			case WALK:
				if(canSeePlayer(tilesData, player)) {
					turnToPlayer(player);
				}
				if(isPlayerCloseForAttack(player)) {
					newState(ATTACK);
				}
				move(tilesData);
				break;
			case ATTACK:
			    if (aniIndex == 0) {
			        attackChecked = false;
			    }

			    if (aniIndex == 3 && !attackChecked) {
			        updateAttackBox();
			        checkHitEnemy(AttackBox, player);
			    }
			    break;
			case HURT:
				break;
			}
		}
	}

	public int flipX() {
	    if (walkDir == LEFT) {
	        return (SLIME_DRAWOFFSET_X * 2) + SLIME_HITBOX_WIDTH;
	    } else {
	        return 0;
	    }
	}

	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (AttackBox.x-xLvlOffset), (int) AttackBox.y, (int) AttackBox.width, (int) AttackBox.height);
	}

	public int flipW() {
		if (walkDir == LEFT) {
			return -1;
		}else {
			return 1;
		}
	}
}
```

## File: src/objects/ObjectManager.java
```java
package objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.ObjectConstants.*;

public class ObjectManager {
    private PlayStates playStates;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage signImg;
    private BufferedImage kandangImg;
    private BufferedImage kandangBwImg;
    private BufferedImage[] equipmentImgs;

    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Sign> signs = new ArrayList<>();
    private ArrayList<Cage> cages = new ArrayList<>();
    private ArrayList<DroppedEquipment> droppedEquipments = new ArrayList<>();

    public static double BOSS_ARMOR_DROP_CHANCE = 0.75;

    public ObjectManager(PlayStates playStates) {
        this.playStates = playStates;
        loadImgs();
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {

        for (Potion p : potions) {
            if (p.isActive() && hitbox.intersects(p.getHitbox())) {
                if (playStates.getPlayer().addItemToInventory(p.getObjType())) {
                    p.setActive(false);
                }
            }
        }


        for (int i = 0; i < droppedEquipments.size(); i++) {
            DroppedEquipment de = droppedEquipments.get(i);
            if (de.isActive() && hitbox.intersects(de.getHitbox())) {
                if (playStates.getPlayer().addItemToInventory(de.getItemType())) {
                    de.setActive(false);
                }
            }
        }
    }

    public void applyEffectToPlayer(Potion p) {
        switch (p.getObjType()) {
            case RED_POTION_1: case RED_POTION_2: case RED_POTION_3:
                playStates.getPlayer().changeHealth(20);
                break;
            case BLUE_POTION_1: case BLUE_POTION_2: case BLUE_POTION_3:
                playStates.getPlayer().changeMana(20);
                break;
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers) {
            if (gc.isActive() && !gc.doAnimation && gc.getHitbox().intersects(attackbox)) {
                gc.setAnimation(true);
                int droppedPotionType = (gc.getObjType() == BOX) ? BLUE_POTION_1 : RED_POTION_1;
                potions.add(new Potion(
                    (int) (gc.getHitbox().x + gc.getHitbox().width / 2),
                    (int) (gc.getHitbox().y - gc.getHitbox().height / 2),
                    droppedPotionType)
                );
                return;
            }
        }

        for (Cage c : cages) {
            if (c.isActive() && !c.isBlinking() && c.getHitbox().intersects(attackbox)) {
                c.setHit();
                return;
            }
        }
    }

    public void loadObjects(ArrayList<Potion> p, ArrayList<GameContainer> c) {
        this.potions = p;
        this.containers = c;
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas("potion_sprites.png");
        potionImgs = new BufferedImage[6][3];
        int[] yKordinat = {8, 34};

        for (int baris = 0; baris < 2; baris++) {
            for (int tipe = 0; tipe < 3; tipe++) {
                for (int frame = 0; frame < 3; frame++) {
                    int idObjek = (baris * 3) + tipe;
                    int kolomGambar = (tipe * 3) + frame;
                    potionImgs[idObjek][frame] = potionSprite.getSubimage(7 + (kolomGambar * 35), yKordinat[baris], 16, 16);
                }
            }
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas("objects_sprites.png");
        containerImgs = new BufferedImage[2][8];
        for (int j = 0; j < containerImgs.length; j++) {
            for (int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
            }
        }

        signImg = LoadSave.GetSpriteAtlas("MediavelFree.png");
        kandangImg = LoadSave.GetSpriteAtlas("kandang .png");
        kandangBwImg = new BufferedImage(kandangImg.getWidth(), kandangImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < kandangImg.getHeight(); y++) {
            for (int x = 0; x < kandangImg.getWidth(); x++) {
                int p = kandangImg.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                kandangBwImg.setRGB(x, y, p);
            }
        }

        equipmentImgs = new BufferedImage[6];
        try {
            equipmentImgs[0] = LoadSave.GetSpriteAtlas("helmet.png");
            equipmentImgs[1] = LoadSave.GetSpriteAtlas("armor.png");
            equipmentImgs[2] = LoadSave.GetSpriteAtlas("Legs.png");
            equipmentImgs[3] = LoadSave.GetSpriteAtlas("ring.png");
            equipmentImgs[4] = LoadSave.GetSpriteAtlas("necklace.png");
            equipmentImgs[5] = LoadSave.GetSpriteAtlas("gloves.png");
        } catch (Exception e) {
            System.out.println("Warning: Ada gambar equipment yang gagal dimuat.");
        }
    }

    public void update() {
        for (Potion p : potions) { if (p.isActive()) p.update(); }
        for (GameContainer gc : containers) { if (gc.isActive()) gc.update(); }
        for (Cage c : cages) { if (c.isActive()) c.update(); }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawSigns(g, xLvlOffset);
        drawCages(g, xLvlOffset);
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);

        for (DroppedEquipment de : droppedEquipments) {
            if (de.isActive()) {
                de.draw(g, xLvlOffset);
            }
        }
    }

    public void spawnEquipment(int x, int y) {
        if (Math.random() <= BOSS_ARMOR_DROP_CHANCE) {
            int randomType = 10 + (int) (Math.random() * 6);
            int eqIndex = randomType - 10;

            if (equipmentImgs != null && eqIndex >= 0 && eqIndex < equipmentImgs.length) {
                BufferedImage itemImg = equipmentImgs[eqIndex];
                droppedEquipments.add(new DroppedEquipment(x, y, randomType, itemImg));
                System.out.println("Boss Menjatuhkan Equipment! ID: " + randomType);
            }
        }
    }

    private void drawCages(Graphics g, int xLvlOffset) {
        for (Cage c : cages) {
            if (c.isActive()) {
                int drawX = (int) (c.getHitbox().x - c.getxDrawOffset() - xLvlOffset);
                int drawY = (int) (c.getHitbox().y - c.getyDrawOffset());

                if (c.isBlinking() && (c.getBlinkTick() / 5) % 2 == 0) {
                    g.drawImage(kandangBwImg, drawX, drawY, KANDANG_WIDTH, KANDANG_HEIGHT, null);
                } else {
                    g.drawImage(kandangImg, drawX, drawY, KANDANG_WIDTH, KANDANG_HEIGHT, null);
                }
            }
        }
    }

    private void drawSigns(Graphics g, int xLvlOffset) {
        for (Sign s : signs) {
            int drawX = (int) (s.getHitbox().x - s.getxDrawOffset() - xLvlOffset);
            int drawY = (int) (s.getHitbox().y - s.getyDrawOffset());

            g.drawImage(signImg, drawX, drawY, SIGN_WIDTH, SIGN_HEIGHT, null);
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(10 * GameCore.SCALE)));
            FontMetrics fm = g.getFontMetrics();

            if (s.getMiddleText() != null && !s.getMiddleText().isEmpty()) {
                g.setColor(Color.BLACK);
                int mx = drawX + (SIGN_WIDTH / 2) - (fm.stringWidth(s.getMiddleText()) / 2);
                int my = drawY + (SIGN_HEIGHT / 2) + (fm.getAscent() / 3);
                g.drawString(s.getMiddleText(), mx, my);
            }

            Rectangle2D.Float pBox = playStates.getPlayer().getHitbox();
            Rectangle2D.Float sBox = s.getHitbox();
            float xDist = Math.abs((pBox.x + pBox.width / 2) - (sBox.x + sBox.width / 2));
            float yDist = Math.abs((pBox.y + pBox.height / 2) - (sBox.y + sBox.height / 2));
            float maxDist = 40 * GameCore.SCALE;
            if (xDist < maxDist && yDist < maxDist) {
                if (s.getTopText() != null && !s.getTopText().isEmpty()) {
                    g.setColor(Color.WHITE);
                    int tx = drawX + (SIGN_WIDTH / 2) - (fm.stringWidth(s.getTopText()) / 2);
                    int ty = drawY - (int)(8 * GameCore.SCALE);
                    g.drawString(s.getTopText(), tx, ty);
                }
            }
        }
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers) {
            if (gc.isActive()) {
                int type = (gc.getObjType() == BOX) ? 1 : 0;
                g.drawImage(containerImgs[type][gc.getAniIndex()],
                    (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                    (int) (gc.getHitbox().y - gc.getyDrawOffset()),
                    CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions) {
            if (p.isActive()) {
                g.drawImage(potionImgs[p.getObjType()][p.getAniIndex()],
                    (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                    (int) (p.getHitbox().y - p.getyDrawOffset()),
                    POTION_WIDTH, POTION_HEIGHT, null);
            }
        }
    }

    public void resetAllObjects() {
        for (Potion p : potions) p.reset();
        for (GameContainer gc : containers) gc.reset();
        for (Cage c : cages) c.reset();
        droppedEquipments.clear();
    }

    public void loadObjectsFromMap(int[][] mapData) {
        potions.clear();
        containers.clear();
        signs.clear();
        cages.clear();
        droppedEquipments.clear();

        for (int j = 0; j < mapData.length; j++) {
            for (int i = 0; i < mapData[0].length; i++) {
                int id = mapData[j][i];
                int xPos = i * GameCore.TILES_SIZE;

                if (id >= 500 && id <= 505) {
                    int yPosPotion = (j * GameCore.TILES_SIZE);
                    potions.add(new Potion(xPos, yPosPotion, id - 500));
                    mapData[j][i] = -1;
                }
                else if (id == 601 || id == 602) {
                    int yPosContainer = (j * GameCore.TILES_SIZE);
                    containers.add(new GameContainer(xPos, yPosContainer, (id == 601) ? BOX : BARREL));
                    mapData[j][i] = -1;
                }
                else if (id >= 700 && id <= 703) {
                    int yPosSign = (j * GameCore.TILES_SIZE) + (int)(15 * GameCore.SCALE);
                    int xPosSign = xPos + (int)(4 * GameCore.SCALE);
                    if (id == 700) signs.add(new Sign(xPosSign, yPosSign, SIGN, "E", "gunakan"));
                    else if (id == 701) signs.add(new Sign(xPosSign, yPosSign, SIGN, "SHOP", "WEAPONS"));
                    else if (id == 702) signs.add(new Sign(xPosSign, yPosSign, SIGN, "DANGER", "BOSS AREA"));
                    else if (id == 703) signs.add(new Sign(xPosSign, yPosSign, SIGN, "SAVE", "POINT"));
                    mapData[j][i] = -1;
                }
                else if (id == 403) {
                    int yPosKandang = (j * GameCore.TILES_SIZE);
                    cages.add(new Cage(xPos, yPosKandang, KANDANG));
                    mapData[j][i] = -1;
                }

                else if (id >= 910 && id <= 915) {
                    int yPosEq = (j * GameCore.TILES_SIZE);
                    int eqType = id - 900;
                    int eqIndex = eqType - 10;
                    if (equipmentImgs != null && eqIndex >= 0 && eqIndex < equipmentImgs.length) {
                        java.awt.image.BufferedImage itemImg = equipmentImgs[eqIndex];
                        int centerX = xPos + (GameCore.TILES_SIZE / 2);
                        int centerY = yPosEq + (GameCore.TILES_SIZE / 2);
                        droppedEquipments.add(new DroppedEquipment(centerX, centerY, eqType, itemImg));
                    }
                    mapData[j][i] = -1;
                }
            }
        }
    }

    public BufferedImage getPotionImg(int type) {
        if (potionImgs != null && type >= 0 && type < potionImgs.length) {
            return potionImgs[type][0];
        }
        return null;
    }

    public BufferedImage getItemImg(int type) {
        if (type >= 0 && type <= 5) {
            return getPotionImg(type);
        }
        else if (type >= 10 && type <= 15) {
            int eqIndex = type - 10;
            if (equipmentImgs != null && eqIndex >= 0 && eqIndex < equipmentImgs.length) {
                return equipmentImgs[eqIndex];
            }
        }
        return null;
    }

    public GameContainer getIntersectingContainer(Rectangle2D.Float nextHitbox) {
        for (GameContainer gc : containers) {
            if (gc.isActive() && !gc.doAnimation && nextHitbox.intersects(gc.getHitbox())) {
                return gc;
            }
        }
        return null;
    }

    public Cage getIntersectingCage(Rectangle2D.Float nextHitbox) {
        for (Cage c : cages) {
            if (c.isActive() && !c.isBlinking() && nextHitbox.intersects(c.getHitbox())) {
                return c;
            }
        }
        return null;
    }
}
```

## File: src/entity/Enemy.java
```java
package entity;

import main.GameCore;
import static utilitytools.HelpMethods.*;
import static utilitytools.Konstanta.Directions.*;
import static utilitytools.Konstanta.EnemyConstants.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.PlayStates;


public abstract class Enemy extends Entity {
	protected int aniIndex, enemyState;
	public int enemyType;
	protected int aniTick;
	protected int aniSpeed = 7;
	protected boolean firstUpdate = true;
	protected boolean inAir = false;
	protected float fallSpeed;
	protected float gravity = 0.444f * GameCore.SCALE;
	protected float walkSpeed = 1.16f * GameCore.SCALE;
	protected int walkDir = LEFT;
	protected int tileY;
	protected int attackDistance = (int) (GameCore.SCALE * 30);
	protected int maxHealth;
	protected int currentHealth;

	protected boolean active = true;
	protected boolean attackChecked;

	protected boolean isInvincible = false;
	protected int invincibilityTick = 0;
	protected final int INVINCIBILITY_DURATION = 20;

	protected float knockbackSpeed = 0;
	protected int knockbackDir = 1;
	protected float knockbackFriction = 0.15f * GameCore.SCALE;

	public Enemy(float x, float y, int width, int height, int enemyType) {
	    super(x, y, width, height);
	    this.enemyType = enemyType;
	    initHitBox(x, y, width, height);
	    maxHealth = utilitytools.Konstanta.EnemyConstants.getMaxHealth(enemyType);
	    currentHealth = maxHealth;
	    this.tileY = (int) (y / GameCore.TILES_SIZE);
	}

	protected void firstUpdateCheck(int[][] tilesData) {
		if(!IsEntityOnFloor(hitBox, tilesData)) {
			inAir = true;
			firstUpdate = false;
		}
	}

	protected void updateInAir(int[][] tilesData) {
		if(canMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, tilesData)) {
			hitBox.y += fallSpeed;
			fallSpeed += gravity;
		}else {
			inAir = false;
			hitBox.y = GetEntityPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
			tileY = (int) (hitBox.y /GameCore.TILES_SIZE);
			fallSpeed = 0;
		}
	}

	protected void checkOnFloor(int[][] tilesData) {
		if (!inAir && !IsEntityOnFloor(hitBox, tilesData)) {
			inAir = true;
			fallSpeed = 0;
			firstUpdate = false;
		}
	}

	protected void move(int[][] tilesData) {
		float xSpeed = 0;

		if(walkDir == LEFT) {
			xSpeed = -walkSpeed;
		}else {
			xSpeed = walkSpeed;
		}
		if(canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, tilesData)) {
			if(isFloor(hitBox, xSpeed, tilesData)) {
				hitBox.x += xSpeed;
				return;
			}
		}
		changeWalkDir();
	}

	protected void turnToPlayer(Player player) {
		if(player.hitBox.x  > hitBox.x) {
			walkDir = RIGHT;
		}else {
			walkDir = LEFT;
		}
	}

	protected void updateEffects(int[][] tilesData) {
	    if (isInvincible) {
	        invincibilityTick++;
	        if (invincibilityTick >= INVINCIBILITY_DURATION) {
	            isInvincible = false;
	            invincibilityTick = 0;
	        }
	    }

	    if (knockbackSpeed > 0) {
	        float xSpeed = knockbackSpeed * knockbackDir;
	        if(utilitytools.HelpMethods.canMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, tilesData)) {
	            hitBox.x += xSpeed;
	        } else {
	        	hitBox.x = utilitytools.HelpMethods.GetEntityPosNextToWall(hitBox, xSpeed);
	            knockbackSpeed = 0;
	        }
	        knockbackSpeed -= knockbackFriction;
	        if (knockbackSpeed < 0) knockbackSpeed = 0;
	    }
	}

	protected boolean canSeePlayer(int[][] tilesData, Player player) {
		int playerTileY = (int) (player.getHitBox().y / GameCore.TILES_SIZE);
		if(playerTileY == tileY) {
			if(isPlayerInRange(player)) {
				if(IsSightClear(tilesData, hitBox, player.hitBox, tileY)) {
					return true;
				}
			}
		}
		return false;
	}



	protected boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitBox.x - hitBox.getX());
		return absValue <= attackDistance * 5;
	}

	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int) Math.abs(player.hitBox.x - hitBox.getX());
		return absValue <= attackDistance;
	}

	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}

	public void hurt(int value, int kbDir, boolean applyKnockback) {
	    if (isInvincible) return;

	    currentHealth -= value;
	    isInvincible = true;

	    if (applyKnockback) {
	        knockbackDir = kbDir;
	        knockbackSpeed = 2.0f * GameCore.SCALE;
	        if (!inAir) {
	            inAir = true;
	            fallSpeed = -1.5f * GameCore.SCALE;
	        }
	    }

	    if(currentHealth <= 0) {
	        newState(MATI);
	    } else {
	        newState(HURT);
	    }
	}

	public void hurt(int value, int kbDir) {
	    hurt(value, kbDir, true);
	}

	public void hurt(int value) {
	    hurt(value, 1, false);
	}

	protected void checkHitEnemy(Rectangle2D.Float AttackBox, Player player) {
	    if(AttackBox.intersects(player.getHitbox())) {
	        player.changeHealth(-getEnemyAtt(enemyType));

	        int kbDir = (player.getHitbox().x > hitBox.x) ? 1 : -1;
	        boolean fromBoss = (this.enemyType == DEMON_BOSS);

	        player.applyKnockback(kbDir, fromBoss);
	        if (fromBoss) {
	            player.getPlayStates().triggerHeavyHit(12, 18, 7);
	        } else {
	            player.getPlayStates().triggerHeavyHit(0, 4, 2);
	        }
	    }
	    attackChecked = true;
	}

	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;

				switch(enemyState) {
				case ATTACK, HURT -> enemyState = IDLE;
				case MATI -> active = false;
				}
			}
		}
	}

	protected void changeWalkDir() {
		if(walkDir == LEFT) {
			walkDir = RIGHT;
		}else {
			walkDir = LEFT;
		}
	}

    public abstract void update(int[][] tilesData, Player player);
    public abstract void draw(Graphics g, int xLvlOffset, BufferedImage[][] spriteAtlas);
    public abstract int getExpReward();

    protected void handleHurtEffects(PlayStates playStates) {}
    protected void handleDeathEffects(PlayStates playStates) {}

    public void hit(int damage, Player player, PlayStates playStates) {
        if (!active || isInvincible) return;

        int kbDir = (player != null && player.getHitbox().x < hitBox.x) ? 1 : -1;
        boolean isCharge = (player != null) && player.isChargeAttack();

        hurt(damage, kbDir, isCharge);

        if (currentHealth <= 0) {
            if (player != null) player.gainExp(getExpReward());
            handleDeathEffects(playStates);
        } else {
            handleHurtEffects(playStates);
        }
    }

    public int getEnemyType() {
		return enemyType;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void resetEnemy() {
		hitBox.x = x;
		hitBox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		fallSpeed = 0;

	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getEnemyState() {
		return enemyState;
	}

	public boolean isActive() {
		return active;
	}

	public int getAniTick() {
	    return aniTick;
	}

	public int getCurrentHealth() {
        return currentHealth;
    }
}
```

## File: src/utilitytools/LoadSave.java
```java
package utilitytools;

import entity.Slime;
import entity.BlueGolem;
import entity.BringerOfDeath;
import entity.DemonBoss;
import entity.Skullwolf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.GameCore;
import static utilitytools.Konstanta.EnemyConstants.*;
import static utilitytools.Konstanta.UI.PauseButtons.*;

public class LoadSave {

	public static final String PLAYER_SPRITE = "player_right.png";
	public static final String WORLD_SPRITE = "map_jungle_fix.png";
	public static final String MAP_1_DATA = "map_1_data.png";
	public static final String MENU_BUTTONS = "map_panel.png";
	public static final String MENU_BACKGROUND = "bg_feline.png";
	public static final String PAUSE_BACKGROUND = "pause_background.png";
	public static final String SOUND_BUTTONS = "sound_buttons.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String VOLUME_SLIDER = "volume_slider.png";

	public static final String SLIME_SPRITE = "enemy_slime.png";
	public static final String DEMON_BOSS_SPRITE = "demon_boss_spritesheet.png";
	public static final String BLUE_GOLEM_SPRITE = "golem_blue.png";
	public static final String SKULLWOLF_SPRITE = "skullwolf.png";


	public static final String MENU_BACKGROUND_IMG = "mainn_menu.jpeg";
	public static final String PLAY_BACKGROUND_IMG = "Background_jungle2.png";
  public static final String CASTLE_BACKGROUND_IMG = "castle_map.png";
	public static final String CLOUDS_01 = "awan_01.png";
	public static final String CLOUDS_02 = "awan_02.png";

	public static final String STATUS_BAR = "statusbar.png";
	public static final String MENU_PANEL = "menu_panel.png";

	public static final String INVENTORY_BG = "inventory_skill_karakter_panel.png";

	public static final String PAUSE_TITLE = "pause.png";
	public static final String PAUSE_MUSIC_TEXT = "music.png";
	public static final String PAUSE_SE_TEXT = "SE.png";
	public static final String PAUSE_VOL_TEXT = "volume.png";


	public static final String PAUSE_PAW_BTN = "paw_button.png";
	public static final String PAUSE_RESUME_BTN = "resume_button.png";
	public static final String PAUSE_RESTART_BTN = "restart_button.png";
	public static final String PAUSE_MENU_BTN = "menu_button.png";

	public static final String PAUSE_CHAIN = "rantai.png";
	public static final String MAP_KASTIL_TXT = "/map_tileset.txt";

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage image = null;
		InputStream is = LoadSave.class.getResourceAsStream("/"+ fileName);
		if (is == null) {
			return createPlaceholder(fileName);
		}
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (image == null) {
			return createPlaceholder(fileName);
		}
		return image;
	}

	private static BufferedImage createPlaceholder(String fileName) {
		int width = 64;
		int height = 64;
		if (PAUSE_BACKGROUND.equals(fileName)) {
			width = (int) (GameCore.GAME_WIDTH / GameCore.SCALE);
			height = (int) (GameCore.GAME_HEIGHT / GameCore.SCALE);
		} else if (SOUND_BUTTONS.equals(fileName)) {
			width = SOUND_SIZE_DEFAULT * 3;
			height = SOUND_SIZE_DEFAULT * 2;
		} else if (URM_BUTTONS.equals(fileName)) {
			width = URM_DEFAULT_SIZE * 3;
			height = URM_DEFAULT_SIZE * 3;
		} else if (VOLUME_BUTTONS.equals(fileName)) {
			width = VOLUME_DEFAULT_WIDTH * 3;
			height = VOLUME_DEFAULT_HEIGHT;
		} else if (VOLUME_SLIDER.equals(fileName)) {
			width = SLIDER_DEFAULT_WIDTH;
			height = VOLUME_DEFAULT_HEIGHT;
		}

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor(new Color(30, 30, 30, 220));
		g2.fillRect(0, 0, width, height);
		g2.setColor(new Color(200, 200, 200, 220));
		g2.drawRect(0, 0, width - 1, height - 1);
		g2.dispose();
		return image;
	}

	public static int[][] GetTilesData(String filePath) {
		ArrayList<int[]> rowList = new ArrayList<>();

		try {
			InputStream is = GameCore.class.getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) {
					continue;
				}

				String[] numbers = line.split(",");
				int[] row = new int[numbers.length];

				for (int col = 0; col < numbers.length; col++) {
					int value = Integer.parseInt(numbers[col].trim());
					if (value == 800 || value == 801 || value == 802 || value == 803 || value == 804) {
						row[col] = -1;
					} else {
						row[col] = value;
					}
				}
				rowList.add(row);
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Gagal memuat map!");
			e.printStackTrace();
		}

		int[][] tilesData = new int[rowList.size()][];
		for (int i = 0; i < rowList.size(); i++) {
			tilesData[i] = rowList.get(i);
		}

		return tilesData;
	}

	public static ArrayList<Slime> GetSlimes(String filePath) {
		ArrayList<Slime> list = new ArrayList<>();
		int[][] tilesData = GetTilesData(filePath);

		try {
			InputStream is = GameCore.class.getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line;
			int row = 0;

			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) {
					continue;
				}

				String[] numbers = line.split(",");

				for (int col = 0; col < numbers.length; col++) {
					int value = Integer.parseInt(numbers[col].trim());


					if (value == 800) {
						int xPos = col * GameCore.TILES_SIZE;
						int yPos = row * GameCore.TILES_SIZE;
						int groundRow = findGroundRow(row, col, tilesData);
						if (groundRow != -1) {
							yPos = (groundRow * GameCore.TILES_SIZE) - SLIME_HITBOX_HEIGHT;
						}

						list.add(new Slime(xPos, yPos));
					}
				}
				row++;
			}
			br.close();

		} catch (Exception e) {
			System.out.println("Gagal memuat musuh Slime!");
			e.printStackTrace();
		}

		return list;
	}

	public static ArrayList<DemonBoss> GetDemonBosses(String filePath) {
		ArrayList<DemonBoss> list = new ArrayList<>();
		int[][] tilesData = GetTilesData(filePath);

		try {
			InputStream is = GameCore.class.getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line;
			int row = 0;

			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) {
					continue;
				}

				String[] numbers = line.split(",");

				for (int col = 0; col < numbers.length; col++) {
					int value = Integer.parseInt(numbers[col].trim());

					if (value == 801) {
						int xPos = col * GameCore.TILES_SIZE;
						int yPos = row * GameCore.TILES_SIZE;
						int groundRow = findGroundRow(row, col, tilesData);
						if (groundRow != -1) {
							yPos = (groundRow * GameCore.TILES_SIZE) - DEMON_BOSS_HITBOX_HEIGHT;
						}

						list.add(new DemonBoss(xPos, yPos));
					}
				}
				row++;
			}
			br.close();

		} catch (Exception e) {
			System.out.println("Gagal memuat Demon Boss!");
			e.printStackTrace();
		}

		return list;
	}

	public static ArrayList<BlueGolem> GetBlueGolems(String filePath) {
	    ArrayList<BlueGolem> list = new ArrayList<>();
	    int[][] tilesData = GetTilesData(filePath);

	    try {
	        InputStream is = GameCore.class.getResourceAsStream(filePath);
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));

	        String line;
	        int row = 0;

	        while ((line = br.readLine()) != null) {
	            if (line.trim().isEmpty()) {
	                continue;
	            }

	            String[] numbers = line.split(",");

	            for (int col = 0; col < numbers.length; col++) {
	                int value = Integer.parseInt(numbers[col].trim());


	                if (value == 802) {
	                    int xPos = col * GameCore.TILES_SIZE;
	                    int yPos = row * GameCore.TILES_SIZE;
	                    int groundRow = findGroundRow(row, col, tilesData);
	                    if (groundRow != -1) {
	                        yPos = (groundRow * GameCore.TILES_SIZE) - BLUE_GOLEM_HITBOX_HEIGHT;
	                    }

	                    list.add(new BlueGolem(xPos, yPos));
	                }
	            }
	            row++;
	        }
	        br.close();

	    } catch (Exception e) {
	        System.out.println("Gagal memuat Blue Golem!");
	        e.printStackTrace();
	    }

	    return list;
	}

	public static ArrayList<BringerOfDeath> GetBringers(String filePath) {
	    ArrayList<BringerOfDeath> list = new ArrayList<>();
	    int[][] tilesData = GetTilesData(filePath);

	    try {
	        InputStream is = GameCore.class.getResourceAsStream(filePath);
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));

	        String line;
	        int row = 0;

	        while ((line = br.readLine()) != null) {
	            if (line.trim().isEmpty()) continue;

	            String[] numbers = line.split(",");

	            for (int col = 0; col < numbers.length; col++) {
	                int value = Integer.parseInt(numbers[col].trim());

	                if (value == 803) {
	                    int xPos = col * GameCore.TILES_SIZE;
	                    int yPos = row * GameCore.TILES_SIZE;
	                    int groundRow = findGroundRow(row, col, tilesData);

	                    if (groundRow != -1) {
	                        yPos = (groundRow * GameCore.TILES_SIZE) - (int)(93 * GameCore.SCALE * 1.5f);
	                    }

	                    list.add(new BringerOfDeath(xPos, yPos));
	                }
	            }
	            row++;
	        }
	        br.close();
	    } catch (Exception e) {
	        System.out.println("Gagal memuat Bringer of Death!");
	        e.printStackTrace();
	    }

	    return list;
	}

	public static ArrayList<Skullwolf> GetSkullwolves(String filePath) {
	    ArrayList<Skullwolf> list = new ArrayList<>();
	    int[][] tilesData = GetTilesData(filePath);

	    try {
	        InputStream is = GameCore.class.getResourceAsStream(filePath);
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String line;
	        int row = 0;

	        while ((line = br.readLine()) != null) {
	            if (line.trim().isEmpty()) continue;

	            String[] numbers = line.split(",");
	            for (int col = 0; col < numbers.length; col++) {
	                int value = Integer.parseInt(numbers[col].trim());

	                if (value == 804) {
	                    int xPos = col * GameCore.TILES_SIZE;
	                    int yPos = row * GameCore.TILES_SIZE;
	                    int groundRow = findGroundRow(row, col, tilesData);
	                    if (groundRow != -1) {
	                        yPos = (groundRow * GameCore.TILES_SIZE) - SKULLWOLF_HITBOX_HEIGHT;
	                    }
	                    list.add(new Skullwolf(xPos, yPos));
	                }
	            }
	            row++;
	        }
	        br.close();
	    } catch (Exception e) {
	        System.out.println("Gagal memuat musuh Skullwolf!");
	        e.printStackTrace();
	    }
	    return list;
	}

	private static int findGroundRow(int startRow, int col, int[][] tilesData) {
		if (tilesData == null) {
			return -1;
		}
		for (int row = startRow + 1; row < tilesData.length; row++) {
			if (col < 0 || col >= tilesData[row].length) {
				continue;
			}
			if (utilitytools.HelpMethods.isSolidTile(tilesData[row][col])) {
				return row;
			}
		}
		return -1;
	}
}
```

## File: src/utilitytools/Konstanta.java
```java
package utilitytools;

import main.GameCore;

public class Konstanta {

	public static class ObjectConstants {

	    public static final int RED_POTION_1 = 0;
	    public static final int RED_POTION_2 = 1;
	    public static final int RED_POTION_3 = 2;


	    public static final int BLUE_POTION_1 = 3;
	    public static final int BLUE_POTION_2 = 4;
	    public static final int BLUE_POTION_3 = 5;


	    public static final int SIGN = 5;

	    public static final int SIGN_WIDTH_DEFAULT = 17;
	    public static final int SIGN_HEIGHT_DEFAULT = 17;
	    public static final int SIGN_WIDTH = (int)(SIGN_WIDTH_DEFAULT * main.GameCore.SCALE);
	    public static final int SIGN_HEIGHT = (int)(SIGN_HEIGHT_DEFAULT * main.GameCore.SCALE);


	    public static final int KANDANG = 8;
	    public static final int KANDANG_WIDTH_DEFAULT = 32;
	    public static final int KANDANG_HEIGHT_DEFAULT = 32;
	    public static final int KANDANG_WIDTH = (int)(KANDANG_WIDTH_DEFAULT * main.GameCore.SCALE);
	    public static final int KANDANG_HEIGHT = (int)(KANDANG_HEIGHT_DEFAULT * main.GameCore.SCALE);


	    public static final int BARREL = 6;
	    public static final int BOX = 7;


	    public static final int HELMET = 10;
	    public static final int ARMOR = 11;
	    public static final int SHOES = 12;
	    public static final int RING = 13;
	    public static final int SACK = 14;
	    public static final int GLOVES = 15;


	    public static final int RED_VAL_1 = 5,  RED_VAL_2 = 10, RED_VAL_3 = 15;
	    public static final int BLUE_VAL_1 = 5, BLUE_VAL_2 = 10, BLUE_VAL_3 = 15;


	    public static final int CONTAINER_WIDTH_DEFAULT = 40;
	    public static final int CONTAINER_HEIGHT_DEFAULT = 30;


	    public static final int POTION_WIDTH_DEFAULT = 16;
	    public static final int POTION_HEIGHT_DEFAULT = 16;


	    public static final int CONTAINER_WIDTH = (int) (GameCore.SCALE * CONTAINER_WIDTH_DEFAULT);
	    public static final int CONTAINER_HEIGHT = (int) (GameCore.SCALE * CONTAINER_HEIGHT_DEFAULT);
	    public static final int POTION_WIDTH = (int) (GameCore.SCALE * POTION_WIDTH_DEFAULT);
	    public static final int POTION_HEIGHT = (int) (GameCore.SCALE * POTION_HEIGHT_DEFAULT);

	    public static int GetSpriteAmount(int object_type) {
	        switch (object_type) {
	            case RED_POTION_1: case RED_POTION_2: case RED_POTION_3:
	            case BLUE_POTION_1: case BLUE_POTION_2: case BLUE_POTION_3:
	                return 3;
	            case BARREL:
	            case BOX:
	                return 8;
	            default:
	                return 1;
	        }
	    }
	}

	public static class Environment {
	    public static final int CLOUDS_01_WIDTH_DEFAULT = 448;
	    public static final int CLOUDS_01_HEIGHT_DEFAULT = 101;
	    public static final int CLOUDS_02_WIDTH_DEFAULT = 74;
	    public static final int CLOUDS_02_HEIGHT_DEFAULT = 24;

	    public static final int CLOUDS_01_WIDTH = (int) (CLOUDS_01_WIDTH_DEFAULT * GameCore.SCALE);
	    public static final int CLOUDS_01_HEIGHT = (int) (CLOUDS_01_HEIGHT_DEFAULT * GameCore.SCALE);
	    public static final int CLOUDS_02_WIDTH = (int) (CLOUDS_01_WIDTH_DEFAULT * GameCore.SCALE);
	    public static final int CLOUDS_02_HEIGHT = (int) (CLOUDS_01_HEIGHT_DEFAULT * GameCore.SCALE);
	}

	public static class EnemyConstants{
		public static final int SLIME = 0;
		public static final int DEMON_BOSS = 1;
		public static final int BLUE_GOLEM = 2;
		public static final int BRINGER_OF_DEATH = 3;
		public static final int SKULLWOLF = 4;

		public static final int IDLE = 0;
		public static final int WALK = 1;
		public static final int ATTACK= 2;
		public static final int JUMP = 3;
		public static final int HURT = 4;
		public static final int MATI = 5;


		public static final int BRINGER_SPELL = 6;
		public static final int BRINGER_SPECIAL = 7;

		public static final int SLIME_WIDTH_DEFAULT = 80;
		public static final int SLIME_HEIGHT_DEFAULT = 80;

		public static final int SLIME_WIDTH = (int) (SLIME_WIDTH_DEFAULT * GameCore.SCALE);
		public static final int SLIME_HEIGHT = (int) (SLIME_HEIGHT_DEFAULT * GameCore.SCALE);

		public static final int SLIME_HITBOX_WIDTH = (int) (16 * GameCore.SCALE);
		public static final int SLIME_HITBOX_HEIGHT = (int) (9 * GameCore.SCALE);

		public static final int SLIME_DRAWOFFSET_X = (int) (16 * GameCore.SCALE);
		public static final int SLIME_DRAWOFFSET_Y = (int) (52 * GameCore.SCALE);

		public static final int DEMON_BOSS_WIDTH_DEFAULT = 288;
		public static final int DEMON_BOSS_HEIGHT_DEFAULT = 160;
		public static final int DEMON_BOSS_SPRITE_COLUMNS = 22;

		public static final int DEMON_BOSS_WIDTH = (int) (DEMON_BOSS_WIDTH_DEFAULT * GameCore.SCALE);
		public static final int DEMON_BOSS_HEIGHT = (int) (DEMON_BOSS_HEIGHT_DEFAULT * GameCore.SCALE);

		public static final int DEMON_BOSS_HITBOX_WIDTH = (int) (46 * GameCore.SCALE);
		public static final int DEMON_BOSS_HITBOX_HEIGHT = (int) (72 * GameCore.SCALE);

		public static final int DEMON_BOSS_DRAWOFFSET_X = (int) (120 * GameCore.SCALE);
		public static final int DEMON_BOSS_DRAWOFFSET_Y = (int) (85 * GameCore.SCALE);


	    public static final int BLUE_GOLEM_WIDTH_DEFAULT = 90;
	    public static final int BLUE_GOLEM_HEIGHT_DEFAULT = 64;
	    public static final int BLUE_GOLEM_SPRITE_COLUMNS = 12;

	    public static final int BLUE_GOLEM_WIDTH = (int) (BLUE_GOLEM_WIDTH_DEFAULT * GameCore.SCALE);
	    public static final int BLUE_GOLEM_HEIGHT = (int) (BLUE_GOLEM_HEIGHT_DEFAULT * GameCore.SCALE);


	    public static final int BLUE_GOLEM_HITBOX_WIDTH = (int) (40 * GameCore.SCALE);
	    public static final int BLUE_GOLEM_HITBOX_HEIGHT = (int) (55 * GameCore.SCALE);
	    public static final int BLUE_GOLEM_DRAWOFFSET_X = (int) (28 * GameCore.SCALE);
	    public static final int BLUE_GOLEM_DRAWOFFSET_Y = (int) (9 * GameCore.SCALE);


	    public static final int BRINGER_WIDTH_DEFAULT = 140;
	    public static final int BRINGER_HEIGHT_DEFAULT = 93;
	    public static final int BRINGER_SPRITE_COLUMNS = 8;


	    public static final int SKULLWOLF_WIDTH_DEFAULT = 64;
	    public static final int SKULLWOLF_HEIGHT_DEFAULT = 64;
	    public static final int SKULLWOLF_WIDTH = (int) (SKULLWOLF_WIDTH_DEFAULT * GameCore.SCALE);
	    public static final int SKULLWOLF_HEIGHT = (int) (SKULLWOLF_HEIGHT_DEFAULT * GameCore.SCALE);


	    public static final int SKULLWOLF_HITBOX_WIDTH = (int) (30 * GameCore.SCALE);
	    public static final int SKULLWOLF_HITBOX_HEIGHT = (int) (30 * GameCore.SCALE);


	    public static final int SKULLWOLF_DRAWOFFSET_X = (int) (17 * GameCore.SCALE);
	    public static final int SKULLWOLF_DRAWOFFSET_Y = (int) (34 * GameCore.SCALE);

		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			switch(enemy_type) {
			case SLIME:
				switch(enemy_state) {
				case IDLE: return 6;
				case WALK: return 6;
				case ATTACK: return 9;
				case JUMP: return 8;
				case HURT: return 3;
				case MATI: return 3;
				}
			case DEMON_BOSS:
				switch(enemy_state) {
				case IDLE: return 6;
				case WALK: return 12;
				case ATTACK: return 15;
				case HURT: return 5;
				case MATI: return 22;
				}
			case BLUE_GOLEM:
	            switch(enemy_state) {
	            case IDLE: return 8;
	            case WALK: return 10;
	            case ATTACK: return 11;
	            case HURT: return 4;
	            case MATI: return 12;
	            }
			case BRINGER_OF_DEATH:
	            switch(enemy_state) {
	                case IDLE: return 8;
	                case WALK: return 8;
	                case ATTACK: return 8;
	                case HURT: return 8;
	                case MATI: return 8;
	                case JUMP: return 8;
	                case BRINGER_SPELL: return 8;
	                case BRINGER_SPECIAL: return 8;
	            }
			case SKULLWOLF:

		        switch(enemy_state) {
		            case IDLE: return 6;
		            case WALK: return 5;
		            case ATTACK: return 5;
		            case HURT: return 4;
		            case MATI: return 7;
		        }
			}
			return 0;
		}

		public static int getMaxHealth(int enemy_type) {
			switch(enemy_type) {
			case SLIME: return 10;
			case DEMON_BOSS: return 80;
			case BLUE_GOLEM: return 150;
			case BRINGER_OF_DEATH: return 200;
			case SKULLWOLF: return 40;
			default: return 1;
			}
		}

		public static int getEnemyAtt(int enemy_type) {
			switch(enemy_type) {
			case SLIME: return 10;
			case DEMON_BOSS: return 25;
			case BLUE_GOLEM: return 40;
			case BRINGER_OF_DEATH: return 50;
			case SKULLWOLF: return 15;
			default: return 0;
			}
		}
	}

	public static class UI{
		public static class Frames{
			public static final int B_WIDTH_DEFAULT = 233;
			public static final int B_HEIGHT_DEFAULT = 138;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * GameCore.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * GameCore.SCALE);
		}

		public static class PauseButtons{
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * GameCore.SCALE);
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * GameCore.SCALE);
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * GameCore.SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * GameCore.SCALE);
			public static final int SLIDER_DEFAULT_WIDTH = 215;
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * GameCore.SCALE);
		}
	}

	public static class Directions{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class KonstantaPlayerRight{
		public static final int IDLE_ACTIVE = 0;
		public static final int IDLE_CHILL = 1;
		public static final int LARI = 3;
		public static final int LOMPAT = 4;
		public static final int DASH = 16;
		public static final int JATUH = 19;
		public static final int ATTACK_1 = 11;
		public static final int CHARGE_ATTACK = 12;
		public static final int GUARD = 15;
		public static final int DOWN = 23;
		public static final int ARISE = 24;

		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case DOWN: return 7;
			case IDLE_CHILL: return 8;
			case ARISE: return 7;
			case IDLE_ACTIVE: return 8;
			case LARI: return 8;
			case LOMPAT: return 11;
			case JATUH: return 6;
			case ATTACK_1: return 8;
			case CHARGE_ATTACK: return 15;
			case GUARD: return 6;
			case DASH: return 10;
			default: return 1;
			}
		}
	}
}
```

## File: src/entity/EnemyManager.java
```java
package entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import gameStates.PlayStates;
import main.GameCore;
import utilitytools.LoadSave;
import static utilitytools.Konstanta.EnemyConstants.*;

public class EnemyManager {

    private PlayStates playStates;

    private Map<Integer, BufferedImage[][]> enemySpriteMap = new HashMap<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public EnemyManager(PlayStates playStates) {
        this.playStates = playStates;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        enemies.clear();
        enemies.addAll(LoadSave.GetSlimes("/map_tutorial_fix.txt"));
        enemies.addAll(LoadSave.GetDemonBosses("/map_tutorial_fix.txt"));
        enemies.addAll(LoadSave.GetBlueGolems("/map_tutorial_fix.txt"));
        enemies.addAll(LoadSave.GetBringers("/map_tutorial_fix.txt"));
        enemies.addAll(LoadSave.GetSkullwolves("/map_tutorial_fix.txt"));
    }

    public void update(int[][] tilesData, Player player) {
        for (Enemy e : enemies) {
            if (e.isActive()) {
                e.update(tilesData, player);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        Graphics g2 = g.create();
        g2.translate(-xLvlOffset, 0);

        for (Enemy e : enemies) {
            if (e.isActive()) {
                BufferedImage[][] atlas = enemySpriteMap.get(e.getEnemyType());
                if (atlas != null) {
                    e.draw(g2, xLvlOffset, atlas);
                }
            }
        }
        g2.dispose();

        drawBossUI(g);
    }

    private void drawBossUI(Graphics g) {
        for (Enemy e : enemies) {
            if ((e.getEnemyType() == DEMON_BOSS || e.getEnemyType() == BLUE_GOLEM) && e.isActive()) {
                int maxWidth = (int) (400 * GameCore.SCALE);
                int height = (int) (20 * GameCore.SCALE);
                int xPos = (GameCore.GAME_WIDTH / 2) - (maxWidth / 2);
                int yPos = (int) (GameCore.GAME_HEIGHT - (40 * GameCore.SCALE));

                float distance = Math.abs(playStates.getPlayer().getHitbox().x - e.getHitBox().x);
                float healthPercentage = (float) e.getCurrentHealth() / e.getMaxHealth();
                int currentWidth = (int) (maxWidth * healthPercentage);

                if (currentWidth < 0) currentWidth = 0;

                if (distance < GameCore.GAME_WIDTH) {
                    g.setColor(new java.awt.Color(50, 50, 50, 200));
                    g.fillRect(xPos, yPos, maxWidth, height);

                    g.setColor(new java.awt.Color(200, 50, 50));
                    g.fillRect(xPos, yPos, currentWidth, height);

                    g.setColor(java.awt.Color.WHITE);
                    g.drawRect(xPos, yPos, maxWidth, height);

                    g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, (int)(16 * GameCore.SCALE)));

                    String bossName = (e.getEnemyType() == DEMON_BOSS) ? "DEMON BOSS" : "BLUE GOLEM";
                    g.drawString(bossName, xPos, yPos - (int)(5 * GameCore.SCALE));

                    break;
                }
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox, int damage, Player player) {
        for (Enemy e : enemies) {
            if (e.isActive() && attackBox.intersects(e.getHitBox())) {

                e.hit(damage, player, playStates);

                if (e.getEnemyType() == DEMON_BOSS) {
                    DemonBoss demonBoss = (DemonBoss) e;

                    if (demonBoss.getCurrentHealth() <= 0 || demonBoss.isDead()) {
                        playStates.getPlayer().gainExp(100);

                        playStates.getObjectManager().spawnEquipment(
                            (int) demonBoss.getHitBox().x,
                            (int) demonBoss.getHitBox().y
                        );

                        playStates.triggerHeavyHit(45, 120, 8);
                    }
                    else if (demonBoss.checkHpThresholdEffect()) {
                        playStates.triggerHeavyHit(20, 25, 12);
                    }
                }

                return;
            }
        }
    }

    private void loadEnemyImages() {

        BufferedImage[][] slimeImg = new BufferedImage[5][9];
        BufferedImage tempSlime = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
        for (int j = 0; j < slimeImg.length; j++) {
            for (int i = 0; i < slimeImg[j].length; i++) {
                slimeImg[j][i] = tempSlime.getSubimage(i * SLIME_WIDTH_DEFAULT, j * SLIME_HEIGHT_DEFAULT,
                        SLIME_WIDTH_DEFAULT, SLIME_HEIGHT_DEFAULT);
            }
        }
        enemySpriteMap.put(SLIME, slimeImg);

        BufferedImage[][] skullwolfImg = new BufferedImage[4][7];
        BufferedImage tempSkullwolf = LoadSave.GetSpriteAtlas(LoadSave.SKULLWOLF_SPRITE);
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 7; i++) {
                skullwolfImg[j][i] = tempSkullwolf.getSubimage(
                    i * SKULLWOLF_WIDTH_DEFAULT,
                    j * SKULLWOLF_HEIGHT_DEFAULT,
                    SKULLWOLF_WIDTH_DEFAULT,
                    SKULLWOLF_HEIGHT_DEFAULT
                );
            }
        }
        enemySpriteMap.put(SKULLWOLF, skullwolfImg);

        BufferedImage[][] bringerImg = new BufferedImage[8][BRINGER_SPRITE_COLUMNS];
        BufferedImage bringerSheet = LoadSave.GetSpriteAtlas("Bringer_of_Death.png");

        loadBringerAnimation(bringerImg, bringerSheet, IDLE, 0);
        loadBringerAnimation(bringerImg, bringerSheet, WALK, 1);
        loadBringerAnimation(bringerImg, bringerSheet, ATTACK, 2);
        loadBringerAnimation(bringerImg, bringerSheet, HURT, 3);
        loadBringerAnimation(bringerImg, bringerSheet, MATI, 4);
        enemySpriteMap.put(BRINGER_OF_DEATH, bringerImg);

        BufferedImage[][] demonBossImg = new BufferedImage[6][DEMON_BOSS_SPRITE_COLUMNS];
        BufferedImage demonBossSheet = LoadSave.GetSpriteAtlas(LoadSave.DEMON_BOSS_SPRITE);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, IDLE, 0);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, WALK, 1);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, ATTACK, 2);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, HURT, 3);
        loadDemonBossAnimation(demonBossImg, demonBossSheet, MATI, 4);
        enemySpriteMap.put(DEMON_BOSS, demonBossImg);

        BufferedImage[][] blueGolemImg = new BufferedImage[6][BLUE_GOLEM_SPRITE_COLUMNS];
        BufferedImage blueGolemSheet = LoadSave.GetSpriteAtlas(LoadSave.BLUE_GOLEM_SPRITE);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, IDLE, 3);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, WALK, 4);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, ATTACK, 0);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, HURT, 2);
        loadBlueGolemAnimation(blueGolemImg, blueGolemSheet, MATI, 1);
        enemySpriteMap.put(BLUE_GOLEM, blueGolemImg);
    }

    private void loadDemonBossAnimation(BufferedImage[][] targetArray, BufferedImage sheet, int targetState, int sourceRow) {
        for (int i = 0; i < GetSpriteAmount(DEMON_BOSS, targetState); i++) {
            targetArray[targetState][i] = sheet.getSubimage(i * DEMON_BOSS_WIDTH_DEFAULT,
                    sourceRow * DEMON_BOSS_HEIGHT_DEFAULT, DEMON_BOSS_WIDTH_DEFAULT, DEMON_BOSS_HEIGHT_DEFAULT);
        }
    }

    private void loadBlueGolemAnimation(BufferedImage[][] targetArray, BufferedImage sheet, int targetState, int sourceRow) {
        for (int i = 0; i < GetSpriteAmount(BLUE_GOLEM, targetState); i++) {
            targetArray[targetState][i] = sheet.getSubimage(
                i * BLUE_GOLEM_WIDTH_DEFAULT,
                sourceRow * BLUE_GOLEM_HEIGHT_DEFAULT,
                BLUE_GOLEM_WIDTH_DEFAULT,
                BLUE_GOLEM_HEIGHT_DEFAULT
            );
        }
    }

    private void loadBringerAnimation(BufferedImage[][] imgArr, BufferedImage sheet, int state, int row) {
        for (int i = 0; i < imgArr[state].length; i++) {
            imgArr[state][i] = sheet.getSubimage(
                i * BRINGER_WIDTH_DEFAULT,
                row * BRINGER_HEIGHT_DEFAULT,
                BRINGER_WIDTH_DEFAULT,
                BRINGER_HEIGHT_DEFAULT
            );
        }
    }

    public void resetAllEnemies() {
        for (Enemy e : enemies) {
            e.resetEnemy();
        }
    }
}
```

## File: src/gameStates/PlayStates.java
```java
package gameStates;

import entity.DemonBoss;
import entity.EnemyManager;
import entity.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.GameCore;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import ui.InventoryOverlay;
import utilitytools.LoadSave;
import world.WorldManager;
import static utilitytools.Konstanta.Environment.*;

public class PlayStates extends States implements StateMethods {

    private Player player;
    private WorldManager worldManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameOverOverlay gameOverOverlay;

    private boolean paused = false;
    private boolean lvlCompleted = false;
    private boolean gameOver = false;

    private InventoryOverlay inventoryOverlay;
    private boolean inventoryOpen = false;

    public int xLvlOffset;
    private int leftBorder = (int) (0.2 * GameCore.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * GameCore.GAME_WIDTH);
    private int maxLvlOffsetX;

    private int hitStopDuration = 0;
    private int shakeDuration = 0;
    private int shakeIntensity = 0;

    private boolean isBossEncountered = false;

    private BufferedImage backgroundImg, clouds_01, clouds_02;
    private int[] clouds_02Pos;
    private Random rnd = new Random();

    public PlayStates(GameCore gc) {
        super(gc);
        initClasses();
        calcLvlOffset();
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAY_BACKGROUND_IMG);
        clouds_01 = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS_01);
        clouds_02 = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS_01);
        clouds_02Pos = new int[8];
        for(int i = 0; i < clouds_02Pos.length; i++)
            clouds_02Pos[i] = (int)(90 * GameCore.SCALE) +  rnd.nextInt((int)(100*GameCore.SCALE));
    }

    private void initClasses() {
        worldManager = new WorldManager(gc);
        enemyManager = new EnemyManager(this);

        objectManager = new ObjectManager(this);
        objectManager.loadObjectsFromMap(worldManager.getCurrentMap().getWorldData());

        player = new Player(200, 200, (int) (64 * GameCore.SCALE), (int) (40 * GameCore.SCALE), this);
        player.loadmapData(worldManager.getCurrentMap().getWorldData());

        pauseOverlay = new PauseOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        inventoryOverlay = new InventoryOverlay(this);
    }

    public void loadSelectedLevel(String mapFilePath, String tilesetName) {
        worldManager.loadSpecificWorld(mapFilePath, tilesetName);
        int[][] newMapData = worldManager.getCurrentMap().getWorldData();

        player.loadmapData(newMapData);
        objectManager = new ObjectManager(this);
        objectManager.loadObjectsFromMap(newMapData);
        enemyManager = new EnemyManager(this);

        calcLvlOffset();


        resetAll(200, 400);


        if(tilesetName.equals("main_tileset.png")) {

            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.CASTLE_BACKGROUND_IMG);
        } else {
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAY_BACKGROUND_IMG);
        }
    }

    private void calcLvlOffset() {
        int mapWidth = worldManager.getCurrentMap().getWorldData()[0].length;
        maxLvlOffsetX = (mapWidth - GameCore.TILES_IN_WIDTH) * GameCore.TILES_SIZE;
        if (maxLvlOffsetX < 0) {
            maxLvlOffsetX = 0;
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        }
        else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        }
        if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }

    private void drawClouds(Graphics g) {

    }

    public void triggerHeavyHit(int freezeFrames, int shakeFrames, int intensity) {
        this.hitStopDuration = freezeFrames;
        this.shakeDuration = shakeFrames;
        this.shakeIntensity = intensity;
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (gameOver) {

        } else if (inventoryOpen) {

        } else {
            if (hitStopDuration > 0) {
                hitStopDuration--;
                if (shakeDuration > 0) shakeDuration--;
                return;
            }

            if (shakeDuration > 0) shakeDuration--;

            worldManager.update();
            objectManager.update();
            player.update();
            enemyManager.update(worldManager.getCurrentMap().getWorldData(), player);
            checkCloseToBorder();

            int endOfMapX = (worldManager.getCurrentMap().getWorldData()[0].length * GameCore.TILES_SIZE) - 50;
            if (player.getHitbox().x >= endOfMapX) {
                setLevelCompleted(true);
                gc.getAudioPlayer().lvlCompleted();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int shakeX = 0;
        int shakeY = 0;

        if (shakeDuration > 0) {
            shakeX = rnd.nextInt(shakeIntensity * 2) - shakeIntensity;
            shakeY = rnd.nextInt(shakeIntensity * 2) - shakeIntensity;
        }

        g2.translate(shakeX, shakeY);


        if (backgroundImg != null) {
            float geserX = 0.25f;
            int bgX = (int)(-xLvlOffset * geserX) % GameCore.GAME_WIDTH;
            g.drawImage(backgroundImg, bgX, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
            g.drawImage(backgroundImg, bgX + GameCore.GAME_WIDTH, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT, null);
            drawClouds(g);
        } else {

            g.setColor(new Color(15, 15, 25));
            g.fillRect(0, 0, GameCore.GAME_WIDTH, GameCore.GAME_HEIGHT);
        }

        worldManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);

        g2.translate(-shakeX, -shakeY);

        if (paused) {
            pauseOverlay.draw(g);
        } else if (lvlCompleted) {
            levelCompletedOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (inventoryOpen) {
            inventoryOverlay.draw(g);
        }
    }

    public void loadNextLevel() {
        worldManager.loadNextWorld();
        int[][] newMapData = worldManager.getCurrentMap().getWorldData();
        float newX = 200;
        float newY = 200;
        resetAll(newX, newY);
        player.loadmapData(newMapData);
        objectManager.loadObjectsFromMap(newMapData);
        calcLvlOffset();
        gc.getAudioPlayer().playSong(audio.AudioPlayer.LEVEL_2);
    }

    public void equipPlayerItem(int itemIndex, String slotType) {
        player.equipItem(itemIndex, slotType);
    }

    @Override public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver) return;
        if (inventoryOpen) {
            inventoryOverlay.mousePressed(e);
            return;
        }
        if (paused) {
            pauseOverlay.mousePressed(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mousePressed(e);
        } else if(e.getButton() == MouseEvent.BUTTON1) {
            player.setCharging(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver) return;
        if (paused) {
            pauseOverlay.mouseReleased(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mouseReleased(e);
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            player.releaseAttack();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver) return;
        if (inventoryOpen) {
            inventoryOverlay.mouseMoved(e);
            return;
        }
        if (paused) {
            pauseOverlay.mouseMoved(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (gameOver) return;
        if (paused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
            return;
        }
        if (lvlCompleted) return;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = !paused;
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_F11) {
            gc.getGameFrame().toggleFullScreen();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            inventoryOpen = !inventoryOpen;
            if (inventoryOpen) {
                inventoryOverlay.resetMenu();
            }
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_F5) {
            utilitytools.SaveLoadManager.saveGame(this);
            return;
        }
        if (inventoryOpen) return;
        if (paused) return;

        switch(e.getKeyCode()) {
            case KeyEvent.VK_A: player.setLeft(true); break;
            case KeyEvent.VK_D: player.setRight(true); break;
            case KeyEvent.VK_SPACE: player.setJump(true); break;
            case KeyEvent.VK_Q: player.setDash(true); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (paused || lvlCompleted || gameOver || inventoryOpen) return;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A: player.setLeft(false); break;
            case KeyEvent.VK_D: player.setRight(false); break;
            case KeyEvent.VK_SPACE: player.setJump(false); break;
        }
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public void resetAll(float targetX, float targetY) {
        enemyManager.resetAllEnemies();
        player.resetAll(targetX, targetY);
        paused = false;
        lvlCompleted = false;
        gameOver = false;
        inventoryOpen = false;
        xLvlOffset = 0;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        if (gameOver) {
            gc.getAudioPlayer().stopSong();
            gc.getAudioPlayer().playEffect(audio.AudioPlayer.GAMEOVER);
        }
    }

    public void checkHitEnemy(Rectangle2D.Float AttackBox, int damage) {
        enemyManager.checkEnemyHit(AttackBox, damage, player);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    public boolean isBossEncountered() {
        return isBossEncountered;
    }

    public WorldManager getWorldManager() { return worldManager; }

    public void loadWorldByIndex(int index) {
        worldManager.setWorldIndex(index);
        int[][] currentMapData = worldManager.getCurrentMap().getWorldData();

        player.loadmapData(currentMapData);
        objectManager = new ObjectManager(this);
        objectManager.loadObjectsFromMap(currentMapData);
        enemyManager = new EnemyManager(this);

        calcLvlOffset();
    }

    public void setPaused(boolean paused) { this.paused = paused; }
    public void setLevelCompleted(boolean levelCompleted) { this.lvlCompleted = levelCompleted; }
    public boolean isPaused() { return paused; }
    public Player getPlayer() { return player; }
    public ObjectManager getObjectManager() { return objectManager; }
    public GameCore getGameCore() { return gc; }
}
```

## File: src/entity/Player.java
```java
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
    private int dashDuration = 18;
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
    private boolean attackCheck;
    private Rectangle2D.Float AttackBox;


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


    public ArrayList<Integer> inventory = new ArrayList<>();
    public final int maxInventorySize = 24;



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
            return true;
        } else {
            System.out.println("Penyimpanan Penuh! Tidak bisa mengambil barang.");
            return false;
        }
    }

    public void unequipItem(int equipSlotIndex) {
        if (inventory.size() >= maxInventorySize) {
            System.out.println("Tas penuh, tidak bisa melepas perlengkapan!");
            return;
        }

        int itemToReturn = -1;

        switch (equipSlotIndex) {
            case 0: itemToReturn = equippedHelmet; equippedHelmet = -1; break;
            case 1: itemToReturn = equippedArmor; equippedArmor = -1; break;
            case 2: itemToReturn = equippedShoes; equippedShoes = -1; break;
            case 3: itemToReturn = equippedAcc1; equippedAcc1 = -1; break;
            case 4: itemToReturn = equippedAcc2; equippedAcc2 = -1; break;
            case 5: itemToReturn = equippedGloves; equippedGloves = -1; break;
        }

        if (itemToReturn != -1) {
            inventory.add(itemToReturn);
            calculateDefense();
            System.out.println("Perlengkapan dilepas!");
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

        } else {
            g.drawImage(animasi[playerAction][aniIndex],
                    (int) (hitBox.x - xDrawOffSet) - xLvlOffset + flipX,
                    (int) (hitBox.y - yDrawOffSet),
                    width * flipW, height, null);
        }



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


    public int getEquippedHelmet() { return equippedHelmet; }
    public int getEquippedArmor() { return equippedArmor; }
    public int getEquippedGloves() { return equippedGloves; }
    public int getEquippedShoes() { return equippedShoes; }
    public int getEquippedAcc1() { return equippedAcc1; }
    public int getEquippedAcc2() { return equippedAcc2; }
    public int getTotalDefense() { return totalDefense; }


    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getMaxExp() { return maxExp; }
    public float getDps() { return dps; }
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    public int getCurrentMana() { return currentMana; }
    public int getMaxMana() { return maxMana; }

    public void setLevel(int level) { this.level = level; }
    public void setExp(int exp) { this.exp = exp; }
    public void setMaxExp(int maxExp) { this.maxExp = maxExp; }
    public void setCurrentHealth(int health) { this.currentHealth = health; }
    public void setCurrentMana(int mana) { this.currentMana = mana; }
    public void setEquippedHelmet(int id) { this.equippedHelmet = id; }
    public void setEquippedArmor(int id) { this.equippedArmor = id; }
    public void setEquippedGloves(int id) { this.equippedGloves = id; }
    public void setEquippedShoes(int id) { this.equippedShoes = id; }
    public void setEquippedAcc1(int id) { this.equippedAcc1 = id; }
    public void setEquippedAcc2(int id) { this.equippedAcc2 = id; }
}
```
