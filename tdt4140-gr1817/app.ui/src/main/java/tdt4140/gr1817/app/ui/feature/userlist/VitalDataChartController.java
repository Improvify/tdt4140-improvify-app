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
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlUserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlWeightRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllWeightsSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateByUserSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetWeightByUserSpecification;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VitalDataChartController
{

    @FXML
    private LineChart<Number,Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    private WeightRepository weightRepository;
    private RestingHeartRateRepository restingHeartRateRepository;

    @Inject
    public VitalDataChartController(WeightRepository weightRepository,RestingHeartRateRepository restingHeartRateRepository) {
        this.weightRepository = weightRepository;
        this.restingHeartRateRepository=restingHeartRateRepository;
    }

    @FXML
    public void initialize(){
        lineChart.setTitle("hubba bubba");
        testUpdate();
    }
    private void testUpdate(){


        /*
        Dette metoden eksisterer kun for å simulere henting fra Db

        User user = new User(1,"abc","bcd",180,new Date(1980,10,10),
                "bob","afskdj","asdf");
        Weight w1 = new Weight(1,79,new Date(2018,11,10),user);
        Weight w2 = new Weight(2,78,new Date(2018,12,10),user);
        Weight w3 = new Weight(3,75,new Date(2019,1,10),user);
        Weight w4 = new Weight(4,73,new Date(2019,2,10),user);
        Weight w5 = new Weight(5,78,new Date(2019,3,10),user);
        Weight w6 = new Weight(5,60,new Date(2019,8,25),user);

        ArrayList<Weight> weights = new ArrayList<>();
        weights.add(w1);
        weights.add(w2);
        weights.add(w3);
        weights.add(w4);
        weights.add(w5);
        weights.add(w6);
        updateLineChart(retrieveWeights(user),retrieveHeartRates(user));

        */

    }
    private List retrieveWeights(User user){
        List<Weight> weights = weightRepository.query(new GetWeightByUserSpecification(user));
        return weights;
    }
    private List retrieveHeartRates(User user){
        List<RestingHeartRate> heartRates = restingHeartRateRepository.query(new GetRestingHeartRateByUserSpecification(user));
        return heartRates;
    }

    private void updateLineChart(List<Weight> weights, List<RestingHeartRate> heartRates){

        XYChart.Series weightSeries = new XYChart.Series();
        XYChart.Series hrSeries = new XYChart.Series();
        //Add measurements to series
        long xLowerBound = Long.MAX_VALUE;
        long xUpperBound = 0;

        float yLowerBound = Integer.MAX_VALUE;
        float yUpperBound = 0;

        for (Weight weight: weights){

            if(weight.getCurrentWeight()>yUpperBound){
                yUpperBound=weight.getCurrentWeight();
            }
            if(weight.getCurrentWeight()<yLowerBound){
                yLowerBound=weight.getCurrentWeight();
            }

            if(weight.getDate().getTime()> xUpperBound){
                xUpperBound =weight.getDate().getTime();
            }
            if(weight.getDate().getTime()<xLowerBound){
                xLowerBound=weight.getDate().getTime();
            }
            XYChart.Data dataPoint = new XYChart.Data(weight.getDate().getTime(),weight.getCurrentWeight());
            weightSeries.getData().add(dataPoint);
        }

        for (RestingHeartRate rhr: heartRates){
            float val = rhr.getHeartRate();
            long time = rhr.getMeasuredAt().getTime();
            if(val>yUpperBound){
                yUpperBound=val;
            }
            if(val<yLowerBound){
                yLowerBound=val;
            }
            if(time > xUpperBound){
                xUpperBound=time;
            }
            if(time<xLowerBound){
                xLowerBound=time;
            }
            XYChart.Data dataPoint = new XYChart.Data(time,val);
            hrSeries.getData().add(dataPoint);
        }


        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(yLowerBound);
        yAxis.setUpperBound(yUpperBound);

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(xLowerBound);
        xAxis.setUpperBound(xUpperBound);
        xAxis.setTickUnit((xUpperBound -xLowerBound)/5);
        xAxis.setMinorTickLength((xUpperBound -xLowerBound)/5);
        xAxis.setMinorTickVisible(false);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                long num = object.longValue();
                Date date = new Date(num);

                return date.toString().substring(0,10)+" " +Integer.toString(date.getYear());
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
