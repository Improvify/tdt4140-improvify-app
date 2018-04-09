package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetServiceProviderPermissionByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.ServiceProviderPermissionsValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("serviceproviderpermissions")
public class ServiceProviderPermissionsResource {

    private final Gson gson;
    private final ServiceProviderPermissionsRepository repository;
    private final ServiceProviderPermissionsValidator validator;
    private final AuthBasicAuthenticator authenticator;

    @Inject
    public ServiceProviderPermissionsResource(ServiceProviderPermissionsRepository repository, Gson gson,
                                              ServiceProviderPermissionsValidator validator,
                                              AuthBasicAuthenticator authenticator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
        this.authenticator = authenticator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createServiceProviderPermissions(String json, @HeaderParam("Authorization") String credentials) {
        if (validator.validate(json)) {
            ServiceProviderPermissions serviceProviderPermissions = gson.fromJson(json,
                    ServiceProviderPermissions.class);

            if (authenticator.authenticate(credentials, serviceProviderPermissions.getUser())) {
                repository.add(serviceProviderPermissions);
                return Response.status(200).entity("Service provider permissions added").build();
            }
            return Response.status(401).entity("Authorization failed").build();
        }
        return Response.status(400).entity("Failed to add service provider permissions, illegal request").build();
    }

    @DELETE
    @Path("{uid}-{sid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteServiceProviderPermissions(@PathParam("uid") int uid, @PathParam("sid") int sid,
                                                     @HeaderParam("Authorization") String credentials) {
        Specification specification = new GetServiceProviderPermissionByIdSpecification(uid, sid);
        try {
            ServiceProviderPermissions serviceProviderPermissions = repository.query(specification).get(0);

            if (authenticator.authenticate(credentials, serviceProviderPermissions.getUser())) {
                repository.remove(specification);
                return Response.status(200).entity("Service provider permissions removed").build();
            }
            return Response.status(401).entity("Authorization failed").build();
        } catch (IndexOutOfBoundsException e) {
            // If goal with given id doesn't exist
            return Response.status(404).entity("Failed to remove service provider permissions, not found").build();
        }
    }
}
