package tdt4140.gr1817.app.ui.feature.viewuser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.app.core.feature.user.GetUserWithId;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class ViewUserControllerTest {

    private ViewUserController viewUserController;
    private Navigator navigatorMock;
    private GetUserWithId getUserWithIdMock;

    @Before
    public void setUp() throws Exception {
        UserSelectionService userSelectionService = new UserSelectionService();
        getUserWithIdMock = Mockito.mock(GetUserWithId.class);
        navigatorMock = Mockito.mock(Navigator.class);

        viewUserController = new ViewUserController(userSelectionService, getUserWithIdMock, navigatorMock);
    }

    @Test
    public void shouldNavigateToSeeUsers() throws Exception {
        // When
        viewUserController.goToSeeUsers();

        // Then
        verify(navigatorMock).navigate(Page.SEE_USERS);
    }

    @Test
    public void shouldSetUserData() throws Exception {
        // Given
        User user = createUserWithAge(22);
        Mockito.when(getUserWithIdMock.getUserWithId(1)).thenReturn(user);

        // When
        viewUserController.loadUserData(1);
        viewUserController.displayUser();

        // Then
        assertThat(viewUserController.ageTextProperty.get(), is("22 years old"));
        assertThat(viewUserController.fullNameTextProperty.get(), is("Test Larsson"));
        assertThat(viewUserController.emailTextProperty.get(), is("test@larsson.com"));
        assertThat(viewUserController.heightTextProperty.get(), is("2.10m tall"));
    }


    private static User createUserWithAge(final int age) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.YEAR, -age);
        Date birthDate = calendar.getTime();
        return new User(1, "Test", "Larsson", 210, birthDate,
                null, null, "test@larsson.com");
    }
}