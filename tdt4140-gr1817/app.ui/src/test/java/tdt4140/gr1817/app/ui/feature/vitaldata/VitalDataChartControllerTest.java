package tdt4140.gr1817.app.ui.feature.vitaldata;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.app.core.feature.user.GetUserWithId;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.core.feature.user.vitaldata.GetVitalDataForUser;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VitalDataChartControllerTest {

    public static final double HEAVIEST_WEIGHT = 79d;
    public static final double LIGHTEST_WEIGHT = 60d;

    List<Weight> weights;
    private VitalDataChartController controller;
    private GetUserWithId getUserWithIdMock;
    private UserSelectionService userSelectionService;
    private GetVitalDataForUser vitalDataForUserMock;
    private User user;
    private DataPointToSeriesAdapter dataPointToSeriesAdapter;
    private Date earliestDate;
    private Date latestDate;

    @Before
    public void setup() {
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

        earliestDate = new Date(2018, 11, 10);
        latestDate = new Date(2019, 8, 25);

        this.weights = Arrays.asList(
                new Weight(1, (float)HEAVIEST_WEIGHT, earliestDate, user),
                new Weight(2, 78, new Date(2018, 12, 10), user),
                new Weight(3, 75, new Date(2019, 1, 10), user),
                new Weight(4, 73, new Date(2019, 2, 10), user),
                new Weight(5, 78, new Date(2019, 3, 10), user),
                new Weight(5, (float)LIGHTEST_WEIGHT, latestDate, user)
        );

        GetVitalDataForUser.VitalData vitalData = new GetVitalDataForUser.VitalData(null, weights, Collections.emptyList());
        Mockito.when(vitalDataForUserMock.load()).thenReturn(vitalData);
    }

    @Test
    public void shouldNotCrashWhenDisplayingMockedData() {
        //TBA
    }

    @Test
    public void shouldSetDefaultAxisBoundsWhenNoUserDataExists() throws Exception {
        // Given
        Calendar calendar = GregorianCalendar.getInstance();


        // When
        controller.loadData();

        long xLower = controller.xLowerBound.get();
        long xUpper = controller.xUpperBound.get();
        double yLower = controller.yLowerBound.get();
        double yUpper = controller.yUpperBound.get();

        // Then

        assertThat(xLower, is(earliestDate.getTime()));
        assertThat(xUpper, is(latestDate.getTime()));
        assertThat(yLower, is(LIGHTEST_WEIGHT));
        assertThat(yUpper, is(HEAVIEST_WEIGHT));
    }
}
