package use_case.merge_calendars;

import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MergeCalendarsOutputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        List<Event> eventList = new ArrayList<>();
        eventList.add(mock(Event.class));

        MergeCalendarsOutputData outputData = new MergeCalendarsOutputData(calendarList, eventList, false);

        assertEquals(calendarList, outputData.getCalendars());
        assertEquals(eventList, outputData.getEvents());
    }

    @Test
    void constructor_EmptyLists_Valid() {
        List<Calendar> calendarList = new ArrayList<>();
        List<Event> eventList = new ArrayList<>();

        MergeCalendarsOutputData outputData = new MergeCalendarsOutputData(calendarList, eventList, false);

        assertTrue(outputData.getCalendars().isEmpty());
        assertTrue(outputData.getEvents().isEmpty());
    }

    @Test
    void getCalendarList_ReturnsSameList() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        List<Event> eventList = new ArrayList<>();

        MergeCalendarsOutputData outputData = new MergeCalendarsOutputData(calendarList, eventList, false);
        List<Calendar> returnedList = outputData.getCalendars();

        // Since no defensive copying is implemented, it should return the same list
        assertEquals(calendarList, returnedList);
    }

    @Test
    void getEventList_ReturnsSameList() {
        List<Calendar> calendarList = new ArrayList<>();
        List<Event> eventList = new ArrayList<>();
        eventList.add(mock(Event.class));

        MergeCalendarsOutputData outputData = new MergeCalendarsOutputData(calendarList, eventList, false);
        List<Event> returnedList = outputData.getEvents();

        // Since no defensive copying is implemented, it should return the same list
        assertEquals(eventList, returnedList);
    }
}