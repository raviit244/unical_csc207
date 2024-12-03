package use_case.merge_calendars;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;

/**
 * Use-case interactor for the Merge Calendars use-case.
 */
public class MergeCalendarsInteractor implements MergeCalendarsInputBoundary {
    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final MergeCalendarsOutputBoundary mergeCalendarsPresenter;

    public MergeCalendarsInteractor(
            CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
            MergeCalendarsOutputBoundary mergeCalendarsPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.mergeCalendarsPresenter = mergeCalendarsPresenter;
    }

    @Override
    public void execute(MergeCalendarsInputData inputData) {
        try {
            final List<Event> allEvents = new ArrayList<>();
            final List<Calendar> calendars = inputData.getCalendars();

            for (Calendar calendar : calendars) {
                final GetEventsDataAccessInterface getEventsDataAccessObject =
                        (GetEventsDataAccessInterface) calendarDataAccessObjectFactory
                                .getCalendarDataAccessObject(calendar);

                final List<Event> calendarEvents = getEventsDataAccessObject.fetchEventsMonth(
                        LocalDate.parse(inputData.getDate()));
                allEvents.addAll(calendarEvents);
            }

            // Sort events by date and time
            allEvents.sort((event1, event2) -> {
                final int dateCompare = event1.getDate().compareTo(event2.getDate());
                if (dateCompare != 0) {
                    return dateCompare;
                }
                return event1.getStartTime().compareTo(event2.getStartTime());
            });

            final MergeCalendarsOutputData outputData = new MergeCalendarsOutputData(calendars, allEvents, false);
            mergeCalendarsPresenter.prepareSuccessView(outputData);
        }
        catch (Exception exception) {
            mergeCalendarsPresenter.prepareFailView("Error merging calendars: " + exception.getMessage());
        }
    }
}
