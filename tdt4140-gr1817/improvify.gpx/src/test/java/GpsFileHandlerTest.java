
import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1817.improvify.gpx.GpsFile;
import tdt4140.gr1817.improvify.gpx.GpsFileHandler;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GpsFileHandlerTest {
    String path;
    @Before
    public void setup(){
        String relativePath = "src/test/resources/exampleactivity.gpx";
        File f = new File(relativePath);
        try {
            this.path = f.getCanonicalPath();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }


    @Test
    public void shouldSimplyPass() {
        assertEquals(0, 0);
    }

    @Test
    public void shouldSuccessfullyCreateGpsFile(){
        //
        GpsFile gpsFile = GpsFileHandler.generateGpsFile(path);

        assertEquals(141,gpsFile.getAverageHeartRate());
        assertEquals(164,gpsFile.getMaxHeartRate());
        assertEquals(9111,gpsFile.getDistanceRun());
    }


}
