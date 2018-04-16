package tdt4140.gr1817.app.ui.feature.vitaldata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.app.core.feature.user.GetUserWithId;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
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
import java.util.Optional;
import java.util.function.Function;

/**
 * Displays a graph of the {@link UserSelectionService selected user's} vital data.
 */
@Slf4j
public class VitalDataChartController {

    private final GetVitalDataForUser vitalUserData;
    private final UserSelectionService userSelectionService;
    private final GetUserWithId getUserWithId;
    private final DataPointToSeriesAdapter dataPointAdapter;

    @FXML
    private LineChart<Long, Float> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    public SimpleDoubleProperty xLowerBound = new SimpleDoubleProperty();
    public SimpleDoubleProperty xUpperBound = new SimpleDoubleProperty();
    public SimpleDoubleProperty yLowerBound = new SimpleDoubleProperty();
    public SimpleDoubleProperty yUpperBound = new SimpleDoubleProperty();


    @Inject
    public VitalDataChartController(GetVitalDataForUser getVitalDataForUser, UserSelectionService userSelectionService,
                                    GetUserWithId getUserWithId, DataPointToSeriesAdapter dataPointToSeriesAdapter) {
        vitalUserData = getVitalDataForUser;
        this.userSelectionService = userSelectionService;
        this.getUserWithId = getUserWithId;
        this.dataPointAdapter = dataPointToSeriesAdapter;
    }

    @FXML
    public void initialize() {
        lineChart.setTitle("User data");

        yAxis.setAutoRanging(false);
        yAxis.lowerBoundProperty().bind(yLowerBound.subtract(10));
        yAxis.upperBoundProperty().bind(yUpperBound.add(10));

        xAxis.setAutoRanging(false);
        xAxis.lowerBoundProperty().bind(xLowerBound);
        xAxis.upperBoundProperty().bind(xUpperBound);
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

        loadData();
    }

    public void loadData() {
        Optional<UserSelectionService.UserId> selectedUserId = userSelectionService.getSelectedUserId();
        if (selectedUserId.isPresent()) {
            int id = selectedUserId.get().getId();
            User user = getUserWithId.getUserWithId(id);

            vitalUserData.setUser(user);
            GetVitalDataForUser.VitalData vitalData = vitalUserData.load();
            updateLineChart(vitalData.getWeights(), vitalData.getHeartRates());

        } else {
            log.warn("No user selected");
        }
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
        XYChart.Series<Long, Float> weightSeries = dataPointAdapter.createChartSeries(weightDataSeries.getData());
        weightSeries.setName("Vekt");


        DataSeries heartRateDataSeries = createDataSeries(heartRates,
                heartRate -> new DataSeries.DataPoint(heartRate.getMeasuredAt().getTime(), heartRate.getHeartRate()));
        XYChart.Series<Long, Float> heartRateSeries = dataPointAdapter.createChartSeries(heartRateDataSeries.getData());
        heartRateSeries.setName("Hvilepuls");

        if (heartRateDataSeries.getData().isEmpty() && weightDataSeries.getData().isEmpty()) {
            final long threeDays = 60L * 60L * 24L * 1000L;
            final long now = new Date().getTime();
            xLowerBound.set(now - threeDays);
            xUpperBound.set(now + threeDays);
            yLowerBound.set(5);
            yUpperBound.set(10);
        } else {
            xLowerBound.set(Math.min(heartRateDataSeries.getXLowerBound(), weightDataSeries.getXLowerBound()));
            xUpperBound.set(Math.max(heartRateDataSeries.getXUpperBound(), weightDataSeries.getXUpperBound()));
            yLowerBound.set(Math.min(heartRateDataSeries.getYLowerBound(), weightDataSeries.getYLowerBound()));
            yUpperBound.set(Math.max(heartRateDataSeries.getYUpperBound(), weightDataSeries.getYUpperBound()));
        }

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

}
