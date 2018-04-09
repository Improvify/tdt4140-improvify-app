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

    private ServiceProviderPermissionsRepository rep;
    private Gson gson = new Gson();
    private ServiceProviderPermissionsResource resource;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(ServiceProviderPermissionsRepository.class);

        final ServiceProviderPermissions permissions = createServiceProviderPermissions();
        when(rep.query(Mockito.any())).thenReturn(Collections.singletonList(permissions));

        final ServiceProviderPermissionsValidator validator = new ServiceProviderPermissionsValidator(gson);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator();
        resource = new ServiceProviderPermissionsResource(rep, gson, validator, authenticator);
    }

    @Test
    public void shouldAddServiceProviderPermissions() throws Exception {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String json = gson.toJson(ServiceProviderPermissions);

        // When
        resource.createServiceProviderPermissions(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(rep).add(Mockito.eq(ServiceProviderPermissions));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotAddWhenInvalidServiceProviderPermissions() throws Exception {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String invalidJson = ServiceProviderPermissions.toString();

        // When
        resource.createServiceProviderPermissions(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotAddServiceProviderPermissionsWhenWrongAuthorization() throws Exception {
        // Given
        ServiceProviderPermissions ServiceProviderPermissions = createServiceProviderPermissions();
        String json = gson.toJson(ServiceProviderPermissions);

        // When
        resource.createServiceProviderPermissions(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldRemoveServiceProviderPermissions() {
        // Given
        int uid = 1, sid = 1;

        // When
        resource.deleteServiceProviderPermissions(uid, sid, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(rep).query(any(Specification.class));
        verify(rep).remove(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotRemoveServiceProviderPermissionsWhenWrongAuthorization() {
        // Given
        int uid = 1, sid = 1;

        // When
        resource.deleteServiceProviderPermissions(uid, sid, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verify(rep).query(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    private static ServiceProviderPermissions createServiceProviderPermissions() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
        ServiceProvider serviceProvider = new ServiceProvider(1, "test");
        return new ServiceProviderPermissions(user, serviceProvider);
    }
}
