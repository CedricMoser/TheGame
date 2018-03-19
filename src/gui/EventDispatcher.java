package gui;

public class EventDispatcher {
    private Event mEvent;

    public EventDispatcher(Event event) {
        this.mEvent = event;
    }

    public void dispatch(Event.Type type, EventHandler handler) {
        if (mEvent.handeled) {
            return;
        }

        if (mEvent.getType() == type) {
            mEvent.handeled = handler.onEvent(mEvent);
        }
    }

}
