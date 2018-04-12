package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.ServiceProviderPermissionsValidator;
import tdt4140.gr1817.serviceprovider.webserver.validation.util.AuthBasicUtil;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ServiceProviderPermissionsResourceTest {

    private ServiceProviderPermissionsRepository permissionsRepository;
    private Gson gson = new Gson();
    private ServiceProviderPermissionsResource resource;

    @Before
    public void setUp() throws Exception {
        permissionsRepository = Mockito.mock(ServiceProviderPermissionsRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        final ServiceProviderPermissions permissions = createServiceProviderPermissions();
        when(permissionsRepository.query(Mockito.any())).thenReturn(Collections.singletonList(permissions));
        when(userRepository.query(Mockito.any())).thenReturn(Collections.singletonList(permissions.getUser()));

        final ServiceProviderPermissionsValidator validator = new ServiceProviderPermissionsValidator(gson);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator();
        resource = new ServiceProviderPermissionsResource(permissionsRepository, userRepository, gson,
                validator, authenticator);
    }

    @Test
    public void shouldAddServiceProviderPermissions() throws Exception {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String json = gson.toJson(ServiceProviderPermissions);

        // When
        resource.createServiceProviderPermissions(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(permissionsRepository).add(Mockito.eq(ServiceProviderPermissions));
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldNotAddWhenInvalidServiceProviderPermissions() throws Exception {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String invalidJson = ServiceProviderPermissions.toString();

        // When
        resource.createServiceProviderPermissions(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldNotAddServiceProviderPermissionsWhenWrongAuthorization() throws Exception {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String json = gson.toJson(ServiceProviderPermissions);

        // When
        resource.createServiceProviderPermissions(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldNotAddServiceProviderPermissionsWhenIllegalHeader() {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String json = gson.toJson(ServiceProviderPermissions);

        // When
        resource.createServiceProviderPermissions(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldUpdateServiceProviderPermissions() throws Exception {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String json = gson.toJson(ServiceProviderPermissions);

        // When
        resource.updateServiceProviderPermissions(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(permissionsRepository).update(Mockito.eq(ServiceProviderPermissions));
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldNotUpdateWhenInvalidServiceProviderPermissions() throws Exception {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String invalidJson = ServiceProviderPermissions.toString();

        // When
        resource.updateServiceProviderPermissions(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldNotUpdateServiceProviderPermissionsWhenWrongAuthorization() throws Exception {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String json = gson.toJson(ServiceProviderPermissions);

        // When
        resource.updateServiceProviderPermissions(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldNotUpdateServiceProviderPermissionsWhenIllegalHeader() {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String json = gson.toJson(ServiceProviderPermissions);

        // When
        resource.updateServiceProviderPermissions(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldRemoveServiceProviderPermissions() {
        // Given
        int uid = 1, sid = 1;

        // When
        resource.deleteServiceProviderPermissions(uid, sid, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(permissionsRepository).query(any(Specification.class));
        verify(permissionsRepository).remove(any(Specification.class));
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldNotRemoveServiceProviderPermissionsWhenWrongAuthorization() {
        // Given
        int uid = 1, sid = 1;

        // When
        resource.deleteServiceProviderPermissions(uid, sid, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verify(permissionsRepository).query(any(Specification.class));
        verifyNoMoreInteractions(permissionsRepository);
    }

    @Test
    public void shouldNotRemoveServiceProviderPermissionsWhenIllegalHeader() {
        // Given
        int uid = 1, sid = 1;

        // When
        resource.deleteServiceProviderPermissions(uid, sid, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        verify(permissionsRepository).query(any(Specification.class));
        verifyNoMoreInteractions(permissionsRepository);
    }

    private static ServiceProviderPermissions createServiceProviderPermissions() {
        Calendar calendar = new GregorianCalendar(2000, 1, 1);
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
        ServiceProvider serviceProvider = new ServiceProvider(1, "test");
        return new ServiceProviderPermissions(user, serviceProvider);
    }
}
