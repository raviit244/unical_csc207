package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotionCalendarTest {
    private NotionCalendar calendar;
    private String validNotionToken;
    private String validDatabaseId;
    private String validCalendarName;

    @BeforeEach
    void setUp() {
        validNotionToken = "secret_test_token_123";
        validDatabaseId = "database_id_456";
        validCalendarName = "Test Notion Calendar";
        calendar = new NotionCalendar(validNotionToken, validDatabaseId, validCalendarName);
    }

    @Test
    void constructor_WithValidParameters_CreatesNotionCalendar() {
        assertNotNull(calendar);
        assertEquals(validNotionToken, calendar.getNotionToken());
        assertEquals(validDatabaseId, calendar.getDatabaseID());
        assertEquals(validCalendarName, calendar.getCalendarName());
        assertEquals("NotionCalendar", calendar.getCalendarApiName());
    }

    @Test
    void constructor_WithNullNotionToken_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new NotionCalendar(null, validDatabaseId, validCalendarName)
        );
        assertEquals("Notion token cannot be null", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyNotionToken_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new NotionCalendar("", validDatabaseId, validCalendarName)
        );
        assertEquals("Notion token cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithBlankNotionToken_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new NotionCalendar("   ", validDatabaseId, validCalendarName)
        );
        assertEquals("Notion token cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithNullDatabaseId_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new NotionCalendar(validNotionToken, null, validCalendarName)
        );
        assertEquals("Database ID cannot be null", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyDatabaseId_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new NotionCalendar(validNotionToken, "", validCalendarName)
        );
        assertEquals("Database ID cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithBlankDatabaseId_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new NotionCalendar(validNotionToken, "   ", validCalendarName)
        );
        assertEquals("Database ID cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithNullCalendarName_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new NotionCalendar(validNotionToken, validDatabaseId, null)
        );
        assertEquals("Calendar name cannot be null", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyCalendarName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new NotionCalendar(validNotionToken, validDatabaseId, "")
        );
        assertEquals("Calendar name cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithBlankCalendarName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new NotionCalendar(validNotionToken, validDatabaseId, "   ")
        );
        assertEquals("Calendar name cannot be empty", exception.getMessage());
    }

    @Test
    void getNotionToken_ReturnsCorrectToken() {
        assertEquals(validNotionToken, calendar.getNotionToken());
    }

    @Test
    void getDatabaseID_ReturnsCorrectDatabaseId() {
        assertEquals(validDatabaseId, calendar.getDatabaseID());
    }

    @Test
    void getCalendarName_ReturnsCorrectName() {
        assertEquals(validCalendarName, calendar.getCalendarName());
    }

    @Test
    void getCalendarApiName_ReturnsCorrectApiName() {
        assertEquals("NotionCalendar", calendar.getCalendarApiName());
    }
}