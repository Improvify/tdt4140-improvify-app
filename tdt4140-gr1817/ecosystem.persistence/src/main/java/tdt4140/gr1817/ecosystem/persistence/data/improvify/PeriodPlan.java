package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.Data;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.util.Date;
import java.util.List;

/**
 * A period plan is a plan for an extended period of time,
 * eg 2 or more days.
 * <p>
 * Each day has a {@link WorkoutPlan} detailing the day's activities.
 * <p>
 * To find all {@link WorkoutPlan}s for a specific {@link PeriodPlan},
 * use {@link PlannedSession}.
 */
@Data
public class PeriodPlan {
    private int id;
    private String name;
    private String description;
    private Date createdAt;

    private Date periodStart;
    private Date periodEnd;

    private Trainer createdByTrainer;
    private User createdFor;

    private List<WorkoutPlan> plans;
}
