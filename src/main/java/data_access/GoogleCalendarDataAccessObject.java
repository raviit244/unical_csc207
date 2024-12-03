package data_access;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import entity.GoogleCalendar;

/**
 * Data Access Object for accessing the Google Calendar API.
 */
public class GoogleCalendarDataAccessObject implements GetEventsDataAccessInterface,
        AddEventDataAccessInterface, DeleteEventDataAccessInterface {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "UniCal";
    private final GoogleCalendar calendar;
    private Calendar service;

    public GoogleCalendarDataAccessObject(GoogleCalendar calendar) {
        this.calendar = calendar;
        try {
            initializeService();
        }
        catch (Exception exception) {
            System.err.println("Error initializing Google Calendar service: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void initializeService() throws GeneralSecurityException, IOException {
        service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                calendar.getHttpRequestInitializer())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public ArrayList<entity.Event> fetchEventsDay(LocalDate date) {
        final LocalDateTime startOfDay = date.atStartOfDay();
        final LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return fetchEvents(startOfDay, endOfDay);
    }

    @Override
    public ArrayList<entity.Event> fetchEventsMonth(LocalDate date) {
        final LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
        final LocalDateTime endOfMonth = date.plusMonths(1).withDayOfMonth(1).atStartOfDay();
        return fetchEvents(startOfMonth, endOfMonth);
    }

    private ArrayList<entity.Event> fetchEvents(LocalDateTime start, LocalDateTime end) {
        final ArrayList<entity.Event> events = new ArrayList<>();
        try {
            final DateTime startDateTime = new DateTime(Date.from(start.atZone(ZoneId.systemDefault()).toInstant()));
            final DateTime endDateTime = new DateTime(Date.from(end.atZone(ZoneId.systemDefault()).toInstant()));

            final Events eventsList = service.events().list(calendar.getCalendarId())
                    .setTimeMin(startDateTime)
                    .setTimeMax(endDateTime)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            for (Event googleEvent : eventsList.getItems()) {
                try {
                    final EventDateTime eventStart = googleEvent.getStart();
                    final EventDateTime eventEnd = googleEvent.getEnd();

                    final LocalDate eventDate;
                    final LocalTime startTime;
                    final LocalTime endTime;

                    if (eventStart.getDateTime() != null) {
                        // For events with specific times
                        final ZonedDateTime zonedStartTime = Instant.ofEpochMilli(eventStart.getDateTime().getValue())
                                .atZone(ZoneId.systemDefault());
                        final ZonedDateTime zonedEndTime = Instant.ofEpochMilli(eventEnd.getDateTime().getValue())
                                .atZone(ZoneId.systemDefault());

                        eventDate = zonedStartTime.toLocalDate();
                        startTime = zonedStartTime.toLocalTime();
                        endTime = zonedEndTime.toLocalTime();
                    }
                    else {
                        // For all-day events
                        eventDate = LocalDate.parse(eventStart.getDate().toString());
                        startTime = LocalTime.of(0, 0);
                        endTime = LocalTime.of(23, 59);
                    }

                    final entity.Event event;
                    if (googleEvent.getSummary() != null) {
                        event = new entity.Event(
                                googleEvent.getSummary(),
                                eventDate,
                                startTime,
                                endTime,
                                calendar
                        );
                    }
                    else {
                        event = new entity.Event(
                                "Untitled Event",
                                eventDate,
                                startTime,
                                endTime,
                                calendar
                        );
                    }
                    events.add(event);
                }
                catch (Exception exception) {
                    System.err.println("Error parsing event: " + exception.getMessage());
                    exception.printStackTrace();
                }
            }
        }
        catch (IOException ioException) {
            System.err.println("Error fetching events: " + ioException.getMessage());
            ioException.printStackTrace();
        }
        return events;
    }

    @Override
    public boolean addEvent(entity.Event event) {
        boolean flag = false;
        try {
            final Event googleEvent = new Event()
                    .setSummary(event.getEventName());

            // Create DateTime for start
            final LocalDateTime startDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());
            final DateTime start = new DateTime(Date.from(
                    startDateTime.atZone(ZoneId.systemDefault()).toInstant()
            ));

            // Create DateTime for end
            final LocalDateTime endDateTime = LocalDateTime.of(event.getDate(), event.getEndTime());
            final DateTime end = new DateTime(Date.from(
                    endDateTime.atZone(ZoneId.systemDefault()).toInstant()
            ));

            // Set timezone for both start and end times
            final EventDateTime startEventDateTime = new EventDateTime()
                    .setDateTime(start)
                    .setTimeZone(ZoneId.systemDefault().getId());

            final EventDateTime endEventDateTime = new EventDateTime()
                    .setDateTime(end)
                    .setTimeZone(ZoneId.systemDefault().getId());

            googleEvent.setStart(startEventDateTime)
                    .setEnd(endEventDateTime);

            service.events().insert(calendar.getCalendarId(), googleEvent).execute();
            flag = true;
        }
        catch (IOException ioException) {
            System.err.println("Error adding event: " + ioException.getMessage());
        }
        return flag;
    }

    @Override
    public boolean deleteEvent(entity.Event event) {
        boolean flag = false;
        try {
            // Find events on the given date
            final LocalDateTime startOfDay = event.getDate().atStartOfDay();
            final LocalDateTime endOfDay = event.getDate().plusDays(1).atStartOfDay();

            final DateTime startTime = new DateTime(Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()));
            final DateTime endTime = new DateTime(Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant()));

            final Events events = service.events().list(calendar.getCalendarId())
                    .setTimeMin(startTime)
                    .setTimeMax(endTime)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            // Find the matching event
            for (Event googleEvent : events.getItems()) {
                if (googleEvent.getSummary().equals(event.getEventName())) {
                    // Additional check for time match if needed
                    final EventDateTime eventStart = googleEvent.getStart();
                    if (eventStart.getDateTime() != null) {
                        final LocalTime eventTime = Instant.ofEpochMilli(eventStart.getDateTime().getValue())
                                .atZone(ZoneId.systemDefault())
                                .toLocalTime();

                        if (eventTime.equals(event.getStartTime())) {
                            service.events().delete(calendar.getCalendarId(), googleEvent.getId()).execute();
                            flag = true;
                        }
                    }
                }
            }
        }
        catch (IOException ioException) {
            System.err.println("Error deleting event: " + ioException.getMessage());
        }
        return flag;
    }
}
