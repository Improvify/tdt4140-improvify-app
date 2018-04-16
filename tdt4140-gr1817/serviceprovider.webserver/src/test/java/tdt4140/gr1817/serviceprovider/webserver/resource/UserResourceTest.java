package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.serviceprovider.webserver.security.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.security.PasswordHashUtil;
import tdt4140.gr1817.serviceprovider.webserver.validation.UserValidator;
import tdt4140.gr1817.serviceprovider.webserver.validation.util.AuthBasicUtil;

import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class UserResourceTest {
    private UserRepository userRepository;
    private UserResource userResource;
    private Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        userRepository = Mockito.mock(UserRepository.class);

        final User user = createUser();
        when(userRepository.query(Mockito.any())).thenReturn(Collections.singletonList(user));

        final UserValidator validator = new UserValidator(gson);
        final PasswordHashUtil passwordHashUtilMock = Mockito.mock(PasswordHashUtil.class);
        when(passwordHashUtilMock.validatePassword(any(String.class), any(String.class))).thenReturn(true);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator(passwordHashUtilMock);
        userResource = new UserResource(userRepository, gson, validator, authenticator, passwordHashUtilMock);
    }

    @Test
    public void shouldGetUser() throws Exception {
        // Given
        String username = "test";

        // When
        final Response response = userResource.getUser(username, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(userRepository).query(any(Specification.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldNotGetUserWhenWrongAuthorization() {
        // Given
        String username = "test";

        // When
        final Response response = userResource.getUser(username, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(userRepository).query(any(Specification.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldNotGetUserWhenIllegalHeader() {
        // Given
        String username = "test";

        // When
        final Response response = userResource.getUser(username, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(userRepository).query(any(Specification.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldAddUser() throws Exception {
        // Given
        User user = createUser();
        String json = gson.toJson(user);

        // When
        final Response response = userResource.createUser(json);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(userRepository).add(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldNotAddWhenInvalidUser() throws Exception {
        // Given
        User user = createUser();
        String invalidJson = user.toString();

        // When
        final Response response = userResource.createUser(invalidJson);

        // Then
        assertThat(response.getStatus(), is(400));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldCallAddAndThrowException() {
        // Given
        User user = createUser();
        String json = gson.toJson(user);
        doThrow(new RuntimeException()).when(userRepository).add(any(User.class));

        // When
        final Response response = userResource.createUser(json);

        // Then
        assertThat(response.getStatus(), is(400));
        verify(userRepository).add(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        // Given
        User user = createUser();
        String json = gson.toJson(user);

        // When
        final Response response = userResource.updateUser(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(userRepository).query(any(Specification.class));
        verify(userRepository).update(Mockito.eq(user));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldNotUpdateWhenInvalidUser() throws Exception {
        // Given
        User user = createUser();
        String invalidJson = user.toString();

        // When
        final Response response = userResource.updateUser(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(400));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldNotUpdateUserWhenWrongAuthorization() {
        // Given
        User user = createUser();
        String json = gson.toJson(user);

        // When
        final Response response = userResource.updateUser(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(userRepository).query(any(Specification.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldNotUpdateUserWhenIllegalHeader() {
        // Given
        User user = createUser();
        String json = gson.toJson(user);

        // When
        final Response response = userResource.updateUser(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(userRepository).query(any(Specification.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldRemoveUser() {
        // Given
        int id = 1;

        // When
        final Response response = userResource.deleteUser(id, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(userRepository).query(any(Specification.class));
        verify(userRepository).remove(any(Specification.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldNotRemoveUserWhenWrongAuthorization() {
        // Given
        int id = 1;

        // When
        final Response response = userResource.deleteUser(id, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(userRepository).query(any(Specification.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldNotRemoveUserWhenIllegalHeader() {
        // Given
        int id = 1;

        // When
        final Response response = userResource.deleteUser(id, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(userRepository).query(any(Specification.class));
        verifyNoMoreInteractions(userRepository);
    }

    private static User createUser() {
        Calendar calendar = new GregorianCalendar(2000, 1, 1);
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        return new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
    }
}
