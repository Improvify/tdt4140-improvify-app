package tdt4140.gr1817.app.ui.feature.workoutsession;

import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WorkoutSessionAdapter {
    public SessionRow adapt(WorkoutSession workoutSession) {
        SimpleDateFormat dt1 = new SimpleDateFormat("d. MMMM yyyy", Locale.ENGLISH);
        return new SessionRow(dt1.format(workoutSession.getStartTime()), workoutSession.getId(),
                workoutSession.getIntensity(),
                Math.round(workoutSession.getKiloCalories()), Math.round(workoutSession.getAverageHeartRate()), Math
                .round(workoutSession.getMaxHeartRate()), Math.round(workoutSession
                .getDistanceRun()), workoutSession.getDurationSeconds(), workoutSession.getUser().getId());
    }
}
