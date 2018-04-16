package tdt4140.gr1817.app.ui.feature.vitaldata;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.app.core.feature.user.GetUserWithId;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.core.feature.user.vitaldata.GetVitalDataForUser;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VitalDataChartControllerTest {

    ArrayList<Weight> weights;
    private VitalDataChartController controller;
    private GetUserWithId getUserWithIdMock;
    private UserSelectionService userSelectionService;
    private GetVitalDataForUser vitalDataForUserMock;
    private User user;
    private DataPointToSeriesAdapter dataPointToSeriesAdapter;

    @Before
    public void setup(){
        userSelectionService = new UserSelectionService();
        vitalDataForUserMock = Mockito.mock(GetVitalDataForUser.class);
        getUserWithIdMock = Mockito.mock(GetUserWithId.class);
        dataPointToSeriesAdapter = new DataPointToSeriesAdapter();

        controller = new VitalDataChartController(vitalDataForUserMock, userSelectionService, getUserWithIdMock,
                dataPointToSeriesAdapter);


        user = new User(1, "abc", "bcd", 180, new Date(1980, 10, 10),
                "bob", "afskdj", "asdf");

        userSelectionService.setSelectedUserId(new UserSelectionService.UserId(user.getId()));
        Mockito.when(getUserWithIdMock.getUserWithId(user.getId())).thenReturn(user);

        Weight w1 = new Weight(1,79,new Date(2018,11,10), user);
        Weight w2 = new Weight(2,78,new Date(2018,12,10), user);
        Weight w3 = new Weight(3,75,new Date(2019,1,10), user);
        Weight w4 = new Weight(4,73,new Date(2019,2,10), user);
        Weight w5 = new Weight(5,78,new Date(2019,3,10), user);
        Weight w6 = new Weight(5,60,new Date(2019,8,25), user);

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

    @Test
    public void shouldSetDefaultAxisBoundsWhenNoUserDataExists() throws Exception {
        // Given
        Calendar calendar = GregorianCalendar.getInstance();


        // When
        controller.loadData();

        double xLower = controller.xLowerBound.get();
        double xUpper = controller.xUpperBound.get();
        double yLower = controller.yLowerBound.get();
        double yUpper = controller.yUpperBound.get();

        // Then

        assertThat(xLower, is(10));
        assertThat(xUpper, is(10));
        assertThat(yLower, is(10));
        assertThat(yUpper, is(10));
    }
}
