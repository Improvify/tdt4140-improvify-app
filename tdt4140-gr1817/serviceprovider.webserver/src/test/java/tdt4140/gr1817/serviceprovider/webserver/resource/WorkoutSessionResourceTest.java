package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.WorkoutSessionValidator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class WorkoutSessionResourceTest {
    private WorkoutSessionRepository rep;
    private final Gson gson = new Gson();
    private WorkoutSessionResource workoutSessionResource;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(WorkoutSessionRepository.class);
        final WorkoutSessionValidator validator = new WorkoutSessionValidator(gson);
        workoutSessionResource = new WorkoutSessionResource(rep, gson, validator);
    }

    @Test
    public void shouldAddWorkoutSession() throws Exception {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        workoutSessionResource.createWorkoutSession(json);

        // Then
        verify(rep).add(Mockito.eq(workoutSession));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotAddWhenInvalidWorkoutSession() throws Exception {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String invalidJson = workoutSession.toString();

        // When
        workoutSessionResource.createWorkoutSession(invalidJson);

        //Then
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldRemoveGoal() {
        // Given
        int id = 1;

        // When
        workoutSessionResource.deleteGoal(id);

        // Then
        verify(rep).remove(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    private static WorkoutSession createWorkoutSession() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "hei", "bu", 2.5f, date, "hellu", "hshs", "123@hotmail.com");
        return new WorkoutSession(1, date, 1, 12.5f, 140.4f, 170.3f, 12.3f, user);
    }
}
