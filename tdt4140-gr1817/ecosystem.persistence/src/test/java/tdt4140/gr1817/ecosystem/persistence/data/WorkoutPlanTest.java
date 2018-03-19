package tdt4140.gr1817.ecosystem.persistence.data;

import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WorkoutPlanTest {



    @Test
    public void shouldAddRowToPlan(){
        List<WorkoutPlanRow> list = new ArrayList<>();
        WorkoutPlan plan = new WorkoutPlan();
        plan.setRows(list);

        WorkoutPlanRow row = new WorkoutPlanRow();

        plan.addRow(row);

        assertEquals(row,plan.getRows().get(0));
    }
    @Test
    public void shouldRemoveRowFromPlan(){
        WorkoutPlan plan = new WorkoutPlan();
        List<WorkoutPlanRow> list = new ArrayList<>();
        plan.setRows(list);

        WorkoutPlanRow row = new WorkoutPlanRow();

        plan.addRow(row);
        plan.removeRow(0);
        assertEquals(true,plan.getRows().isEmpty());
    }
}
