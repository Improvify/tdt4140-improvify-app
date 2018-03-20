package tdt4140.gr1817.ecosystem.persistence.data;

import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;

import static org.junit.Assert.assertEquals;

public class WorkoutPlanRowTest {

    @Test
    public void shouldSuccessfullySetDuration() {

        WorkoutPlanRow row = new WorkoutPlanRow(1, "test", 2600, "80% HR max", "Test comment", new WorkoutPlan(1,
                "test", BuilderFactory.createUser().build()));
        row.setDurationSeconds("15:00");
        assertEquals(15 * 60, row.getDurationSeconds());

        row.setDurationSeconds("17:15:37");
        assertEquals(17 * 3600 + 15 * 60 + 37, row.getDurationSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void durationSetterShouldFailWhenMoreThan59Seconds() {

        WorkoutPlanRow row = BuilderFactory.createWorkoutPlanRow().build();
        row.setDurationSeconds("75");
    }

    @Test(expected = IllegalArgumentException.class)
    public void durationSetterShouldFailWhenMoreThan59Minutes() {
        WorkoutPlanRow row = BuilderFactory.createWorkoutPlanRow().build();
        row.setDurationSeconds("75:00");
    }

    @Test(expected = IllegalArgumentException.class)
    public void durationSetterShouldFailWhenMoreThan23Hours() {
        WorkoutPlanRow row = BuilderFactory.createWorkoutPlanRow().build();
        row.setDurationSeconds("34:33:15");
    }

    @Test(expected = IllegalArgumentException.class)
    public void durationSetterShouldFailWhenIncorrectFormat() {
        WorkoutPlanRow row = BuilderFactory.createWorkoutPlanRow().build();
        row.setDurationSeconds("asdf");
        row.setDurationSeconds(":00");
        row.setDurationSeconds("10:28:29:");
        row.setDurationSeconds("NOTICE ME STUDASS SENPAI!");
    }

}
