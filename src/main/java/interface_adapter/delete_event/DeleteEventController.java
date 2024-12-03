package interface_adapter.delete_event;

import entity.Event;
import use_case.delete_event.DeleteEventInputBoundary;
import use_case.delete_event.DeleteEventInputData;

/**
 * The controller for Delete Event.
 */
public class DeleteEventController {
    private final DeleteEventInputBoundary deleteEventUseCaseInteractor;

    public DeleteEventController(DeleteEventInputBoundary deleteEventUseCaseInteractor) {
        this.deleteEventUseCaseInteractor = deleteEventUseCaseInteractor;
    }

    /**
     * Executes the change calendar day use case with the given calendars and date.
     *
     * @param event Event to be Deleted
     */
    public void execute(Event event) {
        final DeleteEventInputData deleteEventInputData = new DeleteEventInputData(event);
        deleteEventUseCaseInteractor.execute(deleteEventInputData);
    }
}
