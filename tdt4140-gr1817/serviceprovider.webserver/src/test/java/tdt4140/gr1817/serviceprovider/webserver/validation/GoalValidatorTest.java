package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class GoalValidatorTest {

    private GoalValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new GoalValidator(new Gson());
    }

    @Test
    public void shouldBeLegalGoal() throws Exception {
        //Given
        String json = "{\"id\": 1, \"description\": \"Run 3 km in 10 minutes.\", \"isCompleted\": false, \"isCurrent\": true}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldNotRequireId() throws Exception {
        //Given
        String json = "{\"description\": \"Run 3 km in 10 minutes.\", \"isCompleted\": false, \"isCurrent\": true}";

        //When
        boolean outcome = validator.validate(json);

        //Then
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

    @Test
    public void shouldIgnoreIllegalID() throws Exception {
        //Given
        String json = "{\"id\": -5, \"description\": \"Run 3 km in 10 minutes.\", \"isCompleted\": false, \"isCurrent\": true}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }
}