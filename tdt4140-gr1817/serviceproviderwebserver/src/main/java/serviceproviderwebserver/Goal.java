package serviceproviderwebserver;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Goal {
    String description;
    boolean isCompleted;
    boolean isCurrent;
}
