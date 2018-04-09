package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;
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
    private GoalRepository rep;
    private Gson gson = new Gson();
    private GoalResource goalResource;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(GoalRepository.class);

        Goal goal = createGoal();
        when(rep.query(Mockito.any())).thenReturn(Collections.singletonList(goal));

        final GoalValidator validator = new GoalValidator(gson);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator();
        goalResource = new GoalResource(rep, gson, validator, authenticator);
    }

    @Test
    public void shouldAddGoal() throws Exception {
        // Given
        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.createGoal(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(rep).add(Mockito.eq(goal));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotAddWhenInvalidGoal() throws Exception {
        // Given
        Goal goal = createGoal();
        String invalidJson = goal.toString();

        // When
        goalResource.createGoal(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotAddGoalWhenWrongAuthorization() {
        // Given
        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.createGoal(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldRemoveGoal() {
        // Given
        int id = 1;

        // When
        goalResource.deleteGoal(id, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(rep).query(any(Specification.class));
        verify(rep).remove(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotRemoveGoalWhenWrongAuthorization() {
        // Given
        int id = 1;

        // When
        goalResource.deleteGoal(id, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verify(rep).query(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    private static Goal createGoal() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
        return new Goal(1, user, "Test goal", true, false);
    }
}
