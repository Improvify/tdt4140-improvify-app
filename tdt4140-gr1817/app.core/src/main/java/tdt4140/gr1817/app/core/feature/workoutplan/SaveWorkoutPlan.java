package tdt4140.gr1817.app.core.feature.workoutplan;

import lombok.NonNull;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;
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
    private final WorkoutPlanRowRepository workoutPlanRowRepository;

    @Inject
    public SaveWorkoutPlan(@NonNull WorkoutPlanRowRepository workoutPlanRowRepositories
            , @NonNull WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanRowRepository = workoutPlanRowRepositories;
    }

    public void save(int userId, String workoutPlanDescription, List<WorkoutPlanRow> workoutRowList) {
        User user = User.builder()
                .id(userId)
                .build();
        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .description(workoutPlanDescription)
                .createdForUser(user)
                .build();

        workoutPlanRepository.add(workoutPlan);

        for (WorkoutPlanRow row : workoutRowList) {
            WorkoutPlanRow ecosystemRow = WorkoutPlanRow.builder()
                    .description(row.getDescription())
                    .durationSeconds(row.getDurationSeconds())
                    .intensity(row.getIntensity())
                    .comment(row.getComment())
                    .workoutPlan(workoutPlan)
                    .build();
            workoutPlanRowRepository.add(ecosystemRow);
        }
    }
}
