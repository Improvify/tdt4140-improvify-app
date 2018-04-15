package tdt4140.gr1817.app.core.feature.user.vitaldata;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class DataSeriesTest {


    @Test
    public void shouldSetCorrectBounds() throws Exception {
        // Given
        DataSeries dataSeries = new DataSeries();
        List<DataSeries.DataPoint> dataPoints = Arrays.asList(
                new DataSeries.DataPoint(1000, 500), // Max
                new DataSeries.DataPoint(10, 50) // Min
        );

        // When
        dataSeries.setData(dataPoints);

        // Then
        assertThat(dataSeries.getXLowerBound(), is(10L));
        assertThat(dataSeries.getXUpperBound(), is(1000L));
        assertThat(dataSeries.getYLowerBound(), is(50f));
        assertThat(dataSeries.getYUpperBound(), is(500f));
    }
}