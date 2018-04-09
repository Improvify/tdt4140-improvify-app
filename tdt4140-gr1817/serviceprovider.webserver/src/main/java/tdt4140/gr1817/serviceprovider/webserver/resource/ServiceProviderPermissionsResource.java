package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetServiceProviderPermissionByIdSpecification;
import tdt4140.gr1817.serviceprovider.webserver.validation.ServiceProviderPermissionsValidator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("serviceproviderpermissions")
public class ServiceProviderPermissionsResource {

    private final Gson gson;
    private final ServiceProviderPermissionsRepository repository;
    private final ServiceProviderPermissionsValidator validator;

    @Inject
    public ServiceProviderPermissionsResource(ServiceProviderPermissionsRepository repository, Gson gson,
                                              ServiceProviderPermissionsValidator validator) {
        this.repository = repository;
        this.gson = gson;
        this.validator = validator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createServiceProviderPermissions(String json) {
        if (validator.validate(json)) {
            ServiceProviderPermissions serviceProviderPermissions = gson.fromJson(json,
                    ServiceProviderPermissions.class);

            repository.add(serviceProviderPermissions);
            return "Service provider permissions added";
        } else {
            return "Failed to add service provider permissions";
        }
    }

    @DELETE
    @Path("{uid},{sid}")
    public String deleteServiceProviderPermissions(@PathParam("uid") int uid, @PathParam("sid") int sid) {
        repository.remove(new GetServiceProviderPermissionByIdSpecification(uid, sid));
        return "Service provider permissions removed";
    }
}
