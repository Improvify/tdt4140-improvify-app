package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class UserResourceTest {
    UserRepository rep;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(UserRepository.class);
    }

    @Test
    public void shouldAddUser() {
        // Given
        Gson gson = new Gson();
        UserResource userResource = new UserResource(rep, gson);
        User user = createUser();
        String json = gson.toJson(user);

        // When
        userResource.createUser(json);

        // Then
        verify(rep).add(Mockito.eq(user));;
        verifyNoMoreInteractions(rep);
    }

    @Test(expected = JsonSyntaxException.class)
    public void shouldFailToAddUser() {
        // Given
        Gson gson = new Gson();
        UserResource userResource = new UserResource(rep, gson);
        User user = createUser();
        String s = user.toString();

        // When
        userResource.createUser(s);
    }

    private static User createUser() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        return new User(1, "T", "EST", 2.5f, date, "asd", "123", "123@ll.com");
    }
}
