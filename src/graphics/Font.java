package graphics;

import fnt.FNTChar;
import fnt.FNTDocument;
import fnt.FNTPage;
import fnt.FNTParser;
import maths.Vector2;
import maths.Vector3;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Font {
    private List<Texture> mTextures;
    private FNTDocument   mDoc;
    private List<Vector3> mSDFNumbers; // x = size, y = width, z = edge

    /**
     * Constructor of the Font class.
     *
     * @param name Name of the .fnt file without ".fnt".
     */
    public Font(String name) {
        this.mTextures   = new ArrayList<Texture>();
        FNTParser parser = new FNTParser();
        this.mDoc        = parser.parse(FileUtils.readTextFile(name + ".fnt"));
        this.mDoc.extendChars();
        this.mSDFNumbers = new ArrayList<Vector3>();

        // Loads all the textures
        for (int i = 0; i < this.mDoc.getPageCount(); i++) {
            FNTPage cur = this.mDoc.getPage(i);

            this.mTextures.add(new Texture(cur.getFile()));
        }
    }

    /**
     * Adds a new Vector3 to the list which saves the
     * needed numbers for a signed distant field text.
     *
     * @param size  The size for which the given width and edge will be used.
     * @param width The width of the character.
     * @param edge  The edge of the character.
     */
    public void addSDF(float size, float width, float edge) {
        this.mSDFNumbers.add(new Vector3(size, width, edge));
    }

    /**
     * Returns all the uv coordinates needed for a rectangle of the requested character
     * from the texture which contains it.
     *
     * You can get the right texture by calling getTexture(char).
     *
     * @param c Requested character.
     *
     * @return  All uv coordinates needed for a rectangle.
     */
    public Vector2[] getUVs(char c) {
        FNTPage page    = this.mDoc.getPageOfChar(c);
        FNTChar curChar = page.getChar(c);
        Texture texture = this.mTextures.get(page.getID());

        float x = (float) curChar.x      / (float) texture.getWidth();
        float y = (float) curChar.y      / (float) texture.getHeight();
        float w = (float) curChar.width  / (float) texture.getWidth();
        float h = (float) curChar.height / (float) texture.getHeight();

        return new Vector2[] { new Vector2(x, y), new Vector2(x + w, y), new Vector2(x + w, y + h), new Vector2(x, y + h) };
    }

    /**
     * Returns the x and y offset of the given character.
     *
     * @param c Character from which the x and y offset ought be returned.
     *
     * @return The x and y offset of the given character.
     */
    public Vector2 getOffset(char c) {
        FNTChar curChar = this.mDoc.getChar(c);
        return new Vector2(curChar.xoffset, curChar.yoffset);
    }

    /**
     * Returns the x-advance of the requested character.
     *
     * @param c
     *
     * @return
     */
    public int getCharacterXAdvance(char c) {
        FNTChar curChar = this.mDoc.getChar(c);
        return curChar.xadvance;
    }

    public int getCharacterWidth(char c) {
        FNTChar curChar = this.mDoc.getChar(c);
        return curChar.width;
    }

    public int getCharacterHeight(char c) {
        FNTChar curChar = this.mDoc.getChar(c);
        return curChar.height;
    }

    public Vector2 getSDFBySize(float size) {
        for (Vector3 cur : this.mSDFNumbers) {
            if (cur.getX() == size) {
                return new Vector2(cur.getY(), cur.getZ());
            }
        }

        return new Vector2(0.5f, 0.1f);
    }

    public float getOriginalSize() {
        return this.mDoc.getInfo().size;
    }

    public Texture getTexture(char c) {
        FNTPage page = this.mDoc.getPageOfChar(c);

        return this.mTextures.get(page.getID());
    }
}
