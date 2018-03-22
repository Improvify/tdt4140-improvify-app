package tdt4140.gr1817.app.core.feature.user;

import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class GetAllUsersTest {

    private UserRepository userRepository;
    private List<User> users;
    private User user;
    private User user2;

    @Before
    public void setUp() throws Exception {
        user = User.builder().id(1).build();
        user2 = User.builder().id(2).build();
        users = Arrays.asList(user, user2);

        userRepository = mock(UserRepository.class);
        when(userRepository.query(any())).thenReturn(users);
    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        // Given
        final GetAllUsers getAllUsers = new GetAllUsers(userRepository);

        // When
        final List<User> allUsers = getAllUsers.getAll();

        // Then
        assertThat(allUsers, hasSize(2));
        assertThat(allUsers, hasItem(user));
        assertThat(allUsers, hasItem(user2));
    }

    @Test
    public void shouldReturnEmptyListWhenNoUsers() throws Exception {
        // Given
        when(userRepository.query(any())).thenReturn(Collections.emptyList());
        final GetAllUsers getAllUsers = new GetAllUsers(userRepository);

        // When
        final List<User> users = getAllUsers.getAll();

        // Then
        assertThat(users, is(empty()));
    }

    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowOnNull() throws Exception {
        new GetAllUsers(null);
    }
}