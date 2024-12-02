package view;

import entity.Calendar;
import entity.Event;
import interface_adapter.change_calendar_day.ChangeCalendarDayController;
import interface_adapter.change_calendar_month.ChangeCalendarMonthController;
import interface_adapter.change_calendar_month.ChangeCalendarMonthState;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import interface_adapter.merge_calendars.MergeCalendarsController;
import java.util.ArrayList;
import java.time.Month;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.InputEvent;

public class ChangeCalendarMonthView extends JPanel implements ActionListener, PropertyChangeListener {
    private int selectedDay = 1;
    private int selectedRow = 0;
    private int selectedCol = 0;

    public final String viewName = "calendar_month";
    private final ChangeCalendarMonthViewModel changeCalendarMonthViewModel;
    private ChangeCalendarMonthController controller;
    private ChangeCalendarDayController dayController;

    // UI Components
    private JPanel calendarPanel = new JPanel(new GridLayout(0, 7, 2, 2));
    private final JComboBox<Month> monthSelector = new JComboBox<>(Month.values());
    private final JComboBox<Integer> yearSelector = new JComboBox<>();
    private final JButton googleButton = new JButton(ChangeCalendarMonthViewModel.GOOGLE_CALENDAR_BUTTON_LABEL);
    private final JButton notionButton = new JButton(ChangeCalendarMonthViewModel.NOTION_CALENDAR_BUTTON_LABEL);
    private final JButton outlookButton = new JButton(ChangeCalendarMonthViewModel.OUTLOOK_CALENDAR_BUTTON_LABEL);
    private final JLabel errorLabel;
    private final JButton mergeButton = new JButton("Merge Calendars");
    private MergeCalendarsController mergeCalendarsController;
    private static final Color ACTIVE_CALENDAR_COLOR = new Color(200, 200, 255);

    private DayViewOpener dayViewOpener;
    private static final int CELL_HEIGHT = 100;
    private static final int MAX_PREVIEW_EVENTS = 3;

    private final Color SELECTED_BORDER_COLOR = new Color(0, 120, 215);
    private final Color SELECTED_BACKGROUND_COLOR = new Color(229, 243, 255);
    private JPanel[][] dayCells; // Store references to day cells
    private int currentMonthLength;
    private int firstDayOffset;



    public interface DayViewOpener {
        void openDayView(LocalDate date);
    }

    public void setDayViewOpener(DayViewOpener opener) {
        this.dayViewOpener = opener;
    }

    public ChangeCalendarMonthView(ChangeCalendarMonthViewModel viewModel) {
        this.changeCalendarMonthViewModel = viewModel;
        this.changeCalendarMonthViewModel.addPropertyChangeListener(this);

        // Layout setup
        setLayout(new BorderLayout());

        // Create error label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create calendar selection panel
        JPanel sidePanel = createSidePanel();
        add(sidePanel, BorderLayout.WEST);

        // Create month/year selection panel
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Create calendar panel
        calendarPanel = new JPanel(new GridLayout(0, 7, 2, 2));
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create a wrapper panel that includes error label and calendar
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(errorLabel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(calendarPanel), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        setupKeyboardNavigation();
        setFocusable(true);
        requestFocusInWindow();

        googleButton.setToolTipText("Switch to Google Calendar (Ctrl+G)");
        notionButton.setToolTipText("Switch to Notion Calendar (Ctrl+N)");
        mergeButton.setToolTipText("Merge Calendars (Ctrl+T)");


        updateCalendarView();
    }

    private void setupKeyboardNavigation() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Arrow key navigation
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");

        // Calendar switching shortcuts
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), "googleCalendar");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "notionCalendar");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "mergeCalendars");

        // Enter to open day view
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "openDay");
        // Escape to return to month view (when in day view)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "returnToMonth");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK), "focusMonth");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "focusYear");

        // Register actions
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(-1, 0);
            }
        });

        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(1, 0);
            }
        });

        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(0, -1);
            }
        });

        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(0, 1);
            }
        });

        actionMap.put("googleCalendar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCalendarClick(changeCalendarMonthViewModel.getState().getGoogleCalendar(), "Google");
            }
        });

        actionMap.put("notionCalendar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCalendarClick(changeCalendarMonthViewModel.getState().getNotionCalendar(), "Notion");
            }
        });

        actionMap.put("mergeCalendars", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMergeCalendars();
            }
        });

        actionMap.put("openDay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSelectedDay();
            }
        });

        actionMap.put("focusMonth", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                monthSelector.requestFocusInWindow();
                monthSelector.showPopup();
            }
        });

        actionMap.put("focusYear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yearSelector.requestFocusInWindow();
                yearSelector.showPopup();
            }
        });
    }


    private void moveSelection(int deltaCol, int deltaRow) {
        int newRow = selectedRow + deltaRow;
        int newCol = selectedCol + deltaCol;

        // Handle wrapping and invalid positions
        if (newCol < 0) {
            newCol = 6;
            newRow--;
        } else if (newCol > 6) {
            newCol = 0;
            newRow++;
        }

        if (newRow < 0) newRow = 5;
        if (newRow > 5) newRow = 0;

        // Check if the new position contains a valid day
        int dayAtPosition = calculateDayAtPosition(newRow, newCol);
        if (dayAtPosition > 0 && dayAtPosition <= currentMonthLength) {
            selectedRow = newRow;
            selectedCol = newCol;
            selectedDay = dayAtPosition;
            updateSelectedDayVisuals();
        }
    }
    private int calculateDayAtPosition(int row, int col) {
        return row * 7 + col - firstDayOffset + 1;
    }

    private void updateSelectedDayVisuals() {
        // Reset all cells to default appearance
        for (JPanel[] row : dayCells) {
            for (JPanel cell : row) {
                if (cell != null) {
                    cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    cell.setBackground(Color.WHITE);
                }
            }
        }

        // Highlight selected cell
        JPanel selectedCell = dayCells[selectedRow][selectedCol];
        if (selectedCell != null) {
            selectedCell.setBorder(BorderFactory.createLineBorder(SELECTED_BORDER_COLOR, 2));
            selectedCell.setBackground(SELECTED_BACKGROUND_COLOR);
        }
    }

    private void updateSelectedDay() {
        Month selectedMonth = (Month) monthSelector.getSelectedItem();
        int selectedYear = (Integer) yearSelector.getSelectedItem();
        LocalDate firstOfMonth = LocalDate.of(selectedYear, selectedMonth, 1);
        int offset = firstOfMonth.getDayOfWeek().getValue() % 7;

        selectedDay = selectedRow * 7 + selectedCol - offset + 1;

        // Make sure the selected day is valid for the current month
        if (selectedDay > 0 && selectedDay <= selectedMonth.length(Year.isLeap(selectedYear))) {
            repaint();
        }
    }

    private void openSelectedDay() {
        if (selectedDay > 0) {
            Month selectedMonth = (Month) monthSelector.getSelectedItem();
            int selectedYear = (Integer) yearSelector.getSelectedItem();
            LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonth, selectedDay);
            handleDaySelection(selectedDate);
        }
    }

    public void setMergeCalendarsController(MergeCalendarsController controller) {
        this.mergeCalendarsController = controller;
    }

    private void updateCalendarView() {
        calendarPanel.removeAll();
        Map<LocalDate, List<Event>> eventsByDate = getEventsByDate();

        // Add day headers
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : dayNames) {
            JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dayLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            calendarPanel.add(dayLabel);
        }

        Month selectedMonth = (Month) monthSelector.getSelectedItem();
        int selectedYear = (Integer) yearSelector.getSelectedItem();
        LocalDate firstDayOfMonth = LocalDate.of(selectedYear, selectedMonth, 1);
        currentMonthLength = selectedMonth.length(Year.isLeap(selectedYear));
        firstDayOffset = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        // Initialize dayCells array
        dayCells = new JPanel[6][7];

        // Add empty cells before first day
        for (int i = 0; i < firstDayOffset; i++) {
            JPanel emptyCell = new JPanel();
            emptyCell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            dayCells[0][i] = emptyCell;
            calendarPanel.add(emptyCell);
        }

        // Add day cells
        int currentRow = 0;
        int currentCol = firstDayOffset;

        for (int day = 1; day <= currentMonthLength; day++) {
            if (currentCol == 7) {
                currentCol = 0;
                currentRow++;
            }

            LocalDate cellDate = LocalDate.of(selectedYear, selectedMonth, day);
            JPanel dayCell = createDayCell(cellDate, eventsByDate.get(cellDate), day);
            dayCells[currentRow][currentCol] = dayCell;
            calendarPanel.add(dayCell);
            currentCol++;
        }

        // Fill remaining cells
        while (currentRow < 6) {
            while (currentCol < 7) {
                JPanel emptyCell = new JPanel();
                emptyCell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                dayCells[currentRow][currentCol] = emptyCell;
                calendarPanel.add(emptyCell);
                currentCol++;
            }
            currentRow++;
            currentCol = 0;
        }

        updateSelectedDayVisuals();
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private JPanel createDayCell(LocalDate date, List<Event> events, int dayNumber) {
        JPanel cellPanel = new JPanel();
        cellPanel.setLayout(new BoxLayout(cellPanel, BoxLayout.Y_AXIS));
        cellPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        cellPanel.setBackground(Color.WHITE);
        cellPanel.setPreferredSize(new Dimension(0, CELL_HEIGHT));

        // Add day number
        JLabel dayLabel = new JLabel(String.valueOf(dayNumber));
        dayLabel.setFont(new Font("Arial", date.equals(LocalDate.now()) ? Font.BOLD : Font.PLAIN, 14));
        JPanel dayHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        dayHeader.setOpaque(false);
        dayHeader.add(dayLabel);
        cellPanel.add(dayHeader);

        // Add events if they exist
        if (events != null && !events.isEmpty()) {
            cellPanel.setBackground(new Color(255, 245, 245));

            int previewCount = Math.min(events.size(), MAX_PREVIEW_EVENTS);
            for (int i = 0; i < previewCount; i++) {
                Event event = events.get(i);
                JLabel eventLabel = new JLabel("• " + truncateText(event.getEventName(), 20));
                eventLabel.setFont(new Font("Arial", Font.PLAIN, 11));
                eventLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                cellPanel.add(eventLabel);
            }

            if (events.size() > MAX_PREVIEW_EVENTS) {
                JLabel moreLabel = new JLabel("+" + (events.size() - MAX_PREVIEW_EVENTS) + " more");
                moreLabel.setFont(new Font("Arial", Font.ITALIC, 10));
                moreLabel.setForeground(Color.GRAY);
                moreLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
                cellPanel.add(moreLabel);
            }
        }

        // Make the cell clickable
        cellPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleDaySelection(date);
            }
        });

        return cellPanel;
    }
    // Add this new method to handle day selection consistently between mouse and keyboard
    private void handleDaySelection(LocalDate date) {
        if (dayViewOpener != null) {
            ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
            List<Calendar> calendarsToUse = new ArrayList<>();

            if (currentState.isMergedView()) {
                if (currentState.getGoogleCalendar() != null) {
                    calendarsToUse.add(currentState.getGoogleCalendar());
                }
                if (currentState.getNotionCalendar() != null) {
                    calendarsToUse.add(currentState.getNotionCalendar());
                }
            } else {
                Calendar activeCalendar = currentState.getActiveCalendar();
                if (activeCalendar != null) {
                    calendarsToUse.add(activeCalendar);
                }
            }

            dayViewOpener.openDayView(date);
            if (dayController != null && !calendarsToUse.isEmpty()) {
                dayController.execute(calendarsToUse, date.toString());
            }
        }
    }

    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    private Map<LocalDate, List<Event>> getEventsByDate() {
        Map<LocalDate, List<Event>> eventMap = new HashMap<>();
        ChangeCalendarMonthState state = changeCalendarMonthViewModel.getState();

        if (state != null && state.getCurrEvents() != null) {
            for (Event event : state.getCurrEvents()) {
                eventMap.computeIfAbsent(event.getDate(), k -> new ArrayList<>()).add(event);
            }
        }
        return eventMap;
    }

    private void updateButtonColors(Calendar activeCalendar) {
        googleButton.setBackground(UIManager.getColor("Button.background"));
        notionButton.setBackground(UIManager.getColor("Button.background"));
        mergeButton.setBackground(UIManager.getColor("Button.background"));

        if (activeCalendar != null) {
            switch (activeCalendar.getCalendarApiName()) {
                case "GoogleCalendar":
                    googleButton.setBackground(ACTIVE_CALENDAR_COLOR);
                    break;
                case "NotionCalendar":
                    notionButton.setBackground(ACTIVE_CALENDAR_COLOR);
                    break;
            }
        }
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(4, 1, 10, 10));  // Changed from 6 to 4 rows
        sidePanel.setPreferredSize(new Dimension(200, 0));
        sidePanel.setBackground(new Color(68, 168, 167));

        googleButton.addActionListener(this);
        notionButton.addActionListener(this);
        mergeButton.addActionListener(this);

        sidePanel.add(mergeButton);  // Moved to top
        sidePanel.add(googleButton);
        sidePanel.add(notionButton);

        return sidePanel;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout());

        // Configure the already initialized selectors
        for (int year = 2020; year <= 2030; year++) {
            yearSelector.addItem(year);
        }

        // Set current month and year
        LocalDate now = LocalDate.now();
        monthSelector.setSelectedItem(now.getMonth());
        yearSelector.setSelectedItem(now.getYear());

        monthSelector.setToolTipText("Select Month (Ctrl+M)");
        yearSelector.setToolTipText("Select Year (Ctrl+Y)");

        monthSelector.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // Return focus to main panel after selection
                ChangeCalendarMonthView.this.requestFocusInWindow();
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        monthSelector.addActionListener(this);
        yearSelector.addActionListener(this);

        topPanel.add(new JLabel(ChangeCalendarMonthViewModel.MONTH_LABEL));
        topPanel.add(monthSelector);
        topPanel.add(new JLabel(ChangeCalendarMonthViewModel.YEAR_LABEL));
        topPanel.add(yearSelector);

        return topPanel;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == googleButton) {
            handleCalendarClick(changeCalendarMonthViewModel.getState().getGoogleCalendar(), "Google");
        } else if (evt.getSource() == notionButton) {
            handleCalendarClick(changeCalendarMonthViewModel.getState().getNotionCalendar(), "Notion");
        } else if (evt.getSource() == mergeButton) {
            handleMergeCalendars();
        } else if (evt.getSource() == monthSelector || evt.getSource() == yearSelector) {
            handleMonthYearChange();
        }
    }

    private void handleMergeCalendars() {
        ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
        List<Calendar> calendars = new ArrayList<>();

        if (currentState.getGoogleCalendar() != null) {
            calendars.add(currentState.getGoogleCalendar());
        }
        if (currentState.getNotionCalendar() != null) {
            calendars.add(currentState.getNotionCalendar());
        }

        if (!calendars.isEmpty()) {
            Month selectedMonth = (Month) monthSelector.getSelectedItem();
            Integer selectedYear = (Integer) yearSelector.getSelectedItem();
            String date = String.format("%d-%02d-01", selectedYear, selectedMonth.getValue());

            mergeCalendarsController.execute(calendars, date);

            updateButtonColors(null);
            mergeButton.setBackground(ACTIVE_CALENDAR_COLOR);
        }
    }

    private void handleMonthYearChange() {
        ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
        if (currentState != null && currentState.getActiveCalendar() != null) {
            Month selectedMonth = (Month) monthSelector.getSelectedItem();
            Integer selectedYear = (Integer) yearSelector.getSelectedItem();
            String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());

            List<Calendar> calList = new ArrayList<>();
            calList.add(currentState.getActiveCalendar());

            currentState.setCurrMonth(monthName);
            currentState.setCurrYear(selectedYear);
            controller.execute(calList, monthName, selectedYear);

            this.requestFocusInWindow();
        }
    }

    private void handleCalendarClick(Calendar calendar, String calendarType) {
        if (calendar != null) {
            ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
            currentState.setActiveCalendar(calendar);
            currentState.setMergedView(false);  // Reset merged view when clicking individual calendar

            List<Calendar> calList = new ArrayList<>();
            calList.add(calendar);

            Month selectedMonth = (Month) monthSelector.getSelectedItem();
            Integer selectedYear = (Integer) yearSelector.getSelectedItem();
            String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());

            currentState.setCurrMonth(monthName);
            currentState.setCurrYear(selectedYear);

            updateButtonColors(calendar);
            controller.execute(calList, monthName, selectedYear);
        } else {
            errorLabel.setText(calendarType + " Calendar not initialized");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            ChangeCalendarMonthState state = (ChangeCalendarMonthState) evt.getNewValue();
            if (state != null) {
                updateButtonColors(state.getActiveCalendar());
                updateCalendarView();
                errorLabel.setText(state.getError());
            }
        }
    }

    public void setController(ChangeCalendarMonthController controller) {
        this.controller = controller;
    }

    public void setDayController(ChangeCalendarDayController controller) {
        this.dayController = controller;
    }


    public String getViewName() {
        return viewName;
    }
}
