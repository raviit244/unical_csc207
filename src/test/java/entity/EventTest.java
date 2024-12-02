package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

class EventTest {
    private Event event;
    private LocalDate testDate;
    private LocalTime testStartTime;
    private LocalTime testEndTime;
    private Calendar testCalendar;
    private String testEventName;

    @BeforeEach
    void setUp() {
        testEventName = "Test Event";
        testDate = LocalDate.of(2024, 1, 1);
        testStartTime = LocalTime.of(9, 0);
        testEndTime = LocalTime.of(10, 0);
        testCalendar = new Calendar() {
            @Override
            public String getCalendarApiName() {
                return "TestCalendar";
            }

            @Override
            public String getCalendarName() {
                return "Test Calendar";
            }
        };

        event = new Event(testEventName, testDate, testStartTime, testEndTime, testCalendar);
    }

    @Test
    void constructor_ValidParameters_CreatesEvent() {
        assertNotNull(event);
        assertEquals(testEventName, event.getEventName());
        assertEquals(testDate, event.getDate());
        assertEquals(testStartTime, event.getStartTime());
        assertEquals(testEndTime, event.getEndTime());
        assertEquals(testCalendar, event.getCalendarApi());
    }

    @Test
    void constructor_WithNullEventName_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new Event(null, testDate, testStartTime, testEndTime, testCalendar)
        );
        assertEquals("All parameters must be non-null", exception.getMessage());
    }

    @Test
    void constructor_WithNullDate_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new Event(testEventName, null, testStartTime, testEndTime, testCalendar)
        );
        assertEquals("All parameters must be non-null", exception.getMessage());
    }

    @Test
    void constructor_WithNullStartTime_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new Event(testEventName, testDate, null, testEndTime, testCalendar)
        );
        assertEquals("All parameters must be non-null", exception.getMessage());
    }

    @Test
    void constructor_WithNullEndTime_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new Event(testEventName, testDate, testStartTime, null, testCalendar)
        );
        assertEquals("All parameters must be non-null", exception.getMessage());
    }

    @Test
    void constructor_WithNullCalendar_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new Event(testEventName, testDate, testStartTime, testEndTime, null)
        );
        assertEquals("All parameters must be non-null", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyEventName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Event("", testDate, testStartTime, testEndTime, testCalendar)
        );
        assertEquals("Event name cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithBlankEventName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Event("   ", testDate, testStartTime, testEndTime, testCalendar)
        );
        assertEquals("Event name cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithEndTimeBeforeStartTime_ThrowsIllegalArgumentException() {
        LocalTime earlierTime = LocalTime.of(8, 0);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Event(testEventName, testDate, testStartTime, earlierTime, testCalendar)
        );
        assertEquals("End time cannot be before start time", exception.getMessage());
    }

    @Test
    void constructor_TrimsEventName() {
        Event eventWithSpaces = new Event("  Test Event  ", testDate, testStartTime, testEndTime, testCalendar);
        assertEquals("Test Event", eventWithSpaces.getEventName());
    }

    @Test
    void getEventName_ReturnsCorrectName() {
        assertEquals(testEventName, event.getEventName());
    }

    @Test
    void getDate_ReturnsCorrectDate() {
        assertEquals(testDate, event.getDate());
    }

    @Test
    void getStartTime_ReturnsCorrectStartTime() {
        assertEquals(testStartTime, event.getStartTime());
    }

    @Test
    void getEndTime_ReturnsCorrectEndTime() {
        assertEquals(testEndTime, event.getEndTime());
    }

    @Test
    void getCalendarApi_ReturnsCorrectCalendar() {
        assertEquals(testCalendar, event.getCalendarApi());
    }
}