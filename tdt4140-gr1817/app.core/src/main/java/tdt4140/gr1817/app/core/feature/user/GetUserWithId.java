package tdt4140.gr1817.app.core.feature.user;

import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification
        .GetServiceProviderPermissionByUserAndServiceProviderSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class GetUserWithId {

    private final UserRepository userRepository;
    private HonorUserPermissions honorUserPermissions;
    private ServiceProvider improvifyServiceProvider;
    private ServiceProviderPermissionsRepository serviceProviderPermissionsRepository;

    @Inject
    public GetUserWithId(UserRepository userRepository, HonorUserPermissions honorUserPermissions,
                         @Named("improvify") ServiceProvider improvifyServiceProvider,
                         ServiceProviderPermissionsRepository serviceProviderPermissionsRepository) {
        this.userRepository = userRepository;
        this.honorUserPermissions = honorUserPermissions;
        this.improvifyServiceProvider = improvifyServiceProvider;
        this.serviceProviderPermissionsRepository = serviceProviderPermissionsRepository;
    }

    /**
     * Returns the user with the specified id.
     *
     * @param id the users id
     * @return the {@link User}, or {@code null} if none was found
     */
    public User getUserWithId(int id) {
        List<User> result = userRepository.query(new GetUserByIdSpecification(id));
        if (result.isEmpty()) {
            return null;
        }

        User dishonoredUser = result.get(0);

        ServiceProviderPermissions permissions = serviceProviderPermissionsRepository.query(new
                GetServiceProviderPermissionByUserAndServiceProviderSpecification(id, improvifyServiceProvider.getId()
        )).get(0);
        return honorUserPermissions.honorUserPermissions(dishonoredUser, permissions);
    }

}
