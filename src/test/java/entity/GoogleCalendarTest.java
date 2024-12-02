package entity;

import com.google.api.client.http.HttpRequestInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GoogleCalendarTest {
    private GoogleCalendar calendar;
    private String validCredentials;
    private String validAccountName;
    private String validCalendarName;
    private String validCalendarId;

    @BeforeEach
    void setUp() {
        // Sample valid JSON credentials
        validCredentials = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"unical-442813\",\n" +
                "  \"private_key_id\": \"e8345d894d687b197b392a7737da7b1efe0076ab\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCcHgleqDOvfxYZ\\n9kSrpiw3jRO81JcGFZjhdyv3HtTvMWBRbZCj9MU/PLUldHgkeKVrQikgbqS2kiBJ\\nxx9YN0qteW6IDFYGHuouJg9712Vko3zfyurvSEgPvoj1wV6dsbFepTotLsGHPHlU\\nOeTdrUfIW1fg86lLTDCzBKXBtRfEcz7No4qdSBr7AYMVmpYqEYJfEDU9irsqMHgB\\n8upkEwtvzr3QyFIY7os+UNCJNpSMRvhA2O9rxtqcqkHWDBNsqXnNGCToRy1phjTH\\nnSJDFXDi2ewS9qKqT+Bjgz2Zo7lrKPidu9YEz31esfGV6zTewG7kXwGAIibBniu+\\nXUiPm36lAgMBAAECggEABqjqXekfgVRS0Us17TuixcDlL2hDRA1uTun6TGm6jDSn\\nRvn9Ul05/+NbV+xyxp2F8wAoyHsn2Cj+Kx+NOQJs018rha6/CbZOZ6cUgIQ1DVjD\\nFjnuEUScXF2UsqUr397Jcf/q7iwBMf90rpDksEc42XV6F3zVg0KODkj7uDp9N9Jg\\nqZNMbbvC1RvbwuPQRhHuTykytAN1aHBHab9TlcpeY8QofuUYUqEI0iMB3u2R1WKg\\ncBgRtiPypG5qNUisasmHshYE5920/kgpGT+mve4ln34snga7x/tOQ3X9F1hzOnps\\nryj3MmIOqrAxvG1f10RfyhqoAHUPRKVDeXsJ5aEKQQKBgQDWYaYCTaP4WpBiSnjA\\neF89YeCGYTPDI/wF50NeDRYuruF3aEj8MpO9xB9EocNqTBPFwjSaMlzJ0Z/uCwMs\\n4p0BMKmiQMn4Mwp9/RVrDhFjIInzGHOrihlMtivrBGVWH8m+WriibTRd2iCCrYCK\\nI4KQyG70nVo7eBorX9en5LWv5QKBgQC6bMWy4xfkvS04QgC9sPHxYSGvhzCh0qKd\\ndYWOko6BxNZEgNiYD0xogicba1XioE3EncY1M8SxDCO/WAlonVQZ9yHwEKrAsbI7\\nBeOJL9o5stslKhg3FsYWKMv/bpNVOPPw+iJHaFV1Yr8h43BjYygpMk7b3SqzKQ1R\\nos6p+B4nwQKBgQCtYX4MgNRrCHubMtWj+/Yyw5T9uWFOoiyybY37Y6QcD03D77CP\\nXyxDnnkXXaiYsHyRRPab+r02b/XjZD6mL4SBMOUQl0uBE26Z5HRNB6yiOe+3joAG\\nLQM3GmI8iWqXk8/qxp5qpt1iRgZjCYVas0tmYuPxq80NKZ4olDPq0jrqzQKBgEOH\\nu2W9lZ4qWQMUBaixPDMv+D/ZPOihoH5hX+vxcuXXI7bU5aDPieIho4DvNMFPSFx8\\nupvzEMEBoMFjZqUauKhfOU/4+aGrvOBclbvQNcDI5VFlGjh6a28DgEIbEAvDAbq9\\nIUW0qaBIEOx5svCNrrKdoqtKRnl32XdE/o/bbTEBAoGAYVoTtEDhsuv8fUw+eU10\\nmQtUmCxIXzCBxECil2SfPBe0bNjdhCaRDz92Vgn9WBfKfWTiL+LS6TUhWBLCWe7X\\nKf5U1dFDyJISdr7YZ8+EC4SJGbe2nX9Jt2Dv6XpkvVx5ON8dkc8SXck7lvyoiOzZ\\n7ClNPGDGvIp7/KHkCqcONz0=\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"calendar-service@unical-442813.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"104120593216820499230\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/calendar-service%40unical-442813.iam.gserviceaccount.com\",\n" +
                "  \"universe_domain\": \"googleapis.com\"\n" +
                "}";
        validAccountName = "test@test.com";
        validCalendarName = "Test Calendar";
        validCalendarId = "primary";

        calendar = new GoogleCalendar(validCredentials, validAccountName, validCalendarName, validCalendarId);
    }

    @Test
    void constructor_WithValidParameters_CreatesGoogleCalendar() {
        assertNotNull(calendar);
        assertEquals(validAccountName, calendar.getAccountName());
        assertEquals(validCalendarName, calendar.getCalendarName());
        assertEquals(validCalendarId, calendar.getCalendarId());
        assertEquals(validCredentials, calendar.getCredentials());
        assertEquals("GoogleCalendar", calendar.getCalendarApiName());
    }

    @Test
    void constructor_WithNullCredentials_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar(null, validAccountName, validCalendarName, validCalendarId)
        );
        assertEquals("Credentials cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyCredentials_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar("", validAccountName, validCalendarName, validCalendarId)
        );
        assertEquals("Credentials cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithBlankCredentials_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar("   ", validAccountName, validCalendarName, validCalendarId)
        );
        assertEquals("Credentials cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithNullAccountName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar(validCredentials, null, validCalendarName, validCalendarId)
        );
        assertEquals("Account name cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyAccountName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar(validCredentials, "", validCalendarName, validCalendarId)
        );
        assertEquals("Account name cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithNullCalendarName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar(validCredentials, validAccountName, null, validCalendarId)
        );
        assertEquals("Calendar name cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyCalendarName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar(validCredentials, validAccountName, "", validCalendarId)
        );
        assertEquals("Calendar name cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithNullCalendarId_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar(validCredentials, validAccountName, validCalendarName, null)
        );
        assertEquals("Calendar ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyCalendarId_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar(validCredentials, validAccountName, validCalendarName, "")
        );
        assertEquals("Calendar ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithInvalidCredentialsJson_ThrowsIllegalArgumentException() {
        String invalidJson = "invalid json";
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoogleCalendar(invalidJson, validAccountName, validCalendarName, validCalendarId)
        );
        assertTrue(exception.getMessage().contains("Invalid credentials format"));
    }

    @Test
    void getHttpRequestInitializer_WithValidCredentials_ReturnsInitializer() {
        assertDoesNotThrow(() -> {
            HttpRequestInitializer initializer = calendar.getHttpRequestInitializer();
            assertNotNull(initializer);
        });
    }

    @Test
    void getHttpRequestInitializer_CalledMultipleTimes_ReusesSameInitializer() throws IOException {
        HttpRequestInitializer initializer1 = calendar.getHttpRequestInitializer();
        HttpRequestInitializer initializer2 = calendar.getHttpRequestInitializer();
        assertSame(initializer1, initializer2);
    }
}