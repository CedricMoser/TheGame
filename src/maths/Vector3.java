package maths;

public class Vector3 {
    private float mX;
    private float mY;
    private float mZ;

    public Vector3() {
        this.mX = 0.0f;
        this.mY = 0.0f;
        this.mZ = 0.0f;
    }

    public Vector3(float x, float y, float z) {
        this.mX = x;
        this.mY = y;
        this.mZ = z;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public void setY(float y) {
        this.mY = y;
    }

    public void setZ(float z) {
        this.mZ = z;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public float getZ() {
        return mZ;
    }
}
