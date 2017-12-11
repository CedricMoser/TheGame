package gui;

public class KeyEvent extends Event {

    private int mKeyCode;
    private int mScanCode;

    protected KeyEvent(int KeyCode, int ScanCode, Type type) {
        super(type);
        this.mKeyCode = KeyCode;
        this.mScanCode = ScanCode;
    }

    public int getKeyCode() {
        return mKeyCode;
    }

    public int getScanCode() {
        return mScanCode;
    }

    public String toString() {
        return "Eventtype: " + this.getType() + " KeyCode: " + this.getKeyCode() + " ScanCode: " + this.getScanCode() + " Hashcode: " + this.hashCode();
    }
}
