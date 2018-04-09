package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetGoalByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.GoalValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("goal")
public class GoalResource {

    private final Gson gson;
    private final GoalRepository repository;
    private final GoalValidator validator;
    private final AuthBasicAuthenticator authenticator;

    @Inject
    public GoalResource(GoalRepository repository, Gson gson, GoalValidator validator,
                        AuthBasicAuthenticator authenticator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createGoal(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            Goal goal = gson.fromJson(json, Goal.class);

            if (authenticator.authenticate(credentials, goal.getUser())) {
                repository.add(goal);
                return Response.status(200).entity("Goal added").build();
            }
            return Response.status(401).entity("Authorization failed").build();
        }
        return Response.status(400).entity("Failed to add goal, illegal request").build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteGoal(@PathParam("id") int id, @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetGoalByIdSpecification(id);
        try {
            Goal goal = repository.query(specification).get(0);

            if (authenticator.authenticate(credentials, goal.getUser())) {
                repository.remove(specification);
                return Response.status(200).entity("Goal removed").build();
            }
            return Response.status(401).entity("Authorization failed").build();
        } catch (IndexOutOfBoundsException e) {
            // If goal with given id doesn't exist
            return Response.status(404).entity("Failed to remove goal, not found").build();
        }
    }
}
