package tdt4140.gr1817.app.ui.feature.workout;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests that the {@link CreateWorkoutController} works correctly.
 *
 * @author Claus Martinsen
 */
public class CreateWorkoutControllerIT extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        final Injector injector = Guice.createInjector(new JavaFxModule(stage));

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