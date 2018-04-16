package tdt4140.gr1817.app.ui.feature.vitaldata;

import javafx.scene.chart.XYChart;
import tdt4140.gr1817.app.core.feature.user.vitaldata.DataSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class DataPointToSeriesAdapter {

    /**
     * Converts {@link DataSeries.DataPoint DataPoints} to a {@link XYChart.Series}.
     *
     * @param dataPoints
     * @return
     */
    public XYChart.Series<Long, Float> createChartSeries(List<DataSeries.DataPoint> dataPoints) {
        XYChart.Series<Long, Float> series = new XYChart.Series<>();

        List<XYChart.Data<Long, Float>> chartData = new ArrayList<>(dataPoints.size());
        for (DataSeries.DataPoint dataPoint : dataPoints) {
            long x = dataPoint.getTimestamp();
            float y = dataPoint.getValue();
            XYChart.Data<Long, Float> longFloatData = new XYChart.Data<>(x, y);

            chartData.add(longFloatData);
        }
        series.getData().setAll(chartData);

        return series;
    }


}
