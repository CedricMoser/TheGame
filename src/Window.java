import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private static int windows = 0;
    private long mWindowHandle;

    public Window(CharSequence title, int width, int height) {
        if (windows++ == 0) {
            if (!GLFW.glfwInit()) {
                // ERROR
            }
        }

        this.mWindowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

        if (this.mWindowHandle == NULL) {
            // ERROR
        }

        glfwMakeContextCurrent(this.mWindowHandle);
        GL.createCapabilities();
    }

    public boolean IsOpen() {
        return !glfwWindowShouldClose(this.mWindowHandle);
    }

    public void Clear() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void Display() {
        glfwSwapBuffers(this.mWindowHandle);
        glfwPollEvents();
    }
}
