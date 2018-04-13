package tdt4140.gr1817.app.ui.feature.userlist;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateByUserSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetWeightByUserSpecification;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class VitalDataChartController {

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    private WeightRepository weightRepository;
    private RestingHeartRateRepository restingHeartRateRepository;

    @Inject
    public VitalDataChartController(WeightRepository weightRepository,
                                    RestingHeartRateRepository restingHeartRateRepository) {
        this.weightRepository = weightRepository;
        this.restingHeartRateRepository = restingHeartRateRepository;
    }

    @FXML
    public void initialize() {
        lineChart.setTitle("hubba bubba");
        User user = new User(1, "abc", "bcd", 180, new Date(1980, 10, 10),
                "bob", "afskdj", "asdf");
        updateLineChart(retrieveWeights(user), retrieveHeartRates(user));
    }

    private List retrieveWeights(User user) {
        List<Weight> weights = weightRepository.query(new GetWeightByUserSpecification(user));
        return weights;
    }

    private List retrieveHeartRates(User user) {
        List<RestingHeartRate> heartRates
                = restingHeartRateRepository.query(new GetRestingHeartRateByUserSpecification(user));
        return heartRates;
    }

    private void updateLineChart(List<Weight> weights, List<RestingHeartRate> heartRates) {

        XYChart.Series weightSeries = new XYChart.Series();
        XYChart.Series hrSeries = new XYChart.Series();
        //Add measurements to series
        long xLowerBound = Long.MAX_VALUE;
        long xUpperBound = 0;

        float yLowerBound = Integer.MAX_VALUE;
        float yUpperBound = 0;

        for (Weight weight : weights) {

            if (weight.getCurrentWeight() > yUpperBound) {
                yUpperBound = weight.getCurrentWeight();
            }
            if (weight.getCurrentWeight() < yLowerBound) {
                yLowerBound = weight.getCurrentWeight();
            }

            if (weight.getDate().getTime() > xUpperBound) {
                xUpperBound = weight.getDate().getTime();
            }
            if (weight.getDate().getTime() < xLowerBound) {
                xLowerBound = weight.getDate().getTime();
            }
            XYChart.Data dataPoint = new XYChart.Data(weight.getDate().getTime(), weight.getCurrentWeight());
            weightSeries.getData().add(dataPoint);
        }

        for (RestingHeartRate rhr : heartRates) {
            float val = rhr.getHeartRate();
            long time = rhr.getMeasuredAt().getTime();
            if (val > yUpperBound) {
                yUpperBound = val;
            }
            if (val < yLowerBound) {
                yLowerBound = val;
            }
            if (time > xUpperBound) {
                xUpperBound = time;
            }
            if (time < xLowerBound) {
                xLowerBound = time;
            }
            XYChart.Data dataPoint = new XYChart.Data(time, val);
            hrSeries.getData().add(dataPoint);
        }


        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(yLowerBound - 10);
        yAxis.setUpperBound(yUpperBound + 10);

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(xLowerBound);
        xAxis.setUpperBound(xUpperBound);
        xAxis.setTickUnit((xUpperBound - xLowerBound) / 5);
        xAxis.setMinorTickLength((xUpperBound - xLowerBound) / 5);
        xAxis.setMinorTickVisible(false);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                long num = object.longValue();
                Date date = new Date(num);

                return date.toString().substring(0, 10) + " " + Integer.toString(date.getYear());
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        weightSeries.setName("Vekt");
        hrSeries.setName("Hvilepuls");

        lineChart.getData().addAll(weightSeries);
        lineChart.getData().addAll(hrSeries);

    }
}
