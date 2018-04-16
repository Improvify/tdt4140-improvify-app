package tdt4140.gr1817.app.ui.feature.userlist;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Assume;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import tdt4140.gr1817.app.ui.feature.util.MockedUserRepositoryModule;
import tdt4140.gr1817.app.ui.feature.util.NavigatorSpyModule;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Tests that the {@link SeeUsersController} works correctly.
 *
 * @author Kristian Rekstad
 */
public class SeeUsersIT extends ApplicationTest {

    private SeeUsersPage seeUsersPage = new SeeUsersPage(this);

    private Navigator navigatorSpy;
    private WeightRepository weightRepositoryMock;
    private RestingHeartRateRepository restingHeartRateRepository;

    @Override
    public void start(Stage stage) throws Exception {
        final List<User> usersList = Arrays.asList(
                createUser(1, 25).firstName("Pizza").lastName("Spice").build(),
                createUser(2, 22).firstName("Pizza").lastName("Topping").build()
        );

        MockedUserRepositoryModule mockedUserRepositoryModule = new MockedUserRepositoryModule();
        UserRepository userRepositoryMock = mockedUserRepositoryModule.getUserRepositoryMock();
        Mockito.when(userRepositoryMock.query(Mockito.any())).thenReturn(usersList);

        weightRepositoryMock = Mockito.mock(WeightRepository.class);
        restingHeartRateRepository = Mockito.mock(RestingHeartRateRepository.class);

        final Injector injector = Guice.createInjector(
                new JavaFxModule(stage),
                mockedUserRepositoryModule,
                new NavigatorSpyModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(WeightRepository.class).toInstance(weightRepositoryMock);
                        bind(RestingHeartRateRepository.class).toInstance(restingHeartRateRepository);
                    }
                }
                );

        navigatorSpy = injector.getInstance(Navigator.class);
        navigatorSpy.navigate(Page.SEE_USERS);
        Mockito.reset(navigatorSpy);
    }

    @Test
    public void shouldSetUserFieldsInTableColumns() throws Exception {
        // Given
        final Set<Text> firstnameTexts = seeUsersPage.firstnameColumnTextsWithText("Pizza");
        final Set<Text> lastnameTexts = seeUsersPage.lastnameColumnTextWithText("Topping");
        final Set<Node> ageTexts = seeUsersPage.ageColumnWithAge(22);

        // Then
        assertThat(firstnameTexts, hasSize(2));
        assertThat(lastnameTexts, hasSize(1));
        assertThat(ageTexts, hasSize(1));

        verifyThat(seeUsersPage.userTable(), NodeMatchers.isVisible());
    }

    @Test
    public void shouldDisableViewUserButtonWhenNoUserIsSelected() throws Exception {
        // Given
        TableView<UserItem> table = seeUsersPage.userTable();
        Assume.assumeThat(table.getSelectionModel().isEmpty(), is(true));

        // Then
        verifyThat(seeUsersPage.viewSelectedUserButton(), NodeMatchers.isDisabled());
    }

    @Test
    public void shouldEnableViewUserButtonWhenUserIsSelected() throws Exception {
        // Given
        TableView<UserItem> table = seeUsersPage.userTable();

        // When
        table.getSelectionModel().select(0);
        Assume.assumeThat(table.getSelectionModel().isEmpty(), is(false));

        // Then
        verifyThat(seeUsersPage.viewSelectedUserButton(), NodeMatchers.isEnabled());
    }

    @Test
    public void shouldNavigateWhenGlobalStatisticsIsClicked() throws Exception {
        // When
        clickOn(seeUsersPage.viewGlobalStatistics());

        // Then
        Mockito.verify(navigatorSpy).navigate(Page.GLOBAL_STATISTICS);
    }

    @Test
    public void shouldNavigateWhenViewUserIsClicked() throws Exception {
        // Given
        final TableView<UserItem> table = seeUsersPage.userTable();
        table.getSelectionModel().select(0);
        Assume.assumeThat(table.getSelectionModel().getSelectedItem(), is(notNullValue()));

        // When
        clickOn(seeUsersPage.viewSelectedUserButton());

        // Then
        Mockito.verify(navigatorSpy).navigate(Page.VIEW_USER);
    }

    private static User.UserBuilder createUser(int i, int age) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, -age);
        final Date birthDate = calendar.getTime();

        return User.builder()
                .id(i)
                .firstName("Test")
                .lastName("Test2")
                .birthDate(birthDate)
                .email("test@test.com");
    }

}