package tdt4140.gr1817.app.ui.feature.userlist;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;
import sun.plugin.javascript.navig.Array;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class VitalDataChartController
{

    @FXML
    private LineChart<Number,Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    public void initialize(){
        lineChart.setTitle("hubba bubba");
        testUpdate();
    }
    private void testUpdate(){
        /*
        Dette metoden eksisterer kun for å simulere henting fra Db


         */
        User user = new User(10,"abc","bcd",180,new Date(1980,10,10),
                "bob","afskdj","asdf");
        Weight w1 = new Weight(1,79,new Date(2018,11,10),user);
        Weight w2 = new Weight(2,78,new Date(2018,12,10),user);
        Weight w3 = new Weight(3,75,new Date(2019,1,10),user);
        Weight w4 = new Weight(4,73,new Date(2019,2,10),user);
        Weight w5 = new Weight(5,78,new Date(2019,3,10),user);
        ArrayList<Weight> weights = new ArrayList<>();
        weights.add(w1);
        weights.add(w2);
        weights.add(w3);
        weights.add(w4);
        weights.add(w5);

        updateLineChart(weights);

    }

    private void updateLineChart(ArrayList<Weight> weights){

        XYChart.Series series = new XYChart.Series();
        //Add measurements to series
        long lb = Long.MAX_VALUE;
        long hb = 0;
        for (Weight weight: weights){

            if(weight.getDate().getTime()>hb){
                hb=weight.getDate().getTime();
            }
            if(weight.getDate().getTime()<lb){
                lb=weight.getDate().getTime();
            }


            XYChart.Data dataPoint = new XYChart.Data(weight.getDate().getTime(),weight.getCurrentWeight());
            series.getData().add(dataPoint);
        }

        System.out.println(lb);
        System.out.println(hb);


        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(60);
        yAxis.setUpperBound(90);

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(lb);
        xAxis.setUpperBound(hb);
        xAxis.setTickUnit((hb-lb)/5);
        xAxis.setMinorTickLength((hb-lb)/5);
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

        series.setName("Målinger");
        lineChart.getData().addAll(series);


    }
}
