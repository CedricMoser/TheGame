import fnt.FNTDocument;
import fnt.FNTParser;
import graphics.Renderer2D;
import graphics.Texture;
import window.Window;

import java.io.*;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

public class Main {
    public static void main(String[] args) {
        /*
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("Characters.fnt"))));

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

        FNTParser parser = new FNTParser();
        FNTDocument doc = parser.parse(builder.toString());

        System.out.println();
        */
        Window window = new Window("Hello World", 400, 400);
        Renderer2D renderer = new Renderer2D(window, 200);

        long begin = System.currentTimeMillis();
        int  fps = 0;

        Texture tex = new Texture("test.png");
        Texture tex2 = new Texture("test2.png");

        while (window.isOpen()) {
            window.clear();

            renderer.begin();
            renderer.drawRect(10.0f, 10.0f, 100.0f, 100.0f);
            renderer.setColor(0xFF0000FF);
            renderer.drawTriangle(15.0f,15.0f, 45.0f, 15.0f, 30.0f, 45.0f);
            renderer.setColor(0xFF00FFFF);
            renderer.drawCircle(60.0f, 60.0f, 30.0f);
            renderer.setColor(0xFF0000FF);
            renderer.drawRoundedRect(120.0f, 10.0f, 100.0f, 25.0f, 10.0f);
            renderer.setColor(0x0000FFFF);
            renderer.drawRect(230.0f, 10.0f, 100.0f, 25.0f, Renderer2D.RECT_CUT_LEFT_TOP | Renderer2D.RECT_CUT_RIGHT_BOTTOM);

            renderer.drawImage(10.0f, 150.0f, tex.getWidth(), tex.getHeight(), tex);
            renderer.drawImage(10.0f, 300.0f, tex2.getWidth(), tex2.getHeight(), tex2);

            renderer.drawString(15.0f, 15.0f, "Hello world");

            renderer.end();
/*
            int err;
            while ((err = glGetError()) != GL_NO_ERROR) {
                System.err.println("Error: " + err);
            }
*/

            long now = System.currentTimeMillis();
            fps++;

            if (now - begin >= 1000) {
                System.out.println("FPS: " + fps);
                fps = 0;
                begin = now;
            }

            window.display();
        }

        renderer.free();
        window.free();
    }
}