package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class WeightValidatorTest {

    private WeightValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new WeightValidator(new Gson());
    }

    @Test
    public void shouldBeLegalWeight() throws Exception {
        //Given
        String json = "{\"id\": 1, \"currentWeight\": 88, \"date\": \"2018-01-09\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldLackRequiredFields() throws Exception {
        //Given
        String json = "{\"id\": 1, \"date\": \"2018-01-09\"}";

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
    public void shouldIgnoreIllegalID() throws Exception {
        //Given
        String json = "{\"id\": -1, \"currentWeight\": 88, \"date\": \"2018-01-09\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldHaveIllegalWeight() throws Exception {
        //Given
        String json = "{\"id\": 1, \"currentWeight\": -10, \"date\": \"2018-01-09\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldHaveIllegalDate() throws Exception {
        //Given
        String json = "{\"id\": 1, \"currentWeight\": 10, \"date\": \"4000-04-04\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }
}