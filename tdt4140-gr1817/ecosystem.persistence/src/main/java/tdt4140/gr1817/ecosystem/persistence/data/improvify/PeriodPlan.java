package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.Data;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.util.Date;
import java.util.List;

/**
 * A period plan is a plan for an extended period of time,
 * eg 2 or more days.
 *
 * Each day has a {@link WorkoutPlan} detailing the day's activities.
 */
@Data
public class PeriodPlan {
    private int id;
    private Date createdAt;
    private Date start;
    private Date end;
    private int createdByTrainerId;
    private User createdFor;

    private List<WorkoutPlan> plans;
}
