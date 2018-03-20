package tdt4140.gr1817.app.ui.feature.userlist;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

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
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class SeeUsersIT extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        final List<User> usersList = Arrays.asList(
                createUser(1, 25).firstName("Pizza").lastName("Spice").build(),
                createUser(2, 22).firstName("Pizza").lastName("Topping").build()
        );
        Mockito.when(userRepositoryMock.query(Mockito.any())).thenReturn(usersList);

        final Injector injector = Guice.createInjector(new JavaFxModule(stage),
                new MockedUserRepositoryModule(userRepositoryMock));
        final Navigator navigator = injector.getInstance(Navigator.class);
        navigator.navigate(Page.SEE_USERS);
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
        verifyThat("#viewSelectedUser", NodeMatchers.isDisabled());
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