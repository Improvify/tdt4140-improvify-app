package tdt4140.gr1817.improvify.gpx;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class GpsFile {
    private static int secondsInMinute = 60;
    private static int minutesInHour = 60;
    private static int hoursInDay = 24;

    private LocalDateTime startTime;
    private float averageHeartRate;
    private float maxHeartRate;
    private float distanceRun;
    private int duration; //in seconds

    public GpsFile(ArrayList<GpsPoint> pointList) {

        //Set startTime to first time point in file.
        this.startTime = pointList.get(0).getTime();

        //calculate average heart rate
        this.averageHeartRate = calculateAverageHeartRate(pointList);

        //find max heart rate
        this.maxHeartRate = calculateMaxHeartRate(pointList);

        //calculate distance run
        this.distanceRun = calculateTotalDistance(pointList);

        //find time difference between first trackpoint and last.
        this.duration = calculateDuration(pointList);
    }

    private static int calculateTotalDistance(ArrayList<GpsPoint> pointList) {

        double total = 0;

        for (int i = 0; i < pointList.size() - 1; i++) {
            total += calculateDistanceBetweenPoints(pointList.get(i), pointList.get(i + 1));
        }


        return (int) total;
    }

    private int calculateDuration(ArrayList<GpsPoint> pointList) {
        System.out.println(pointList.get(pointList.size() - 1));
        LocalDateTime endTime = pointList.get(pointList.size() - 1).getTime();
        int deltaT = 0;
        deltaT += (endTime.getSecond() - this.startTime.getSecond());
        deltaT += secondsInMinute * (endTime.getMinute() - this.startTime.getMinute());
        deltaT += secondsInMinute * minutesInHour * (endTime.getHour() - this.startTime.getHour());
        if (deltaT < 0) {
            throw new IllegalStateException("Activity time was somehow negative");
        }
        return deltaT;
    }

    private static int calculateMaxHeartRate(ArrayList<GpsPoint> pointList) {
        int max = 0;
        for (GpsPoint p : pointList) {
            if (p.getHeartRate() > max) {
                max = p.getHeartRate();
            }
        }
        return max;
    }

    private static int calculateAverageHeartRate(ArrayList<GpsPoint> pointList) {
        int total = 0;
        for (GpsPoint p : pointList) {
            total += p.getHeartRate();
        }
        return total / pointList.size();
    }

    private static double calculateDistanceBetweenPoints(GpsPoint p1, GpsPoint p2) {
        //Employs the Haversine Formula in order to calculate the distance between two points
        //based on Longitude and Latitude
        int r = 6370000; //Radius of the earth. Ish.

        double dLat = Math.toRadians(p1.getLatitude() - p2.getLatitude());
        double dLon = Math.toRadians(p1.getLongitude() - p2.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = r * c;

        return d;
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Henrik\\IdeaProjects\\pu\\tdt4140-gr1817\\improvify.gpx\\src\\main\\java\\tdt4140"
                + ".gr1817.improvify.gpx\\exampleactivity.gpx";

    }

}
