package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetGoalByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.GoalValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("goal")
public class GoalResource {

    private final Gson gson;
    private final GoalRepository repository;
    private final GoalValidator validator;

    @Inject
    public GoalResource(GoalRepository repository, Gson gson, GoalValidator validator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createGoal(String json) {
        if (validator.validate(json)) {
            Goal goal = gson.fromJson(json, Goal.class);
            repository.add(goal);
            return "Goal added";
        } else {
            return "Failed to add goal";
        }
    }

    @DELETE
    @Path("{id}")
    public String deleteGoal(@PathParam("id") int id) {
        repository.remove(new GetGoalByIdSpecification(id));
        return "Goal removed";
    }
}
