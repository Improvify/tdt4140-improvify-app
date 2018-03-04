package tdt4140.gr1817.improvify.gpx;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GpsFileHandler {


    public static ArrayList<String> loadFile(String path) throws FileNotFoundException,IOException{

        ArrayList<String> fileArr = new ArrayList<>(1000);

            FileReader inputStream = new FileReader(path);
            BufferedReader reader = new BufferedReader(inputStream);
            String l;
            while((l = reader.readLine()) != null){
                fileArr.add(l);
            }
            reader.close();

        return fileArr;
    }

    public static ArrayList<GpsPoint> generateGpsPoints(ArrayList<String> fileList){
        ArrayList<GpsPoint> gpsPoints = new ArrayList<GpsPoint>();

        //This point is initialized only for reasons of allowing the code to compile. It gets removed by the end.
        //This is, in my opinion, a hack. And it should be dealt with differently
        GpsPoint gpsPoint = new GpsPoint();

        for (String line: fileList){

            if(line.startsWith("      <trkpt")){
                //Make new Trackpoint
                gpsPoints.add(gpsPoint);

                gpsPoint = new GpsPoint();


                //Split the relevant line into an array on whitespaces
                String[] L = line.trim().split(" ");

                String latstring = L[1];
                String lonstring = L[2];

                latstring = latstring.substring(5,20);
                lonstring = lonstring.substring(5,20);


                //Finally, convert relevant strings into doubles and insert into gpsPoint values
                gpsPoint.setLatitude(Double.parseDouble(latstring));
                gpsPoint.setLongitude(Double.parseDouble(lonstring));

            }

            else if(line.startsWith("        <time>")){
                    //TODO: Add actual functionality here
                    gpsPoint.setTime(10);
            }

            else if (line.startsWith("        <ele>")){

                String ele = line.trim().substring(5,8);
                gpsPoint.setElevation(Integer.parseInt(ele));
                //add elevation
            }
            else if (line.trim().startsWith("<ns3:hr")){
                //add HR
                String hr = line.trim().substring(8,11);
                gpsPoint.setHeartRate(Integer.parseInt(hr));
            }


        }

        //remove dummy point at index 0. Once again, this should probably be dealt with otherwise
        gpsPoints.remove(0);

        return gpsPoints;
    }


    public static void main(String[] args){

    }
}
