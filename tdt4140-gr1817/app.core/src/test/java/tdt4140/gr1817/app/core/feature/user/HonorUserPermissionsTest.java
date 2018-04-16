package tdt4140.gr1817.app.core.feature.user;

import javafx.util.BuilderFactory;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class HonorUserPermissionsTest {


    private UserRepository userRepositoryMock;
    private HonorUserPermissions honorUserPermissionsMock;
    private ServiceProvider serviceProviderMock;
    private ServiceProviderPermissions serviceProviderPermissions;

    @Before
    public void setUp() throws Exception {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        honorUserPermissionsMock = Mockito.mock(HonorUserPermissions.class);
        serviceProviderMock = Mockito.mock(ServiceProvider.class);


    }

    @Test
    public void shouldSetAllFieldsToNull() throws Exception {
        // Given
        User user = new User(5, "Navn", "Navnesen", 5f, new Date(), "Navniboi",
                "sikkertpassord", "navn@navnesen.no");
        HonorUserPermissions honorUserPermissions = new HonorUserPermissions();
        serviceProviderPermissions = new ServiceProviderPermissions(user, serviceProviderMock, false,
                false, false,
                false, false, false, false, false);


        // When
        User user1 = honorUserPermissions.honorUserPermissions(user, serviceProviderPermissions);

        // Then

        assertNull(user1.getFirstName());
        assertNull(user1.getLastName());
        assertNull(user1.getEmail());
        assertNull(user1.getBirthDate());
        assertNull(user1.getUsername());
        assertThat(user1.getHeight(), is(-1f));


    }


}