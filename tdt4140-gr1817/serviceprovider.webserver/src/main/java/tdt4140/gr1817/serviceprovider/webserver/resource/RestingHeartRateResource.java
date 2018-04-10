package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.RestingHeartRateValidator;

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
@Path("restingheartrate")
public class RestingHeartRateResource {

    private final Gson gson;
    private final RestingHeartRateRepository repository;
    private final RestingHeartRateValidator validator;
    private final AuthBasicAuthenticator authenticator;

    @Inject
    public RestingHeartRateResource(RestingHeartRateRepository repository, Gson gson,
                                    RestingHeartRateValidator validator, AuthBasicAuthenticator authenticator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createRestingHeartRate(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            RestingHeartRate restingHeartRate = gson.fromJson(json, RestingHeartRate.class);

            if (authenticator.authenticate(credentials, restingHeartRate.getMeasuredBy())) {
                repository.add(restingHeartRate);
                return Response.status(200).entity("Resting heart rate added").build();
            }
            return Response.status(401).entity("Authorization failed").build();
        }
        return Response.status(400).entity("Failed to add resting heart rate, illegal request").build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteRestingHeartRate(@PathParam("id") int id, @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetRestingHeartRateByIdSpecification(id);
        try {
            RestingHeartRate restingHeartRate = repository.query(specification).get(0);

            if (authenticator.authenticate(credentials, restingHeartRate.getMeasuredBy())) {
                repository.remove(specification);
                return Response.status(200).entity("Resting heart rate removed").build();
            }
            return Response.status(401).entity("Authorization failed").build();
        } catch (IndexOutOfBoundsException e) {
            // If rate with given id doesn't exist
            return Response.status(404).entity("Failed to remove resting heart rate, not found").build();
        }
    }
}
