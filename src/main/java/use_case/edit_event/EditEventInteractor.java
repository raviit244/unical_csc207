package use_case.edit_event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import data_access.AddEventDataAccessInterface;
import data_access.CalendarDataAccessObjectFactory;
import data_access.DeleteEventDataAccessInterface;
import entity.Calendar;
import entity.Event;

/**
 * Use-case interactor for the Edit Event use-case.
 */
public class EditEventInteractor implements EditEventInputBoundary {
    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final EditEventOutputBoundary editEventPresenter;

    public EditEventInteractor(
            CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
            EditEventOutputBoundary editEventPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.editEventPresenter = editEventPresenter;
    }

    @Override
    public void execute(EditEventInputData inputData) {
        try {
            if (inputData.getEventName().trim().isEmpty()) {
                editEventPresenter.prepareFailView("Event name cannot be empty");
                return;
            }

            final LocalDate date = LocalDate.parse(inputData.getDate());
            final LocalTime startTime = LocalTime.parse(inputData.getStartTime());
            final LocalTime endTime = LocalTime.parse(inputData.getEndTime());

            if (endTime.isBefore(startTime)) {
                editEventPresenter.prepareFailView("End time cannot be before start time");
                return;
            }

            final Calendar calendar = inputData.getCalendar();
            final Event originalEvent = inputData.getOriginalEvent();
            final Event newEvent = new Event(
                    inputData.getEventName(),
                    date,
                    startTime,
                    endTime,
                    calendar
            );

            // Delete the original event
            final DeleteEventDataAccessInterface deleteEventDataAccessObject =
                    (DeleteEventDataAccessInterface) calendarDataAccessObjectFactory
                            .getCalendarDataAccessObject(calendar);
            if (!deleteEventDataAccessObject.deleteEvent(originalEvent)) {
                editEventPresenter.prepareFailView("Failed to update original event");
                return;
            }

            // Add the new event
            final AddEventDataAccessInterface addEventDataAccessObject =
                    (AddEventDataAccessInterface) calendarDataAccessObjectFactory.getCalendarDataAccessObject(calendar);
            if (!addEventDataAccessObject.addEvent(newEvent)) {
                editEventPresenter.prepareFailView("Failed to add updated event");
                return;
            }

            final EditEventOutputData outputData = new EditEventOutputData(originalEvent, newEvent, false);
            editEventPresenter.prepareSuccessView(outputData);

        }
        catch (DateTimeParseException exception) {
            if (exception.getParsedString().contains(":")) {
                editEventPresenter.prepareFailView("Invalid time format. Use HH:mm (24-hour format)");
            }
            else {
                editEventPresenter.prepareFailView("Invalid date format. Use YYYY-MM-DD");
            }
        }
        catch (Exception exception) {
            editEventPresenter.prepareFailView("Error editing event: " + exception.getMessage());
        }
    }
}
