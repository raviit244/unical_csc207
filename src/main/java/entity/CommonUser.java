package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * The representation of a common user in our program, an implementation of the User interface.
 */
public class CommonUser implements User {
    private final String name;
    private final String password;
    private List<Calendar> calendars;

    public CommonUser(String name, String password) {
        if (name == null || password == null) {
            throw new NullPointerException("Username and password cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        this.name = name.trim();
        this.password = password;
        this.calendars = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void addCalendar(Calendar calendar) {
        if (calendar == null) {
            throw new NullPointerException("Calendar cannot be null");
        }
        calendars.add(calendar);
    }
}
