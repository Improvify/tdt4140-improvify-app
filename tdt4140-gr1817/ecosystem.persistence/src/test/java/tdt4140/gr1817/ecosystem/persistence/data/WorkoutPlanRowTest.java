package tdt4140.gr1817.ecosystem.persistence.data;

import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;

import static org.junit.Assert.assertEquals;

public class WorkoutPlanRowTest {

    @Test
    public void shouldSuccessfullySetDuration() {

        WorkoutPlanRow row = new WorkoutPlanRow();
        row.setDuration("15:00");
        assertEquals("PT15M", row.getDuration().toString());

        row.setDuration("17:15:37");
        assertEquals("PT17H15M37S", row.getDuration().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void durationSetterShouldFailWhenMoreThan59Seconds() {

        WorkoutPlanRow row = new WorkoutPlanRow();
        row.setDuration("75");
    }

    @Test(expected = IllegalArgumentException.class)
    public void durationSetterShouldFailWhenMoreThan59Minutes() {
        WorkoutPlanRow row = new WorkoutPlanRow();
        row.setDuration("75:00");
    }

    @Test(expected = IllegalArgumentException.class)
    public void durationSetterShouldFailWhenMoreThan23Hours() {
        WorkoutPlanRow row = new WorkoutPlanRow();
        row.setDuration("34:33:15");
    }

    @Test(expected = IllegalArgumentException.class)
    public void durationSetterShouldFailWhenIncorrectFormat()
    {
        WorkoutPlanRow row = new WorkoutPlanRow();
        row.setDuration("asdf");
        row.setDuration(":00");
        row.setDuration("10:28:29:");
        row.setDuration("NOTICE ME STUDASS SENPAI!");
    }

}
