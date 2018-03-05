package serviceproviderwebserver;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
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

    @Inject
    public WorkoutSessionResource(WorkoutSessionRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createWorkoutSession(String json) {
        WorkoutSession workoutSession = gson.fromJson(json, WorkoutSession.class);
        //validateWorkoutSessionInput();
        repository.add(workoutSession);
        return "Workoutsession added";
    }
}
