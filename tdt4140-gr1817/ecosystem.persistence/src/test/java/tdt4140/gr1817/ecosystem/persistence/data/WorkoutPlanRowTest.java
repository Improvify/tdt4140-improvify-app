package tdt4140.gr1817.ecosystem.persistence.data;

import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;

import static org.junit.Assert.assertEquals;

public class WorkoutPlanRowTest {

    @Test
    public void shouldSuccessfullySetDuration(){

        WorkoutPlanRow row = new WorkoutPlanRow();
        row.setDuration("15:00");
        assertEquals("PT15M",row.getDuration().toString());

        row.setDuration("17:15:37");
        assertEquals("PT17H15M37S",row.getDuration().toString());

    }

}
