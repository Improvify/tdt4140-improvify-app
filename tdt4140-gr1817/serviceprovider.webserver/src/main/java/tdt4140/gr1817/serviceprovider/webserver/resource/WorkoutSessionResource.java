package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByUsernameSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify.GetAllWorkoutSessionForUserSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify.GetWorkoutSessionByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.WorkoutSessionValidator;

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
@Path("workoutsession")
public class WorkoutSessionResource {

    private final Gson gson;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final UserRepository userRepository;
    private final WorkoutSessionValidator validator;
    private final AuthBasicAuthenticator authenticator;

    @Inject
    public WorkoutSessionResource(WorkoutSessionRepository workoutSessionRepository, UserRepository userRepository,
                                  Gson gson, WorkoutSessionValidator validator, AuthBasicAuthenticator authenticator) {
        this.workoutSessionRepository = workoutSessionRepository;
        this.userRepository = userRepository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkoutSessions(@PathParam("username") String username,
                                       @HeaderParam("Authorization") String credentials) {
        try {
            User user = userRepository.query(new GetUserByUsernameSpecification(username)).get(0);

            if (authenticator.authenticate(credentials, user)) {
                final List<WorkoutSession> workoutSessions = workoutSessionRepository
                        .query(new GetAllWorkoutSessionForUserSpecification(user.getId()));
                String json = gson.toJson(workoutSessions);
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
    public Response createWorkoutSession(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            WorkoutSession workoutSession = gson.fromJson(json, WorkoutSession.class);

            try {
                User user = getCorrectUserDataFromDatabase(workoutSession.getUser());
                workoutSession.setUser(user);

                if (authenticator.authenticate(credentials, workoutSession.getUser())) {
                    workoutSessionRepository.add(workoutSession);
                    return Response.status(200).entity("{\"message\":\"Workout session added\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400)
                .entity("{\"message\":\"Failed to add workout session, illegal json for workout session\"}").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWorkoutSession(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            WorkoutSession workoutSession = gson.fromJson(json, WorkoutSession.class);

            try {
                User user = getCorrectUserDataFromDatabase(workoutSession.getUser());
                workoutSession.setUser(user);

                if (authenticator.authenticate(credentials, workoutSession.getUser())) {
                    workoutSessionRepository.update(workoutSession);
                    return Response.status(200).entity("{\"message\":\"Workout session updated\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400)
                .entity("{\"message\":\"Failed to update workout session, illegal json for workout session\"}").build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWorkoutSession(@PathParam("id") int id, @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetWorkoutSessionByIdSpecification(id);
        try {
            WorkoutSession workoutSession = workoutSessionRepository.query(specification).get(0);

            if (authenticator.authenticate(credentials, workoutSession.getUser())) {
                workoutSessionRepository.remove(specification);
                return Response.status(200).entity("{\"message\":\"Workout session removed\"}").build();
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
