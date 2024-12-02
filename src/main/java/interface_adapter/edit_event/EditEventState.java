package interface_adapter.edit_event;

import entity.Calendar;
import entity.Event;

public class EditEventState {
  private Event originalEvent = null;
  private String eventName = "";
  private String date = "";
  private String startTime = "";
  private String endTime = "";
  private String eventNameError = null;
  private String dateError = null;
  private String timeError = null;
  private String calendarError = null;
  private String generalError = null;  // Added general error field
  private Calendar selectedCalendar = null;
  private boolean useCaseFailed = false;

  // Copy constructor for immutability
  public EditEventState(EditEventState copy) {
    originalEvent = copy.originalEvent;
    eventName = copy.eventName;
    date = copy.date;
    startTime = copy.startTime;
    endTime = copy.endTime;
    eventNameError = copy.eventNameError;
    dateError = copy.dateError;
    timeError = copy.timeError;
    calendarError = copy.calendarError;
    generalError = copy.generalError;
    selectedCalendar = copy.selectedCalendar;
    useCaseFailed = copy.useCaseFailed;
  }

  // Default constructor
  public EditEventState() {}

  // Getters
  public Event getOriginalEvent() { return originalEvent; }
  public String getEventName() { return eventName; }
  public String getDate() { return date; }
  public String getStartTime() { return startTime; }
  public String getEndTime() { return endTime; }
  public String getEventNameError() { return eventNameError; }
  public String getDateError() { return dateError; }
  public String getTimeError() { return timeError; }
  public String getCalendarError() { return calendarError; }
  public String getGeneralError() { return generalError; }
  public Calendar getSelectedCalendar() { return selectedCalendar; }
  public boolean isUseCaseFailed() { return useCaseFailed; }

  // Setters
  public void setOriginalEvent(Event event) { this.originalEvent = event; }
  public void setEventName(String eventName) { this.eventName = eventName; }
  public void setDate(String date) { this.date = date; }
  public void setStartTime(String startTime) { this.startTime = startTime; }
  public void setEndTime(String endTime) { this.endTime = endTime; }
  public void setEventNameError(String error) { this.eventNameError = error; }
  public void setDateError(String error) { this.dateError = error; }
  public void setTimeError(String error) { this.timeError = error; }
  public void setCalendarError(String error) { this.calendarError = error; }
  public void setGeneralError(String error) { this.generalError = error; }
  public void setSelectedCalendar(Calendar calendar) { this.selectedCalendar = calendar; }
  public void setUseCaseFailed(boolean failed) { this.useCaseFailed = failed; }
}
