package use_case.change_calendar_day;

import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ChangeCalendarDayOutputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(mock(Event.class));
        boolean useCaseFailed = false;

        ChangeCalendarDayOutputData outputData = new ChangeCalendarDayOutputData(
                calendarList, eventList, useCaseFailed);

        assertEquals(calendarList, outputData.getCalendarList());
        assertEquals(eventList, outputData.getEventList());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void constructor_NullCalendarList_ThrowsException() {
        ArrayList<Event> eventList = new ArrayList<>();
        assertThrows(NullPointerException.class,
                () -> new ChangeCalendarDayOutputData(null, eventList, false));
    }

    @Test
    void constructor_NullEventList_ThrowsException() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        assertThrows(NullPointerException.class,
                () -> new ChangeCalendarDayOutputData(calendarList, null, false));
    }

    @Test
    void getCalendarList_ReturnsCopy() {
        ArrayList<Calendar> originalList = new ArrayList<>();
        originalList.add(mock(Calendar.class));
        ArrayList<Event> eventList = new ArrayList<>();

        ChangeCalendarDayOutputData outputData = new ChangeCalendarDayOutputData(
                originalList, eventList, false);
        ArrayList<Calendar> returnedList = outputData.getCalendarList();

        assertNotSame(originalList, returnedList);
        assertEquals(originalList, returnedList);
    }

    @Test
    void getEventList_ReturnsCopy() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        ArrayList<Event> originalList = new ArrayList<>();
        originalList.add(mock(Event.class));

        ChangeCalendarDayOutputData outputData = new ChangeCalendarDayOutputData(
                calendarList, originalList, false);
        ArrayList<Event> returnedList = outputData.getEventList();

        assertNotSame(originalList, returnedList);
        assertEquals(originalList, returnedList);
    }
}
