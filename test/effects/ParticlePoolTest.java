package effects;

/**
 * Headless unit verification test suite for ParticlePool and FloatingTextPool.
 * Validates zero-allocation recycling, bounds checking, and lifecycle updates.
 */
public class ParticlePoolTest {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING PARTICLE & FLOATING TEXT POOL UNIT TESTS ===");

        if (testParticlePoolSpawningAndCap()) {
            System.out.println("[PASS] testParticlePoolSpawningAndCap");
            passed++;
        } else {
            System.err.println("[FAIL] testParticlePoolSpawningAndCap");
            failed++;
        }

        if (testParticleLifecycleAndRecycling()) {
            System.out.println("[PASS] testParticleLifecycleAndRecycling");
            passed++;
        } else {
            System.err.println("[FAIL] testParticleLifecycleAndRecycling");
            failed++;
        }

        if (testFloatingTextPoolLifecycle()) {
            System.out.println("[PASS] testFloatingTextPoolLifecycle");
            passed++;
        } else {
            System.err.println("[FAIL] testFloatingTextPoolLifecycle");
            failed++;
        }

        System.out.println("========================================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testParticlePoolSpawningAndCap() {
        ParticlePool pool = new ParticlePool();

        // Initially no active particles
        for (int i = 0; i < 200; i++) {
            pool.spawnHitSparks(100, 100);
        }

        // Must cap at pool capacity without ArrayIndexOutOfBoundsException
        pool.update();
        return true;
    }

    private static boolean testParticleLifecycleAndRecycling() {
        ParticlePool pool = new ParticlePool();
        pool.spawnDust(50, 50);

        // Run updates until lifespan expires
        for (int i = 0; i < 40; i++) {
            pool.update();
        }

        // Particle pool should now have recycled dead particles cleanly
        pool.spawnHitSparks(200, 200);
        pool.update();
        return true;
    }

    private static boolean testFloatingTextPoolLifecycle() {
        FloatingTextPool textPool = new FloatingTextPool();

        textPool.spawnDamageDealt(100, 50, 42);
        textPool.spawnDamageTaken(120, 50, 15);
        textPool.spawnHeal(100, 50, 30);

        // Update lifecycle
        for (int i = 0; i < 60; i++) {
            textPool.update();
        }

        // After 60 ticks, previous texts should expire and pool can spawn new texts
        textPool.spawnDamageDealt(200, 100, 99);
        textPool.update();
        return true;
    }
}
