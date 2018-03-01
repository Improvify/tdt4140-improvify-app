package tdt4140.gr1817.improvify.gpx;

import lombok.Data;

@Data
public class GpsPoint {

    private double time;
    private double longitude;
    private double latitude;
    private int elevation;
    private int heartRate;
}
