package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllServiceProviderPermissionsForUserSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetServiceProviderPermissionByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByUsernameSpecification;
import tdt4140.gr1817.serviceprovider.webserver.security.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.ServiceProviderPermissionsValidator;

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
import java.util.List;

@Slf4j
@Path("serviceproviderpermissions")
public class ServiceProviderPermissionsResource {

    private final Gson gson;
    private final ServiceProviderPermissionsRepository permissionsRepository;
    private final UserRepository userRepository;
    private final ServiceProviderPermissionsValidator validator;
    private final AuthBasicAuthenticator authenticator;

    @Inject
    public ServiceProviderPermissionsResource(ServiceProviderPermissionsRepository permissionsRepository,
                                              UserRepository userRepository, Gson gson,
                                              ServiceProviderPermissionsValidator validator,
                                              AuthBasicAuthenticator authenticator) {
        this.permissionsRepository = permissionsRepository;
        this.userRepository = userRepository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServiceProviderPermissions(@PathParam("username") String username,
                                                  @HeaderParam("Authorization") String credentials) {
        try {
            User user = userRepository.query(new GetUserByUsernameSpecification(username)).get(0);

            if (authenticator.authenticate(credentials, user)) {
                final List<ServiceProviderPermissions> permissions = permissionsRepository
                        .query(new GetAllServiceProviderPermissionsForUserSpecification(user.getId()));
                String json = gson.toJson(permissions);
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
    public Response createServiceProviderPermissions(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            ServiceProviderPermissions serviceProviderPermissions = gson.fromJson(json,
                    ServiceProviderPermissions.class);

            try {
                User user = getCorrectUserDataFromDatabase(serviceProviderPermissions.getUser());
                serviceProviderPermissions.setUser(user);

                if (authenticator.authenticate(credentials, serviceProviderPermissions.getUser())) {
                    permissionsRepository.add(serviceProviderPermissions);
                    return Response.status(200).entity("{\"message\":\"Service provider permissions added\"}").build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400)
                .entity("{\"message\":\"Failed to add service provider permissions, illegal json for permissions\"}")
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateServiceProviderPermissions(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            ServiceProviderPermissions serviceProviderPermissions = gson.fromJson(json,
                    ServiceProviderPermissions.class);

            try {
                User user = getCorrectUserDataFromDatabase(serviceProviderPermissions.getUser());
                serviceProviderPermissions.setUser(user);

                if (authenticator.authenticate(credentials, serviceProviderPermissions.getUser())) {
                    permissionsRepository.update(serviceProviderPermissions);
                    return Response.status(200).entity("{\"message\":\"Service provider permissions updated\"}")
                            .build();
                }
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            } catch (RuntimeException e) {
                return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
            }
        }
        return Response.status(400)
                .entity("{\"message\":\"Failed to update service provider permissions, illegal json for permissions\"}")
                .build();
    }

    @DELETE
    @Path("{uid}-{sid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteServiceProviderPermissions(@PathParam("uid") int uid, @PathParam("sid") int sid,
                                                     @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetServiceProviderPermissionByIdSpecification(uid, sid);
        try {
            ServiceProviderPermissions serviceProviderPermissions = permissionsRepository.query(specification).get(0);

            if (authenticator.authenticate(credentials, serviceProviderPermissions.getUser())) {
                permissionsRepository.remove(specification);
                return Response.status(200).entity("{\"message\":\"Service provider permissions removed\"}").build();
            }
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        } catch (RuntimeException e) {
            return Response.status(401).entity("{\"message\":\"Authorization failed\"}").build();
        }
    }

    private User getCorrectUserDataFromDatabase(User user) {
        Specification specification = new GetUserByIdSpecification(user.getId());
        return userRepository.query(specification).get(0);
    }
}
