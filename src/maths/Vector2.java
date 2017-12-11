package maths;

public class Vector2 {
    private float mX;
    private float mY;

    public Vector2() {
        this(0.0f, 0.0f);
    }

    public Vector2(float x, float y) {
        this.mX = x;
        this.mY = y;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public void setY(float y) {
        this.mY = y;
    }

    public float getX() {
        return this.mX;
    }

    public float getY() {
        return this.mY;
    }
}
