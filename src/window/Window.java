package window;

import gui.Event;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static int windows = 0;
    private long mWindowHandle;
    private int  mWidth;
    private int  mHeight;
    private WindowSizeCallBack mWindowSizeCallBack;
    private MouseButtonCallBack mMouseButtonCallBack;
    private ArrayList<Event> mEventHandlerList;
    private KeyCallBack mKeyCallBack;
    private MouseMoveCallBack mMouseMoveCallBack;


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
        this.mWindowSizeCallBack = new WindowSizeCallBack(this);
        this.mMouseButtonCallBack = new MouseButtonCallBack(this);
        this.mEventHandlerList = new ArrayList<Event>();
        this.mKeyCallBack = new KeyCallBack(this);
        this.mMouseMoveCallBack = new MouseMoveCallBack(this);

        glfwSetCursorPosCallback(this.mWindowHandle, this.mMouseMoveCallBack);
        glfwSetKeyCallback(this.mWindowHandle, this.mKeyCallBack);
        glfwSetWindowSizeCallback(this.mWindowHandle, this.mWindowSizeCallBack);
        glfwSetMouseButtonCallback(this.mWindowHandle, this.mMouseButtonCallBack);

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

    protected void setSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        glViewport(0, 0, this.mWidth, this.mHeight);
    }

    protected long getHandle() {
        return this.mWindowHandle;
    }

    protected void pushEvent(Event event) {
        mEventHandlerList.add(event);
    }

    public Event pollEvent() {
        return this.mEventHandlerList.size() > 0 ? mEventHandlerList.remove(0) : null;
    }
}
