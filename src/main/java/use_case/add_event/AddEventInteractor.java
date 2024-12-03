package use_case.add_event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import data_access.AddEventDataAccessInterface;
import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.Event;

/**
 * Use-case interactor that runs the Add Event use-case.
 */
public class AddEventInteractor implements AddEventInputBoundary {
    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final AddEventOutputBoundary addEventPresenter;

    public AddEventInteractor(
            CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
            AddEventOutputBoundary addEventPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.addEventPresenter = addEventPresenter;
    }

    @Override
    public void execute(AddEventInputData inputData) {
        try {
            if (inputData.getEventName().trim().isEmpty()) {
                addEventPresenter.prepareFailView("Event name cannot be empty");
                return;
            }

            // Parse date first
            final LocalDate date;
            try {
                date = LocalDate.parse(inputData.getDate());
            }
            catch (DateTimeParseException dateTimeParseException) {
                addEventPresenter.prepareFailView("Invalid date format. Use YYYY-MM-DD");
                return;
            }

            // Parse times separately
            final LocalTime startTime;
            final LocalTime endTime;
            try {
                startTime = LocalTime.parse(inputData.getStartTime());
            }
            catch (DateTimeParseException dateTimeParseException) {
                addEventPresenter.prepareFailView("Invalid time format. Use HH:mm (24-hour format)");
                return;
            }

            try {
                endTime = LocalTime.parse(inputData.getEndTime());
            }
            catch (DateTimeParseException dateTimeParseException) {
                addEventPresenter.prepareFailView("Invalid time format. Use HH:mm (24-hour format)");
                return;
            }

            if (endTime.isBefore(startTime)) {
                addEventPresenter.prepareFailView("End time cannot be before start time");
                return;
            }

            final Calendar calendar = inputData.getCalendar();
            final Event event = new Event(
                    inputData.getEventName(),
                    date,
                    startTime,
                    endTime,
                    calendar
            );

            final AddEventDataAccessInterface addEventDataAccessObject =
                    (AddEventDataAccessInterface) calendarDataAccessObjectFactory
                            .getCalendarDataAccessObject(calendar);

            if (!addEventDataAccessObject.addEvent(event)) {
                addEventPresenter.prepareFailView("Failed to add event to calendar");
                return;
            }

            final AddEventOutputData outputData = new AddEventOutputData(event, false);
            addEventPresenter.prepareSuccessView(outputData);

        }
        catch (Exception exception) {
            addEventPresenter.prepareFailView("Error adding event: " + exception.getMessage());
        }
    }
}
