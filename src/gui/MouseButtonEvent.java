package gui;

public class MouseButtonEvent extends Event {

    private int mX;
    private int mY;
    protected int mButton;

    public MouseButtonEvent(int button, int x, int y, Type type) {
        super(type);
        this.mX = x;
        this.mY = y;
        this.mButton = button;
    }

    public int getmX() {
        return mX;
    }

    @Override
    public String toString() {
        return "Eventtype: " + this.getType() + " X: " + this.getmX() + " Y: " + this.getmY() + " Hashcode: " + this.hashCode();
    }

    public int getmY() {
        return mY;
    }

    public int getmButton() {
        return mButton;
    }
}
