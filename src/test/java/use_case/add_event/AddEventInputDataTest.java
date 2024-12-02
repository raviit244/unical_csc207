package use_case.add_event;

import entity.Calendar;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AddEventInputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        Calendar mockCalendar = mock(Calendar.class);
        String eventName = "Test Event";
        String date = "2024-01-01";
        String startTime = "09:00";
        String endTime = "10:00";

        AddEventInputData inputData = new AddEventInputData(eventName, date, startTime, endTime, mockCalendar);

        assertEquals(eventName, inputData.getEventName());
        assertEquals(date, inputData.getDate());
        assertEquals(startTime, inputData.getStartTime());
        assertEquals(endTime, inputData.getEndTime());
        assertEquals(mockCalendar, inputData.getCalendar());
    }
}