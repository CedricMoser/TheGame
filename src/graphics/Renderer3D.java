package graphics;

import org.lwjgl.opengl.GL11;

public class Renderer3D {

    public void render(VertexArray vertexArray) {
        vertexArray.bind();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexArray.getCount());

    }
}
