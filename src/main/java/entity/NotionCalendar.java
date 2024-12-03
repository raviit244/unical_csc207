package entity;

/**
 * Represents a Notion Calendar.
 */
public class NotionCalendar implements Calendar {
    private final String notionToken;
    private final String databaseID;
    private final String calendarName;

    /**
     * Creates a new Notion Calendar instance.
     * @param notionToken The authentication token for Notion API
     * @param databaseID The ID of the Notion database to use as calendar
     * @param calendarName The display name for the calendar
     * @throws NullPointerException if any parameter is null
     * @throws IllegalArgumentException if any parameter is empty or blank
     */
    public NotionCalendar(String notionToken, String databaseID, String calendarName) {
        if (notionToken == null) {
            throw new NullPointerException("Notion token cannot be null");
        }
        if (databaseID == null) {
            throw new NullPointerException("Database ID cannot be null");
        }
        if (calendarName == null) {
            throw new NullPointerException("Calendar name cannot be null");
        }
        if (notionToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Notion token cannot be empty");
        }
        if (databaseID.trim().isEmpty()) {
            throw new IllegalArgumentException("Database ID cannot be empty");
        }
        if (calendarName.trim().isEmpty()) {
            throw new IllegalArgumentException("Calendar name cannot be empty");
        }

        this.notionToken = notionToken;
        this.databaseID = databaseID;
        this.calendarName = calendarName;
    }

    @Override
    public String getCalendarApiName() {
        return "NotionCalendar";
    }

    @Override
    public String getCalendarName() {
        return calendarName;
    }

    public String getNotionToken() {
        return notionToken;
    }

    public String getDatabaseID() {
        return databaseID;
    }
}
