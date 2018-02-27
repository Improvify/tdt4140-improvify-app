package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.Data;
import tdt4140.gr1817.ecosystem.persistence.data.User;

/**
 * A single service provider.
 *
 * <p>A {@link User} can give permissions to a service provider,
 * allowing the service provider to see certain data about the user.
 * </p>
 *
 * @see ServiceProviderPermissions
 * @see User
 */
@Data
public class ServiceProvider {
    private int id;
    private String name;
}
