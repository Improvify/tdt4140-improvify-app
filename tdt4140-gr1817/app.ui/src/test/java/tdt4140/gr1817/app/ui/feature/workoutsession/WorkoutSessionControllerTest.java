/*package tdt4140.gr1817.app.ui.feature.workoutsession;

import javafx.beans.property.SimpleObjectProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.app.core.feature.user.GetAllUsers;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.core.feature.workoutsession.GetAllWorkoutSessions;
import tdt4140.gr1817.app.ui.feature.userlist.SeeUsersController;
import tdt4140.gr1817.app.ui.feature.userlist.UserItem;
import tdt4140.gr1817.app.ui.feature.userlist.UserItemAdapter;
import tdt4140.gr1817.app.ui.feature.viewuser.ViewUserController;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import javax.inject.Provider;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkoutSessionControllerTest {
    private GetAllWorkoutSessions getAllWorkoutSessionsMock;
    private Provider<GetAllWorkoutSessions> getAllWorkoutSessionsMockProvider;
    private Navigator navigatorMock;
    private ViewUserController controller;
    private UserSelectionService userSelectionServiceMock;

    @Before
    public void setUp() throws Exception {
        getAllWorkoutSessionsMock = Mockito.mock(GetAllWorkoutSessions.class);
        getAllWorkoutSessionsMockProvider = () -> getAllWorkoutSessionsMock;
        navigatorMock = Mockito.mock(Navigator.class);
        userSelectionServiceMock = Mockito.mock(UserSelectionService.class);

        controller = new SeeUsersController(navigatorMock, getAllWorkoutSessionsMockProvider, userSelectionServiceMock,
                new WorkoutSessionAdapter());
    }

    @Test
    public void shouldLoadUsers() throws Exception {
        // Given
        final  = createUser(1, 18);
        final User user2 = createUser(2, 22);
        final List<User> allWorkoutSessions = Arrays.asList(user1, user2);
        when(getAllWorkoutSessionsMock.getAll()).thenReturn(allWorkoutSessions);

        // When
        controller.loadUsers();

        // Then
        verify(getAllUsersMock).getAll();
        assertThat(controller.userItemList, hasSize(2));
    }

    @Test
    public void shouldNavigateToGlobalStatistics() throws Exception {
        // When
        controller.showGlobalStatistics();

        // Then
        verify(navigatorMock).navigate(Page.GLOBAL_STATISTICS);
    }

    @Test
    public void shouldNavigateToViewUser() throws Exception {
        // When
        controller.showSelectedUser();

        // Then
        verify(navigatorMock).navigate(Page.VIEW_USER);
    }

    @Test
    public void shouldSetSelectedUserWhenNavigatingToViewUser() throws Exception {
        // Given
        UserItem userItem = new UserItem(5, "Test", "Test", "", 18);
        controller.selectedUserItem = new SimpleObjectProperty<>(userItem);

        // When
        controller.showSelectedUser();

        // Then
        verify(userSelectionServiceMock).setSelectedUserId(new UserSelectionService.UserId(5));
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
*/