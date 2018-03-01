package serviceproviderwebserver;

import com.google.gson.Gson;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.Date;

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
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(String json) {
        serviceproviderwebserver.WorkoutSessionResource.WorkoutSession workoutSession = gson.fromJson(json, serviceproviderwebserver.WorkoutSessionResource.WorkoutSession.class);
        System.out.println(workoutSession.kcal);
    }
}
