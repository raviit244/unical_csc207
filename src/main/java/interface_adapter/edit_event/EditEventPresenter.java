package interface_adapter.edit_event;

import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import use_case.edit_event.EditEventOutputBoundary;
import use_case.edit_event.EditEventOutputData;

public class EditEventPresenter implements EditEventOutputBoundary {
  private final EditEventViewModel editEventViewModel;
  private final ChangeCalendarDayViewModel dayViewModel;

  public EditEventPresenter(EditEventViewModel editEventViewModel,
                            ChangeCalendarDayViewModel dayViewModel) {
    this.editEventViewModel = editEventViewModel;
    this.dayViewModel = dayViewModel;
  }

  @Override
  public void prepareSuccessView(EditEventOutputData outputData) {
    // Update both view models
    dayViewModel.removeEvent(outputData.getOldEvent());
    dayViewModel.addEvent(outputData.getNewEvent());
    dayViewModel.firePropertyChanged();

    EditEventState state = new EditEventState();
    state.setUseCaseFailed(false);
    editEventViewModel.setState(state);
    editEventViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(String error) {
    EditEventState state = new EditEventState();

    // Determine which type of error it is and set accordingly
    if (error.toLowerCase().contains("name")) {
      state.setEventNameError(error);
    } else if (error.toLowerCase().contains("date")) {
      state.setDateError(error);
    } else if (error.toLowerCase().contains("time")) {
      state.setTimeError(error);
    } else if (error.toLowerCase().contains("calendar")) {
      state.setCalendarError(error);
    } else {
      state.setGeneralError(error); // Use generalError instead of error
    }

    state.setUseCaseFailed(true);
    editEventViewModel.setState(state);
    editEventViewModel.firePropertyChanged();
  }
}
