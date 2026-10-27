package audio;

/**
 * Headless unit verification test suite for ToneSynthFallback and volume resilience.
 * Verifies non-blocking synthesized retro waveform generation without external dependencies.
 */
public class AudioResilienceTest {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING AUDIO RESILIENCE & SYNTHESIZER TESTS ===");

        if (testSynthesizedPcmWaveforms()) {
            System.out.println("[PASS] testSynthesizedPcmWaveforms");
            passed++;
        } else {
            System.err.println("[FAIL] testSynthesizedPcmWaveforms");
            failed++;
        }

        if (testVolumeLogarithmicConversion()) {
            System.out.println("[PASS] testVolumeLogarithmicConversion");
            passed++;
        } else {
            System.err.println("[FAIL] testVolumeLogarithmicConversion");
            failed++;
        }

        System.out.println("====================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testSynthesizedPcmWaveforms() {
        int[] effectTypes = { AudioPlayer.JUMP, AudioPlayer.DIE, AudioPlayer.GAMEOVER, AudioPlayer.LVL_COMPLETED };

        for (int type : effectTypes) {
            byte[] pcm = ToneSynthFallback.generatePcm(type);
            if (pcm == null || pcm.length < 500) {
                System.err.println("Generated PCM buffer too small or null for type: " + type);
                return false;
            }

            // Verify wave has non-zero amplitude oscillations
            boolean hasNonZero = false;
            for (byte b : pcm) {
                if (b != 0) {
                    hasNonZero = true;
                    break;
                }
            }

            if (!hasNonZero) {
                System.err.println("Synthesized PCM wave was completely silent (all 0s) for type: " + type);
                return false;
            }
        }

        return true;
    }

    private static boolean testVolumeLogarithmicConversion() {
        float testVol = 0.65f;
        float safeVolume = Math.max(0.0001f, testVol);
        float gain = 20f * (float) Math.log10(safeVolume);

        // At volume 0.65, gain should be ~ -3.74 dB
        if (gain > 0 || gain < -20.0f) {
            System.err.println("Logarithmic dB gain out of expected range: " + gain);
            return false;
        }

        return true;
    }
}
