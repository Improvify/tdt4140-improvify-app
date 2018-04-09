package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ServiceProviderPermissionsValidatorTest {
    private ServiceProviderPermissionsValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new ServiceProviderPermissionsValidator(new Gson());
    }

    @Test
    public void shouldBeLegalPermissions() throws Exception {
        // Given
        String json = "{\n" +
                "  \"weight\": true, \"height\": true,\n" +
                "  \"email\": false, \"name\": false,\n" +
                "  \"username\": false, \"restingHeartRate\": true,\n" +
                "  \"workoutSession\": true, \"birthDate\" : true\n" +
                "}";

        // When
        boolean outcome = validator.validate(json);

        // Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldBeMalformedJson() throws Exception {
        //Given
        String json = "(\"id\": 2)";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }
}