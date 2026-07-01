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