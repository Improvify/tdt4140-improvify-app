package tdt4140.gr1817.app.ui.feature.viewuser;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;
import tdt4140.gr1817.app.core.feature.user.vitaldata.DataSeries;
import tdt4140.gr1817.app.core.feature.user.vitaldata.GetVitalDataForUser;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @see #setUser(User)
 */
public class VitalDataChartController {

    private final GetVitalDataForUser vitalUserData;

    @FXML
    private LineChart<Long, Float> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    SimpleDoubleProperty xLowerBound = new SimpleDoubleProperty();
    SimpleDoubleProperty xUpperBound = new SimpleDoubleProperty();
    SimpleDoubleProperty yLowerBound = new SimpleDoubleProperty();
    SimpleDoubleProperty yUpperBound = new SimpleDoubleProperty();


    @Inject
    public VitalDataChartController(GetVitalDataForUser getVitalDataForUser) {
        vitalUserData = getVitalDataForUser;
    }

    @FXML
    public void initialize() {
        lineChart.setTitle("User data");

        yAxis.setAutoRanging(false);
        yAxis.lowerBoundProperty().bind(yLowerBound.subtract(10));
        yAxis.upperBoundProperty().bind(yUpperBound.add(10));

        xAxis.setAutoRanging(false);
        xAxis.lowerBoundProperty().bind(xLowerBound);
        xAxis.upperBoundProperty().bind(yUpperBound);
        xAxis.tickUnitProperty().bind(xUpperBound.subtract(xLowerBound).divide(5)); // (xU - xL) / 5
        xAxis.minorTickLengthProperty().bind(xUpperBound.subtract(xLowerBound).divide(5)); // (xU - xL) / 5
        xAxis.setMinorTickVisible(false);

        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

            @Override
            public String toString(Number timestamp) {
                return dateFormat.format(new Date(timestamp.longValue()));
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
    }

    public void setUser(User user) {
        vitalUserData.setUser(user);
        GetVitalDataForUser.VitalData vitalData = vitalUserData.load();

        updateLineChart(vitalData.getWeights(), vitalData.getHeartRates());
    }

    /**
     * Adds measurements to data series.
     *
     * @param weights    list of weight measurements for the given user
     * @param heartRates list of resting heart rate measurements for the given user
     */
    private void updateLineChart(List<Weight> weights, List<RestingHeartRate> heartRates) {
        DataSeries weightDataSeries = createDataSeries(weights,
                weight -> new DataSeries.DataPoint(weight.getDate().getTime(), weight.getCurrentWeight()));
        XYChart.Series<Long, Float> weightSeries = createChartSeries(weightDataSeries.getData());
        weightSeries.setName("Vekt");


        DataSeries heartRateDataSeries = createDataSeries(heartRates,
                heartRate -> new DataSeries.DataPoint(heartRate.getMeasuredAt().getTime(), heartRate.getHeartRate()));
        XYChart.Series<Long, Float> heartRateSeries = createChartSeries(heartRateDataSeries.getData());
        heartRateSeries.setName("Hvilepuls");

        xLowerBound.set(Math.min(heartRateDataSeries.getXLowerBound(), weightDataSeries.getXLowerBound()));
        xUpperBound.set(Math.max(heartRateDataSeries.getXUpperBound(), weightDataSeries.getXUpperBound()));
        yLowerBound.set(Math.min(heartRateDataSeries.getYLowerBound(), weightDataSeries.getYLowerBound()));
        yUpperBound.set(Math.max(heartRateDataSeries.getYUpperBound(), weightDataSeries.getYUpperBound()));

        ObservableList<XYChart.Series<Long, Float>> data = lineChart.getData();
        data.setAll(weightSeries, heartRateSeries);
    }

    /**
     * Converts a list of data to data points.
     *
     * @param data            data to convert
     * @param dataPointMapper function to convert data of type T to {@link DataSeries.DataPoint}
     * @param <T>             type of data
     * @return a series with {@link DataSeries.DataPoint DataPoints} and min/max values for x and y
     */
    public <T> DataSeries createDataSeries(List<T> data, Function<T, DataSeries.DataPoint> dataPointMapper) {
        DataSeries dataSeries = new DataSeries();
        List<DataSeries.DataPoint> dataPoints = new ArrayList<>(data.size());

        for (T datum : data) {
            DataSeries.DataPoint dataPoint = dataPointMapper.apply(datum);
            dataPoints.add(dataPoint);
        }

        dataSeries.setData(dataPoints);

        return dataSeries;
    }

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
