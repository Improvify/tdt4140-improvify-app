package serviceproviderwebserver;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("restingheartrate")
public class RestingHeartRateResource {
    RestingHeartRateRepository repository;
    private Gson gson;

    @Inject
    public RestingHeartRateResource(RestingHeartRateRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRestingHeartRate(String json) {
        RestingHeartRate weight = gson.fromJson(json, RestingHeartRate.class);
        //RestingHeartRateValidator validator = new RestingHeartRateValidator();
        //validator.validate(json);
        return "Resting heart rate added";
    }
}
