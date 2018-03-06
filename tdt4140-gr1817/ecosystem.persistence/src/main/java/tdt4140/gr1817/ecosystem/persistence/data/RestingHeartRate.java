package tdt4140.gr1817.ecosystem.persistence.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RestingHeartRate {
    private int id;
    private Date measuredAt;
    private int heartRate;
    private User measuredBy;
}
