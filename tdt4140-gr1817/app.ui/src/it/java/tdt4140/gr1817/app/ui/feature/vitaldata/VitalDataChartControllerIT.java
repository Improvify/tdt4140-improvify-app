package tdt4140.gr1817.app.ui.feature.vitaldata;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.stage.Stage;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.ui.feature.util.MockedUserRepositoryModule;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
@Ignore("Work in progress")
public class VitalDataChartControllerIT extends ApplicationTest {

    private User user;

    @Override
    public void start(Stage stage) throws Exception {
        user = new User(1, "Test", "Last", 185, new Date(), "username", "123", "");

        MockedUserRepositoryModule mockedUserRepositoryModule = new MockedUserRepositoryModule();
        UserRepository userRepositoryMock = mockedUserRepositoryModule.getUserRepositoryMock();
        Mockito.when(userRepositoryMock.query(any()))
                .thenReturn(Collections.singletonList(user));

        Injector injector = Guice.createInjector(
                new JavaFxModule(stage),
                mockedUserRepositoryModule
        );
        Navigator navigator = injector.getInstance(Navigator.class);

        injector.getInstance(UserSelectionService.class)
                .setSelectedUserId(new UserSelectionService.UserId(user.getId()));

        navigator.navigate(Page.VITAL_DATA_CHART);
    }

    @Test
    public void shouldShowWeightDataPoints() throws Exception {
        // TODO
    }

    @Test
    public void shouldShowRestingHeartRateDataPoints() throws Exception {
        // TODO
    }
}