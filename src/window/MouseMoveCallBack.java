package window;

import gui.MouseMovedEvent;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class MouseMoveCallBack implements GLFWCursorPosCallbackI {
    Window mWindow;

    public MouseMoveCallBack(Window window) {
        this.mWindow = window;
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        if (window == mWindow.getHandle()) {
            mWindow.pushEvent(new MouseMovedEvent(xpos, ypos));

        }
    }
}
