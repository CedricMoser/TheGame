package gui;

public class MouseMovedEvent extends Event {
    private double mX;
    private double mY;


    public MouseMovedEvent(double x, double y) {
        super(Event.Type.Mouse_Moved);
        this.mX = x;
        this.mY = y;
    }

    public double getmX() {
        return mX;
    }

    public double getmY() {

        return mY;
    }

    @Override
    public String toString() {
        return "Eventtype: " + this.getType() + " X: " + this.getmX() + " Y: " + this.getmY() + " Hashcode: " + this.hashCode();
    }
}
