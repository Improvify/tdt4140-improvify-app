package tdt4140.gr1817.ecosystem.persistence.data;

import lombok.Data;

import java.util.Date;

@Data
public class RestingHeartRate {
    private int id;
    private Date measuredAt;
    private int heartRate;
    private User measuredBy;
}
