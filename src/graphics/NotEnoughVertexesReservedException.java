package graphics;

public class NotEnoughVertexesReservedException extends RuntimeException {
    public NotEnoughVertexesReservedException(int reserved, int required) {
        super("Reserved: " + reserved + " Required: " + required);
    }
}
