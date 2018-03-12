package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify.GetWorkoutSessionByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.WorkoutSessionValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
        if (validator.validate(json)) {
            WorkoutSession workoutSession = gson.fromJson(json, WorkoutSession.class);
            repository.add(workoutSession);
            return "Workoutsession added";
        } else {
            return "Failed to add workoutsession";
        }
    }

    @DELETE
    @Path("{id}")
    public String deleteGoal(@PathParam("id") int id) {
        repository.remove(new GetWorkoutSessionByIdSpecification(id));
        return "Workout session removed";
    }
}
