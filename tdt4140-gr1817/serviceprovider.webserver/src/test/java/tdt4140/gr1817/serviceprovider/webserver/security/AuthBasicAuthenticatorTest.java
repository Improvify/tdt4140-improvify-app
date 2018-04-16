package tdt4140.gr1817.serviceprovider.webserver.security;

import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.serviceprovider.webserver.validation.util.AuthBasicUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AuthBasicAuthenticatorTest {

    private User user;
    private final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator();

    @Before
    public void setUp() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        this.user = new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
    }

    @Test
    public void shouldBeCorrectCredentials() {
        // Given
        user.setUsername("test");
        user.setPassword("123");

        // When
        boolean outcome = authenticator.authenticate(AuthBasicUtil.HEADER_TEST_123, user);

        // Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldBeWrongUsername() {
        // Given
        user.setUsername("wrong");
        user.setPassword("123");

        // When
        boolean outcome = authenticator.authenticate(AuthBasicUtil.HEADER_TEST_123, user);

        // Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldBeWrongPassword() {
        // Given
        user.setUsername("test");
        user.setPassword("wrong");

        // When
        boolean outcome = authenticator.authenticate(AuthBasicUtil.HEADER_TEST_123, user);

        // Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldBeWrongUsernameAndPassword() {
        // Given
        user.setUsername("even");
        user.setPassword("wronger");

        // When
        boolean outcome = authenticator.authenticate(AuthBasicUtil.HEADER_TEST_123, user);

        // Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldNotAcceptNoCredentials() {
        // Given
        user.setUsername("test");
        user.setPassword("123");

        // When
        boolean outcome = authenticator.authenticate(null, user);

        // Then
        assertThat(outcome, is(false));
    }
}
