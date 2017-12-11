package gui;

public class Event {

    private Type mType;
    boolean handeled;

    public enum Type {
        Mouse_Pressed,
        Mouse_Released,
        Mouse_Moved,
        Key_Pressed,
        Key_Released
    }


    protected Event(Type type) {
        this.mType = type;
    }

    public Type getType() {
        return mType;
    }
}
