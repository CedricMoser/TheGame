package window;


import gui.MousePressedEvent;
import gui.MouseReleasedEvent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseButtonCallBack implements GLFWMouseButtonCallbackI {

    private Window mWindow;


    public MouseButtonCallBack(Window window) {
        this.mWindow = window;
    }

    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (window == this.mWindow.getHandle()) {
            double[] y = new double[1];
            double[] x = new double[1];
            GLFW.glfwGetCursorPos(this.mWindow.getHandle(), x, y);
            switch (action) {
                case GLFW_PRESS: {
                    mWindow.pushEvent(new MousePressedEvent(button, (int) x[0], (int) y[0]));
                    break;
                }
                case GLFW_RELEASE: {
                    mWindow.pushEvent(new MouseReleasedEvent(button, (int) x[0], (int) y[0]));
                    break;
                }
                default:
                    break;
            }

        }

    }
}
