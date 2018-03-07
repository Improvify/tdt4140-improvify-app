package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.RestingHeartRateValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
        RestingHeartRate restingHeartRate = gson.fromJson(json, RestingHeartRate.class);
        if (validator.validate(json)) {
            repository.add(restingHeartRate);
            return "Resting heart rate added";
        } else {
            return "Failed to add resting heart rate";
        }
    }
}
