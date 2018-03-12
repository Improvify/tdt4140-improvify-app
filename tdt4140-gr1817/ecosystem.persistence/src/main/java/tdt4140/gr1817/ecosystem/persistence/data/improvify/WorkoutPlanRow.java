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

    public void setDurationSeconds(String stringFromField) {
        //Enforce hh:mm:ss format
        //Enforce max of 23:59:59 in any field


        //Convert human-readable string to ISO-8601 standard
        String initialTimeArray[] = stringFromField.split(":");

        String timeArray[] = {"00","00","00"};

        if (initialTimeArray.length == 1) {
             timeArray[2] = initialTimeArray[0];
        } else if (initialTimeArray.length == 2) {
            timeArray[2] = initialTimeArray[1];
            timeArray[1] = initialTimeArray[0];
        } else {
            timeArray = initialTimeArray;
        }


        this.durationSeconds = Integer.parseInt(timeArray[0])
                *3600+Integer.parseInt(timeArray[1])
                *60+Integer.parseInt(timeArray[2]);

    }

}
