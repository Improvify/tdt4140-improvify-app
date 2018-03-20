package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tdt4140.gr1817.ecosystem.persistence.data.User;

/**
 * A workout plan with a collection of exercises ("rows") to be executed during a session.
 * <p>
 * A workout plan is tailored for a specific user.
 */
@Data
@Builder
@AllArgsConstructor
public class WorkoutPlan {
    private int id;
    private String description;
    private User createdForUser;
}
