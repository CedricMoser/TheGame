package graphics;

import fnt.FNTChar;
import fnt.FNTDocument;
import fnt.FNTParser;
import maths.Vector2;

import java.io.*;

public class Font {
    private Texture mTexture;
    private FNTDocument mDoc;

    public Font(String name) {
        this.mTexture    = new Texture(name + ".png");
        FNTParser parser = new FNTParser();
        this.mDoc        = parser.parse(this.readFile(name + ".fnt"));
    }

    private String readFile(String path) {
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public Vector2[] getUVs(char c) {
        FNTChar curChar = this.getChar(c);

        float x = (float) curChar.x      / (float) this.mTexture.getWidth();
        float y = (float) curChar.y      / (float) this.mTexture.getHeight();
        float w = (float) curChar.width  / (float) this.mTexture.getWidth();
        float h = (float) curChar.height / (float) this.mTexture.getHeight();

        return new Vector2[] { new Vector2(x, y), new Vector2(x + w, y), new Vector2(x + w, y + h), new Vector2(x, y + h) };
    }

    public Vector2 getOffset(char c) {
        FNTChar curChar = this.getChar(c);
        return new Vector2(curChar.xoffset, curChar.yoffset);
    }

    public int getCharacterWidth(char c) {
        FNTChar curChar = this.getChar(c);
        return curChar.width;
    }

    public int getCharacterHeight(char c) {
        FNTChar curChar = this.getChar(c);
        return curChar.height;
    }

    private FNTChar getChar(int id) {
        for (FNTChar cur : this.mDoc.getChars()) {
            if (cur.id == id) {
                return cur;
            }
        }

        return null;
    }

    public Texture getTexture() {
        return this.mTexture;
    }
}
