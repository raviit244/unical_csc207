package use_case.merge_calendars;

import entity.Calendar;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MergeCalendarsInputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        String date = "2024-01-01";

        MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendarList, date);

        assertEquals(calendarList, inputData.getCalendars());
        assertEquals(date, inputData.getDate());
    }

    @Test
    void constructor_EmptyCalendarList_Valid() {
        List<Calendar> calendarList = new ArrayList<>();
        String date = "2024-01-01";

        MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendarList, date);

        assertTrue(inputData.getCalendars().isEmpty());
        assertEquals(date, inputData.getDate());
    }

    @Test
    void getCalendarList_ReturnsSameList() {
        List<Calendar> calendarList = new ArrayList<>();
        Calendar mockCalendar = mock(Calendar.class);
        calendarList.add(mockCalendar);
        String date = "2024-01-01";

        MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendarList, date);
        List<Calendar> returnedList = inputData.getCalendars();

        // Since no defensive copying is implemented, it should return the same list
        assertEquals(calendarList, returnedList);
    }
}
