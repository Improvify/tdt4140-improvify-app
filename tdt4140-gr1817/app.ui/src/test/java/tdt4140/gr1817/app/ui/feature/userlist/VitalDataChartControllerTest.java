package tdt4140.gr1817.app.ui.feature.userlist;

import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;

import java.util.ArrayList;
import java.util.Date;

public class VitalDataChartControllerTest {

    ArrayList<Weight> weights;

    @Before
    public void setup(){
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
        this.weights=weights;
    }

    @Test
    public void shouldNotCrashWhenDisplayingMockedData(){
        //TBA
    }
}
