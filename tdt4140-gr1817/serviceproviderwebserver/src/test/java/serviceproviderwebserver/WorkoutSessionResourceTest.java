package serviceproviderwebserver;

import com.google.gson.Gson;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;

import java.util.Date;

public class WorkoutSessionResourceTest {

    @Test
    public void shouldCreateUserOnPost() {
        // Given
        WorkoutSessionRepository repository = Mockito.mock(WorkoutSessionRepository.class);
        Gson gson = new Gson();
        WorkoutSessionResource resource = new WorkoutSessionResource(repository, gson);
        WorkoutSessionResource.WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        //resource.createUser(json);

        // Then
    }

    private static WorkoutSessionResource.WorkoutSession createWorkoutSession() {
        WorkoutSessionResource.WorkoutSession workoutSession = new WorkoutSessionResource.WorkoutSession();
        workoutSession.kcal = 700;
        workoutSession.avgHeartRate = 180;
        workoutSession.date = new Date();
        workoutSession.distanceRun = 100;
        workoutSession.maxHeartRate = 190;
        return null;
    }
}
