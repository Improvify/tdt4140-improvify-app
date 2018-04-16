package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllRestingHeartRateForUserSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByUsernameSpecification;
import tdt4140.gr1817.serviceprovider.webserver.security.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.RestingHeartRateValidator;

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
@Path("restingheartrate")
public class RestingHeartRateResource {

    private final Gson gson;
    private final RestingHeartRateRepository restingHeartRateRepository;
    private final UserRepository userRepository;
    private final RestingHeartRateValidator validator;
    private final AuthBasicAuthenticator authenticator;

    @Inject
    public RestingHeartRateResource(RestingHeartRateRepository restingHeartRateRepository,
                                    UserRepository userRepository, Gson gson, RestingHeartRateValidator validator,
                                    AuthBasicAuthenticator authenticator) {
        this.restingHeartRateRepository = restingHeartRateRepository;
        this.userRepository = userRepository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestingHeartRates(@PathParam("username") String username,
                                         @HeaderParam("Authorization") String credentials) {
        try {
            User user = userRepository.query(new GetUserByUsernameSpecification(username)).get(0);

            if (authenticator.authenticate(credentials, user)) {
                final List<RestingHeartRate> restingHeartRates = restingHeartRateRepository
                        .query(new GetAllRestingHeartRateForUserSpecification(user.getId()));
                String json = gson.toJson(restingHeartRates);
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
    public Response createRestingHeartRate(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            RestingHeartRate restingHeartRate = gson.fromJson(json, RestingHeartRate.class);

            try {
                User user = getCorrectUserDataFromDatabase(restingHeartRate.getMeasuredBy());
                restingHeartRate.setMeasuredBy(user);

                if (authenticator.authenticate(credentials, restingHeartRate.getMeasuredBy())) {
                    restingHeartRateRepository.add(restingHeartRate);
                    return Response.status(200).entity("{\"message\":\"Resting heart rate added\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400)
                .entity("{\"message\":\"Failed to add resting heart rate, illegal json for heart rate\"}")
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRestingHeartRate(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            RestingHeartRate restingHeartRate = gson.fromJson(json, RestingHeartRate.class);

            try {
                User user = getCorrectUserDataFromDatabase(restingHeartRate.getMeasuredBy());
                restingHeartRate.setMeasuredBy(user);

                if (authenticator.authenticate(credentials, restingHeartRate.getMeasuredBy())) {
                    restingHeartRateRepository.update(restingHeartRate);
                    return Response.status(200).entity("{\"message\":\"Resting heart rate updated\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400)
                .entity("{\"message\":\"Failed to update resting heart rate, illegal json for heart rate\"}")
                .build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRestingHeartRate(@PathParam("id") int id, @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetRestingHeartRateByIdSpecification(id);
        try {
            RestingHeartRate restingHeartRate = restingHeartRateRepository.query(specification).get(0);

            if (authenticator.authenticate(credentials, restingHeartRate.getMeasuredBy())) {
                restingHeartRateRepository.remove(specification);
                return Response.status(200).entity("{\"message\":\"Resting heart rate removed\"}").build();
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
