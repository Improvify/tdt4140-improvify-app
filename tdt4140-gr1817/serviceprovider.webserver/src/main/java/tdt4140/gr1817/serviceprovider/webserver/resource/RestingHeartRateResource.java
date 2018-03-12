package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.RestingHeartRateValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("restingheartrate")
public class RestingHeartRateResource {

    private final Gson gson;
    private final RestingHeartRateRepository repository;
    private final RestingHeartRateValidator validator;

    @Inject
    public RestingHeartRateResource(RestingHeartRateRepository repository, Gson gson,
                                    RestingHeartRateValidator validator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRestingHeartRate(String json) {
        if (validator.validate(json)) {
            RestingHeartRate restingHeartRate = gson.fromJson(json, RestingHeartRate.class);
            repository.add(restingHeartRate);
            return "Resting heart rate added";
        } else {
            return "Failed to add resting heart rate";
        }
    }

    @DELETE
    @Path("{id}")
    public String deleteGoal(@PathParam("id") int id) {
        repository.remove(new GetRestingHeartRateByIdSpecification(id));
        return "Resting heart rate removed";
    }
}
