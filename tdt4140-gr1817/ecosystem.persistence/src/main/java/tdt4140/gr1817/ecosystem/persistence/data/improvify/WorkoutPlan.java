package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.Data;
import tdt4140.gr1817.ecosystem.persistence.data.User;

@Data
public class WorkoutPlan {
    private int id;
    private User createdForUser;
    private PeriodPlan parentPlan;
}
