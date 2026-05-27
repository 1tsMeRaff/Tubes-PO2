package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

public class SoundButton extends PauseButton {
    private boolean mouseOver;
    private boolean mousePressed;
    private boolean muted;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void update() {
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Antialiasing dinyalakan agar garis tepi tombol dan ikon poligon halus
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- WARNA KHAS RETRO ---
        Color fill = new Color(0, 0, 0, 200); // Latar hitam semi-transparan
        Color border = Color.WHITE;
        Color icon = Color.WHITE;
        
        // Hover: Menggunakan warna kursor emas RyiSnow
        if (mouseOver) {
            border = new Color(240, 190, 90);
        }
        // Pressed: Tombol menyala
        if (mousePressed) {
            fill = new Color(240, 190, 90);
            border = Color.WHITE;
            icon = Color.BLACK;
        }

        // --- BENTUK TOMBOL ---
        int arc = 15; // Sudut diatur statis (tidak terlalu bulat/pill-shape)
        
        // Latar
        g2.setColor(fill);
        g2.fillRoundRect(x, y, width, height, arc, arc);
        
        // Garis Tepi (Lebih tebal agar tegas)
        g2.setColor(border);
        g2.setStroke(new BasicStroke(3f)); 
        g2.drawRoundRect(x, y, width, height, arc, arc);

        // --- MENGGAMBAR IKON SPEAKER ---
        int spW = width / 4;
        int spH = height / 3;
        int spX = x + width / 4;
        int spY = y + height / 2 - spH / 2;
        
        g2.setColor(icon);
        g2.fillRect(spX, spY, spW, spH); // Kotak dasar

        Polygon tri = new Polygon();
        tri.addPoint(spX + spW, spY - (spH/4)); // Atas
        tri.addPoint(spX + spW + spW, spY + (spH/2)); // Kanan (ujung lancip)
        tri.addPoint(spX + spW, spY + spH + (spH/4)); // Bawah
        g2.fillPolygon(tri);

        // --- TANDA SILANG JIKA MUTED ---
        if (muted) {
            g2.setColor(new Color(255, 50, 50)); // Merah cerah
            g2.setStroke(new BasicStroke(4f));
            // Garis coret diagonal
            g2.drawLine(x + width / 5, y + height / 5, x + width - width / 5, y + height - height / 5);
        }

        g2.dispose();
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() { return mouseOver; }
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
    public boolean isMousePressed() { return mousePressed; }
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }
}