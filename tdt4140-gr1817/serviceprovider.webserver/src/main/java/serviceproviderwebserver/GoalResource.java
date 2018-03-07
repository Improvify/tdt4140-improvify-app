package serviceproviderwebserver;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("goal")
public class GoalResource {

    private final GoalRepository repository;
    private final Gson gson;

    @Inject
    public GoalResource(GoalRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createGoal(String json) {
        //Goal goal = gson.fromJson(json, goal.class);
        //GoalValidator validator = new GoalValidator;
        //validator.validate(json);
        //repository.add(goal);
        return "Goal added";
    }
}
