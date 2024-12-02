package use_case.change_calendar_month;

import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ChangeCalendarMonthOutputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        List<Event> eventList = new ArrayList<>();
        eventList.add(mock(Event.class));

        ChangeCalendarMonthOutputData outputData = new ChangeCalendarMonthOutputData(calendarList, eventList);

        assertEquals(calendarList, outputData.getCalendarList());
        assertEquals(eventList, outputData.getEventList());
    }

    @Test
    void constructor_NullCalendarList_ThrowsException() {
        List<Event> eventList = new ArrayList<>();
        assertThrows(NullPointerException.class,
                () -> new ChangeCalendarMonthOutputData(null, eventList));
    }

    @Test
    void constructor_NullEventList_ThrowsException() {
        List<Calendar> calendarList = new ArrayList<>();
        assertThrows(NullPointerException.class,
                () -> new ChangeCalendarMonthOutputData(calendarList, null));
    }

    @Test
    void getCalendarList_ReturnsSameList() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        List<Event> eventList = new ArrayList<>();

        ChangeCalendarMonthOutputData outputData = new ChangeCalendarMonthOutputData(calendarList, eventList);

        assertEquals(calendarList, outputData.getCalendarList());
    }

    @Test
    void getEventList_ReturnsSameList() {
        List<Calendar> calendarList = new ArrayList<>();
        List<Event> eventList = new ArrayList<>();
        eventList.add(mock(Event.class));

        ChangeCalendarMonthOutputData outputData = new ChangeCalendarMonthOutputData(calendarList, eventList);

        assertEquals(eventList, outputData.getEventList());
    }
}