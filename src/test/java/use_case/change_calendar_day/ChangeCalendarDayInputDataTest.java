package use_case.change_calendar_day;

import entity.Calendar;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ChangeCalendarDayInputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        String date = "2024-01-01";

        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(calendarList, date);

        assertEquals(calendarList, inputData.getCalendarList());
        assertEquals(date, inputData.getDate());
    }

    @Test
    void constructor_NullCalendarList_ThrowsException() {
        String date = "2024-01-01";
        assertThrows(NullPointerException.class,
                () -> new ChangeCalendarDayInputData(null, date));
    }

    @Test
    void constructor_NullDate_ThrowsException() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        assertThrows(NullPointerException.class,
                () -> new ChangeCalendarDayInputData(calendarList, null));
    }

    @Test
    void getCalendarList_ReturnsCopy() {
        ArrayList<Calendar> originalList = new ArrayList<>();
        Calendar mockCalendar = mock(Calendar.class);
        originalList.add(mockCalendar);

        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(originalList, "2024-01-01");
        ArrayList<Calendar> returnedList = inputData.getCalendarList();

        // Verify it's a different list instance but with same content
        assertNotSame(originalList, returnedList);
        assertEquals(originalList, returnedList);
    }
}
