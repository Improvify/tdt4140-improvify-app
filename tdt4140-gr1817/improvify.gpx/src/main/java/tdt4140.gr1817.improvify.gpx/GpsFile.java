package tdt4140.gr1817.improvify.gpx;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class GpsFile {

    private int sessionID;
    private double time;
    private int intensity;
    private int KCal;
    private int averageHR;
    private int maxHR;
    private int distanceRun;

    public GpsFile(ArrayList<GpsPoint> L){

    }

    public static double calculateTotalDistance(ArrayList<GpsPoint> L){

        double total = 0;

        for (int i = 0;i<L.size()-1;i++){
            total += calculateDistanceBetweenPoints(L.get(i),L.get(i+1));
        }


        return total;
    }
    public static int getMaxHeartRate(ArrayList<GpsPoint> L){
        int max = 0;
        for(GpsPoint p: L){
            if (p.getHeartRate()> max){
                max = p.getHeartRate();
            }
        }
        return max;
    }
    public static int calculateAverageAndMaxHeartRate(ArrayList<GpsPoint> L){
        int total = 0;
        for(GpsPoint HR: L){
            total += HR.getHeartRate();
        }
        return total/L.size();
    }

    public static double calculateDistanceBetweenPoints(GpsPoint p1, GpsPoint p2){
        //Employs the Haversine Formula in order to calculate the distance between two points
        //based on Longitude and Latitude
        int R = 6370000; //Radius of the earth. Ish.

        double dLat = Math.toRadians(p1.getLatitude()-p2.getLatitude());
        double dLon = Math.toRadians(p1.getLongitude()-p2.getLongitude());

        double a = Math.sin(dLat/2)*Math.sin(dLat/2) +
                Math.cos(Math.toRadians(p1.getLatitude()))*Math.cos(Math.toRadians(p2.getLatitude()))*
                Math.sin(dLon/2)*Math.sin(dLon/2);

        double c = 2* Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        double d = R*c;

        return d;
    }

    public static void main(String[] args){
        String FILEPATH = "C:\\Users\\Henrik\\IdeaProjects\\pu\\tdt4140-gr1817\\improvify.gpx\\src\\main\\java\\tdt4140.gr1817.improvify.gpx\\exampleactivity.gpx";
        tdt4140.gr1817.improvify.gpx.GpsFileHandler handler = new tdt4140.gr1817.improvify.gpx.GpsFileHandler();
        try {
            ArrayList<String> arr = handler.loadFile(FILEPATH);
            ArrayList<GpsPoint> points = handler.generateGpsPoints(arr);
            double dist = GpsFile.calculateTotalDistance(points);

            System.out.println("*");
            System.out.println("The total distance run is "+Double.toString(dist) +" meters.");
            System.out.print(GpsFile.calculateAverageAndMaxHeartRate(points));
        }
        catch (Exception e){
            System.out.println(e);
        }





    }

}
