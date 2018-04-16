package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.serviceprovider.webserver.security.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.security.PasswordHashUtil;
import tdt4140.gr1817.serviceprovider.webserver.validation.RestingHeartRateValidator;
import tdt4140.gr1817.serviceprovider.webserver.validation.util.AuthBasicUtil;

import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RestingHeartRateResourceTest {
    private RestingHeartRateRepository restingHeartRateRepository;
    private Gson gson = new Gson();
    private RestingHeartRateResource restingHeartRateResource;

    @Before
    public void setUp() throws Exception {
        restingHeartRateRepository = Mockito.mock(RestingHeartRateRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        final RestingHeartRate restingHeartRate = createRestingHeartRate();
        when(restingHeartRateRepository.query(Mockito.any()))
                .thenReturn(Collections.singletonList(restingHeartRate));
        when(userRepository.query(Mockito.any()))
                .thenReturn(Collections.singletonList(restingHeartRate.getMeasuredBy()));

        final RestingHeartRateValidator validator = new RestingHeartRateValidator(gson);

        final PasswordHashUtil passwordHashUtilMock = Mockito.mock(PasswordHashUtil.class);
        when(passwordHashUtilMock.validatePassword(any(String.class), any(String.class))).thenReturn(true);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator(passwordHashUtilMock);
        restingHeartRateResource = new RestingHeartRateResource(restingHeartRateRepository, userRepository, gson,
                validator, authenticator);
    }

    @Test
    public void shouldGetRestingHeartRates() throws Exception {
        // Given
        String username = "test";

        // When
        final Response response = restingHeartRateResource
                .getRestingHeartRates(username, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(restingHeartRateRepository).query(any(Specification.class));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotGetRestingHeartRatesWhenWrongAuthorization() {
        // Given
        String username = "test";

        // When
        final Response response = restingHeartRateResource.getRestingHeartRates(username, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotGetRestingHeartRatesWhenIllegalHeader() {
        // Given
        String username = "test";

        // When
        final Response response = restingHeartRateResource.getRestingHeartRates(username, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldAddRestingHeartRate() throws Exception {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String json = gson.toJson(restingHeartRate);

        // When
        final Response response = restingHeartRateResource.createRestingHeartRate(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(restingHeartRateRepository).add(Mockito.eq(restingHeartRate));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotAddWhenInvalidRestingHeartRate() throws Exception {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String invalidJson = restingHeartRate.toString();

        // When
        final Response response = restingHeartRateResource.createRestingHeartRate(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(400));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotAddRestingHeartRateWhenWrongAuthorization() throws Exception {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String json = gson.toJson(restingHeartRate);

        // When
        final Response response = restingHeartRateResource.createRestingHeartRate(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotAddRestingHeartRateWhenIllegalHeader() {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String json = gson.toJson(restingHeartRate);

        // When
        final Response response = restingHeartRateResource.createRestingHeartRate(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldUpdateRestingHeartRate() throws Exception {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String json = gson.toJson(restingHeartRate);

        // When
        final Response response = restingHeartRateResource.updateRestingHeartRate(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(restingHeartRateRepository).update(Mockito.eq(restingHeartRate));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotUpdateWhenInvalidRestingHeartRate() throws Exception {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String invalidJson = restingHeartRate.toString();

        // When
        final Response response = restingHeartRateResource.updateRestingHeartRate(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(400));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotUpdateRestingHeartRateWhenWrongAuthorization() throws Exception {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String json = gson.toJson(restingHeartRate);

        // When
        final Response response = restingHeartRateResource.updateRestingHeartRate(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotUpdateRestingHeartRateWhenIllegalHeader() {
        // Given
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String json = gson.toJson(restingHeartRate);

        // When
        final Response response = restingHeartRateResource.updateRestingHeartRate(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldRemoveRestingHeartRate() {
        // Given
        int id = 1;

        // When
        final Response response = restingHeartRateResource.deleteRestingHeartRate(id, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(restingHeartRateRepository).query(any(Specification.class));
        verify(restingHeartRateRepository).remove(any(Specification.class));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotRemoveRestingHeartRateWhenWrongAuthorization() {
        // Given
        int id = 1;

        // When
        final Response response = restingHeartRateResource.deleteRestingHeartRate(id, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(restingHeartRateRepository).query(any(Specification.class));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    @Test
    public void shouldNotRemoveRestingHeartRateWhenIllegalHeader() {
        // Given
        int id = 1;

        // When
        final Response response = restingHeartRateResource.deleteRestingHeartRate(id, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(restingHeartRateRepository).query(any(Specification.class));
        verifyNoMoreInteractions(restingHeartRateRepository);
    }

    private static RestingHeartRate createRestingHeartRate() {
        Calendar calendar = new GregorianCalendar(2000, 1, 1);
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
        return new RestingHeartRate(1, date, 88, user);
    }
}
