package tdt4140.gr1817.app.ui.feature.viewuser;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.stage.Stage;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.ui.feature.util.MockedUserRepositoryModule;
import tdt4140.gr1817.app.ui.feature.util.NavigatorSpyModule;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class ViewUserControllerIT extends ApplicationTest {

    private ViewUserPage viewUserPage = new ViewUserPage(this);
    private Navigator navigatorSpy;
    private UserRepository userRepositoryMock;
    private WeightRepository weightRepositoryMock;
    private RestingHeartRateRepository restingHeartRateRepository;

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        MockedUserRepositoryModule mockedUserRepositoryModule = new MockedUserRepositoryModule();
        userRepositoryMock = mockedUserRepositoryModule.getUserRepositoryMock();
        weightRepositoryMock = Mockito.mock(WeightRepository.class);
        restingHeartRateRepository = Mockito.mock(RestingHeartRateRepository.class);

        Injector injector = Guice.createInjector(
                new JavaFxModule(stage),
                new NavigatorSpyModule(),
                mockedUserRepositoryModule,
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(WeightRepository.class).toInstance(weightRepositoryMock);
                        bind(RestingHeartRateRepository.class).toInstance(restingHeartRateRepository);
                    }
                }
        );

        UserSelectionService userSelectionService = injector.getInstance(UserSelectionService.class);
        userSelectionService.setSelectedUserId(new UserSelectionService.UserId(1));

        navigatorSpy = injector.getInstance(Navigator.class);
        navigatorSpy.navigate(Page.VIEW_USER);
        Mockito.reset(navigatorSpy);
    }

    @Test
    public void shouldNavigateToSeeUsersWhenBackClicked() throws Exception {
        // When
        clickOn(viewUserPage.backButton());

        // Then
        Mockito.verify(navigatorSpy).navigate(Page.SEE_USERS);
    }

    @Test
    public void shouldShowSelectedUserInfo() throws Exception {
        // Given
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        Date birthDate = calendar.getTime();

        User user = new User(1, "Bob", "TestMan", 185, birthDate, null, null, "bob@gmail.com");
        when(userRepositoryMock.query(any()))
                .thenReturn(Collections.singletonList(user));

        // When
        interact(() -> navigatorSpy.navigate(Page.VIEW_USER)); // make the controller read from repository again

        // Then
        verifyThat(viewUserPage.fullNameLabel(), hasText("Bob TestMan"));
        verifyThat(viewUserPage.emailLabel(), hasText("bob@gmail.com"));
        verifyThat(viewUserPage.ageLabel(), hasText("18 years old"));
    }

}