package utilitytools;

import entity.Slime;
import entity.BlueGolem;
import entity.DemonBoss;
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
//	public static final String MAP_TUTORIAL = "map_tutorial.png";
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
	
	
//	public static final String MENU_BACKGROUND = "MediavelFree.png";
	public static final String MENU_BACKGROUND_IMG = "mainn_menu.jpeg";
//	public static final String PLAY_BACKGROUND_IMG = "Background_0.png";
	public static final String PLAY_BACKGROUND_IMG = "Background_jungle2.png";
	public static final String CLOUDS_01 = "awan_01.png";
	public static final String CLOUDS_02 = "awan_02.png";
	
	public static final String STATUS_BAR = "statusbar.png";
	public static final String MENU_PANEL = "menu_panel.png";
	// --- ASET UNTUK INVENTORY ---
	public static final String INVENTORY_BG = "inventory_skill_karakter_panel.png";
	// --- ASET BARU UNTUK PAUSE MENU ---
    public static final String PAUSE_TITLE = "pause.png";
    public static final String PAUSE_MUSIC_TEXT = "music.png";
    public static final String PAUSE_SE_TEXT = "SE.png";
    public static final String PAUSE_VOL_TEXT = "volume.png";
    
    // Tombol-tombol
    public static final String PAUSE_PAW_BTN = "paw_button.png";
    public static final String PAUSE_RESUME_BTN = "resume_button.png";
    public static final String PAUSE_RESTART_BTN = "restart_button.png";
    public static final String PAUSE_MENU_BTN = "menu_button.png";
    
    // (Opsional) Jika kamu mau pakai rantainya sebagai hiasan di belakang bel
    public static final String PAUSE_CHAIN = "rantai.png";

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
					if (value == 800 || value == 801) {
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
					
					// Mengakomodasi format ID branch dev (200) dan dev-Rafi (2000)
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

	                // Kita gunakan angka 802 sebagai ID penanda Blue Golem di file CSV/TXT map
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