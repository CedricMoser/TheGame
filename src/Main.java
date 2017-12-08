
public class Main {
    public static void main(String[] args) {
        Window window = new Window("TheGame", 400, 400);

        while (window.IsOpen()) {
            window.Clear();
            window.Display();
        }
    }
}