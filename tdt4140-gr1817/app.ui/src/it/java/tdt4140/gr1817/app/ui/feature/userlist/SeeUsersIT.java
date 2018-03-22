package tdt4140.gr1817.app.ui.feature.userlist;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Assume;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.app.ui.javafx.SceneFactory;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
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

    private Navigator navigatorSpy = null;

    @Override
    public void start(Stage stage) throws Exception {
        final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        final List<User> usersList = Arrays.asList(
                createUser(1, 25).firstName("Pizza").lastName("Spice").build(),
                createUser(2, 22).firstName("Pizza").lastName("Topping").build()
        );
        Mockito.when(userRepositoryMock.query(Mockito.any())).thenReturn(usersList);

        final Injector injector = Guice.createInjector(new JavaFxModule(stage),
                new MockedUserRepositoryModule(userRepositoryMock),
                new AbstractModule() {
                    @Override
                    protected void configure() { }

                    @Provides @Singleton
                    public Navigator provideSpyNavigator(Stage stage1, ResourceBundle bundle, Provider<FXMLLoader> loader, SceneFactory sceneFactory) {
                        final Navigator navigator = new Navigator(stage1, bundle, loader, sceneFactory);
                        navigatorSpy = Mockito.spy(navigator);
                        return navigatorSpy;
                    }
                });

        final Navigator navigator = injector.getInstance(Navigator.class);
        navigator.navigate(Page.SEE_USERS);
        Mockito.reset(navigatorSpy);
    }

    @Test
    public void shouldSetUserFieldsInTableColumns() throws Exception {
        final Set<Node> firstnameTexts = from(lookup("#firstnameColumn"))
                .lookup(NodeMatchers.hasText("Pizza"))
                .match(node -> node instanceof Text)
                .queryAll();

        final Set<Node> lastnameTexts = from(lookup("#lastnameColumn"))
                .lookup(NodeMatchers.hasText("Topping"))
                .match(node -> node instanceof Text)
                .queryAll();

        final Set<Node> ageTexts = from(lookup("#ageColumn"))
                .lookup("22")
                .queryAll();

        assertThat(firstnameTexts, hasSize(2));
        assertThat(lastnameTexts, hasSize(1));
        assertThat(ageTexts, hasSize(1));

        verifyThat("#userTable", NodeMatchers.isVisible());
    }

    @Test
    public void shouldDisableViewUserButtonWhenNoUserIsSelected() throws Exception {
        // Given
        TableView<UserItem> table = lookup("#userTable").query();
        Assume.assumeThat(table.getSelectionModel().isEmpty(), is(true));

        // Then
        verifyThat("#viewSelectedUser", NodeMatchers.isDisabled());
    }

    @Test
    public void shouldEnableViewUserButtonWhenUserIsSelected() throws Exception {
        // Given
        TableView<UserItem> table = lookup("#userTable").query();

        // When
        table.getSelectionModel().select(0);
        Assume.assumeThat(table.getSelectionModel().isEmpty(), is(false));

        // Then
        verifyThat("#viewSelectedUser", NodeMatchers.isEnabled());
    }

    @Test
    public void shouldNavigateWhenGlobalStatisticsIsClicked() throws Exception {
        // When
        clickOn("#globalStatistics");

        // Then
        Mockito.verify(navigatorSpy).navigate(Page.GLOBAL_STATISTICS);
    }

    @Test
    public void shouldNavigateWhenViewUserIsClicked() throws Exception {
        // Given
        final TableView<UserItem> table = lookup("#userTable").query();
        table.getSelectionModel().select(0);
        Assume.assumeThat(table.getSelectionModel().getSelectedItem(), is(notNullValue()));

        // When
        clickOn("#viewSelectedUser");

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

    private static class MockedUserRepositoryModule extends AbstractModule {
        private final UserRepository userRepositoryMock;

        public MockedUserRepositoryModule(UserRepository userRepositoryMock) {
            this.userRepositoryMock = userRepositoryMock;
        }

        @Override
        protected void configure() {
            bind(UserRepository.class).toInstance(userRepositoryMock);
        }
    }
}