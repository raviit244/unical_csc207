package app;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List; // Make sure you have this import instead of java.awt.List

import data_access.*;
import entity.*;
import interface_adapter.add_event.*;
import interface_adapter.change_calendar_day.*;
import interface_adapter.change_calendar_month.*;
import interface_adapter.delete_event.*;
import interface_adapter.edit_event.EditEventController;
import interface_adapter.edit_event.EditEventPresenter;
import interface_adapter.edit_event.EditEventViewModel;
import use_case.add_event.AddEventInteractor;
import use_case.change_calendar_day.ChangeCalendarDayInteractor;
import use_case.change_calendar_month.ChangeCalendarMonthInteractor;
import use_case.delete_event.DeleteEventInteractor;
import use_case.edit_event.EditEventInteractor;
import view.*;
import interface_adapter.merge_calendars.MergeCalendarsController;
import interface_adapter.merge_calendars.MergeCalendarsPresenter;
import use_case.merge_calendars.MergeCalendarsInteractor;
import interface_adapter.change_calendar_day.ChangeCalendarDayController;
import interface_adapter.change_calendar_month.ChangeCalendarMonthController;

public class Main {
    public static void main(String[] args) {
        try {
            // Create view models first
            ChangeCalendarMonthViewModel monthViewModel = new ChangeCalendarMonthViewModel();
            ChangeCalendarDayViewModel dayViewModel = new ChangeCalendarDayViewModel();
            AddEventViewModel addEventViewModel = new AddEventViewModel();
            DeleteEventViewModel deleteEventViewModel = new DeleteEventViewModel();
            MergeCalendarsPresenter mergeCalendarsPresenter = new MergeCalendarsPresenter(monthViewModel);

            // Create calendar factory and calendars
            CalendarDataAccessObjectFactory calendarDAOFactory = new CalendarDataAccessObjectFactory();

            // Load credentials from files
            String googleCredentials = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/unical-hello-246263be14ba.json")));

            // Initialize calendars
            Calendar googleCalendar = new GoogleCalendar(
                    googleCredentials,
                    "calendar-service@unical-hello.iam.gserviceaccount.com",
                    "UniCal Google Calendar",
                    "unical.207.test2@gmail.com"
            );

            Calendar notionCalendar = new NotionCalendar(
                    "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",
                    "148fc62c766b80cda8edeb8afb359493",
                    "UniCal Notion Calendar"
            );

            // Create presenters
            ChangeCalendarMonthPresenter monthPresenter = new ChangeCalendarMonthPresenter(monthViewModel);
            ChangeCalendarDayPresenter dayPresenter = new ChangeCalendarDayPresenter(dayViewModel);
            AddEventPresenter addEventPresenter = new AddEventPresenter(addEventViewModel, dayViewModel);
            DeleteEventPresenter deleteEventPresenter = new DeleteEventPresenter(deleteEventViewModel, dayViewModel);

            // Create interactors
            ChangeCalendarMonthInteractor monthInteractor = new ChangeCalendarMonthInteractor(calendarDAOFactory, monthPresenter);
            ChangeCalendarDayInteractor dayInteractor = new ChangeCalendarDayInteractor(calendarDAOFactory, dayPresenter);
            AddEventInteractor addEventInteractor = new AddEventInteractor(calendarDAOFactory, addEventPresenter);
            DeleteEventInteractor deleteEventInteractor = new DeleteEventInteractor(calendarDAOFactory, deleteEventPresenter);
            MergeCalendarsInteractor mergeCalendarsInteractor = new MergeCalendarsInteractor(calendarDAOFactory, mergeCalendarsPresenter);

            // Create controllers
            ChangeCalendarMonthController monthController = new ChangeCalendarMonthController(monthInteractor);
            ChangeCalendarDayController dayController = new ChangeCalendarDayController(dayInteractor);
            AddEventController addEventController = new AddEventController(addEventInteractor);
            DeleteEventController deleteEventController = new DeleteEventController(deleteEventInteractor);
            EditEventViewModel editEventViewModel = new EditEventViewModel();
            EditEventPresenter editEventPresenter = new EditEventPresenter(editEventViewModel, dayViewModel);
            EditEventInteractor editEventInteractor = new EditEventInteractor(calendarDAOFactory, editEventPresenter);
            EditEventController editEventController = new EditEventController(editEventInteractor);
            EditEventView editEventView = new EditEventView(editEventViewModel, editEventController);
            MergeCalendarsController mergeCalendarsController = new MergeCalendarsController(mergeCalendarsInteractor);


            // Create list of available calendars
            ArrayList<Calendar> availableCalendars = new ArrayList<>();
            availableCalendars.add(googleCalendar);
            availableCalendars.add(notionCalendar);

            // Create views
            ChangeCalendarMonthView monthView = new ChangeCalendarMonthView(monthViewModel);
            monthView.setController(monthController);

            monthView.setMergeCalendarsController(mergeCalendarsController);
            monthView.setDayController(dayController);


            ChangeCalendarDayView dayView = new ChangeCalendarDayView(dayViewModel);
            dayView.setChangeCalendarDayController(dayController);
            dayView.setDeleteEventController(deleteEventController);
            dayView.setMergeCalendarsController(mergeCalendarsController);

            AddEventView addEventView = new AddEventView(addEventViewModel, addEventController, availableCalendars);
            dayView.setAddEventView(addEventView);

            dayView.setEditEventView(editEventView);
            dayView.setEditEventController(editEventController);

            // Set initial state for month view
            ChangeCalendarMonthState initialMonthState = new ChangeCalendarMonthState();
            initialMonthState.setGoogleCalendar(googleCalendar);
            initialMonthState.setNotionCalendar(notionCalendar);
            initialMonthState.setCurrCalendarList(availableCalendars);
            initialMonthState.setActiveCalendar(googleCalendar);
            monthViewModel.setState(initialMonthState);

            // Set up merged view by default
            initialMonthState.setMergedView(true);
            List<Calendar> calendarsToMerge = new ArrayList<>();
            if (googleCalendar != null) {
                calendarsToMerge.add(googleCalendar);
            }
            if (notionCalendar != null) {
                calendarsToMerge.add(notionCalendar);
            }

            // Get current date for initial view
            LocalDate now = LocalDate.now();
            String initialDate = String.format("%d-%02d-01", now.getYear(), now.getMonthValue());

            // Execute merge calendars
            mergeCalendarsController.execute(calendarsToMerge, initialDate);


            monthView.setMergeCalendarsController(mergeCalendarsController);

            // Create and setup main frame
            JFrame frame = new JFrame("UniCal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);

            // Setup card layout for view switching
            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            mainPanel.add(monthView, "month");
            mainPanel.add(dayView, "day");

            // Setup day view opener
            monthView.setDayViewOpener(date -> {
                System.out.println("Opening day view for: " + date);
                dayViewModel.setCurrentDate(date);

                ArrayList<Calendar> activeCalendarList = new ArrayList<>();
                ChangeCalendarMonthState monthState = monthViewModel.getState();

                if (monthState.isMergedView()) {
                    if (monthState.getGoogleCalendar() != null) {
                        activeCalendarList.add(monthState.getGoogleCalendar());
                    }
                    if (monthState.getNotionCalendar() != null) {
                        activeCalendarList.add(monthState.getNotionCalendar());
                    }
                } else if (monthState.getActiveCalendar() != null) {
                    activeCalendarList.add(monthState.getActiveCalendar());
                }

                dayController.execute(activeCalendarList, date.toString());
                cardLayout.show(mainPanel, "day");
            });


            frame.add(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error initializing calendars: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
