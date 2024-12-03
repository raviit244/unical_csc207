package use_case.change_calendar_month;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;

/**
 * The Change Month Calendar Interactor.
 */
public class ChangeCalendarMonthInteractor implements ChangeCalendarMonthInputBoundary {
    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final ChangeCalendarMonthOutputBoundary changeCalendarMonthPresenter;

    public ChangeCalendarMonthInteractor(
            CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
            ChangeCalendarMonthOutputBoundary changeCalendarMonthPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.changeCalendarMonthPresenter = changeCalendarMonthPresenter;
    }

    @Override
    public void execute(ChangeCalendarMonthInputData inputData) {
        try {
            final List<Event> events = new ArrayList<>();
            final List<Calendar> calendars = inputData.getCalendarList();

            for (Calendar calendar : calendars) {
                final GetEventsDataAccessInterface getEventsDataAccessObject =
                        (GetEventsDataAccessInterface) calendarDataAccessObjectFactory
                                .getCalendarDataAccessObject(calendar);

                events.addAll(getEventsDataAccessObject.fetchEventsMonth(LocalDate.parse(inputData.getDate())));
            }

            final ChangeCalendarMonthOutputData outputData = new ChangeCalendarMonthOutputData(calendars, events);
            changeCalendarMonthPresenter.prepareSuccessView(outputData);
        }
        catch (Exception exception) {
            changeCalendarMonthPresenter.prepareFailView("Error fetching calendar data: " + exception.getMessage());
        }
    }
}

