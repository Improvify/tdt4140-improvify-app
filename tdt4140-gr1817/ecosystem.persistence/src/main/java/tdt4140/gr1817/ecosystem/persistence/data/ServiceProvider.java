package tdt4140.gr1817.ecosystem.persistence.data;

import lombok.Data;
import lombok.NonNull;

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
    private @NonNull String name;
}
