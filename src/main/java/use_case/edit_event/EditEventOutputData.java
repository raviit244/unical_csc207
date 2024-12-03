package use_case.edit_event;

import entity.Event;

/**
 * Output Data for the Edit Event use-case.
 */
public class EditEventOutputData {
    private final Event oldEvent;
    private final Event newEvent;
    private final boolean useCaseFailed;

    public EditEventOutputData(Event oldEvent, Event newEvent, boolean useCaseFailed) {
        this.oldEvent = oldEvent;
        this.newEvent = newEvent;
        this.useCaseFailed = useCaseFailed;
    }

    public Event getOldEvent() {
        return oldEvent;
    }

    public Event getNewEvent() {
        return newEvent;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
