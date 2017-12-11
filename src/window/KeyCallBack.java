package window;

import gui.KeyPressedEvent;
import gui.KeyReleasedEvent;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyCallBack implements GLFWKeyCallbackI {

    private Window mWindow;

    public KeyCallBack(Window window) {
        this.mWindow = window;
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (this.mWindow.getHandle() == window) {
            switch (action) {
                case GLFW_PRESS: {
                    this.mWindow.pushEvent(new KeyPressedEvent(key, scancode));
                    break;
                }
                case GLFW_RELEASE: {
                    this.mWindow.pushEvent(new KeyReleasedEvent(key, scancode));
                    break;
                }
                default:
                    break;
            }


        }

    }

}
