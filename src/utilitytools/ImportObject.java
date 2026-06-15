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