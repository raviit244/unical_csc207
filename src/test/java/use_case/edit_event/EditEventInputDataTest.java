package use_case.edit_event;

import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class EditEventInputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        String eventName = "Test Event";
        String date = "2024-01-01";
        String startTime = "09:00";
        String endTime = "10:00";
        Calendar mockCalendar = mock(Calendar.class);
        Event mockOriginalEvent = mock(Event.class);

        EditEventInputData inputData = new EditEventInputData(
                eventName, date, startTime, endTime, mockCalendar, mockOriginalEvent);

        assertEquals(eventName, inputData.getEventName());
        assertEquals(date, inputData.getDate());
        assertEquals(startTime, inputData.getStartTime());
        assertEquals(endTime, inputData.getEndTime());
        assertEquals(mockCalendar, inputData.getCalendar());
        assertEquals(mockOriginalEvent, inputData.getOriginalEvent());
    }
}