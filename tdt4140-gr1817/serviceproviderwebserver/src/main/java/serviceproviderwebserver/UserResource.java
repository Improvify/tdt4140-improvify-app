package serviceproviderwebserver;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import java.util.Date;

@Slf4j
@Path("user")
public class UserResource{
    UserRepository repository;
    private Gson gson;

    public class User {
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createUser(String json) {
        //tdt4140.gr1817.ecosystem.persistence.data.User user = gson.fromJson(json, tdt4140.gr1817.ecosystem.persistence.data.User.class);
        //repository.add(user);
        //return "304 OK";
        return json;
    }
}
