import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1817.improvify.gpx.GpsFile;
import tdt4140.gr1817.improvify.gpx.GpsPoint;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class GpsFileTest {
    ArrayList<GpsPoint> points;
    GpsFile file;
    @Before
    public void setup(){
        this.points = new ArrayList<>();
        GpsPoint p1 = new GpsPoint();
        p1.setLongitude(10.001);
        p1.setLatitude(10.002);
        p1.setHeartRate(120);
        p1.setTime(LocalDateTime.of(1990,5,12,14,15,12));
        GpsPoint p2 = new GpsPoint();
        p2.setLongitude(10.002);
        p2.setLatitude(10.002);
        p2.setHeartRate(122);
        p2.setTime(LocalDateTime.of(1990,5,12,14,15,14));
        this.points.add(p1);
        this.points.add(p2);
        file  = new GpsFile(this.points);
    }

    @Test
    public void shouldHaveCorrectAverageHeartRate(){
        assertEquals(121,file.getAverageHeartRate());
    }
    @Test
    public void shouldHaveCorrectMaximumHeartRate(){
        assertEquals(122,file.getMaxHeartRate());
    }
    @Test
    public void shouldHaveCorrectDuration(){
        assertEquals(2,file.getDuration());
    }

}
