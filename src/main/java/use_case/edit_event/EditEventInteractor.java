package use_case.edit_event;

import data_access.CalendarDataAccessObjectFactory;
import data_access.DeleteEventDataAccessInterface;
import data_access.AddEventDataAccessInterface;
import entity.Calendar;
import entity.Event;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class EditEventInteractor implements EditEventInputBoundary {
  private final CalendarDataAccessObjectFactory calendarDAOFactory;
  private final EditEventOutputBoundary editEventPresenter;

  public EditEventInteractor(
    CalendarDataAccessObjectFactory calendarDAOFactory,
    EditEventOutputBoundary editEventPresenter) {
    this.calendarDAOFactory = calendarDAOFactory;
    this.editEventPresenter = editEventPresenter;
  }

  @Override
  public void execute(EditEventInputData inputData) {
    try {
      if (inputData.getEventName().trim().isEmpty()) {
        editEventPresenter.prepareFailView("Event name cannot be empty");
        return;
      }

      LocalDate date = LocalDate.parse(inputData.getDate());
      LocalTime startTime = LocalTime.parse(inputData.getStartTime());
      LocalTime endTime = LocalTime.parse(inputData.getEndTime());

      if (endTime.isBefore(startTime)) {
        editEventPresenter.prepareFailView("End time cannot be before start time");
        return;
      }

      Calendar calendar = inputData.getCalendar();
      Event originalEvent = inputData.getOriginalEvent();
      Event newEvent = new Event(
        inputData.getEventName(),
        date,
        startTime,
        endTime,
        calendar
      );

      // Delete the original event
      DeleteEventDataAccessInterface deleteEventDAO =
        (DeleteEventDataAccessInterface) calendarDAOFactory.getCalendarDataAccessObject(calendar);
      if (!deleteEventDAO.deleteEvent(originalEvent)) {
        editEventPresenter.prepareFailView("Failed to update original event");
        return;
      }

      // Add the new event
      AddEventDataAccessInterface addEventDAO =
        (AddEventDataAccessInterface) calendarDAOFactory.getCalendarDataAccessObject(calendar);
      if (!addEventDAO.addEvent(newEvent)) {
        editEventPresenter.prepareFailView("Failed to add updated event");
        return;
      }

      EditEventOutputData outputData = new EditEventOutputData(originalEvent, newEvent, false);
      editEventPresenter.prepareSuccessView(outputData);

    } catch (DateTimeParseException e) {
      if (e.getParsedString().contains(":")) {
        editEventPresenter.prepareFailView("Invalid time format. Use HH:mm (24-hour format)");
      } else {
        editEventPresenter.prepareFailView("Invalid date format. Use YYYY-MM-DD");
      }
    } catch (Exception e) {
      editEventPresenter.prepareFailView("Error editing event: " + e.getMessage());
    }
  }
}
