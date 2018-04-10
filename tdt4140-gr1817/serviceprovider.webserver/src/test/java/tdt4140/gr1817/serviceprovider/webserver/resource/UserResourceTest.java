package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.UserValidator;
import tdt4140.gr1817.serviceprovider.webserver.validation.util.AuthBasicUtil;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class UserResourceTest {
    private UserRepository rep;
    private UserResource userResource;
    private Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(UserRepository.class);

        User user = createUser();
        when(rep.query(Mockito.any())).thenReturn(Collections.singletonList(user));

        final UserValidator validator = new UserValidator(gson);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator();
        userResource = new UserResource(rep, gson, validator, authenticator);
    }

    @Test
    public void shouldAddUser() throws Exception {
        // Given
        User user = createUser();
        String json = gson.toJson(user);

        // When
        userResource.createUser(json);

        // Then
        verify(rep).add(Mockito.eq(user));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotAddWhenInvalidUser() throws Exception {
        // Given
        User user = createUser();
        String invalidJson = user.toString();

        // When
        userResource.createUser(invalidJson);

        // Then
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldRemoveUser() {
        // Given
        int id = 1;

        // When
        userResource.deleteUser(id, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(rep).query(any(Specification.class));
        verify(rep).remove(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotRemoveUserWhenWrongAuthorization() {
        // Given
        int id = 1;

        // When
        userResource.deleteUser(id, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verify(rep).query(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    private static User createUser() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        return new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
    }
}
