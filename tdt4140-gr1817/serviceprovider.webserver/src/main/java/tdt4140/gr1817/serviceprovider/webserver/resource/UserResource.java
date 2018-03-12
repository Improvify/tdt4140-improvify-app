package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.UserValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("user")
public class UserResource {

    private Gson gson;
    private final UserRepository repository;
    private final UserValidator validator;

    @Inject
    public UserResource(UserRepository repository, Gson gson, UserValidator validator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createUser(String json) {
        if (validator.validate(json)) {
            User user = gson.fromJson(json, User.class);
            repository.add(user);
            return "User added";
        } else {
            return "Failed to add user";
        }
    }
}
