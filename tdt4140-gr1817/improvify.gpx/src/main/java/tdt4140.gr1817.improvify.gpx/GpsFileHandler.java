package tdt4140.gr1817.improvify.gpx;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;

public final class GpsFileHandler {

    private GpsFileHandler() {
    }


    public static GpsFile generateGpsFile(String path) {
        //TODO: dont suck
        try {
            ArrayList<String> lines = loadFile(path);
            ArrayList<GpsPoint> datapoints = GpsFileHandler.generateGpsPoints(lines);
            GpsFile file = new GpsFile(datapoints);
            return file;
        } catch (IOException e) {
            System.out.println("File not found: " + e.toString());
            return null;
        }

    }

    private static ArrayList<String> loadFile(String path) throws FileNotFoundException, IOException {

        ArrayList<String> fileArr = new ArrayList<>(1000);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(path), "utf-8")
        )) {
            String l;
            while ((l = reader.readLine()) != null) {
                fileArr.add(l);
            }
        }

        return fileArr;
    }


    private static ArrayList<GpsPoint> generateGpsPoints(ArrayList<String> fileList) {
        ArrayList<GpsPoint> gpsPoints = new ArrayList<GpsPoint>();

        //This point is initialized only for reasons of allowing the code to compile. It gets removed by the end.
        //This is, in my opinion, a hack. And it should be dealt with differently
        GpsPoint gpsPoint = new GpsPoint();

        for (String line : fileList) {

            if (line.startsWith("      <trkpt")) {
                //Make new Trackpoint
                gpsPoints.add(gpsPoint);
                gpsPoint = new GpsPoint();


                //Split the relevant line into an array on whitespaces
                String[] trackpointList = line.trim().split(" ");

                String latstring = trackpointList[1];
                String lonstring = trackpointList[2];

                latstring = latstring.substring(5, 20);
                lonstring = lonstring.substring(5, 20);


                //Finally, convert relevant strings into doubles and insert into gpsPoint values
                gpsPoint.setLatitude(Double.parseDouble(latstring));
                gpsPoint.setLongitude(Double.parseDouble(lonstring));

            } else if (line.startsWith("        <time>")) {

                String timeString = line.trim().substring(6, 25);

                int year = Integer.parseInt(timeString.substring(0, 4));
                int month = Integer.parseInt(timeString.substring(8, 10));
                int day = Integer.parseInt(timeString.substring(5, 7));
                int hour = Integer.parseInt(timeString.substring(11, 13));
                int minute = Integer.parseInt(timeString.substring(14, 16));
                int second = Integer.parseInt((timeString.substring(17, 19)));

                LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
                gpsPoint.setTime(dateTime);

            } else if (line.trim().startsWith("<ele>")) {

                String ele = line.trim().substring(5, 10).split("[.<]")[0];
                gpsPoint.setElevation(Integer.parseInt(ele));
                //add elevation
            } else if (line.trim().startsWith("<ns3:hr")) {
                //add HR
                String hr = line.trim().substring(8, 11);
                gpsPoint.setHeartRate(Integer.parseInt(hr));
            }


        }

        //remove dummy point at index 0. Once again, this should probably be dealt with otherwise
        gpsPoints.remove(0);

        return gpsPoints;
    }

}
