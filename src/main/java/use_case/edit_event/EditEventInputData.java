package use_case.edit_event;

import entity.Calendar;

public class EditEventInputData {
  private final String eventName;
  private final String date;
  private final String startTime;
  private final String endTime;
  private final Calendar calendar;
  private final entity.Event originalEvent;

  public EditEventInputData(String eventName, String date, String startTime, String endTime,
                            Calendar calendar, entity.Event originalEvent) {
    this.eventName = eventName;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.calendar = calendar;
    this.originalEvent = originalEvent;
  }

  public String getEventName() { return eventName; }
  public String getDate() { return date; }
  public String getStartTime() { return startTime; }
  public String getEndTime() { return endTime; }
  public Calendar getCalendar() { return calendar; }
  public entity.Event getOriginalEvent() { return originalEvent; }
}
