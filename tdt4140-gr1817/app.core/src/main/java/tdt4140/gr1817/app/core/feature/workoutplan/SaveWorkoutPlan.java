package tdt4140.gr1817.app.core.feature.workoutplan;

import lombok.NonNull;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRowRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Fredrik Jenssen.
 *
 * @author Fredrik Jenssen
 */
public class SaveWorkoutPlan {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final List<WorkoutPlanRowRepository> workoutPlanRowRepositories;

    @Inject
    public SaveWorkoutPlan(@NonNull List<WorkoutPlanRowRepository> workoutPlanRowRepositories
            , @NonNull WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanRowRepositories = workoutPlanRowRepositories;
    }
}
