package validation;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class GoalValidatorTest {

    private Validator validator = new GoalValidator();

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
    public void shouldLackRequiredFields() throws Exception {
        //Given
        String json = "{\"description\": \"Run 3 km in 10 minutes.\", \"isCompleted\": false, \"isCurrent\": true}";

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
    public void shouldHaveIllegalID() throws Exception {
        //Given
        String json = "{\"id\": -5, \"description\": \"Run 3 km in 10 minutes.\", \"isCompleted\": false, \"isCurrent\": true}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }
}