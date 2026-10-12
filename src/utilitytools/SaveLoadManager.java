package utilitytools;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import main.GameCore;
import entity.Player;
import gameStates.PlayStates;

public class SaveLoadManager {
    private static final String SAVE_FILE = "savegame.txt";

    public static void saveGame(PlayStates playStates) {
        Path targetPath = Paths.get(SAVE_FILE);
        Path tempPath = Paths.get(SAVE_FILE + ".tmp");

        try {
            Player p = playStates.getPlayer();

            try (BufferedWriter writer = Files.newBufferedWriter(tempPath, StandardCharsets.UTF_8)) {
                writer.write("level=" + p.getLevel()); writer.newLine();
                writer.write("exp=" + p.getExp()); writer.newLine();
                writer.write("maxExp=" + p.getMaxExp()); writer.newLine();
                writer.write("currentHealth=" + p.getCurrentHealth()); writer.newLine();
                writer.write("currentMana=" + p.getCurrentMana()); writer.newLine();
                writer.write("x=" + p.getHitbox().x); writer.newLine();
                writer.write("y=" + p.getHitbox().y); writer.newLine();

                writer.write("equippedHelmet=" + p.getEquippedHelmet()); writer.newLine();
                writer.write("equippedArmor=" + p.getEquippedArmor()); writer.newLine();
                writer.write("equippedGloves=" + p.getEquippedGloves()); writer.newLine();
                writer.write("equippedShoes=" + p.getEquippedShoes()); writer.newLine();
                writer.write("equippedAcc1=" + p.getEquippedAcc1()); writer.newLine();
                writer.write("equippedAcc2=" + p.getEquippedAcc2()); writer.newLine();

                StringBuilder invStr = new StringBuilder("inventory=");
                for (int i = 0; i < p.inventory.size(); i++) {
                    invStr.append(p.inventory.get(i));
                    if (i < p.inventory.size() - 1) invStr.append(",");
                }
                writer.write(invStr.toString()); writer.newLine();

                writer.write("worldIndex=" + playStates.getWorldManager().getWorldIndex()); writer.newLine();
            }

            try {
                Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("Game saved successfully to " + SAVE_FILE);
        } catch (Exception e) {
            System.err.println("Failed to save game!");
            e.printStackTrace();
        }
    }

    public static boolean loadGame(GameCore gc) {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No save file found!");
            return false;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name())) {
            gc.getPlay().resetAll(200, 200); 
            Player p = gc.getPlay().getPlayer();
            int wIndex = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.contains("=")) continue;
                
                String[] parts = line.split("=", 2);
                String key = parts[0].trim();
                String value = parts.length > 1 ? parts[1].trim() : "";

                try {
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
                                    if (!item.trim().isEmpty()) {
                                        p.inventory.add(Integer.parseInt(item.trim()));
                                    }
                                }
                            }
                            break;
                            
                        case "worldIndex": 
                            wIndex = Integer.parseInt(value); 
                            break;
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("Skipping malformed save line: " + line);
                }
            }
            p.calculateDefense();
            gc.getPlay().loadWorldByIndex(wIndex);

            System.out.println("Game loaded successfully!");
            return true;
        } catch (Exception e) {
            System.err.println("Failed to load game!");
            e.printStackTrace();
            return false;
        }
    }
}