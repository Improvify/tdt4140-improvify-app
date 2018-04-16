package tdt4140.gr1817.app.core.feature.workoutsession;

import lombok.NonNull;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify.GetWorkoutSessionByLoggedBySpecification;


import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetAllWorkoutSessions {
    private final WorkoutSessionRepository workoutSessionRepository;

    @Inject
    public GetAllWorkoutSessions(@NonNull WorkoutSessionRepository workoutSessionRepository) {
        this.workoutSessionRepository = workoutSessionRepository;
    }

    public List<WorkoutSession> getAll(int loggedBy) {
        List<WorkoutSession> allWorkoutSessions = workoutSessionRepository
                .query(new GetWorkoutSessionByLoggedBySpecification(loggedBy));
        if (allWorkoutSessions == null) {
            allWorkoutSessions = new ArrayList<>(0);
        }
        return allWorkoutSessions;
    }

    //public List<WorkoutSession> getByDate(String startDate, String stopDate) {
        //List<WorkoutSession> workoutSessions = workoutSessionRepository.query(new GetWorkout)
    //}
}
