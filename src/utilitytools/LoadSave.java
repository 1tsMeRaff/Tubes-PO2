package utilitytools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.Slime;
import main.GameCore;
import static utilitytools.Konstanta.UI.PauseButtons.*;
import static utilitytools.Konstanta.EnemyConstants.SLIME;

public class LoadSave {

	public static final String PLAYER_SPRITE = "player_right.png";
	public static final String WORLD_SPRITE = "main_tileset.png";
	public static final String MAP_1_DATA = "map_1_data.png";
	public static final String MENU_BUTTONS = "Menu_Frames.png";
	public static final String MENU_BACKGROUND = "Frames_baru.png";
	public static final String PAUSE_BACKGROUND = "pause_background.png";
	public static final String SOUND_BUTTONS = "sound_buttons.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String VOLUME_SLIDER = "volume_slider.png";
	
	
	public static final String SLIME_SPRITE = "enemy_slime.png";
//	public static final String MENU_BACKGROUND = "MediavelFree.png";
	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		
		BufferedImage image = null;
		InputStream is = LoadSave.class.getResourceAsStream("/"+ fileName);
		if (is == null) {
			return createPlaceholder(fileName);
		}
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
	
	public static ArrayList<Slime> GetSlimes(String filePath) {
	    ArrayList<Slime> list = new ArrayList<>();
	    
	    int[][] levelData = GetTilesData(filePath);
	    
	    for (int row = 0; row < GameCore.TILES_IN_HEIGHT; row++) {
	        for (int col = 0; col < GameCore.TILES_IN_WIDTH; col++) {
	            
	            int value = levelData[row][col];
	            
	            if (value == SLIME) { 
	                int xPos = col * GameCore.TILES_SIZE; 
	                int yPos = row * GameCore.TILES_SIZE;
	                
	                list.add(new Slime(xPos, yPos));
	            }
	        }
	    }
	    return list;
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
