package graphics;

import maths.Mat4;
import maths.Vector2;
import org.lwjgl.BufferUtils;
import window.Window;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

public class Renderer2D {
    protected static final byte LOC_POSITION   = 0;
    protected static final byte LOC_COLOR      = 1;
    protected static final byte LOC_UV         = 2;
    protected static final byte LOC_TEXTURE_ID = 3;
    protected static final byte LOC_SDF_WIDTH  = 4;
    protected static final byte LOC_SDF_EDGE   = 5;
    private static final int FLOATS_PER_VERTEX = 3 + 4 + 2 + 1 + 1 + 1; // Position, Color, UV, TextureID, width(SDF), edge(SDF)    SDF: Signed Distance Field
    private static final int STRIDE = Float.BYTES * FLOATS_PER_VERTEX;

    public static final int RECT_CUT_LEFT_TOP     = 0x01;
    public static final int RECT_CUT_LEFT_BOTTOM  = 0x02;
    public static final int RECT_CUT_RIGHT_TOP    = 0x04;
    public static final int RECT_CUT_RIGHT_BOTTOM = 0x08;

    private Window        mWindow;
    private int           mVAO;
    private int           mVBO;
    private ByteBuffer    mPointer;
    private int           mPointerOffset;
    private Shader        mShader;
    private Color         mColor;
    private ShortBuffer   mIndexBuffer;
    private int           mMaxVertexes;
    private int           mVertexCount;

    private List<Texture> mImages;

    private Font          mFont;

    public Renderer2D(Window window, int vertexCount) {
        this.mWindow      = window;
        this.mMaxVertexes = vertexCount;

        this.mVAO = glGenVertexArrays();
        this.mVBO = glGenBuffers();

        glBindVertexArray(this.mVAO);
        glBindBuffer(GL_ARRAY_BUFFER, this.mVBO);

        glBufferData(GL_ARRAY_BUFFER, FloatBuffer.allocate(FLOATS_PER_VERTEX * this.mMaxVertexes), GL_DYNAMIC_DRAW);
        glEnableVertexAttribArray(LOC_POSITION);
        glEnableVertexAttribArray(LOC_COLOR);
        glEnableVertexAttribArray(LOC_UV);
        glEnableVertexAttribArray(LOC_TEXTURE_ID);
        glEnableVertexAttribArray(LOC_SDF_WIDTH);
        glEnableVertexAttribArray(LOC_SDF_EDGE);

        glVertexAttribPointer(LOC_POSITION  , 3, GL_FLOAT, false, STRIDE, 0);
        glVertexAttribPointer(LOC_COLOR     , 4, GL_FLOAT, false, STRIDE, Float.BYTES * 3);
        glVertexAttribPointer(LOC_UV        , 2, GL_FLOAT, false, STRIDE, Float.BYTES * (3 + 4));
        glVertexAttribPointer(LOC_TEXTURE_ID, 1, GL_FLOAT, false, STRIDE, Float.BYTES * (3 + 4 + 2));
        glVertexAttribPointer(LOC_SDF_WIDTH , 1, GL_FLOAT, false, STRIDE, Float.BYTES * (3 + 4 + 2 + 1));
        glVertexAttribPointer(LOC_SDF_EDGE  , 1, GL_FLOAT, false, STRIDE, Float.BYTES * (3 + 4 + 2 + 1 + 1));

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        this.mPointer       = null;
        this.mPointerOffset = 0;

        this.mShader = new Shader("renderer2d.shader");

        this.mIndexBuffer = BufferUtils.createShortBuffer(vertexCount * 3);

        this.mVertexCount = 0;

        this.mImages = new ArrayList<Texture>();

        this.mFont = null;
        this.mColor = Color.WHITE;
    }

    /**
     * Destructor
     */
    public void free() {
        glDeleteBuffers(this.mVBO);
        glDeleteVertexArrays(this.mVAO);
    }

    public void setColor(int color) {
        this.mColor = new Color(color);
    }

    public void setColor(Color color) {
        this.mColor = color;
    }

    public void setFont(Font font) {
        this.mFont = font;
    }

    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.require(3);

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
        this.require(points + 1);

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

    public void drawImage(float x, float y, float w, float h, Texture texture) {
        this.require(4);

        int id = this.getTextureId(texture);

        this.pushVertex(x       , y       , 0.0f, 0.0f, id, false);
        this.pushVertex(x + w, y       , 1.0f, 0.0f, id, false);
        this.pushVertex(x + w, y + h, 1.0f, 1.0f, id, false);
        this.pushVertex(x       , y + h, 0.0f, 1.0f, id, false);

        this.pushTriangleToIndexBuffer(this.mVertexCount, this.mVertexCount + 1, this.mVertexCount + 2);
        this.pushTriangleToIndexBuffer(this.mVertexCount + 2, this.mVertexCount + 3, this.mVertexCount);

        this.mVertexCount += 4;
    }

    public void drawRect(float x, float y, float w, float h) {
        this.require(4);

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
        this.require(4 * points + 4); // 4 sides * points per side + 4 of the mids

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

        int required = 3 * 4;
        required += cutCorners[0] ? 0 : 1;
        required += cutCorners[1] ? 0 : 1;
        required += cutCorners[2] ? 0 : 1;
        required += cutCorners[3] ? 0 : 1;

        this.require(required);

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

    public void drawString(float x, float y, String text, float size) {
        this.require(4 * text.length());

        float offsetX = 0.0f;
        float offsetY = 0.0f;
        float scale   = size / this.mFont.getOriginalSize();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == ' ') {
                offsetX += this.mFont.getCharacterXAdvance('-');
                continue;
            } else if (c == '\n') {
                offsetY += this.mFont.getCharacterHeight('M');
                offsetX = 0.0f;
                continue;
            }

            this.drawCharacter(offsetX * scale + x, offsetY  * scale + y, c, size);

            offsetX += this.mFont.getCharacterXAdvance(c);
        }
    }

    private void drawCharacter(float x, float y, char c, float size) {
        int id = this.getTextureId(this.mFont.getTexture(c));

        float scale   = size / this.mFont.getOriginalSize();
        float w = this.mFont.getCharacterWidth(c);
        float h = this.mFont.getCharacterHeight(c);
        Vector2[] uvs = this.mFont.getUVs(c);
        Vector2   off = this.mFont.getOffset(c);
        float offX = off.getX() * scale;
        float offY = off.getY() * scale;
        Vector2 sdf = this.mFont.getSDFBySize(size);

        this.pushVertex(offX + x            , offY + y            , uvs[0].getX(), uvs[0].getY(), id, false, sdf.getX(), sdf.getY());
        this.pushVertex(offX + x + w * scale, offY + y            , uvs[1].getX(), uvs[1].getY(), id, false, sdf.getX(), sdf.getY());
        this.pushVertex(offX + x + w * scale, offY + y + h * scale, uvs[2].getX(), uvs[2].getY(), id, false, sdf.getX(), sdf.getY());
        this.pushVertex(offX + x            , offY + y + h * scale, uvs[3].getX(), uvs[3].getY(), id, false, sdf.getX(), sdf.getY());

        this.pushTriangleToIndexBuffer(this.mVertexCount, this.mVertexCount + 1, this.mVertexCount + 2);
        this.pushTriangleToIndexBuffer(this.mVertexCount + 2, this.mVertexCount + 3, this.mVertexCount);

        this.mVertexCount += 4;
    }

    public void begin() {
        glBindVertexArray(this.mVAO);
        glBindBuffer(GL_ARRAY_BUFFER, this.mVBO);
        this.mPointer       = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY);
        this.mPointerOffset = 0;
        this.mVertexCount   = 0;
        this.mImages.clear();
        this.mIndexBuffer.clear();
    }

    public void end() {
        glUnmapBuffer(GL_ARRAY_BUFFER);
        this.mIndexBuffer.flip();

        this.mShader.bind();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Mat4 projection = Mat4.ortho(0.0f, this.mWindow.getWidth(), 0.0f, this.mWindow.getHeight(), -1.0f, 1.0f);

        int loc_projection = glGetUniformLocation(this.mShader.GetProgram(), "projection");

        glUniformMatrix4fv(loc_projection, false, projection.getFloatArray());

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.mImages.size(); i++) {
            builder.append("textures[");
            builder.append(i);
            builder.append("]");

            glActiveTexture(GL_TEXTURE0 + i);
            this.mImages.get(i).bind();

            glUniform1i(glGetUniformLocation(this.mShader.GetProgram(), builder.toString()), i);

            builder.setLength(0);
        }

        glDrawElements(GL_TRIANGLES, this.mIndexBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        glDisable(GL_BLEND);

        Shader.unbind();
    }

    private void pushVertex(float x, float y) {
        this.pushVertex(x, y, 0.0f, 0.0f, -1.0f, true);
    }

    private void pushVertex(float x, float y, float u, float v, float textureID, boolean useColor) {
        this.pushVertex(x, y, u, v, textureID, useColor, 0.0f, 0.0f);
    }

    private void pushVertex(float x, float y, float u, float v, float textureID, boolean useColor, float sdf_width, float sdf_edge) {
        Color color = useColor ? this.mColor : Color.WHITE;

        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 0, x);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 1, y);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 2, 0.0f);

        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 3, color.getRf());
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 4, color.getGf());
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 5, color.getBf());
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 6, color.getAf());

        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 7, u);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 8, v);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 9, textureID);

        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 10, sdf_width);
        this.mPointer.putFloat(this.mPointerOffset + Float.BYTES * 11, sdf_edge);

        this.mPointerOffset += FLOATS_PER_VERTEX * Float.BYTES;
    }

    private void pushTriangleToIndexBuffer(int first, int second, int third) {
        this.mIndexBuffer = this.mIndexBuffer.put((short) first);
        this.mIndexBuffer = this.mIndexBuffer.put((short) second);
        this.mIndexBuffer = this.mIndexBuffer.put((short) third);
    }

    private int getTextureId(Texture texture) {
        if (this.mImages.size() > 31) {
            this.end();
            this.begin();
        }

        int id = -1;

        for (int i = 0; i < this.mImages.size(); i++) {
            if (this.mImages.get(i) == texture) {
                id = i;
                break;
            }
        }

        if (id == -1) {
            id = this.mImages.size();
            this.mImages.add(texture);
        }

        return id;
    }

    private void require(int vertexCount) {
        if (vertexCount > this.mMaxVertexes) { throw new NotEnoughVertexesReservedException(this.mMaxVertexes, vertexCount); }

        if (this.mVertexCount + vertexCount > this.mMaxVertexes) {
            this.end();
            this.begin();
        }
    }
}
