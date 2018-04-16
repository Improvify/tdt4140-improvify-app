package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllWeightsForUserSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByUsernameSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetWeightByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.security.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.WeightValidator;

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
@Path("weight")
public class WeightResource {

    private final Gson gson;
    private final WeightRepository weightRepository;
    private final UserRepository userRepository;
    private final WeightValidator validator;
    private final AuthBasicAuthenticator authenticator;

    @Inject
    public WeightResource(WeightRepository weightRepository, UserRepository userRepository, Gson gson,
                          WeightValidator validator, AuthBasicAuthenticator authenticator) {
        this.weightRepository = weightRepository;
        this.userRepository = userRepository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeights(@PathParam("username") String username,
                               @HeaderParam("Authorization") String credentials) {
        try {
            User user = userRepository.query(new GetUserByUsernameSpecification(username)).get(0);

            if (authenticator.authenticate(credentials, user)) {
                final List<Weight> weights = weightRepository
                        .query(new GetAllWeightsForUserSpecification(user.getId()));
                String json = gson.toJson(weights);
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
    public Response createWeight(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            Weight weight = gson.fromJson(json, Weight.class);

            try {
                User user = getCorrectUserDataFromDatabase(weight.getUser());
                weight.setUser(user);

                if (authenticator.authenticate(credentials, weight.getUser())) {
                    weightRepository.add(weight);
                    return Response.status(200).entity("{\"message\":\"Weight added\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400).entity("{\"message\":\"Failed to add weight, illegal json for weight\"}").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWeight(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            Weight weight = gson.fromJson(json, Weight.class);

            try {
                User user = getCorrectUserDataFromDatabase(weight.getUser());
                weight.setUser(user);

                if (authenticator.authenticate(credentials, weight.getUser())) {
                    weightRepository.update(weight);
                    return Response.status(200).entity("{\"message\":\"Weight updated\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400).entity("{\"message\":\"Failed to update weight, illegal json for weight\"}")
                .build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWeight(@PathParam("id") int id, @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetWeightByIdSpecification(id);
        try {
            Weight weight = weightRepository.query(specification).get(0);

            if (authenticator.authenticate(credentials, weight.getUser())) {
                weightRepository.remove(specification);
                return Response.status(200).entity("{\"message\":\"Weight removed\"}").build();
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
