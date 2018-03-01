package tdt4140.gr1817.improvify.gpx;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GpsFileHandler {


    public ArrayList<String> loadFile(String path){

        ArrayList<String> fileArr = new ArrayList<>(1000);
        try {
            FileReader inputStream = new FileReader(path);
            BufferedReader reader = new BufferedReader(inputStream);
            String l;
            while((l = reader.readLine()) != null){
                fileArr.add(l);
            }
            reader.close();
        }
        catch(FileNotFoundException f){
            System.out.println(f);
        }
        catch(IOException e){
            System.out.println(e);
        }
        return fileArr;
    }

    public ArrayList<GpsPoint> generateGpsPoints(ArrayList<String> fileList){
        ArrayList<GpsPoint> gpsPoints = new ArrayList<GpsPoint>();

        //This point is initialized only for reasons of allowing the code to compile. It gets removed by the end.
        //This is, in my opinion, a hack. And it should be dealt with different
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

        String FILEPATH = "C:\\Users\\Henrik\\IdeaProjects\\pu\\tdt4140-gr1817\\improvify.gpx\\src\\main\\java\\tdt4140.gr1817.improvify.gpx\\exampleactivity.gpx";
        tdt4140.gr1817.improvify.gpx.GpsFileHandler handler = new tdt4140.gr1817.improvify.gpx.GpsFileHandler();
        ArrayList<String> arr = handler.loadFile(FILEPATH);
        ArrayList<GpsPoint> points = handler.generateGpsPoints(arr);
        System.out.println(points);
    }
}
