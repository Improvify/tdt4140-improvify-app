package serviceproviderwebserver;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
public class CurrentWeight {
    private float currentWeight;
    private Date date;

    public CurrentWeight(float c, Date d) {
        currentWeight = c;
        date = d;
    }
}