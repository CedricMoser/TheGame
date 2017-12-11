package gui;

public class KeyPressedEvent extends KeyEvent {

    public KeyPressedEvent(int KeyCode, int ScanCode) {
        super(KeyCode, ScanCode, Type.Key_Pressed);
    }
}
