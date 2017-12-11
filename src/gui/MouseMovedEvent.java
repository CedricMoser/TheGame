package gui;

public class MouseMovedEvent extends Event {
    private int mX;
    private int mY;
    private boolean mDragged;

    public MouseMovedEvent(int x, int y, boolean dragged) {
        super(Event.Type.Mouse_Moved);
        this.mX = x;
        this.mY = y;
        this.mDragged = dragged;

    }

    public int getmX() {
        return mX;
    }

    public int getmY() {

        return mY;
    }

    public boolean getmDragged() {

        return mDragged;
    }
}
