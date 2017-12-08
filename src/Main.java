import graphics.Renderer2D;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

public class Main {
    public static void main(String[] args) {
        Window window = new Window("Hello World", 400, 400);
        Renderer2D renderer = new Renderer2D(100);

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
            renderer.end();

            int err;
            while ((err = glGetError()) != GL_NO_ERROR) {
                System.err.println("Error: " + err);
            }

            window.display();
        }

        renderer.free();
    }
}