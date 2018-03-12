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
        if(!stringFromField.matches("^([0-9][0-9])(:[0-9][0-9]){0,2}$")){
            throw new IllegalArgumentException("Duration input must match hh:mm:ss format");
        }


        //Convert human-readable string to ISO-8601 standard
        String initialTimeArray[] = stringFromField.split(":");

        String timeArray[] = {"00", "00", "00"};

        if (initialTimeArray.length == 1) {
            timeArray[2] = initialTimeArray[0];
        } else if (initialTimeArray.length == 2) {
            timeArray[2] = initialTimeArray[1];
            timeArray[1] = initialTimeArray[0];
        } else {
            timeArray = initialTimeArray;
        }

        //Check that slot values are within 23:59:59 bounds
        if (Integer.parseInt(timeArray[0]) > 23) {
            throw new IllegalArgumentException("Duration hour field cannot be more than 23");
        }
        if (Integer.parseInt(timeArray[1]) > 59) {
            throw new IllegalArgumentException("Duration minute field cannot be more than 59");
        }
        if (Integer.parseInt(timeArray[2]) > 59) {
            throw new IllegalArgumentException("Duration second field cannot be more than 59");
        }

        this.durationSeconds = Integer.parseInt(timeArray[0])
                *3600+Integer.parseInt(timeArray[1])
                *60+Integer.parseInt(timeArray[2]);

    }

}
