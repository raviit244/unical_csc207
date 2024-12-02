package data_access;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import entity.GoogleCalendar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;

public class GoogleCalendarDataAccessObject implements GetEventsDataAccessInterface, AddEventDataAccessInterface, DeleteEventDataAccessInterface {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "UniCal";
    private final GoogleCalendar calendar;
    private Calendar service;

    public GoogleCalendarDataAccessObject(GoogleCalendar calendar) {
        this.calendar = calendar;
        try {
            initializeService();
        } catch (Exception e) {
            System.err.println("Error initializing Google Calendar service: " + e.getMessage());
            e.printStackTrace();
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
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return fetchEvents(startOfDay, endOfDay);
    }

    @Override
    public ArrayList<entity.Event> fetchEventsMonth(LocalDate date) {
        LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = date.plusMonths(1).withDayOfMonth(1).atStartOfDay();
        return fetchEvents(startOfMonth, endOfMonth);
    }

    private ArrayList<entity.Event> fetchEvents(LocalDateTime start, LocalDateTime end) {
        ArrayList<entity.Event> events = new ArrayList<>();
        try {
            DateTime startDateTime = new DateTime(Date.from(start.atZone(ZoneId.systemDefault()).toInstant()));
            DateTime endDateTime = new DateTime(Date.from(end.atZone(ZoneId.systemDefault()).toInstant()));

            Events eventsList = service.events().list(calendar.getCalendarId())
                    .setTimeMin(startDateTime)
                    .setTimeMax(endDateTime)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            for (Event googleEvent : eventsList.getItems()) {
                try {
                    EventDateTime eventStart = googleEvent.getStart();
                    EventDateTime eventEnd = googleEvent.getEnd();

                    LocalDate eventDate;
                    LocalTime startTime;
                    LocalTime endTime;

                    if (eventStart.getDateTime() != null) {
                        // For events with specific times
                        ZonedDateTime zonedStartTime = Instant.ofEpochMilli(eventStart.getDateTime().getValue())
                                .atZone(ZoneId.systemDefault());
                        ZonedDateTime zonedEndTime = Instant.ofEpochMilli(eventEnd.getDateTime().getValue())
                                .atZone(ZoneId.systemDefault());

                        eventDate = zonedStartTime.toLocalDate();
                        startTime = zonedStartTime.toLocalTime();
                        endTime = zonedEndTime.toLocalTime();
                    } else {
                        // For all-day events
                        eventDate = LocalDate.parse(eventStart.getDate().toString());
                        startTime = LocalTime.of(0, 0); // Start of day
                        endTime = LocalTime.of(23, 59); // End of day
                    }

                    entity.Event event = new entity.Event(
                            googleEvent.getSummary() != null ? googleEvent.getSummary() : "Untitled Event",
                            eventDate,
                            startTime,
                            endTime,
                            calendar
                    );
                    events.add(event);
                } catch (Exception e) {
                    System.err.println("Error parsing event: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching events: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public boolean addEvent(entity.Event event) {
        try {
            Event googleEvent = new Event()
                    .setSummary(event.getEventName());

            // Create DateTime for start
            LocalDateTime startDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());
            DateTime start = new DateTime(Date.from(
                    startDateTime.atZone(ZoneId.systemDefault()).toInstant()
            ));

            // Create DateTime for end
            LocalDateTime endDateTime = LocalDateTime.of(event.getDate(), event.getEndTime());
            DateTime end = new DateTime(Date.from(
                    endDateTime.atZone(ZoneId.systemDefault()).toInstant()
            ));

            // Set timezone for both start and end times
            EventDateTime startEventDateTime = new EventDateTime()
                    .setDateTime(start)
                    .setTimeZone(ZoneId.systemDefault().getId());

            EventDateTime endEventDateTime = new EventDateTime()
                    .setDateTime(end)
                    .setTimeZone(ZoneId.systemDefault().getId());

            googleEvent.setStart(startEventDateTime)
                    .setEnd(endEventDateTime);

            service.events().insert(calendar.getCalendarId(), googleEvent).execute();
            return true;
        } catch (IOException e) {
            System.err.println("Error adding event: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteEvent(entity.Event event) {
        try {
            // Find events on the given date
            LocalDateTime startOfDay = event.getDate().atStartOfDay();
            LocalDateTime endOfDay = event.getDate().plusDays(1).atStartOfDay();

            DateTime startTime = new DateTime(Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()));
            DateTime endTime = new DateTime(Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant()));

            Events events = service.events().list(calendar.getCalendarId())
                    .setTimeMin(startTime)
                    .setTimeMax(endTime)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            // Find the matching event
            for (Event googleEvent : events.getItems()) {
                if (googleEvent.getSummary().equals(event.getEventName())) {
                    // Additional check for time match if needed
                    EventDateTime eventStart = googleEvent.getStart();
                    if (eventStart.getDateTime() != null) {
                        LocalTime eventTime = Instant.ofEpochMilli(eventStart.getDateTime().getValue())
                                .atZone(ZoneId.systemDefault())
                                .toLocalTime();

                        if (eventTime.equals(event.getStartTime())) {
                            service.events().delete(calendar.getCalendarId(), googleEvent.getId()).execute();
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error deleting event: " + e.getMessage());
            return false;
        }
    }
}
