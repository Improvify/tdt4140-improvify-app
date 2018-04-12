package tdt4140.gr1817.app.core.feature.user;

import com.github.npathai.hamcrestopt.OptionalMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertThat;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class UserSelectionServiceTest {

    private UserSelectionService userSelectionService;

    @Before
    public void setUp() throws Exception {
        userSelectionService = new UserSelectionService();
    }

    @Test
    public void shouldReturnEmptyByDefault() throws Exception {
        // When
        Optional<UserSelectionService.UserId> selectedUser = userSelectionService.getSelectedUserId();

        // Then
        assertThat(selectedUser, OptionalMatchers.isEmpty());
    }

    @Test
    public void shouldReturnSelectedUser() throws Exception {
        // Given
        UserSelectionService.UserId userId = new UserSelectionService.UserId(5);
        userSelectionService.setSelectedUserId(userId);

        // When
        Optional<UserSelectionService.UserId> selectedUser = userSelectionService.getSelectedUserId();

        // Then
        assertThat(selectedUser, OptionalMatchers.isPresentAndIs(userId));
    }


    @Test
    public void nullShouldUnsetSelectedUser() throws Exception {
        // Given
        UserSelectionService.UserId userId = new UserSelectionService.UserId(8);
        userSelectionService.setSelectedUserId(userId);

        // When
        userSelectionService.setSelectedUserId(null);
        Optional<UserSelectionService.UserId> selectedUser = userSelectionService.getSelectedUserId();

        // Then
        assertThat(selectedUser, OptionalMatchers.isEmpty());
    }

}