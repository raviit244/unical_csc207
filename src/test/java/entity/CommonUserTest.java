package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommonUserTest {
    private CommonUser user;
    private final String testUsername = "testUser";
    private final String testPassword = "testPass123";

    @BeforeEach
    void setUp() {
        user = new CommonUser(testUsername, testPassword);
    }

    @Test
    void constructor_CreatesUserWithCorrectAttributes() {
        assertNotNull(user);
        assertEquals(testUsername, user.getName());
        assertEquals(testPassword, user.getPassword());
    }

    @Test
    void constructor_WithNullUsername_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new CommonUser(null, testPassword)
        );
        assertEquals("Username and password cannot be null", exception.getMessage());
    }

    @Test
    void constructor_WithNullPassword_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new CommonUser(testUsername, null)
        );
        assertEquals("Username and password cannot be null", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyUsername_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new CommonUser("", testPassword)
        );
        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyPassword_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new CommonUser(testUsername, "")
        );
        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithBlankUsername_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new CommonUser("   ", testPassword)
        );
        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_WithBlankPassword_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new CommonUser(testUsername, "   ")
        );
        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    void constructor_TrimsUsername() {
        CommonUser userWithSpaces = new CommonUser("  testUser  ", testPassword);
        assertEquals("testUser", userWithSpaces.getName());
    }

    @Test
    void getName_ReturnsCorrectUsername() {
        assertEquals(testUsername, user.getName());
    }

    @Test
    void getPassword_ReturnsCorrectPassword() {
        assertEquals(testPassword, user.getPassword());
    }

    @Test
    void addCalendar_AddsCalendarSuccessfully() {
        Calendar testCalendar = new Calendar() {
            @Override
            public String getCalendarApiName() {
                return "TestCalendar";
            }

            @Override
            public String getCalendarName() {
                return "Test Calendar";
            }
        };

        assertDoesNotThrow(() -> user.addCalendar(testCalendar));
    }

    @Test
    void addCalendar_WithNullCalendar_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                user.addCalendar(null)
        );
        assertEquals("Calendar cannot be null", exception.getMessage());
    }
}