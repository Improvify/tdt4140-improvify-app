package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.Data;

/**
 * A specific exercise in a {@link WorkoutPlan}.
 * <p>
 * If a {@link WorkoutPlan} is a collection of exercises listed as a table,
 * then each {@link WorkoutPlanRow} is a row in that table.
 * <br/>
 * Each {@link WorkoutPlanRow} must be created for a specific {@link WorkoutPlan}.</p>
 * <p>
 * For a diagram showing the relationships in workout plans,
 * see {@code "/docs/workout model/Workout plan concept.png"}
 * </p>
 *
 * @author Kristian Rekstad
 */
@Data
public class WorkoutPlanRow {
    private int id;
    /**
     * The "what" for an exercise, eg.: {@code "Warmup"}, {@code "Running interval"}, {@code "Active rest"}.
     */
    private String description;
    private int durationSeconds;
    /**
     * Intensity, given as eg.: {@code "60% Max heart rate"}, {@code "moderate"}.
     */
    private String intensity;
    private String comment;

    /**
     * The {@link WorkoutPlan} where this row exists in.
     */
    private WorkoutPlan workoutPlan;
}
