package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.WeightValidator;
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

public class WeightResourceTest {
    private WeightRepository weightRepository;
    private Gson gson = new Gson();
    private WeightResource weightResource;

    @Before
    public void setUp() throws Exception {
        weightRepository = Mockito.mock(WeightRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        final Weight weight = createWeight();
        when(weightRepository.query(Mockito.any())).thenReturn(Collections.singletonList(weight));
        when(userRepository.query(Mockito.any())).thenReturn(Collections.singletonList(weight.getUser()));

        final WeightValidator validator = new WeightValidator(gson);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator();
        weightResource = new WeightResource(weightRepository, userRepository, gson, validator, authenticator);
    }

    @Test
    public void shouldGetWeights() throws Exception {
        // Given
        String username = "test";

        // When
        final Response response = weightResource.getWeights(username, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(weightRepository).query(any(Specification.class));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldNotGetWeightsWhenWrongAuthorization() {
        // Given
        String username = "test";

        // When
        final Response response = weightResource.getWeights(username, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldNotGetWeightsWhenIllegalHeader() {
        // Given
        String username = "test";

        // When
        final Response response = weightResource.getWeights(username, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldAddWeight() throws Exception {
        // Given
        Weight weight = createWeight();
        String json = gson.toJson(weight);

        // When
        final Response response = weightResource.createWeight(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(weightRepository).add(Mockito.eq(weight));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldNotAddWhenInvalidWeight() throws Exception {
        // Given
        Weight weight = createWeight();
        String invalidJson = weight.toString();

        // When
        final Response response = weightResource.createWeight(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(400));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldAddWeightWhenWrongAuthorization() throws Exception {
        // Given
        Weight weight = createWeight();
        String json = gson.toJson(weight);

        // When
        final Response response = weightResource.createWeight(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldNotAddWeightWhenIllegalHeader() {
        // Given
        Weight weight = createWeight();
        String json = gson.toJson(weight);

        // When
        final Response response = weightResource.createWeight(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldUpdateWeight() throws Exception {
        // Given
        Weight weight = createWeight();
        String json = gson.toJson(weight);

        // When
        final Response response = weightResource.updateWeight(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(weightRepository).update(Mockito.eq(weight));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldNotUpdateWhenInvalidWeight() throws Exception {
        // Given
        Weight weight = createWeight();
        String invalidJson = weight.toString();

        // When
        final Response response = weightResource.updateWeight(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(400));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldNotUpdateWeightWhenWrongAuthorization() throws Exception {
        // Given
        Weight weight = createWeight();
        String json = gson.toJson(weight);

        // When
        final Response response = weightResource.updateWeight(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldNotUpdateWeightWhenIllegalHeader() {
        // Given
        Weight weight = createWeight();
        String json = gson.toJson(weight);

        // When
        final Response response = weightResource.updateWeight(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldRemoveWeight() {
        // Given
        int id = 1;

        // When
        final Response response = weightResource.deleteWeight(id, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(weightRepository).query(any(Specification.class));
        verify(weightRepository).remove(any(Specification.class));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldNotRemoveWeightWhenWrongAuthorization() {
        // Given
        int id = 1;

        // When
        final Response response = weightResource.deleteWeight(id, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(weightRepository).query(any(Specification.class));
        verifyNoMoreInteractions(weightRepository);
    }

    @Test
    public void shouldNotRemoveWeightWhenIllegalHeader() {
        // Given
        int id = 1;

        // When
        final Response response = weightResource.deleteWeight(id, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(weightRepository).query(any(Specification.class));
        verifyNoMoreInteractions(weightRepository);
    }

    private static Weight createWeight() {
        Calendar calendar = new GregorianCalendar(2000, 1, 1);
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
        return new Weight(1, 140.4f, date, user);
    }
}
