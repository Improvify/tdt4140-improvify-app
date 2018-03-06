package serviceproviderwebserver;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("user")
public class UserResource {
    UserRepository repository;
    private Gson gson;

    /*public class User {
        public int id;
        public String firstName;
        public String lastName;
        public float height;
        public Date birthDate;
        public String username;
        public String password;
        public String email;
    }*/

    @Inject
    public UserResource(UserRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createUser(String json) {
        User user = gson.fromJson(json, User.class);
        //UserValidator validator = new UserValidator();
        //validator.validate(json);
        repository.add(user);
        return "User added";
    }
}
