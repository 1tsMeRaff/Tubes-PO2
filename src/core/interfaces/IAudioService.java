package core.interfaces;

/**
 * Service abstraction for the audio playback subsystem.
 * Part of DIP (Dependency Inversion Principle) modernization.
 */
public interface IAudioService {
    void playSong(int song);
    void stopSong();
    void playEffect(int effect);
    void playAttackSound();
    void setLevelSong(int lvlIndex);
    void lvlCompleted();
    void setVolume(float volume);
    void toggleSongMute();
    void toggleEffectMute();
    void destroy();
}
