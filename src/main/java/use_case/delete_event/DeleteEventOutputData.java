package use_case.delete_event;

import entity.Event;

/**
 * Output Data for the Delete Event use-case.
 */
public class DeleteEventOutputData {
    private final Event event;
    private final boolean useCaseFailed;

    public DeleteEventOutputData(Event event, boolean useCaseFailed) {
        if (event == null) {
            throw new NullPointerException("Event cannot be null");
        }
        this.event = event;
        this.useCaseFailed = useCaseFailed;
    }

    public Event getEvent() {
        return event;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
