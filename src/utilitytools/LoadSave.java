package utilitytools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GameCore;

public class LoadSave {

	public static final String PLAYER_SPRITE = "player_right.png";
	public static final String WORLD_SPRITE = "main_tileset.png";
	public static final String MAP_1_DATA = "map_1_data.png";
	public static final String MENU_FRAMES = "Menu_Frames.png";
	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		
		BufferedImage image = null;
		InputStream is = LoadSave.class.getResourceAsStream("/"+ fileName);
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return image;
	}
	
	public static int[][] GetTilesData(String filePath) {
	    int[][] tilesData = new int[GameCore.TILES_IN_HEIGHT][GameCore.TILES_IN_WIDTH];
	    
	    try {
	        InputStream is = GameCore.class.getResourceAsStream(filePath); 
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        
	        for (int row = 0; row < GameCore.TILES_IN_HEIGHT; row++) {
	            String line = br.readLine();
	            
	            if (line != null) {
	                String[] numbers = line.split(","); 
	                
	                for (int col = 0; col < GameCore.TILES_IN_WIDTH; col++) {
	                    tilesData[row][col] = Integer.parseInt(numbers[col].trim());
	                }
	            }
	        }
	        br.close();
	        
	    } catch (Exception e) {
	        System.out.println("Gagal memuat map!");
	        e.printStackTrace();
	    }
	    
	    return tilesData;
	}
	
//	public static int[][] GetTilesData(){
//		
//		BufferedImage image = GetSpriteAtlas(MAP_1_DATA);
//		int[][] tilesData = new int[GameCore.TILES_IN_HEIGHT][GameCore.TILES_IN_WIDTH];
//		
//		for(int j = 0; j < image.getHeight(); j++) {
//			for(int i = 0; i < image.getWidth(); i++) {
//				Color color = new Color(image.getRGB(i, j));
//				int value = color.getRed();
//				if(value >= 150) {
//					value = 0;
//				}
//				tilesData[j][i] = color.getRed();
//			}
//		}
//		return tilesData;
//	}
}
