package objects;

public class Sign extends GameObject {
    private String middleText;
    private String topText;

    public Sign(int x, int y, int objType, String middleText, String topText) {
        super(x, y, objType);
        this.middleText = middleText;
        this.topText = topText;
        

        initHitbox(17, 17);
        xDrawOffset = 0;
        yDrawOffset = 0;
    }

    public String getMiddleText() { return middleText; }
    public String getTopText() { return topText; }
}