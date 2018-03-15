package tdt4140.gr1817.ecosystem.persistence.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Represents a user's workout session.
 *
 * A user can have many workout sessions that they have logged.
 */
@Data
@Builder
public class WorkoutSession {
    public static final int INTENSITY_MINIMUM = 0;
    public static final int INTENSITY_MAXIMUM = 10;

    private int id;
    private Date startTime;
    private int intensity;
    private float kiloCalories;
    private float averageHeartRate;
    private float maxHeartRate;
    private float distanceRun;
    private int durationSeconds;
    private User user;

    public WorkoutSession(int id, Date time, int intensity, float kiloCalories, float averageHeartRate,
                          float maxHeartRate, float distanceRun, int durationSeconds, User user) {
        validateIntensity(intensity);

        this.id = id;
        this.startTime = time;
        this.durationSeconds = durationSeconds;
        this.intensity = intensity;
        this.kiloCalories = kiloCalories;
        this.averageHeartRate = averageHeartRate;
        this.maxHeartRate = maxHeartRate;
        this.distanceRun = distanceRun;
        this.user = user;
    }

    public void setIntensity(int intensity) {
        validateIntensity(intensity);
        this.intensity = intensity;
    }

    private static void validateIntensity(int intensity) {
        if (intensity < INTENSITY_MINIMUM || intensity > INTENSITY_MAXIMUM) {
            throw new IllegalArgumentException(String.format("Intensity must be in range (%s, %s), but was %s",
                    INTENSITY_MINIMUM, INTENSITY_MAXIMUM, intensity));
        }
    }

}
