package tdt4140.gr1817.ecosystem.persistence.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a {@link User}'s goal.
 */
@Data
@AllArgsConstructor
@Builder
public class Goal {
    int id;
    User user;
    String description;
    boolean isCompleted;
    boolean isCurrent;
}
