package interface_adapter.add_event;

import entity.Calendar;
import use_case.add_event.AddEventInputBoundary;
import use_case.add_event.AddEventInputData;

public class AddEventController {
  private final AddEventInputBoundary addEventUseCaseInteractor;

  public AddEventController(AddEventInputBoundary addEventUseCaseInteractor) {
    this.addEventUseCaseInteractor = addEventUseCaseInteractor;
  }

  public void execute(String eventName, String date, String startTime, String endTime, Calendar calendar) {
    AddEventInputData addEventInputData = new AddEventInputData(
            eventName,
            date,
            startTime,
            endTime,
            calendar
    );

    addEventUseCaseInteractor.execute(addEventInputData);
  }
}
