package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.WeightValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("weight")
public class WeightResource {
    WeightRepository repository;
    private Gson gson;

    @Inject
    public WeightResource(WeightRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createWeight(String json) {
        Weight weight = gson.fromJson(json, Weight.class);
        WeightValidator validator = new WeightValidator();
        if (validator.validate(json)) {
            repository.add(weight);
            return "Weight added";
        } else {
            return "Failed to add weight";
        }
    }
}
