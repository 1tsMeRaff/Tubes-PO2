package utilitytools;

import java.util.ArrayList;
import main.GameCore;
import objects.GameContainer;
import objects.Potion;
import static utilitytools.Konstanta.ObjectConstants.*;

public class ImportObject {

    // Method static agar bisa dipanggil langsung tanpa 'new ImportObject()'
    public static void GetObjectsFromMap(int[][] mapData, ArrayList<Potion> potions, ArrayList<GameContainer> containers) {
        
        // 1. Bersihkan arena dari objek map sebelumnya
        potions.clear();
        containers.clear();
        
        // 2. Scan seluruh baris dan kolom CSV
        for (int j = 0; j < mapData.length; j++) {
            for (int i = 0; i < mapData[0].length; i++) {
                
                int id = mapData[j][i];
                
                // Ubah kordinat kotak CSV jadi kordinat piksel di layar
                int xPos = i * GameCore.TILES_SIZE;
                int yPos = j * GameCore.TILES_SIZE;

                // 3. Munculkan objek sesuai ID
                
                // --- RAMUAN MERAH (Seri 300) ---
                if (id == 300) { potions.add(new Potion(xPos, yPos, RED_POTION_1)); mapData[j][i] = 11; }
                else if (id == 301) { potions.add(new Potion(xPos, yPos, RED_POTION_2)); mapData[j][i] = 11; }
                else if (id == 302) { potions.add(new Potion(xPos, yPos, RED_POTION_3)); mapData[j][i] = 11; }
                
                // --- RAMUAN BIRU (Seri 300) ---
                else if (id == 303) { potions.add(new Potion(xPos, yPos, BLUE_POTION_1)); mapData[j][i] = 11; }
                else if (id == 304) { potions.add(new Potion(xPos, yPos, BLUE_POTION_2)); mapData[j][i] = 11; }
                else if (id == 305) { potions.add(new Potion(xPos, yPos, BLUE_POTION_3)); mapData[j][i] = 11; }
                
                // --- CONTAINER (KOTAK & TONG) ---
                else if (id == 401) { containers.add(new GameContainer(xPos, yPos, BOX)); mapData[j][i] = 11; }
                else if (id == 402) { containers.add(new GameContainer(xPos, yPos, BARREL)); mapData[j][i] = 11; }
            }
        }
    }
}