package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllGoalsForUserSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetGoalByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByUsernameSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.GoalValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
@Path("goal")
public class GoalResource {

    private final Gson gson;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalValidator validator;
    private final AuthBasicAuthenticator authenticator;

    @Inject
    public GoalResource(GoalRepository goalRepository, UserRepository userRepository, Gson gson,
                        GoalValidator validator, AuthBasicAuthenticator authenticator) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGoals(@PathParam("username") String username, @HeaderParam("Authorization") String credentials) {
        try {
            User user = userRepository.query(new GetUserByUsernameSpecification(username)).get(0);

            if (authenticator.authenticate(credentials, user)) {
                final List<Goal> goals = goalRepository.query(new GetAllGoalsForUserSpecification(user.getId()));
                String json = gson.toJson(goals);
                return Response.status(200).entity(json).build();
            }
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        } catch (RuntimeException e) {
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGoal(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            Goal goal = gson.fromJson(json, Goal.class);

            try {
                User user = getCorrectUserDataFromDatabase(goal.getUser());
                goal.setUser(user);

                if (authenticator.authenticate(credentials, goal.getUser())) {
                    goalRepository.add(goal);
                    return Response.status(200).entity("{\"message\":\"Goal added\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                log.error("Failed to create goal", e);
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400).entity("{\"message\":\"Failed to add goal, illegal json for goal\"}").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGoal(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            Goal goal = gson.fromJson(json, Goal.class);

            try {
                User user = getCorrectUserDataFromDatabase(goal.getUser());
                goal.setUser(user);

                if (authenticator.authenticate(credentials, goal.getUser())) {
                    goalRepository.update(goal);
                    return Response.status(200).entity("{\"message\":\"Goal updated\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400).entity("{\"message\":\"Failed to update goal, illegal json for goal\"}").build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGoal(@PathParam("id") int id, @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetGoalByIdSpecification(id);
        try {
            Goal goal = goalRepository.query(specification).get(0);

            if (authenticator.authenticate(credentials, goal.getUser())) {
                goalRepository.remove(specification);
                return Response.status(200).entity("{\"message\":\"Goal removed\"}").build();
            }
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        } catch (RuntimeException e) {
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        }
    }

    private User getCorrectUserDataFromDatabase(User user) {
        Specification specification = new GetUserByIdSpecification(user.getId());
        return userRepository.query(specification).get(0);
    }
}
