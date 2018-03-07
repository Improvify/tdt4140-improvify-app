package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.WorkoutSessionValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("workoutsession")
public class WorkoutSessionResource {

    private final Gson gson;
    private final WorkoutSessionRepository repository;
    private final WorkoutSessionValidator validator;

    @Inject
    public WorkoutSessionResource(WorkoutSessionRepository repository, Gson gson, WorkoutSessionValidator validator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createWorkoutSession(String json) {
        WorkoutSession workoutSession = gson.fromJson(json, WorkoutSession.class);
        if (validator.validate(json)) {
            repository.add(workoutSession);
            return "Workoutsession added";
        } else {
            return "Failed to add workoutsession";
        }

    }
}
