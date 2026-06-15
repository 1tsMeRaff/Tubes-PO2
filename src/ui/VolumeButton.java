package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class VolumeButton extends PauseButton {
    
    private int buttonX, minX, maxX;
    private int sliderWidth;
    
    // Variabel ukuran kotak pegangan slider
    private int knobWidth;
    private int knobHeight;
    
    private boolean mouseOver, mousePressed;

    public VolumeButton(int x, int y, int width, int height, int sliderWidth, int sliderHeight) {
        super(x, y, sliderWidth, height); 
        this.sliderWidth = sliderWidth;
        
        // --- PERBAIKAN UKURAN KOTAK SLIDER (KNOB) ---
        // Kita rampingkan menjadi setengah dari lebar bawaan
        this.knobWidth = width / 2; 
        // Kita pendekkan tingginya menjadi sekitar 60% dari tinggi bawaan
        this.knobHeight = (int)(height * 0.6); 
        
        this.minX = x; 
        this.maxX = x + sliderWidth - knobWidth; 
        this.buttonX = minX + (sliderWidth / 2) - (knobWidth / 2); 
    }

    public void update() {
        bounds.x = buttonX; 
        bounds.width = knobWidth;
        // Hitbox mengikuti tinggi kotak knob agar lebih presisi
        bounds.y = y + (height / 2) - (knobHeight / 2);
        bounds.height = knobHeight;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int trackY = y + (height / 2) - 4;
        int trackHeight = 8; 

        // 1. Gambar Jalur Slider (Background)
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRect(x, trackY, sliderWidth, trackHeight);

        // 2. Gambar Isi Volume (Warna Emas)
        float volumeRatio = 0;
        if (maxX - minX > 0) {
            volumeRatio = (float) (buttonX - minX) / (maxX - minX);
        }
        int fillWidth = (int) (sliderWidth * volumeRatio);
        
        g2.setColor(new Color(240, 190, 90)); 
        g2.fillRect(x + 2, trackY + 2, Math.max(0, fillWidth - 4), trackHeight - 3);

        // 3. Gambar Knob (Pegangan)
        int knobY = y + (height / 2) - (knobHeight / 2);

        Color knobColor = mouseOver ? new Color(240, 190, 90) : Color.WHITE;
        if (mousePressed) {
            knobColor = new Color(200, 150, 50); 
        }
        
        g2.setColor(knobColor);
        g2.fillRect(buttonX, knobY, knobWidth, knobHeight);
        
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(buttonX, knobY, knobWidth, knobHeight);

        g2.dispose();
    }

    public void changeX(int xPos) {
        if (xPos < minX) {
            buttonX = minX;
        } else if (xPos > maxX) {
            buttonX = maxX;
        } else {
            buttonX = xPos;
        }
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    // --- TAMBAHAN FUNGSI UNTUK MENGAMBIL NILAI VOLUME ---
    // Menghasilkan angka antara 0.0f (muting) sampai 1.0f (full volume)
    public float GetFloatValue() {
        if (maxX - minX > 0) {
            return (float) (buttonX - minX) / (maxX - minX);
        }
        return 0f;
    }

    // --- GETTERS AND SETTERS ---
    
    public boolean isMouseOver() { 
        return mouseOver; 
    }
    
    public void setMouseOver(boolean mouseOver) { 
        this.mouseOver = mouseOver; 
    }
    
    public boolean isMousePressed() { 
        return mousePressed; 
    }
    
    public void setMousePressed(boolean mousePressed) { 
        this.mousePressed = mousePressed; 
    }
}