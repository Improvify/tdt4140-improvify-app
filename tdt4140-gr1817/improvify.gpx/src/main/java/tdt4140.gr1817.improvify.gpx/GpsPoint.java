package tdt4140.gr1817.improvify.gpx;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GpsPoint {

    private LocalDateTime time;
    private double longitude;
    private double latitude;
    private int elevation;
    private int heartRate;
}
