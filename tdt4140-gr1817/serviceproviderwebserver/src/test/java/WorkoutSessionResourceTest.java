import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import serviceproviderwebserver.WorkoutSessionResource;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class WorkoutSessionResourceTest {
    WorkoutSessionRepository rep;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(WorkoutSessionRepository.class);
    }

    @Test
    public void shouldAddWorkoutSession() {
        // Given
        Gson gson = new Gson();
        WorkoutSessionResource workoutSessionResource = new WorkoutSessionResource(rep, gson);
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        workoutSessionResource.createWorkoutSession(json);

        // Then
        verify(rep).add(Mockito.eq(workoutSession));
        verifyNoMoreInteractions(rep);
    }

    @Test(expected = JsonSyntaxException.class)
    public void shouldFailToAddWorkoutSession(){
        // Given
        Gson gson = new Gson();
        WorkoutSessionResource workoutSessionResource = new WorkoutSessionResource(rep, gson);
        WorkoutSession workoutSession = createWorkoutSession();
        String s = workoutSession.toString();

        // When
        workoutSessionResource.createWorkoutSession(s);
    }

    private static WorkoutSession createWorkoutSession() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1,"hei", "bu", 2.5f, date, "hellu", "hshs", "123@hotmail.com");
        return new WorkoutSession(1, date, 1, 12.5f, 140.4f, 170.3f, 12.3f, user);
    }
}
