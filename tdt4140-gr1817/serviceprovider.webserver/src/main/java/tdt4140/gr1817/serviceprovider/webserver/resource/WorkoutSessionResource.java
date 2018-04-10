package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify.GetWorkoutSessionByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.WorkoutSessionValidator;

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
@Path("workoutsession")
public class WorkoutSessionResource {

    private final Gson gson;
    private final WorkoutSessionRepository repository;
    private final WorkoutSessionValidator validator;
    private final AuthBasicAuthenticator authenticator;

    @Inject
    public WorkoutSessionResource(WorkoutSessionRepository repository, Gson gson, WorkoutSessionValidator validator,
                                  AuthBasicAuthenticator authenticator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createWorkoutSession(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            WorkoutSession workoutSession = gson.fromJson(json, WorkoutSession.class);

            if (authenticator.authenticate(credentials, workoutSession.getUser())) {
                repository.add(workoutSession);
                return Response.status(200).entity("Workout session added").build();
            }
            return Response.status(401).entity("Authorization failed").build();
        }
        return Response.status(400).entity("Failed to add workout session, illegal request").build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteWorkoutSession(@PathParam("id") int id, @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetWorkoutSessionByIdSpecification(id);
        try {
            WorkoutSession workoutSession = repository.query(specification).get(0);

            if (authenticator.authenticate(credentials, workoutSession.getUser())) {
                repository.remove(specification);
                return Response.status(200).entity("Workout session removed").build();
            }
            return Response.status(401).entity("Authorization failed").build();
        } catch (IndexOutOfBoundsException e) {
            // If workout session with given id doesn't exist
            return Response.status(404).entity("Failed to remove workout session, not found").build();
        }
    }
}
