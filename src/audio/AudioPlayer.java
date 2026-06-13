package audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.Random;

public class AudioPlayer {

	// BGM Index
	public static int MENU_1 = 0;
	public static int LEVEL_1 = 1;
	public static int LEVEL_2 = 2;

	// SFX Index
	public static int DIE = 0;
	public static int JUMP = 1;
	public static int GAMEOVER = 2;
	public static int LVL_COMPLETED = 3;
	public static int ATTACK_ONE = 4;
	public static int ATTACK_TWO = 5;
	public static int ATTACK_THREE = 6;

	private Clip[] songs, effects;
	private int currentSongId;
	private float volume = 0.8f; // Volume standar (50%)
	private boolean songMute, effectMute;
	private Random rand = new Random();

	public AudioPlayer() {
		loadSongs();
		loadEffects();
		playSong(MENU_1); // Putar lagu menu saat game pertama dibuka
	}

	private void loadSongs() {
		// Nama file BGM harus persis dengan yang ada di src/resources/audio/
		String[] names = { "menu", "level1", "level2" };
		songs = new Clip[names.length];
		for (int i = 0; i < songs.length; i++) {
			songs[i] = getClip(names[i]);
		}
	}

	private void loadEffects() {
		// Nama file SFX harus persis dengan yang ada di src/resources/audio/
		String[] names = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3" };
		effects = new Clip[names.length];
		for (int i = 0; i < effects.length; i++) {
			effects[i] = getClip(names[i]);
		}
		
		updateEffectsVolume();
	}

	private Clip getClip(String name) {
//		URL url = getClass().getResource("/audio/" + name + ".wav");
		URL url = getClass().getResource("/resources/audio/" + name + ".wav");
		
		// [SISTEM ANTI-CRASH] Jika file belum ada, jangan lakukan apa-apa
		if (url == null) {
			System.out.println("Audio belum siap: " + name + ".wav (Menunggu aset dari ketua)");
			return null; 
		}

		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(audio);
			return c;
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
		updateEffectsVolume();
	}

	public void stopSong() {
		if (songs[currentSongId] != null) {
			if (songs[currentSongId].isActive()) {
				songs[currentSongId].stop();
			}
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
		int start = 4;
		start += rand.nextInt(3); // Pilih acak antara attack 1, 2, atau 3
		playEffect(start);
	}

	public void playEffect(int effect) {
		if (effects[effect] != null) {
			effects[effect].setMicrosecondPosition(0);
			effects[effect].start();
		}
	}

	public void playSong(int song) {
		stopSong();

		currentSongId = song;
		updateSongVolume();
		
		if (songs[currentSongId] != null) {
			songs[currentSongId].setMicrosecondPosition(0);
			songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY); // BGM diulang terus menerus
		}
	}

	public void toggleSongMute() {
		this.songMute = !songMute;
		for (Clip c : songs) {
			if (c != null) {
				BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
				booleanControl.setValue(songMute);
			}
		}
	}

	public void toggleEffectMute() {
		this.effectMute = !effectMute;
		for (Clip c : effects) {
			if (c != null) {
				BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
				booleanControl.setValue(effectMute);
			}
		}
		if (!effectMute) {
			playEffect(JUMP); // Suara feedback saat SFX dinyalakan
		}
	}

	private void updateSongVolume() {
		if (songs[currentSongId] != null) {
			FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}

	private void updateEffectsVolume() {
		for (Clip c : effects) {
			if (c != null) {
				FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
				float range = gainControl.getMaximum() - gainControl.getMinimum();
				float gain = (range * volume) + gainControl.getMinimum();
				gainControl.setValue(gain);
			}
		}
	}
}