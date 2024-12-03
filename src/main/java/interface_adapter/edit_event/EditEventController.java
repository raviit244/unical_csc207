package interface_adapter.edit_event;

import entity.Calendar;
import entity.Event;
import use_case.edit_event.EditEventInputBoundary;
import use_case.edit_event.EditEventInputData;

/**
 * The controller for Edit Event.
 */
public class EditEventController {
    private final EditEventInputBoundary editEventUseCaseInteractor;

    public EditEventController(EditEventInputBoundary editEventUseCaseInteractor) {
        this.editEventUseCaseInteractor = editEventUseCaseInteractor;
    }

    /**
     * Executes the change calendar day use case with the given calendars and date.
     *
     * @param eventName Name of the Event
     * @param date date of the event
     * @param startTime start Time of the event
     * @param endTime End TIme of the Event
     * @param calendar Calendar
     * @param originalEvent THe Original Event to Be Edited.
     */
    public void execute(String eventName, String date, String startTime, String endTime,
                        Calendar calendar, Event originalEvent) {
        final EditEventInputData editEventInputData = new EditEventInputData(
                eventName, date, startTime, endTime, calendar, originalEvent
        );

        editEventUseCaseInteractor.execute(editEventInputData);
    }
}
