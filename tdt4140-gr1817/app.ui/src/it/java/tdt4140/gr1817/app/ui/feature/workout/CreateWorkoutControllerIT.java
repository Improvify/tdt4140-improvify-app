package tdt4140.gr1817.app.ui.feature.workout;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;
import tdt4140.gr1817.app.ui.feature.util.MockedUserRepositoryModule;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRowRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests that the {@link CreateWorkoutController} works correctly.
 *
 * @author Claus Martinsen
 */
public class CreateWorkoutControllerIT extends ApplicationTest {

    private UserRepository userRepositoryMock;
    private WorkoutPlanRowRepository workoutPlanRowRepositoryMock;
    private WorkoutPlanRepository workoutPlanRepositoryMock;

    @Override
    public void start(Stage stage) throws Exception {
        workoutPlanRepositoryMock = Mockito.mock(WorkoutPlanRepository.class);
        workoutPlanRowRepositoryMock = Mockito.mock(WorkoutPlanRowRepository.class);

        MockedUserRepositoryModule mockedUserRepositoryModule = new MockedUserRepositoryModule();
        userRepositoryMock = mockedUserRepositoryModule.getUserRepositoryMock();

        final Injector injector = Guice.createInjector(
                new JavaFxModule(stage),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(WorkoutPlanRepository.class).toInstance(workoutPlanRepositoryMock);
                        bind(WorkoutPlanRowRepository.class).toInstance(workoutPlanRowRepositoryMock);
                    }
                },
                mockedUserRepositoryModule
        );

        final Navigator navigator = injector.getInstance(Navigator.class);
        navigator.navigate(Page.CREATE_WORKOUT);
    }


    @Test
    public void shouldAddRowWhenClickingAdd() {
        // Given
        final TableView<WorkoutRow> tableView = lookup("#workoutTable").query();
        final int oldRowCount = tableView.getItems().size();

        // When
        clickOn("#addButton");
        final int newRowCount = tableView.getItems().size();

        // Then
        assertThat(newRowCount, is(oldRowCount + 1));
    }

    @Test
    public void shouldRemoveRowWhenClickingRemoveAfterSelectingRow() {
        // Given
        final TableView<WorkoutRow> tableView = lookup("#workoutTable").query();
        final int oldRowCount = tableView.getItems().size();

        // When
        tableView.getSelectionModel().selectFirst();
        clickOn("#deleteButton");
        final int newRowCount = tableView.getItems().size();

        // Then
        assertThat(newRowCount, is(oldRowCount - 1));
    }
}