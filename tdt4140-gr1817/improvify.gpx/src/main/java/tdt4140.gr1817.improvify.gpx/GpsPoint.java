package tdt4140.gr1817.improvify.gpx;

import lombok.Data;

@Data
public class GpsPoint {

    double time;
    double longitude;
    double latitude;
    int elevation;
    int heartRate;
}
