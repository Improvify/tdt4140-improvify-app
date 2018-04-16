package tdt4140.gr1817.app.ui.feature.vitaldata;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.junit.Test;
import tdt4140.gr1817.app.core.feature.user.vitaldata.DataSeries;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class DataPointToSeriesAdapterTest {


    @Test
    public void shouldAdaptDataPoints() throws Exception {
        // Given
        DataPointToSeriesAdapter dataPointToSeriesAdapter = new DataPointToSeriesAdapter();
        List<DataSeries.DataPoint> dataPoints = Arrays.asList(new DataSeries.DataPoint(1337L, 2f));

        // When
        XYChart.Series<Long, Float> chartSeries = dataPointToSeriesAdapter.createChartSeries(dataPoints);


        // Then
        ObservableList<XYChart.Data<Long, Float>> data = chartSeries.getData();
        assertThat(data, hasSize(1));

        XYChart.Data<Long, Float> chartData = data.get(0);
        assertThat(chartData.getXValue(), is(1337L));
        assertThat(chartData.getYValue(), is(2f));
    }
}