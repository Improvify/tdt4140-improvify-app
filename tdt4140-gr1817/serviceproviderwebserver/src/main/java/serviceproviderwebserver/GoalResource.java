package serviceproviderwebserver;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Slf4j
@Path("goal")
public class GoalResource {
    GoalRepository repository;
    private Gson gson;

    public class GoalRepository {
        String description;
        boolean isCompleted;
        boolean isCurrent;
    }

    @Inject
    public GoalRepository(GoalRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createGoal(String json) {
        //serviceproviderwebserver.WeightResource.Weight weight = gson.fromJson(json, serviceproviderwebserver.WeightResource.Weight.class);
        return json;
    }
}
