package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.RestingHeartRateValidator;
import tdt4140.gr1817.serviceprovider.webserver.validation.util.AuthBasicUtil;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RestingHeartRateResourceTest {
    private RestingHeartRateRepository rep;
    private Gson gson = new Gson();
    private RestingHeartRateResource restingHeartRateResource;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(RestingHeartRateRepository.class);

        RestingHeartRate restingHeartRate = createRestingHeartRate();
        when(rep.query(Mockito.any())).thenReturn(Collections.singletonList(restingHeartRate));

        final RestingHeartRateValidator validator = new RestingHeartRateValidator(gson);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator();
        restingHeartRateResource = new RestingHeartRateResource(rep, gson, validator, authenticator);
    }

    @Test
    public void shouldAddRestingHeartRate() throws Exception {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String json = gson.toJson(restingHeartRate);

        // When
        restingHeartRateResource.createRestingHeartRate(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(rep).add(Mockito.eq(restingHeartRate));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotAddWhenInvalidRestingHeartRate() throws Exception {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String invalidJson = restingHeartRate.toString();

        // When
        restingHeartRateResource.createRestingHeartRate(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotAddRestingHeartRateWhenWrongAuthorization() throws Exception {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String json = gson.toJson(restingHeartRate);

        // When
        restingHeartRateResource.createRestingHeartRate(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldRemoveRestingHeartRate() {
        // Given
        int id = 1;

        // When
        restingHeartRateResource.deleteRestingHeartRate(id, AuthBasicUtil.HEADER_TEST_123);

        // Then
        verify(rep).query(any(Specification.class));
        verify(rep).remove(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotRemoveGoalWhenWrongAuthorization() {
        // Given
        int id = 1;

        // When
        restingHeartRateResource.deleteRestingHeartRate(id, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        verify(rep).query(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    private static RestingHeartRate createRestingHeartRate() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
        return new RestingHeartRate(1, date, 88, user);
    }
}
