package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;

public class Window implements GLFWWindowSizeCallbackI {
    private static int windows = 0;
    private long mWindowHandle;
    private int  mWidth;
    private int  mHeight;

    public Window(CharSequence title, int width, int height) {
        if (windows++ == 0) {
            if (!GLFW.glfwInit()) {
                // ERROR
            }
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        this.mWindowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        this.mWidth        = width;
        this.mHeight       = height;

        glfwSetWindowSizeCallback(this.mWindowHandle, this);

        if (this.mWindowHandle == NULL) {
            // ERROR
        }

        glfwMakeContextCurrent(this.mWindowHandle);
        GL.createCapabilities();

        glViewport(0, 0, 400, 400);
    }

    public void free() {
        glfwDestroyWindow(this.mWindowHandle);

        if (windows-- == 1) {
            glfwTerminate();
        }
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(this.mWindowHandle);
    }

    public void clear() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void display() {
        glfwSwapBuffers(this.mWindowHandle);
        glfwPollEvents();
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    @Override
    public void invoke(long window, int width, int height) {
        if (this.mWindowHandle == window) {
            this.mWidth  = width;
            this.mHeight = height;

            glViewport(0, 0, this.mWidth, this.mHeight);
        }
    }
}
