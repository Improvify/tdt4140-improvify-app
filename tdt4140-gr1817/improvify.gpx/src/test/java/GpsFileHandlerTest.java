
import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1817.improvify.gpx.GpsFile;
import tdt4140.gr1817.improvify.gpx.GpsFileHandler;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
    public void shouldSuccessfullyCreateGpsFile(){
        //
        GpsFile gpsFile = GpsFileHandler.generateGpsFile(path);

        assertThat((float)141.0,is(gpsFile.getAverageHeartRate()));
        assertThat((float)164.0,is(gpsFile.getMaxHeartRate()));
        assertThat((float) 9111,is(gpsFile.getDistanceRun()));
    }
}
