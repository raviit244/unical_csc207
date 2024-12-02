package interface_adapter.edit_event;

import interface_adapter.ViewModel;

public class EditEventViewModel extends ViewModel<EditEventState> {
  public static final String TITLE_LABEL = "Edit Event";
  public static final String EVENT_NAME_LABEL = "Event Name";
  public static final String DATE_LABEL = "Date (YYYY-MM-DD)";
  public static final String START_TIME_LABEL = "Start Time (HH:mm)";
  public static final String END_TIME_LABEL = "End Time (HH:mm)";
  public static final String SAVE_BUTTON_LABEL = "Save";
  public static final String CANCEL_BUTTON_LABEL = "Cancel";
  public static final String CALENDAR_LABEL = "Calendar";

  public EditEventViewModel() {
    super("edit event");
    setState(new EditEventState());
  }
}
