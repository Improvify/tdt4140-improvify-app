package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.UserValidator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class UserResourceTest {
    private UserRepository rep;
    private UserResource userResource;
    private Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(UserRepository.class);
        final UserValidator validator = new UserValidator(gson);
        userResource = new UserResource(rep, gson, validator);
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
        userResource.deleteGoal(id);

        // Then
        verify(rep).remove(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    private static User createUser() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        return new User(1, "T", "EST", 2.5f, date, "asd", "123", "123@ll.com");
    }
}
