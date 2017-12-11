package gui;

public class MouseReleasedEvent extends MouseButtonEvent {
    public MouseReleasedEvent(int button, int x, int y) {
        super(button, x, y, Type.Mouse_Released);
    }
}
