package gui;

public class KeyReleasedEvent extends KeyEvent {

    public KeyReleasedEvent(int KeyCode, int ScanCode) {
        super(KeyCode, ScanCode, Type.Key_Released);
    }
}
