package interface_adapter.edit_event;

import use_case.edit_event.EditEventInputBoundary;
import use_case.edit_event.EditEventInputData;
import entity.Calendar;
import entity.Event;

public class EditEventController {
  private final EditEventInputBoundary editEventUseCaseInteractor;

  public EditEventController(EditEventInputBoundary editEventUseCaseInteractor) {
    this.editEventUseCaseInteractor = editEventUseCaseInteractor;
  }

  public void execute(String eventName, String date, String startTime, String endTime,
                      Calendar calendar, Event originalEvent) {
    EditEventInputData editEventInputData = new EditEventInputData(
      eventName, date, startTime, endTime, calendar, originalEvent
    );

    editEventUseCaseInteractor.execute(editEventInputData);
  }
}
