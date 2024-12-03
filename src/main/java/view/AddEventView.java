package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import entity.Calendar;
import interface_adapter.add_event.AddEventController;
import interface_adapter.add_event.AddEventState;
import interface_adapter.add_event.AddEventViewModel;

/**
 * The view for adding an event.
 */
public class AddEventView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "add event";
    private final AddEventViewModel addEventViewModel;
    private final AddEventController addEventController;

    // UI Components
    private final JTextField eventNameField = new JTextField(15);
    private final JTextField dateField = new JTextField(15);
    private final JTextField startTimeField = new JTextField(15);
    private final JTextField endTimeField = new JTextField(15);
    private final JComboBox<Calendar> calendarComboBox;
    private final JButton addButton;
    private final JButton cancelButton;
    private final JLabel eventNameErrorField = new JLabel();
    private final JLabel dateErrorField = new JLabel();
    private final JLabel timeErrorField = new JLabel();
    private final JLabel calendarErrorField = new JLabel();

    /**
     * DocumentChangeListener for the Add Event view.
     */
    private static class DocumentChangeListener implements DocumentListener {

        private final Consumer<String> updateAction;

        DocumentChangeListener(Consumer<String> updateAction) {
            this.updateAction = updateAction;
        }

        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            updateField(documentEvent);
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            updateField(documentEvent);
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            updateField(documentEvent);
        }

        private void updateField(DocumentEvent documentEvent) {
            try {
                final String text = documentEvent.getDocument().getText(0, documentEvent.getDocument().getLength());
                updateAction.accept(text);
            }
            catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Constructor for the Add Event view.
     *
     * @param viewModel          the view model
     * @param controller         the controller
     * @param availableCalendars the list of available calendars
     */
    public AddEventView(AddEventViewModel viewModel,
                        AddEventController controller,
                        List<Calendar> availableCalendars) {
        this.addEventViewModel = viewModel;
        this.addEventController = controller;
        this.addEventViewModel.addPropertyChangeListener(this);

        // Initialize UI components
        calendarComboBox = new JComboBox<>(availableCalendars.toArray(new Calendar[0]));
        addButton = new JButton(AddEventViewModel.ADD_BUTTON_LABEL);
        cancelButton = new JButton(AddEventViewModel.CANCEL_BUTTON_LABEL);

        addButton.setToolTipText("Add Event (Ctrl+A)");

        // Set up layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title
        final JLabel title = new JLabel(AddEventViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        // Event name panel
        final LabelTextPanel eventNamePanel = new LabelTextPanel(
                new JLabel(AddEventViewModel.EVENT_NAME_LABEL), eventNameField);
        add(eventNamePanel);
        add(eventNameErrorField);
        eventNameErrorField.setForeground(Color.RED);

        // Date panel
        final LabelTextPanel datePanel = new LabelTextPanel(
                new JLabel(AddEventViewModel.DATE_LABEL), dateField);
        add(datePanel);
        add(dateErrorField);
        dateErrorField.setForeground(Color.RED);

        // Start Time panel
        final LabelTextPanel startTimePanel = new LabelTextPanel(
                new JLabel("Start Time (HH:mm)"), startTimeField);
        add(startTimePanel);

        // End Time panel
        final LabelTextPanel endTimePanel = new LabelTextPanel(
                new JLabel("End Time (HH:mm)"), endTimeField);
        add(endTimePanel);

        add(timeErrorField);
        timeErrorField.setForeground(Color.RED);

        // Calendar selection panel
        final JPanel calendarPanel = new JPanel();
        calendarPanel.add(new JLabel(AddEventViewModel.CALENDAR_LABEL));
        calendarPanel.add(calendarComboBox);
        add(calendarPanel);
        add(calendarErrorField);
        calendarErrorField.setForeground(Color.RED);

        // Buttons panel
        final JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(cancelButton);
        add(buttons);

        final InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        final ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "add");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");

        actionMap.put("add", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                final Calendar selectedCalendar = (Calendar) calendarComboBox.getSelectedItem();
                if (selectedCalendar != null) {
                    try {
                        final String currentDate = dateField.getText();
                        final String startTime = startTimeField.getText();
                        final String endTime = endTimeField.getText();

                        addEventController.execute(
                                eventNameField.getText(),
                                currentDate,
                                startTime,
                                endTime,
                                selectedCalendar
                        );
                    }
                    catch (DateTimeParseException dateTimeParseException) {
                        timeErrorField.setText("Invalid time format. Use HH:mm (24-hour format)");
                    }
                }
                else {
                    calendarErrorField.setText("Please select a calendar");
                }
            }
        });

        actionMap.put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
                final Window window = SwingUtilities.getWindowAncestor(AddEventView.this);
                if (window instanceof JDialog) {
                    window.dispose();
                }
            }
        });

        // Action listeners
        addButton.addActionListener(this);
        cancelButton.addActionListener(event -> {
            resetFields();
            final Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                window.dispose();
            }
        });

        // Add document listeners
        addDocumentListeners();

        // Initialize the date field with the current date
        final LocalDate currentDate = LocalDate.now();
        setSelectedDate(currentDate);
    }

    private void addDocumentListeners() {
        eventNameField.getDocument().addDocumentListener(
                new DocumentChangeListener(state -> {
                    final AddEventState currentState = addEventViewModel.getState();
                    currentState.setEventName(state);
                    addEventViewModel.setState(currentState);
                })
        );

        dateField.getDocument().addDocumentListener(
                new DocumentChangeListener(state -> {
                    final AddEventState currentState = addEventViewModel.getState();
                    currentState.setDate(state);
                    addEventViewModel.setState(currentState);
                })
        );

        startTimeField.getDocument().addDocumentListener(
                new DocumentChangeListener(state -> {
                    final AddEventState currentState = addEventViewModel.getState();
                    currentState.setStartTime(state);
                    addEventViewModel.setState(currentState);
                })
        );

        endTimeField.getDocument().addDocumentListener(
                new DocumentChangeListener(state -> {
                    final AddEventState currentState = addEventViewModel.getState();
                    currentState.setEndTime(state);
                    addEventViewModel.setState(currentState);
                })
        );
    }

    private void resetFields() {
        eventNameField.setText("");
        dateField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");
        calendarComboBox.setSelectedIndex(0);
        eventNameErrorField.setText("");
        dateErrorField.setText("");
        timeErrorField.setText("");
        calendarErrorField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            final Calendar selectedCalendar = (Calendar) calendarComboBox.getSelectedItem();
            if (selectedCalendar != null) {
                try {
                    final String currentDate = dateField.getText();
                    final String startTime = startTimeField.getText();
                    final String endTime = endTimeField.getText();

                    addEventController.execute(
                            eventNameField.getText(),
                            currentDate,
                            startTime,
                            endTime,
                            selectedCalendar
                    );
                }
                catch (DateTimeParseException dateTimeParseException) {
                    timeErrorField.setText("Invalid time format. Use HH:mm (24-hour format)");
                }
            }
            else {
                calendarErrorField.setText("Please select a calendar");
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final AddEventState state = (AddEventState) evt.getNewValue();
            if (!state.isUseCaseFailed()) {
                resetFields();
                final Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof JDialog) {
                    window.dispose();
                }
            }
            else {
                eventNameErrorField.setText(state.getEventNameError());
                dateErrorField.setText(state.getDateError());
                timeErrorField.setText(state.getTimeError());
                calendarErrorField.setText(state.getCalendarError());
            }
        }
    }

    /**
     * Sets the selected date.
     *
     * @param date the date to which the selected date will be set.
     */
    public void setSelectedDate(LocalDate date) {
        dateField.setText(date.toString());

        AddEventState currentState = addEventViewModel.getState();
        currentState = new AddEventState(currentState);
        currentState.setDate(date.toString());
        addEventViewModel.setState(currentState);
    }

    public String getViewName() {
        return viewName;
    }
}
