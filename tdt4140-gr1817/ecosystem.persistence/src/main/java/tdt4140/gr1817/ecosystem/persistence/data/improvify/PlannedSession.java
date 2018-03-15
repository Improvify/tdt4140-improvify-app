package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * A session that is planned in a {@link PeriodPlan} to be executed on a specific date.
 * In the session a {@link WorkoutPlan} is to be followed.
 * </p>
 * @author Kristian Rekstad
 */
@Data
@AllArgsConstructor
public class PlannedSession {
    private Date date;
    // Use int for simplicity, or PeriodPlan for consistency? Depends on how this class will be used
    private int periodPlanId;
    private int workoutPlanId;
}
