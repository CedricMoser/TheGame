package graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;

public class Texture {
    private int mTextureID;
    private BufferedImage mImage;

    public Texture() {
        this.mTextureID = glGenTextures();
        this.mImage     = null;
    }

    public Texture(String path) {
        this();
        this.loadFromFile(path);
    }

    public void free() {
        glDeleteTextures(this.mTextureID);
    }

    public boolean loadFromFile(String path) {
        try {
            this.mImage = ImageIO.read(new File(path));
        } catch (IOException e) {
            this.mImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

            e.printStackTrace();
            return false;
        }

        this.bind();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, this.mImage.getWidth(), this.mImage.getHeight(), 0, GL_BGRA, GL_UNSIGNED_BYTE, this.mImage.getRGB(0, 0, this.mImage.getWidth(), this.mImage.getHeight(), null, 0, this.mImage.getWidth()));

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        Texture.unbind();

        return true;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.mTextureID);
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return this.mImage.getWidth();
    }

    public int getHeight() {
        return this.mImage.getHeight();
    }
}
