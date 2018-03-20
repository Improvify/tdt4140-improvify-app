package tdt4140.gr1817.ecosystem.persistence.data;

import org.junit.Test;

import java.util.Date;

public class ServiceProviderPermissionsTest {


    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowOnNullUserAndService() throws Exception {
        new ServiceProviderPermissions(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowOnNullUser() throws Exception {
        ServiceProvider serviceProvider = new ServiceProvider(1, "");
        new ServiceProviderPermissions(null, serviceProvider);
    }

    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowOnNullService() throws Exception {
        User user = createUser();
        new ServiceProviderPermissions(user, null);
    }

    private static User createUser() {
        return new User(0, "", "", 0, new Date(), "", "", "");
    }
}