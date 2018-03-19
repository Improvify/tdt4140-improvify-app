package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.Data;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.util.List;

/**
 * A workout plan with a collection of exercises ("rows") to be executed during a session.
 * <p>
 * A workout plan is tailored for a specific user.
 */
@Data
public class WorkoutPlan {
    private int id;
    private String description;
    private User createdForUser;
    private PeriodPlan parentPlan;
    private List<WorkoutPlanRow> rows;


    public void addRow(WorkoutPlanRow row) {
        rows.add(row);
    }

    public void removeRow(int index) {
        rows.remove(index);
    }

}
