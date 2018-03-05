package serviceproviderwebserver;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Slf4j
@Path("workoutsession")
public class WorkoutSessionResource {
    WorkoutSessionRepository repository;
    private Gson gson;

    static class WorkoutSession {
        Date date;
        int intensity;
        int kcal;
        int avgHeartRate;
        int maxHeartRate;
        float distanceRun;
    }

    @Inject
    public WorkoutSessionResource(WorkoutSessionRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createWorkoutSession(String json) {
        //serviceproviderwebserver.WorkoutSessionResource.WorkoutSession workoutSession = gson.fromJson(json, serviceproviderwebserver.WorkoutSessionResource.WorkoutSession.class);
        return json;
    }
}
