package graphics;


import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VertexArray {
    private int mVAO;
    private int mCount;
    private VertexBuffer mVBO;

    public VertexArray() {
        this.mVAO = GL30.glGenVertexArrays();
    }

    public void setBuffer(VertexBuffer buffer) {
        this.mVBO = buffer;
    }

    public void bind() {
        GL30.glBindVertexArray(mVAO);
        mVBO.bind();
    }

    public static void unbind() {
        GL30.glBindVertexArray(0);
        VertexBuffer.unbind();
    }

    public int getID() {
        return mVAO;
    }

    public void VertexAttributPointer(int loc, int size, int type, boolean normalized, int stride, int offset) {
        GL20.glEnableVertexAttribArray(loc);
        GL20.glVertexAttribPointer(loc, size, type, normalized, stride, offset);
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
    }
}
