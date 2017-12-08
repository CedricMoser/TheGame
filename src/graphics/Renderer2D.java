package graphics;

import maths.Mat4;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

public class Renderer2D {
    protected static final byte LOC_POSITION   = 0;
    protected static final byte LOC_COLOR      = 1;
    protected static final byte LOC_UV         = 2;
    protected static final byte LOC_TEXTURE_ID = 3;
    private static final int FLOATS_PER_VERTEX = 3 + 4 + 2 + 1; // Position, Color, UV, TextureID
    private static final int STRIDE = Float.BYTES * FLOATS_PER_VERTEX;

    public static final int RECT_CUT_LEFT_TOP     = 0x01;
    public static final int RECT_CUT_LEFT_BOTTOM  = 0x02;
    public static final int RECT_CUT_RIGHT_TOP    = 0x04;
    public static final int RECT_CUT_RIGHT_BOTTOM = 0x08;

    private int         mVAO;
    private int         mVBO;
    private ByteBuffer  mPointer;
    private int         mPointerOffset;
    private Shader      mShader;
    private int         mColor;
    private ShortBuffer mIndexBuffer;
    private int         mVertexCount;

    public Renderer2D(int vertexCount) {
        this.mVAO = glGenVertexArrays();
        this.mVBO = glGenBuffers();

        glBindVertexArray(this.mVAO);
        glBindBuffer(GL_ARRAY_BUFFER, this.mVBO);

        glBufferData(GL_ARRAY_BUFFER, FloatBuffer.allocate(FLOATS_PER_VERTEX * vertexCount), GL_DYNAMIC_DRAW);
        glEnableVertexAttribArray(LOC_POSITION);
        glEnableVertexAttribArray(LOC_COLOR);
        glEnableVertexAttribArray(LOC_UV);
        glEnableVertexAttribArray(LOC_TEXTURE_ID);

        glVertexAttribPointer(LOC_POSITION  , 3, GL_FLOAT, false, STRIDE, 0);
        glVertexAttribPointer(LOC_COLOR     , 4, GL_FLOAT, false, STRIDE, Float.BYTES * 3);
        glVertexAttribPointer(LOC_UV        , 2, GL_FLOAT, false, STRIDE, Float.BYTES * (3 + 4));
        glVertexAttribPointer(LOC_TEXTURE_ID, 1, GL_FLOAT, false, STRIDE, Float.BYTES * (3 + 4 + 2));

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        this.mPointer = null;
        this.mPointerOffset = 0;

        this.mShader = new Shader("renderer2d.shader");

        this.mIndexBuffer = BufferUtils.createShortBuffer(vertexCount * 3);

        this.mVertexCount = 0;
    }

    /**
     * Destructor
     */
    public void free() {
        glDeleteBuffers(this.mVBO);
        glDeleteVertexArrays(this.mVAO);
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.pushVertex(x1, y1);
        this.pushVertex(x2, y2);
        this.pushVertex(x3, y3);

        this.pushTriangleToIndexBuffer(this.mVertexCount, this.mVertexCount + 1, this.mVertexCount + 2);
        this.mVertexCount += 3;
    }

    public void drawCircle(float cx, float cy, float radius) {
        this.drawCircle(cx, cy, radius, 16);
    }

    public void drawCircle(float cx, float cy, float radius, int points) {
        this.pushVertex(cx, cy);

        for (int i = 0; i < points; i++) {
            this.pushVertex(
                radius * (float) Math.cos((360.0f / (float) points * i) * Math.PI / 180.0f) + cx,
                radius * (float) Math.sin((360.0f / (float) points * i) * Math.PI / 180.0f) + cy
            );

            this.pushTriangleToIndexBuffer(this.mVertexCount, this.mVertexCount + i, this.mVertexCount + i + 1);
        }

        this.pushTriangleToIndexBuffer(this.mVertexCount, this.mVertexCount + 1, this.mVertexCount + points);

        this.mVertexCount += points + 1;
    }

    public void drawRect(float x, float y, float w, float h) {
        this.mColor = 0x00FF00FF;
        this.pushVertex(x, y);
        this.pushVertex(x + w, y);
        this.pushVertex(x + w, y + h);
        this.pushVertex(x, y + h);

        this.pushTriangleToIndexBuffer(this.mVertexCount, this.mVertexCount + 1, this.mVertexCount + 2);
        this.pushTriangleToIndexBuffer(this.mVertexCount + 2, this.mVertexCount + 3, this.mVertexCount);

        this.mVertexCount += 4;
    }

    public void drawRoundedRect(float x, float y, float w, float h, float radius) {
        this.drawRoundedRect(x, y, w, h, radius, 12);
    }

    public void drawRoundedRect(float x, float y, float w, float h, float radius, int points) {
        float radiusX = radius * 2.0f > w ? w / 2.0f : radius;
        float radiusY = radius * 2.0f > h ? h / 2.0f : radius;

        float[] offsets = { // order is important!
            x + w - radiusX, y + h - radiusY,
            x + radiusX    , y + h - radiusY,
            x + radiusX    , y + radiusY,
            x + w - radiusX, y + radiusY
        };

        int tmpVertexCount = this.mVertexCount;

        for (int asd = 0; asd < 4; asd++) {
            this.pushVertex(offsets[asd * 2], offsets[asd * 2 + 1]);

            int end = points * (asd + 1);
            for (int i = points * asd, a = 0; i < end; i++, a++) {
                this.pushVertex(
                    radiusX * (float) Math.cos(360.0f / ((float) points * 4.0f) * (float) (i) * Math.PI / 180.0f) + offsets[asd * 2],
                    radiusY * (float) Math.sin(360.0f / ((float) points * 4.0f) * (float) (i) * Math.PI / 180.0f) + offsets[asd * 2 + 1]
                );

                this.pushTriangleToIndexBuffer(tmpVertexCount, tmpVertexCount + a, tmpVertexCount + a + 1);
            }

            tmpVertexCount += points + 1;
        }

        // mid
        this.pushTriangleToIndexBuffer(this.mVertexCount + points        , this.mVertexCount + points + 2        , this.mVertexCount + points * 3 + 2);
        this.pushTriangleToIndexBuffer(this.mVertexCount + points * 3 + 2, this.mVertexCount + points * 3 + 4    , this.mVertexCount + points);

        // left
        this.pushTriangleToIndexBuffer(this.mVertexCount + points * 2 + 1, this.mVertexCount + points + 1        , this.mVertexCount + points * 2 + 2);
        this.pushTriangleToIndexBuffer(this.mVertexCount + points * 2 + 2, this.mVertexCount + points * 2 + 3    , this.mVertexCount + points * 2 + 1);

        // right
        this.pushTriangleToIndexBuffer(this.mVertexCount + points * 4 + 3, this.mVertexCount + points * 3 + 2 + 1, this.mVertexCount + 1);
        this.pushTriangleToIndexBuffer(this.mVertexCount + 1             , this.mVertexCount                     , this.mVertexCount + points * 3 + 2 + 1);

        this.mVertexCount = tmpVertexCount;
    }

    public void drawRect(float x, float y, float w, float h, int flags) {
        final float small = w >= h ? h : w;
        final float sx    = small / 4.0f;
        final float sy    = small / 4.0f;

        boolean[] cutCorners = {
            (flags & 0x01) >= 1, // Left top
            (flags & 0x04) >= 1, // Right top
            (flags & 0x02) >= 1, // Left bottom
            (flags & 0x08) >= 1  // Right bottom
        };

        float[] offsets = {
            x + sx    , y + sy    , x    , y,     // Top left
            x + w - sx, y + sy    , x + w, y,     // Top right
            x + sx    , y + h - sy, x    , y + h, // Bottom left
            x + w - sx, y + h - sy, x + w, y + h  // Bottom right
        };

        int[] mids = { 0, 0, 0, 0 };
        int tmpVertexCount = this.mVertexCount;

        for (int i = 0; i < 4; i++) {
            this.pushVertex(offsets[i * 4], offsets[i * 4 + 1]);     // Mid
            this.pushVertex(offsets[i * 4], offsets[i * 4 + 3]);     // Top / Bottom
            this.pushVertex(offsets[i * 4 + 2], offsets[i * 4 + 1]); // Left / Right
            mids[i] = tmpVertexCount;

            this.pushTriangleToIndexBuffer(tmpVertexCount, tmpVertexCount + 1, tmpVertexCount + 2);

            if (!cutCorners[i]) {
                this.pushVertex(offsets[i * 4 + 2], offsets[i * 4 + 3]); // Corner
                this.pushTriangleToIndexBuffer(tmpVertexCount + 1, tmpVertexCount + 2, tmpVertexCount + 3);
                tmpVertexCount += 4;
            } else {
                tmpVertexCount += 3;
            }
        }

        /*
         * MID          + 0
         * Top / Bottom + 1
         * Left / Right + 2
         * Corner       + 3
         */
        // Top mid
        this.pushTriangleToIndexBuffer(mids[0], mids[0] + 1, mids[1]);
        this.pushTriangleToIndexBuffer(mids[1], mids[1] + 1, mids[0] + 1);

        // Right
        this.pushTriangleToIndexBuffer(mids[1], mids[1] + 2, mids[3]);
        this.pushTriangleToIndexBuffer(mids[3], mids[3] + 2, mids[1] + 2);

        // Bottom mid
        this.pushTriangleToIndexBuffer(mids[3], mids[3] + 1, mids[2]);
        this.pushTriangleToIndexBuffer(mids[2], mids[2] + 1, mids[3] + 1);

        // Left
        this.pushTriangleToIndexBuffer(mids[2], mids[2] + 2, mids[0]);
        this.pushTriangleToIndexBuffer(mids[0], mids[0] + 2, mids[2] + 2);

        // Mid mid
        this.pushTriangleToIndexBuffer(mids[0], mids[1], mids[2]);
        this.pushTriangleToIndexBuffer(mids[2], mids[3], mids[1]);

        this.mVertexCount = tmpVertexCount;
    }

    public void begin() {
        glBindVertexArray(this.mVAO);
        glBindBuffer(GL_ARRAY_BUFFER, this.mVBO);
        this.mPointer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY);
        this.mPointerOffset = 0;
        this.mVertexCount = 0;
        this.mIndexBuffer.position(0);
    }

    public void end() {
        glUnmapBuffer(GL_ARRAY_BUFFER);
        this.mIndexBuffer.flip();

        this.mShader.bind();

        Mat4 model = new Mat4(1.0f);
        Mat4 projection = Mat4.ortho(0.0f, 400, 0.0f, 400.0f, -1.0f, 1.0f);

        int loc_model      = glGetUniformLocation(this.mShader.GetProgram(), "model");
        int loc_projection = glGetUniformLocation(this.mShader.GetProgram(), "projection");

        glUniformMatrix4fv(loc_model     , false, model.getFloatArray());
        glUniformMatrix4fv(loc_projection, false, projection.getFloatArray());

        glDrawElements(GL_TRIANGLES, this.mIndexBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        this.mShader.unbind();
    }

    private void pushVertex(float x, float y) {
        this.pushVertex(x, y, 0.0f, 0.0f, -1.0f, true);
    }

    private void pushVertex(float x, float y, float u, float v, float textureID, boolean useColor) {
        int color = useColor ? this.mColor : 0xFFFFFFFF;

        float r = (color >> 24 & 0xFF) / 255.0f;
        float g = (color >> 16 & 0xFF) / 255.0f;
        float b = (color >> 8  & 0xFF) / 255.0f;
        float a = (color       & 0xFF) / 255.0f;

        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 0, x);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 1, y);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 2, 0.0f);

        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 3, r);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 4, g);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 5, b);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 6, a);

        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 7, u);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 8, v);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 9, textureID);

        this.mPointerOffset += FLOATS_PER_VERTEX * Float.BYTES;
    }

    private void pushTriangleToIndexBuffer(int first, int second, int third) {
        this.mIndexBuffer = this.mIndexBuffer.put((short) first);
        this.mIndexBuffer = this.mIndexBuffer.put((short) second);
        this.mIndexBuffer = this.mIndexBuffer.put((short) third);
    }
}
