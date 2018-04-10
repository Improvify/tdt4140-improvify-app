package tdt4140.gr1817.app.core.feature.workoutplan;


import lombok.Data;
import lombok.NonNull;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRowRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification
        .GetAllWorkoutplanRowsForWorkoutplanByIDSpecification;

import java.util.List;

@Data


public class WorkoutplanToMarkdownConverter {


    private WorkoutPlanRowRepository workoutPlanRowRepository;

    public WorkoutplanToMarkdownConverter(@NonNull WorkoutPlanRowRepository workoutPlanRowRepository) {
        this.workoutPlanRowRepository = workoutPlanRowRepository;
    }

    public String createMarkdownString(WorkoutPlan workoutplanToConvert) {
        int id = workoutplanToConvert.getId();
        GetAllWorkoutplanRowsForWorkoutplanByIDSpecification specification = new
                GetAllWorkoutplanRowsForWorkoutplanByIDSpecification(id);
        List<WorkoutPlanRow> rows = workoutPlanRowRepository.query(specification);

        String markdown = "";
        markdown += "Description|Duration|Intensity|Comment\n"
                + "-----------|--------|---------|-------\n";

        for (WorkoutPlanRow row : rows) {
            markdown += String.format("%s | %s | %s | %s \n", row.getDescription(), row.getDurationSeconds(),
                    row.getIntensity(), row.getComment());
        }

        return markdown;
    }

}
