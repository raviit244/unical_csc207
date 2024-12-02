package use_case.change_calendar_month;

import entity.Calendar;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ChangeCalendarMonthInputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        String date = "2024-01-01";

        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendarList, date);

        assertEquals(calendarList, inputData.getCalendarList());
        assertEquals(date, inputData.getDate());
    }

    @Test
    void constructor_NullCalendarList_ThrowsException() {
        String date = "2024-01-01";
        assertThrows(NullPointerException.class,
                () -> new ChangeCalendarMonthInputData(null, date));
    }

    @Test
    void constructor_NullDate_ThrowsException() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        assertThrows(NullPointerException.class,
                () -> new ChangeCalendarMonthInputData(calendarList, null));
    }

    @Test
    void getCalendarList_ReturnsSameList() {
        List<Calendar> calendarList = new ArrayList<>();
        Calendar mockCalendar = mock(Calendar.class);
        calendarList.add(mockCalendar);
        String date = "2024-01-01";

        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendarList, date);

        assertEquals(calendarList, inputData.getCalendarList());
    }
}
