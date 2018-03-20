package graphics;

import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;

public class VertexBuffer {
    private int mVBO;

    public VertexBuffer(ByteBuffer data) {
        mVBO = GL15.glGenBuffers();
        GL15.glBindBuffer(mVBO, GL15.GL_ARRAY_BUFFER);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);

    }

    public void bind() {
        GL15.glBindBuffer(mVBO, GL15.GL_ARRAY_BUFFER);
    }

    public static void unbind() {
        GL15.glBindBuffer(0, GL15.GL_ARRAY_BUFFER);
    }

    public int getID() {
        return mVBO;
    }
}
