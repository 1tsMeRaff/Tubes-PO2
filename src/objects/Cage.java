package objects;

public class Cage extends GameObject {
    private int blinkTick = 0;
    private boolean blinking = false;

    public Cage(int x, int y, int objType) {
        super(x, y, objType);
        
        // Atur ukuran hitbox kandang
        initHitbox(32, 32); 
        xDrawOffset = 0;
        yDrawOffset = 0;
    }

    public void update() {
        if (blinking) {
            blinkTick++;
            // Berkedip selama 40 frame sebelum akhirnya hancur
            if (blinkTick >= 40) { 
                blinking = false;
                setActive(false); // Objek dihancurkan
            }
        }
    }

    // Fungsi ini dipanggil saat pedang mengenai kandang
    public void setHit() {
        if (!blinking) {
            blinking = true;
        }
    }

    public boolean isBlinking() { return blinking; }
    public int getBlinkTick() { return blinkTick; }
}