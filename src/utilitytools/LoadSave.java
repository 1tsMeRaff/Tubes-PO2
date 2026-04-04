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
	    // Siapkan array kosong sesuai ukuran map
	    int[][] tilesData = new int[GameCore.TILES_IN_HEIGHT][GameCore.TILES_IN_WIDTH];
	    
	    try {
	        // Mengambil file CSV dari folder resources
	        InputStream is = GameCore.class.getResourceAsStream(filePath); 
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        
	        // Looping untuk membaca file baris per baris (Y axis / Height)
	        for (int row = 0; row < GameCore.TILES_IN_HEIGHT; row++) {
	            String line = br.readLine(); // Baca satu baris penuh
	            
	            if (line != null) {
	                // Pisahkan angka berdasarkan tanda koma
	                String[] numbers = line.split(","); 
	                
	                // Looping untuk memasukkan angka ke dalam array (X axis / Width)
	                for (int col = 0; col < GameCore.TILES_IN_WIDTH; col++) {
	                    // Tiled kadang memberi nilai -1 untuk tile kosong, atau ID yang besar.
	                    // Pastikan array animasi/tile kamu tidak out of bounds saat membaca ID ini nanti.
	                    tilesData[row][col] = Integer.parseInt(numbers[col].trim());
	                }
	            }
	        }
	        br.close(); // Jangan lupa tutup reader
	        
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
