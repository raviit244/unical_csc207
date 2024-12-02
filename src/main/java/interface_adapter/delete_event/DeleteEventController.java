package interface_adapter.delete_event;

import entity.Event;
import use_case.delete_event.DeleteEventInputBoundary;
import use_case.delete_event.DeleteEventInputData;

public class DeleteEventController {
  private final DeleteEventInputBoundary deleteEventUseCaseInteractor;

  public DeleteEventController(DeleteEventInputBoundary deleteEventUseCaseInteractor) {
    this.deleteEventUseCaseInteractor = deleteEventUseCaseInteractor;
  }

  public void execute(Event event) {
    DeleteEventInputData deleteEventInputData = new DeleteEventInputData(event);
    deleteEventUseCaseInteractor.execute(deleteEventInputData);
  }
}
