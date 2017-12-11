package gui;

public class MousePressedEvent extends MouseButtonEvent {

    public MousePressedEvent(int button, int x, int y) {
        super(button, x, y, Type.Mouse_Pressed);
    }
}
