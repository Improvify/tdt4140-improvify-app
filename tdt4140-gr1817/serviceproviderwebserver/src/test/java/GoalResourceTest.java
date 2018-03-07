import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import serviceproviderwebserver.GoalResource;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GoalResourceTest {
    GoalRepository rep;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(GoalRepository.class);
    }

    @Test
    public void shouldAddGoal() {
        // Given
        Gson gson = new Gson();
        GoalResource goalResource = new GoalResource(rep, gson);
        Goal goal = createGoal();
        String json = gson.toJson(goal);

        // When
        goalResource.createGoal(json);

        // Then
        verify(rep).add(Mockito.eq(goal));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldFailToAddGoal(){
        // Given
        Gson gson = new Gson();
        GoalResource goalResource = new GoalResource(rep, gson);
        Goal goal = createGoal();
        String s = goal.toString();

        // When
        goalResource.createGoal(s);

        // Then

        verify(rep).add(Mockito.eq(goal));
        verifyNoMoreInteractions(rep);
    }

    private static Goal createGoal() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1,"hei", "bu", 2.5f, date, "hellu", "hshs", "123@hotmail.com");
        return new Goal(1, user, "bu", true, false);
    }
}
