package serviceproviderwebserver;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
public class WorkoutSession {
    Date date;
    int intensity;
    int kcal;
    int avgHeartRate;
    int maxHeartRate;
    float distanceRun;
}
