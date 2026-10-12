package audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

/**
 * Pure Java SE arcade tone synthesizer fallback.
 * Generates dynamic 8-bit retro sound effects when physical .wav files
 * are missing on disk, eliminating silent actions (adheres to DIP and Audio Resilience).
 */
public class ToneSynthFallback {

    public static final float SAMPLE_RATE = 8000.0f;

    public static Clip createFallbackClip(int effectType) {
        byte[] pcmData = generatePcm(effectType);
        if (pcmData == null || pcmData.length == 0) {
            return null;
        }

        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(format, pcmData, 0, pcmData.length);
            return clip;
        } catch (LineUnavailableException | IllegalArgumentException e) {
            // Headless environments without audio devices return null gracefully
            return null;
        }
    }

    public static byte[] generatePcm(int effectType) {
        switch (effectType) {
            case AudioPlayer.JUMP:
                return generateJumpSound();
            case AudioPlayer.DIE:
                return generateDieSound();
            case AudioPlayer.GAMEOVER:
                return generateGameOverSound();
            case AudioPlayer.LVL_COMPLETED:
                return generateVictorySound();
            default:
                return generateBlipSound();
        }
    }

    private static byte[] generateJumpSound() {
        int samples = (int) (0.13f * SAMPLE_RATE);
        byte[] buffer = new byte[samples];
        double phase = 0;

        for (int i = 0; i < samples; i++) {
            float progress = (float) i / samples;
            float freq = 260.0f + (380.0f * progress);
            phase += 2.0 * Math.PI * freq / SAMPLE_RATE;

            float env = (progress < 0.1f) ? (progress / 0.1f) : (1.0f - progress);
            buffer[i] = (byte) (105.0 * Math.sin(phase) * env);
        }
        return buffer;
    }

    private static byte[] generateDieSound() {
        int samples = (int) (0.24f * SAMPLE_RATE);
        byte[] buffer = new byte[samples];
        double phase = 0;

        for (int i = 0; i < samples; i++) {
            float progress = (float) i / samples;
            float freq = Math.max(80.0f, 480.0f - (380.0f * progress));
            phase += 2.0 * Math.PI * freq / SAMPLE_RATE;

            float env = (1.0f - progress);
            // Slight square wave grit for retro hit crunch
            double raw = Math.sin(phase) > 0 ? 0.8 : -0.8;
            buffer[i] = (byte) (100.0 * raw * env);
        }
        return buffer;
    }

    private static byte[] generateGameOverSound() {
        int noteSamples = (int) (0.14f * SAMPLE_RATE);
        float[] notes = { 330.0f, 293.0f, 220.0f };
        byte[] buffer = new byte[noteSamples * notes.length];

        int idx = 0;
        for (float noteFreq : notes) {
            double phase = 0;
            for (int i = 0; i < noteSamples; i++) {
                float progress = (float) i / noteSamples;
                phase += 2.0 * Math.PI * noteFreq / SAMPLE_RATE;
                float env = (progress < 0.05f) ? (progress / 0.05f) : (1.0f - (progress * 0.9f));
                buffer[idx++] = (byte) (100.0 * Math.sin(phase) * env);
            }
        }
        return buffer;
    }

    private static byte[] generateVictorySound() {
        float[] notes = { 440.0f, 554.0f, 659.0f, 880.0f };
        int[] durations = { (int) (0.09f * SAMPLE_RATE), (int) (0.09f * SAMPLE_RATE), 
                            (int) (0.09f * SAMPLE_RATE), (int) (0.22f * SAMPLE_RATE) };

        int totalSamples = 0;
        for (int d : durations) totalSamples += d;

        byte[] buffer = new byte[totalSamples];
        int idx = 0;

        for (int n = 0; n < notes.length; n++) {
            float noteFreq = notes[n];
            int noteLen = durations[n];
            double phase = 0;

            for (int i = 0; i < noteLen; i++) {
                float progress = (float) i / noteLen;
                phase += 2.0 * Math.PI * noteFreq / SAMPLE_RATE;
                float env = (progress < 0.05f) ? (progress / 0.05f) : (1.0f - (progress * 0.8f));
                buffer[idx++] = (byte) (110.0 * Math.sin(phase) * env);
            }
        }
        return buffer;
    }

    private static byte[] generateBlipSound() {
        int samples = (int) (0.08f * SAMPLE_RATE);
        byte[] buffer = new byte[samples];
        double phase = 0;

        for (int i = 0; i < samples; i++) {
            float progress = (float) i / samples;
            phase += 2.0 * Math.PI * 520.0 / SAMPLE_RATE;
            float env = 1.0f - progress;
            buffer[i] = (byte) (90.0 * Math.sin(phase) * env);
        }
        return buffer;
    }
}
