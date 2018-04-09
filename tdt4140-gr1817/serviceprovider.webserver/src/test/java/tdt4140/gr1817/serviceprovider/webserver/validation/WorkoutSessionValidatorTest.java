package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class WorkoutSessionValidatorTest {

    private WorkoutSessionValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new WorkoutSessionValidator(new Gson());
    }

    @Test
    public void shouldBeLegalWorkoutSession() throws Exception {
        //Given
        String json = "{\"id\": 1, \"startTime\": \"2018-01-09T13:04:23.000Z\", \"intensity\": 6, \"kiloCalories\": 451.5, \"averageHeartRate\": 155, \"maxHeartRate\": 177, \"distanceRun\": 7.8, \"durationSeconds\": 3600}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldLackRequiredFields() throws Exception {
        //Given
        String json = "{\"id\": 1, \"intensity\": 6, \"kiloCalories\": 451.5, \"averageHeartRate\": 155, \"maxHeartRate\": 177, \"distanceRun\": 7.8, \"durationSeconds\": 3600}";

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
        String json = "{\"id\": -1, \"startTime\": \"2018-01-09T13:04:23.000Z\", \"intensity\": 6, \"kiloCalories\": 451.5, \"averageHeartRate\": 155, \"maxHeartRate\": 177, \"distanceRun\": 7.8, \"durationSeconds\": 3600}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldHaveIllegalTime() throws Exception {
        //Given
        String json = "{\"id\": 1, \"startTime\": \"3018-01-09T13:04:23.000Z\", \"intensity\": 6, \"kiloCalories\": 451.5, \"averageHeartRate\": 155, \"maxHeartRate\": 177, \"distanceRun\": 7.8, \"durationSeconds\": 3600}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldHaveIllegalIntensity() throws Exception {
        //Given
        String tooLowIntensityJson = "{\"id\": 1, \"startTime\": \"2018-01-09T13:04:23.000Z\", \"intensity\": -4, \"kiloCalories\": 451.5, \"averageHeartRate\": 155, \"maxHeartRate\": 177, \"distanceRun\": 7.8, \"durationSeconds\": 3600}";
        String tooBigIntensityJson = "{\"id\": 1, \"startTime\": \"2018-01-09T13:04:23.000Z\", \"intensity\": 9000, \"kiloCalories\": 451.5, \"averageHeartRate\": 155, \"maxHeartRate\": 177, \"distanceRun\": 7.8, \"durationSeconds\": 3600}";

        //When
        boolean outcomeTooLowIntensity = validator.validate(tooLowIntensityJson);
        boolean outcomeTooBigIntensity = validator.validate(tooBigIntensityJson);

        //Then
        assertThat(outcomeTooLowIntensity, is(false));
        assertThat(outcomeTooBigIntensity, is(false));
    }

    @Test
    public void shouldHaveIllegalKcal() throws Exception {
        //Given
        String json = "{\"id\": 1, \"startTime\": \"2018-01-09T13:04:23.000Z\", \"intensity\": 6, \"kiloCalories\": -1, \"averageHeartRate\": 155, \"maxHeartRate\": 177, \"distanceRun\": 7.8, \"durationSeconds\": 3600}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldHaveIllegalHeartRate() throws Exception {
        //Given
        String averageHeartRateJson = "{\"id\": 1, \"startTime\": \"2018-01-09T13:04:23.000Z\", \"intensity\": 6, \"kiloCalories\": 451.5, \"averageHeartRate\": -155, \"maxHeartRate\": 177, \"distanceRun\": 7.8, \"durationSeconds\": 3600}";
        String maxHeartRateJson = "{\"id\": 1, \"startTime\": \"2018-01-09T13:04:23.000Z\", \"intensity\": 6, \"kiloCalories\": 451.5, \"averageHeartRate\": 155, \"maxHeartRate\": -177, \"distanceRun\": 7.8, \"durationSeconds\": 3600}";

        //When
        boolean outcomeAverageHeartRate = validator.validate(averageHeartRateJson);
        boolean outcomeMaxHeartRate = validator.validate(maxHeartRateJson);

        //Then
        assertThat(outcomeAverageHeartRate, is(false));
        assertThat(outcomeMaxHeartRate, is(false));
    }

    @Test
    public void shouldHaveIllegalDistance() throws Exception {
        //Given
        String json = "{\"id\": 1, \"startTime\": \"2018-01-09T13:04:23.000Z\", \"intensity\": 6, \"kiloCalories\": 451.5, \"averageHeartRate\": 155, \"maxHeartRate\": 177, \"distanceRun\": -1, \"durationSeconds\": 3600}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldHaveIllegalDuration() throws Exception {
        //Given
        String json = "{\"id\": 1, \"startTime\": \"2018-01-09T13:04:23.000Z\", \"intensity\": 6, \"kiloCalories\": 451.5, \"averageHeartRate\": 155, \"maxHeartRate\": 177, \"distanceRun\": 5, \"durationSeconds\": -1}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }
}