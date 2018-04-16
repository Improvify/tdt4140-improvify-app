package tdt4140.gr1817.app.ui.feature.workoutsession;

import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
public class WorkoutSessionAdapter {

    public WorkoutsessionController.SessionRow adapt(WorkoutSession workoutSession) {

        return new WorkoutsessionController.SessionRow(workoutSession.getStartTime(), workoutSession.getId(),
                workoutSession.getIntensity(),
                workoutSession.getKiloCalories(), workoutSession.getAverageHeartRate(),
                workoutSession.getMaxHeartRate(), workoutSession.getDistanceRun(), workoutSession.getDurationSeconds());
    }
}
