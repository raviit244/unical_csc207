package use_case.delete_event;

import entity.Event;

/**
 * Input Data for the Delete Event use-case.
 */
public class DeleteEventInputData {
    private final Event event;

    public DeleteEventInputData(Event event) {
        if (event == null) {
            throw new NullPointerException("Event cannot be null");
        }
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
