package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.GoalValidator;
import tdt4140.gr1817.serviceprovider.webserver.validation.util.AuthBasicUtil;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class GoalResourceTest {
    private GoalRepository goalRepository;
    private Gson gson = new Gson();
    private GoalResource goalResource;

    @Before
    public void setUp() throws Exception {
        goalRepository = Mockito.mock(GoalRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        final Goal goal = createGoal();
        when(goalRepository.query(Mockito.any())).thenReturn(Collections.singletonList(goal));
        when(userRepository.query(Mockito.any())).thenReturn(Collections.singletonList(goal.getUser()));

        final GoalValidator validator = new GoalValidator(gson);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator();
        goalResource = new GoalResource(goalRepository, userRepository, gson, validator, authenticator);
    }

    @Test
    public void shouldAddGoal() throws Exception {
        // Given
        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.createGoal(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(goalRepository).add(Mockito.eq(goal));
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldNotAddWhenInvalidGoal() throws Exception {
        // Given
        Goal goal = createGoal();
        String invalidJson = goal.toString();

        // When
        goalResource.createGoal(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldNotAddGoalWhenWrongAuthorization() {
        // Given
        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.createGoal(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldNotAddGoalWhenIllegalHeader() {
        // Given
        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.createGoal(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldUpdateGoal() throws Exception {
        // Given
        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.updateGoal(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(goalRepository).update(Mockito.eq(goal));
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldNotUpdateWhenInvalidGoal() throws Exception {
        // Given
        Goal goal = createGoal();
        String invalidJson = goal.toString();

        // When
        goalResource.updateGoal(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldNotUpdateGoalWhenWrongAuthorization() {
        // Given
        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.updateGoal(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldNotUpdateGoalWhenIllegalHeader() {
        // Given
        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.updateGoal(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldRemoveGoal() {
        // Given
        int id = 1;

        // When
        goalResource.deleteGoal(id, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(goalRepository).query(any(Specification.class));
        verify(goalRepository).remove(any(Specification.class));
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldNotRemoveGoalWhenWrongAuthorization() {
        // Given
        int id = 1;

        // When
        goalResource.deleteGoal(id, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verify(goalRepository).query(any(Specification.class));
        verifyNoMoreInteractions(goalRepository);
    }

    @Test
    public void shouldNotRemoveGoalWhenIllegalHeader() {
        // Given
        int id = 1;

        // When
        goalResource.deleteGoal(id, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        verify(goalRepository).query(any(Specification.class));
        verifyNoMoreInteractions(goalRepository);
    }

    private static Goal createGoal() {
        Calendar calendar = new GregorianCalendar(2000, 1, 1);
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
        return new Goal(1, user, "Test goal", true, false);
    }
}
