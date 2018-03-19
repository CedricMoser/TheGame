package graphics;

public class Color {
    public static final Color WHITE = new Color(0xFFFFFFFF);
    public static final Color BLACK = new Color(0x00000000);
    public static final Color BLUE  = new Color(0x0000FFFF);
    public static final Color RED   = new Color(0xFF0000FF);
    public static final Color GREEN = new Color(0x00FF00FF);

    private int mColor;

    public Color() {
        this.mColor = 0x00000000;
    }

    public Color(int color) {
        this.mColor = color;
    }

    public Color(int r, int g, int b, int a) {
        this.mColor = (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | (a & 0xFF);
    }

    public int getR() {
        return (this.mColor >> 24) & 0xFF;
    }

    public float getRf() {
        return ((this.mColor >> 24) & 0xFF) / 255.0f;
    }

    public int getG() {
        return (this.mColor >> 16) & 0xFF;
    }

    public float getGf() {
        return ((this.mColor >> 16) & 0xFF) / 255.0f;
    }

    public int getB() {
        return (this.mColor >> 8) & 0xFF;
    }

    public float getBf() {
        return ((this.mColor >> 8) & 0xFF) / 255.0f;
    }

    public int getA() {
        return this.mColor & 0xFF;
    }

    public float getAf() {
        return (this.mColor & 0xFF) / 255.0f;
    }

    public int getColor() {
        return this.mColor;
    }
}
