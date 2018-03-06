package tdt4140.gr1817.ecosystem.persistence.data;

import lombok.Data;

/**
 * Represents a {@link User}'s goal.
 */
@Data
public class Goal {
    int id;
    User user;
    String description;
    boolean isCompleted;
    boolean isCurrent;
}
