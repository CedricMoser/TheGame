package window;

import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

public class WindowSizeCallBack implements GLFWWindowSizeCallbackI {

    private Window mWindow;

    public WindowSizeCallBack(Window window) {
        this.mWindow = window;
    }

    @Override
    public void invoke(long window, int width, int height) {
        if (window == this.mWindow.getHandle()) {
            this.mWindow.setSize(width, height);
        }
    }

}
