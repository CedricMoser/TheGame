import logging.Logger;

public class Main {
    public static void main(String[] args) {
        Window window = new Window("Hello World", 400, 400);

        while (window.IsOpen()) {
            window.Clear();
            window.Display();
        }
        Logger test = new Logger();
        test.info("testinfo");
        test.warning("testwarnung");
        test.error("testerror");
    }
}