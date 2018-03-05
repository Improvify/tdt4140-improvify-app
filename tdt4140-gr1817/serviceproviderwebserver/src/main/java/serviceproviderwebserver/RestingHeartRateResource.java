package serviceproviderwebserver;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Slf4j
@Path("restingheartrate")
public class RestingHeartRateResource {
    RestingHeartRateRepository repository;
    private Gson gson;

    public class RestingHeartRate {
        int heartRateValue;
        Date date;
    }

    @Inject
    public RestingHeartRateResource(RestingHeartRateRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRestingHeartRate(String json) {
        //serviceproviderwebserver.WeightResource.Weight weight = gson.fromJson(json, serviceproviderwebserver.WeightResource.Weight.class);
        return json;
    }
}
