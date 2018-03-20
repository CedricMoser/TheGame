package graphics;

import maths.Vector3;

public class Vertex {
    private Vector3 mPos;

    public Vertex(float x, float y, float z) {
        this.mPos = new Vector3(x, y, z);
    }

    public Vector3 getPos() {
        return mPos;
    }

    public void setPos(float x, float y, float z) {
        this.mPos.setX(x);
        this.mPos.setY(y);
        this.mPos.setZ(z);
    }
}
