package tdt4140.gr1817.ecosystem.persistence.data;

import org.junit.Test;
import tdt4140.gr1817.improvify.gpx.GpsFile;
import tdt4140.gr1817.improvify.gpx.GpsFileHandler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class WorkoutSessionTest {


    @Test(expected = IllegalArgumentException.class)
    public void consturctorShouldThrowWhenTooLowIntensity() throws Exception {
        new WorkoutSession(0, null, WorkoutSession.INTENSITY_MINIMUM-1, 0, 0,0,0, 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenTooLowIntensity() throws Exception {
        // Given
        WorkoutSession session = new WorkoutSession(0, null, WorkoutSession.INTENSITY_MINIMUM, 0, 0, 0, 0, 0, null);

        // When
        session.setIntensity(WorkoutSession.INTENSITY_MINIMUM - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowWhenTooLargeIntensity() throws Exception {
        new WorkoutSession(0, null, WorkoutSession.INTENSITY_MAXIMUM + 1, 0, 0, 0, 0, 0, null);
    }

    @Test
    public void shouldWorkWhenAtMinIntensity() throws Exception {
        // When
        WorkoutSession session = new WorkoutSession(0, null, WorkoutSession.INTENSITY_MINIMUM, 0, 0, 0, 0, 0, null);

        // Then
        assertThat(session.getIntensity(), is(WorkoutSession.INTENSITY_MINIMUM));
    }

    @Test
    public void shouldWorkWhenAtMaxIntensity() throws Exception {
        // When
        WorkoutSession session = new WorkoutSession(0, null, WorkoutSession.INTENSITY_MAXIMUM, 0, 0, 0, 0, 0, null);

        // Then
        assertThat(session.getIntensity(), is(WorkoutSession.INTENSITY_MAXIMUM));
    }
    @Test
    public void shouldCreateWorkoutSessionFromGpsFile(){
        //When
        GpsFile gpsFile = GpsFileHandler.generateGpsFile("src/test/resources/exampleactivity.gpx");
        WorkoutSession workoutSession = new WorkoutSession(0,5,null,gpsFile);

        //Then
        assertThat(workoutSession.getDistanceRun(),is(gpsFile.getDistanceRun()));
        assertThat(workoutSession.getAverageHeartRate(),is(gpsFile.getAverageHeartRate()));
    }
}