package serviceproviderwebserver;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import java.util.Date;

@Path("user")
public class UserResource{
    UserRepository repository;
    private Gson gson;

    static class User {
        public int id;
        public String firstName;
        public String lastName;
        public float height;
        public Date birthDate;
        public String username;
        public String password;
        public String email;
    }

    @Inject
    public UserResource(UserRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(String json) {
        User user = gson.fromJson(json, User.class);
        System.out.println(user.username);
        /*
        user.password
        Gson gson = new Gson();

        gson.fromJson(json, User.class)
        System.out.println(currentWeight);

        repository.add(new User(0, "name", "lastname", 19.0f, new Date(), "kek", "123", "heh@ok.com"));

        return currentWeight;
*/
    }
}
