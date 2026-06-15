package utilitytools;

import java.awt.geom.Rectangle2D;
import main.GameCore;


public class HelpMethods {

    public static boolean isSolidTile(int tileID) {
        return tileID != -1;
    }

    public static boolean isSolid(float x, float y, int[][] lvlData) {
        int xIndex = (int) (x / GameCore.TILES_SIZE);
        int yIndex = (int) (y / GameCore.TILES_SIZE);

        if (yIndex < 0 || yIndex >= lvlData.length ||
            xIndex < 0 || xIndex >= lvlData[yIndex].length) {
            return true;
        }

        return isSolidTile(lvlData[yIndex][xIndex]);
    }

    public static boolean isSolidTile(int xTile, int yTile, int[][] lvlData) {
        if (yTile < 0 || yTile >= lvlData.length ||
            xTile < 0 || xTile >= lvlData[yTile].length) {
            return true;
        }
        return isSolidTile(lvlData[yTile][xTile]);
    }

    public static boolean canMoveHere(float x, float y, float width, float height, int[][] tilesData) {
        float leftX = x;
        float rightX = x + width - 0.01f;  // Ganti -1 menjadi -0.01f
        float topY = y;
        float bottomY = y + height - 0.01f; // Ganti -1 menjadi -0.01f

        int leftCol = (int) (leftX / GameCore.TILES_SIZE);
        int rightCol = (int) (rightX / GameCore.TILES_SIZE);
        int topRow = (int) (topY / GameCore.TILES_SIZE);
        int bottomRow = (int) (bottomY / GameCore.TILES_SIZE);

        for (int row = topRow; row <= bottomRow; row++) {
            for (int col = leftCol; col <= rightCol; col++) {
                if (row < 0 || row >= tilesData.length ||
                    col < 0 || col >= tilesData[row].length) {
                    return false;
                }
                int tileID = tilesData[row][col];
                if (isSolidTile(tileID)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static float GetEntityPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        if (xSpeed > 0) {
            // Pemain menabrak dinding di kanan.
            // Cari posisi batas kanan dari tile yang sedang ditempati pemain.
            int currentTileRight = (int) ((hitBox.x + hitBox.width) / GameCore.TILES_SIZE);
            int tileRightEdge = (currentTileRight + 1) * GameCore.TILES_SIZE;
            
            // Beri jarak sangat kecil (0.01f) agar tidak menyentuh tile dinding
            return tileRightEdge - hitBox.width - 0.01f;
        } else {
            // Pemain menabrak dinding di kiri.
            int currentTileLeft = (int) (hitBox.x / GameCore.TILES_SIZE);
            return currentTileLeft * GameCore.TILES_SIZE;
        }
    }

//    public static float GetEntityPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
//        int currentTile = (int) (hitBox.x / GameCore.TILES_SIZE);
//        if (xSpeed > 0) {
//            // Bergerak ke kanan → tempelkan ke batas kiri tile berikutnya
//            int tileXpos = currentTile * GameCore.TILES_SIZE;
//            int xOffSet = (int) (GameCore.TILES_SIZE - hitBox.width);
//            return tileXpos + xOffSet - 1;
//        } else {
//            // Bergerak ke kiri → tempelkan ke batas kiri tile saat ini
//            return currentTile * GameCore.TILES_SIZE;
//        }
//    }
    
    public static float GetEntityPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
        float yAfterMove = hitBox.y + airSpeed;
        if (airSpeed > 0) {
            // Jatuh mendarat di lantai
            int currentTileBottom = (int) ((yAfterMove + hitBox.height) / GameCore.TILES_SIZE);
            int tileYPos = currentTileBottom * GameCore.TILES_SIZE;
            return tileYPos - hitBox.height - 0.01f; 
        } else {
            // Melompat menabrak atap
            int currentTileTop = (int) (yAfterMove / GameCore.TILES_SIZE);
            int tileYPos = currentTileTop * GameCore.TILES_SIZE;
            return tileYPos + GameCore.TILES_SIZE;
        }
    }

//    public static float GetEntityPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
//        float yAfterMove = hitBox.y + airSpeed;
//        if (airSpeed > 0) {
//            // Jatuh ke bawah: pakai posisi setelah bergerak agar tile lantai tepat
//            int currentTile = (int) ((yAfterMove + hitBox.height) / GameCore.TILES_SIZE);
//            int tileYPos = currentTile * GameCore.TILES_SIZE;
//            return tileYPos - hitBox.height;
//        } else {
//            // Mentok ke langit-langit: pakai posisi setelah bergerak agar tile plafon tepat
//            int currentTile = (int) (yAfterMove / GameCore.TILES_SIZE);
//            int tileYPos = currentTile * GameCore.TILES_SIZE;
//            return tileYPos + GameCore.TILES_SIZE;
//        }
//    }
    
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Gunakan && (DAN) - Pemain baru jatuh jika KEDUA titik kakinya tidak ada lantai
        // Kurangi 0.01f pada pengecekan sisi kanan untuk menghindari pembacaan tile tetangga
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData) &&
            !isSolid(hitbox.x + hitbox.width - 0.01f, hitbox.y + hitbox.height + 1, lvlData)) {
            return false; 
        }
        return true; 
    }

//    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
//        // Jika salah satu titik di bawah tidak solid → entitas tidak di lantai
//        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData) ||
//            !isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
//            return false;
//        }
//        return true;
//    }

    public static boolean IsSightClear(int[][] tilesData,
                                       Rectangle2D.Float firstHitBox,
                                       Rectangle2D.Float secondHitBox,
                                       int tileY) {
        int firstXTile = (int) (firstHitBox.x / GameCore.TILES_SIZE);
        int secondXTile = (int) (secondHitBox.x / GameCore.TILES_SIZE);

        int leftXTile = Math.min(firstXTile, secondXTile);
        int rightXTile = Math.max(firstXTile, secondXTile);

        // Periksa tile di antara, tidak termasuk tile tempat mereka berdiri
        for (int x = leftXTile + 1; x < rightXTile; x++) {
            if (isSolidTile(x, tileY, tilesData)) {
                return false;   // ada penghalang 
            }
        }
        return true;   // tidak ada penghalang
    }

    public static boolean isFloor(Rectangle2D.Float hitBox, float xSpeed, int[][] tilesData) {
        if (xSpeed > 0) {
            // Jika bergerak ke kanan, cek lantai di bawah sisi KANAN (depan) hitbox
            return isSolid(hitBox.x + hitBox.width + xSpeed, hitBox.y + hitBox.height + 1, tilesData);
        } else {
            // Jika bergerak ke kiri, cek lantai di bawah sisi KIRI (depan) hitbox
            return isSolid(hitBox.x + xSpeed, hitBox.y + hitBox.height + 1, tilesData);
        }
    }
}