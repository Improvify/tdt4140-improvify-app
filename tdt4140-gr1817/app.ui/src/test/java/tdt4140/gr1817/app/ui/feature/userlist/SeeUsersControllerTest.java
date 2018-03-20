package tdt4140.gr1817.app.ui.feature.userlist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.app.core.feature.user.GetAllUsers;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import javax.inject.Provider;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class SeeUsersControllerTest {

    private GetAllUsers getAllUsersMock;
    private Provider<GetAllUsers> getAllUsersMockProvider;

    @Before
    public void setUp() throws Exception {
        getAllUsersMock = Mockito.mock(GetAllUsers.class);
        getAllUsersMockProvider = () -> getAllUsersMock;
    }

    @Test
    public void shouldLoadUsers() throws Exception {
        // Given
        final User user1 = createUser(1, 18);
        final User user2 = createUser(2, 22);
        final List<User> allUsers = Arrays.asList(user1, user2);
        when(getAllUsersMock.getAll()).thenReturn(allUsers);

        final SeeUsersController controller = new SeeUsersController(getAllUsersMockProvider, new UserItemAdapter());

        // When
        controller.loadUsers();

        // Then
        verify(getAllUsersMock).getAll();
        assertThat(controller.userItemList, hasSize(2));
    }

    private static User createUser(int i, int age) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, -age);
        final Date birthDate = calendar.getTime();

        return User.builder()
                .id(i)
                .firstName("Test")
                .lastName("Test2")
                .birthDate(birthDate)
                .email("test@test.com")
                .build();
    }
}