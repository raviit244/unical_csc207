package use_case.add_event;

import data_access.AddEventDataAccessInterface;
import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.Event;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

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
            LocalDate date;
            try {
                date = LocalDate.parse(inputData.getDate());
            } catch (DateTimeParseException e) {
                addEventPresenter.prepareFailView("Invalid date format. Use YYYY-MM-DD");
                return;
            }

            // Parse times separately
            LocalTime startTime;
            LocalTime endTime;
            try {
                startTime = LocalTime.parse(inputData.getStartTime());
            } catch (DateTimeParseException e) {
                addEventPresenter.prepareFailView("Invalid time format. Use HH:mm (24-hour format)");
                return;
            }

            try {
                endTime = LocalTime.parse(inputData.getEndTime());
            } catch (DateTimeParseException e) {
                addEventPresenter.prepareFailView("Invalid time format. Use HH:mm (24-hour format)");
                return;
            }

            if (endTime.isBefore(startTime)) {
                addEventPresenter.prepareFailView("End time cannot be before start time");
                return;
            }

            Calendar calendar = inputData.getCalendar();
            Event event = new Event(
                    inputData.getEventName(),
                    date,
                    startTime,
                    endTime,
                    calendar
            );

            AddEventDataAccessInterface addEventDataAccessObject =
                    (AddEventDataAccessInterface) calendarDataAccessObjectFactory
                            .getCalendarDataAccessObject(calendar);

            if (!addEventDataAccessObject.addEvent(event)) {
                addEventPresenter.prepareFailView("Failed to add event to calendar");
                return;
            }

            AddEventOutputData outputData = new AddEventOutputData(event, false);
            addEventPresenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            addEventPresenter.prepareFailView("Error adding event: " + e.getMessage());
        }
    }
}