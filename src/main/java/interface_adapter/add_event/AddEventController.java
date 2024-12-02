package interface_adapter.add_event;

import entity.Calendar;
import use_case.add_event.AddEventInputBoundary;
import use_case.add_event.AddEventInputData;

/**
 * The controller for Adding Event Use Case.
 */
public class AddEventController {
    private final AddEventInputBoundary addEventUseCaseInteractor;

    public AddEventController(AddEventInputBoundary addEventUseCaseInteractor) {
        this.addEventUseCaseInteractor = addEventUseCaseInteractor;
    }

    /**
     * Executes the Login Use Case.
     * @param eventName the name of the event
     * @param date the date of the event
     * @param startTime the starting Time of the event
     * @param endTime the starting Time of the event
     * @param calendar the starting Time of the event
     */
    public void execute(String eventName, String date, String startTime, String endTime, Calendar calendar) {
        final AddEventInputData addEventInputData = new AddEventInputData(
              eventName,
              date,
              startTime,
              endTime,
              calendar
        );

        addEventUseCaseInteractor.execute(addEventInputData);
    }
}
