package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RestingHeartRateValidatorTest {

    private RestingHeartRateValidator validator;

    @Before
    public void setUp() throws Exception {
        final Gson gson = new Gson();
        validator = new RestingHeartRateValidator(gson);
    }

    @Test
    public void shouldBeLegalRestingHeartRate() throws Exception {
        //Given
        String json = "{\"id\": 1, \"measuredAt\": \"2018-02-15\", \"heartRate\": 66}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldLackRequiredFields() throws Exception {
        //Given
        String json = "{\"heartRate\": 66}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
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

    @Test
    public void shouldHaveIllegalDate() throws Exception {
        //Given
        String json = "{\"id\": 1, \"measuredAt\": \"3018-02-15\", \"heartRate\": 66}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldIgnoreIllegalID() throws Exception {
        //Given
        String json = "{\"id\": -2, \"measuredAt\": \"2018-02-15\", \"heartRate\": 66}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldHaveIllegalHeartRate() throws Exception {
        //Given
        String json = "{\"id\": 1, \"measuredAt\": \"2018-02-15\", \"heartRate\": -1}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }
}