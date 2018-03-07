package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;

import java.util.Date;

public class WorkoutSessionValidator implements Validator {

    private Gson gson;

    public WorkoutSessionValidator() {
        this.gson = new Gson();
    }

    @Override
    public boolean validate(String json) {
        try {
            WorkoutSession workoutSession = gson.fromJson(json, WorkoutSession.class);

            return (isValidDistance(workoutSession.getDistanceRun())
                    && isValidID(workoutSession.getId())
                    && isValidKCal(workoutSession.getKiloCalories())
                    && isValidHeartRate(workoutSession.getAverageHeartRate())
                    && isValidHeartRate(workoutSession.getMaxHeartRate())
                    && isValidDate(workoutSession.getTime())
                    && isValidIntensity(workoutSession.getIntensity()));

        } catch (JsonSyntaxException | NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks that the distance is positive.
     *
     * @param distance The distance to be checked.
     * @return If the distance is valid.
     */
    private boolean isValidDistance(float distance) {
        return distance > 0;
    }

    /**
     * Checks that the ID is positive.
     *
     * @param id The ID to be checked.
     * @return If the ID is valid.
     */
    private boolean isValidID(int id) {
        return id > 0;
    }

    /**
     * Checks that the calories is positive.
     *
     * @param kcal The calories to be checked.
     * @return If the heart rate is valid.
     */
    private boolean isValidKCal(float kcal) {
        return kcal > 0;
    }

    /**
     * Checks that the intensity is between 1 and 10.
     *
     * @param intensity The intensity to be checked.
     * @return If the intensity is valid.
     */
    private boolean isValidIntensity(int intensity) {
        return intensity >= WorkoutSession.INTENSITY_MINIMUM && intensity <= WorkoutSession.INTENSITY_MAXIMUM;
    }

    /**
     * Checks that the heart rate is positive.
     *
     * @param heartRate The heart rate to be checked.
     * @return If the heart rate is valid.
     */
    private boolean isValidHeartRate(float heartRate) {
        return heartRate > 0;
    }

    /**
     * Checks that the date is in the past.
     *
     * @param date The date to be checked.
     * @return If the date is valid.
     */
    private boolean isValidDate(Date date) {
        return date.before(new Date());
    }
}
