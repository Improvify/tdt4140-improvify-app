package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.GoalValidator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GoalResourceTest {
    private GoalRepository rep;
    private Gson gson = new Gson();
    private GoalResource goalResource;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(GoalRepository.class);
        final GoalValidator validator = new GoalValidator(gson);
        goalResource = new GoalResource(rep, gson, validator);
    }

    @Test
    public void shouldAddGoal() throws Exception {
        // Given

        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.createGoal(json);

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
        goalResource.createGoal(invalidJson);

        // Then
        verifyNoMoreInteractions(rep);
    }

    private static Goal createGoal() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "hei", "bu", 2.5f, date, "hellu", "hshs", "123@hotmail.com");
        return new Goal(1, user, "bu", true, false);
    }
}
