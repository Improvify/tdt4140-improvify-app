package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByUsernameSpecification;
import tdt4140.gr1817.serviceprovider.webserver.security.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.security.PasswordHashUtil;
import tdt4140.gr1817.serviceprovider.webserver.validation.UserValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("user")
public class UserResource {

    private final Gson gson;
    private final UserRepository repository;
    private final UserValidator validator;
    private final AuthBasicAuthenticator authenticator;
    private final PasswordHashUtil passwordHashUtil;

    @Inject
    public UserResource(UserRepository repository, Gson gson, UserValidator validator,
                        AuthBasicAuthenticator authenticator, PasswordHashUtil passwordHashUtil) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
        this.passwordHashUtil = passwordHashUtil;
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username, @HeaderParam("Authorization") String credentials) {
        try {
            User user = repository.query(new GetUserByUsernameSpecification(username)).get(0);

            if (authenticator.authenticate(credentials, user)) {
                String json = gson.toJson(user);
                return Response.status(200).entity(json).build();
            }
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        } catch (RuntimeException e) {
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String json) {
        if (validator.validate(json)) {
            User user = gson.fromJson(json, User.class);
            // Hash the password
            user.setPassword(passwordHashUtil.hashPassword(user.getPassword()));
            try {
                repository.add(user);
                return Response.status(200).entity("{\"message\":\"User added\"}").build();
            } catch (RuntimeException e) {
                return Response.status(400).entity("{\"message\":\"Failed to add user, illegal request\"}").build();
            }
        }
        return Response.status(400).entity("{\"message\":\"Failed to add user, illegal json for user\"}").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            User user = gson.fromJson(json, User.class);

            try {
                user = repository.query(new GetUserByIdSpecification(user.getId())).get(0);

                if (authenticator.authenticate(credentials, user)) {
                    repository.update(user);
                    return Response.status(200).entity("{\"message\":\"User updated\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400).entity("{\"message\":\"Failed to update user, illegal json for user\"}").build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") int id, @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetUserByIdSpecification(id);
        try {
            User user = repository.query(specification).get(0);

            if (authenticator.authenticate(credentials, user)) {
                repository.remove(specification);
                return Response.status(200).entity("{\"message\":\"User removed\"}").build();
            }
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        } catch (RuntimeException e) {
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        }
    }
}
