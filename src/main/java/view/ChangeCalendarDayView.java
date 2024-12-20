package view;

import entity.Calendar;
import entity.Event;
import interface_adapter.change_calendar_day.ChangeCalendarDayController;
import interface_adapter.change_calendar_day.ChangeCalendarDayState;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import interface_adapter.delete_event.DeleteEventController;
import interface_adapter.edit_event.EditEventController;
import interface_adapter.merge_calendars.MergeCalendarsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.time.format.DateTimeFormatter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import java.awt.event.InputEvent;

public class ChangeCalendarDayView extends JPanel implements ActionListener, PropertyChangeListener {
  private final String viewName = "calendar_day";
  private final ChangeCalendarDayViewModel dayViewModel;
  private final DefaultListModel<Event> eventListModel;
  private final JList<Event> eventList;
  private final JLabel dateLabel;
  private final JLabel errorLabel;
  private final JButton addButton;
  private final JButton deleteButton;
  private final JButton backButton;

  private static final KeyStroke ESCAPE_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
  private static final KeyStroke ADD_EVENT_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK);
  private static final KeyStroke DELETE_EVENT_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
  private static final KeyStroke EDIT_EVENT_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);

  private JButton editButton;
  private EditEventView editEventView;
  private EditEventController editEventController;

  private AddEventView addEventView;
  private DeleteEventController deleteEventController;
  private ChangeCalendarDayController changeCalendarDayController;
  private MergeCalendarsController mergeCalendarsController;

  public void setMergeCalendarsController(MergeCalendarsController controller) {
    this.mergeCalendarsController = controller;
  }

  private void setupKeyboardNavigation() {
    setFocusable(true);

    InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = getActionMap();

    // Add keyboard shortcuts
    inputMap.put(ESCAPE_KEY, "back");
    inputMap.put(ADD_EVENT_KEY, "addEvent");
    inputMap.put(DELETE_EVENT_KEY, "deleteEvent");
    inputMap.put(EDIT_EVENT_KEY, "editEvent");

    // Register actions
    actionMap.put("back", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        handleBackToMonth();
      }
    });

    actionMap.put("addEvent", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showAddEventDialog();
      }
    });

    actionMap.put("deleteEvent", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        handleDeleteEvent();
      }
    });

    actionMap.put("editEvent", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        handleEditEvent();
      }
    });

    // Enable keyboard navigation in the event list
    eventList.setFocusable(true);
    eventList.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        int selectedIndex = eventList.getSelectedIndex();
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          handleEditEvent();
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
          handleDeleteEvent();
        }
      }
    });
  }

  public ChangeCalendarDayView(ChangeCalendarDayViewModel viewModel) {
    this.dayViewModel = viewModel;
    this.dayViewModel.addPropertyChangeListener(this);

    // Initialize components
    eventListModel = new DefaultListModel<>();
    eventList = new JList<>(eventListModel);
    dateLabel = new JLabel("", SwingConstants.CENTER);
    errorLabel = new JLabel();
    addButton = new JButton(ChangeCalendarDayViewModel.ADD_EVENT_BUTTON_LABEL);
    deleteButton = new JButton(ChangeCalendarDayViewModel.DELETE_EVENT_BUTTON_LABEL);
    backButton = new JButton(ChangeCalendarDayViewModel.BACK_TO_MONTH_BUTTON_LABEL);

    // Setup UI
    setupUI();
    setupListeners();

  }

  private void setupUI() {
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Header panel with date
    JPanel headerPanel = new JPanel(new BorderLayout());
    dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
    errorLabel.setForeground(Color.RED);
    headerPanel.add(dateLabel, BorderLayout.CENTER);
    headerPanel.add(errorLabel, BorderLayout.SOUTH);
    add(headerPanel, BorderLayout.NORTH);

    // Events list with custom renderer
    eventList.setCellRenderer(new EventListCellRenderer());
    JScrollPane scrollPane = new JScrollPane(eventList);
    add(scrollPane, BorderLayout.CENTER);

    // Buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    buttonPanel.add(addButton);
    editButton = new JButton("Edit Event");
    buttonPanel.add(editButton, 1);
    buttonPanel.add(deleteButton);
    buttonPanel.add(backButton);
    add(buttonPanel, BorderLayout.SOUTH);

    addButton.setToolTipText("Add Event (Ctrl+A)");
    deleteButton.setToolTipText("Delete Event (Ctrl+D)");
    editButton.setToolTipText("Edit Event (Ctrl+E)");
    backButton.setToolTipText("Back to Month View (Esc)");

    setupKeyboardNavigation();

  }

  private void setupListeners() {
    addButton.addActionListener(this);
    deleteButton.addActionListener(this);
    backButton.addActionListener(this);
    editButton.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() == addButton) {
      showAddEventDialog();
    } else if (evt.getSource() == deleteButton) {
      handleDeleteEvent();
    } else if (evt.getSource() == backButton) {
      handleBackToMonth();
    }
    else if (evt.getSource() == editButton) {
      handleEditEvent();
    }
  }

  private void showAddEventDialog() {
    if (addEventView != null) {
      LocalDate currentDate = dayViewModel.getState().getDate();
      System.out.println("Opening add event dialog for date: " + currentDate);

      addEventView.setSelectedDate(currentDate);

      JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
              "Add Event", true);

      Container oldParent = addEventView.getParent();
      if (oldParent != null) {
        oldParent.remove(addEventView);
      }

      dialog.setContentPane(addEventView);
      dialog.pack();
      dialog.setLocationRelativeTo(this);
      addEventView.setVisible(true);
      dialog.setVisible(true);
    }
  }

  private void handleDeleteEvent() {
    Event selectedEvent = eventList.getSelectedValue();
    if (selectedEvent != null) {
      int confirm = JOptionPane.showConfirmDialog(
              this,
              ChangeCalendarDayViewModel.DELETE_CONFIRM_MESSAGE,
              ChangeCalendarDayViewModel.DELETE_EVENT_BUTTON_LABEL,
              JOptionPane.YES_NO_OPTION
      );

      if (confirm == JOptionPane.YES_OPTION && deleteEventController != null) {
        deleteEventController.execute(selectedEvent);
      }
    } else {
      errorLabel.setText(ChangeCalendarDayViewModel.ERROR_NO_SELECTION);
    }
  }

  private void handleBackToMonth() {
    Container parent = getParent();
    if (parent instanceof JPanel) {
      CardLayout layout = (CardLayout) parent.getLayout();
      layout.show(parent, "month");

      // Update state when returning to month view
      if (dayViewModel.getState() != null && !dayViewModel.getState().getCalendarList().isEmpty()) {
        List<Calendar> calendars = dayViewModel.getState().getCalendarList();
        if (calendars.size() > 1) {
          // If we have multiple calendars, we're in merged view
          Month selectedMonth = dayViewModel.getCurrentDate().getMonth();
          int selectedYear = dayViewModel.getCurrentDate().getYear();
          String date = String.format("%d-%02d-01", selectedYear, selectedMonth.getValue());

          if (mergeCalendarsController != null) {
            mergeCalendarsController.execute(calendars, date);
          }
        }
      }
    }
  }

  private void handleEditEvent() {
    Event selectedEvent = eventList.getSelectedValue();
    if (selectedEvent != null) {
      editEventView.setEvent(selectedEvent);

      JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
              "Edit Event", true);
      Container oldParent = editEventView.getParent();
      if (oldParent != null) {
        oldParent.remove(editEventView);
      }

      dialog.setContentPane(editEventView);
      dialog.pack();
      dialog.setLocationRelativeTo(this);
      editEventView.setVisible(true);
      dialog.setVisible(true);
    } else {
      errorLabel.setText(ChangeCalendarDayViewModel.ERROR_NO_SELECTION);
    }
  }

//  private void updateDateLabel(LocalDate date) {
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
//    String formattedDate = date.format(formatter);
//    dateLabel.setText("Events for " + formattedDate);
//  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("state")) {
      ChangeCalendarDayState state = (ChangeCalendarDayState) evt.getNewValue();
      if (state != null) {
        //updateDateLabel(state.getDate());
        updateEventsList(state.getEventList());
        errorLabel.setText(state.getError());

        // Select first event if exists
        if (eventListModel.getSize() > 0) {
          eventList.setSelectedIndex(0);
          eventList.requestFocusInWindow();
        }

        if (addEventView != null) {
          addEventView.setSelectedDate(state.getDate());
        }
      }
    }
  }

  private void updateEventsList(List<Event> events) {
    eventListModel.clear();
    for (Event event : events) {
      eventListModel.addElement(event);
    }
  }

  private static class EventListCellRenderer extends DefaultListCellRenderer {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

      Event event = (Event) value;
      String displayText = String.format("%s - %s to %s (%s)",
              event.getEventName(),
              event.getStartTime().format(TIME_FORMATTER),
              event.getEndTime().format(TIME_FORMATTER),
              event.getCalendarApi().getCalendarName());

      Component component = super.getListCellRendererComponent(
              list, displayText, index, isSelected, cellHasFocus);

      if (!isSelected) {
        // Color-code events based on their calendar source
        if (event.getCalendarApi().getCalendarApiName().equals("GoogleCalendar")) {
          setForeground(new Color(0, 100, 0));  // Dark green for Google
        } else if (event.getCalendarApi().getCalendarApiName().equals("NotionCalendar")) {
          setForeground(new Color(0, 0, 139));  // Dark blue for Notion
        }
      }

      // Add keyboard focus visual indication
      if (cellHasFocus) {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
      }

      return component;
    }
  }

  // Setters for dependent views and controllers
  public void setAddEventView(AddEventView view) {
    this.addEventView = view;
  }

  public void setDeleteEventController(DeleteEventController controller) {
    this.deleteEventController = controller;
  }

  public void setChangeCalendarDayController(ChangeCalendarDayController controller) {
    this.changeCalendarDayController = controller;
  }

  public String getViewName() {
    return viewName;
  }

  public void setEditEventView(EditEventView view) {
    this.editEventView = view;
  }

  public void setEditEventController(EditEventController controller) {
    this.editEventController = controller;
  }
}
