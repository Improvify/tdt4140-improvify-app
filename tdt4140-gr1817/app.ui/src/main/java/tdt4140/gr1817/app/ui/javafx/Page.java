package tdt4140.gr1817.app.ui.javafx;

import java.util.ResourceBundle;

/**
 * Enum of all pages in the application that a user can navigate to.
 */
public enum Page {

    CREATE_WORKOUT {
        @Override
        public String fxmlPath() {
            return "create_workout.fxml";
        }

        @Override
        public String titleKey() {
            return "create_workout.title";
        }
    };

    /**
     * Path to fxml file in resources.
     *
     * <p>
     * Relative to the package where the file is loaded.
     * Use {@code /} to indicate that the file lies at the root in resources.
     * </p>
     */
    public abstract String fxmlPath();

    /**
     * The key in a {@link ResourceBundle} for the screen title.
     * @see javafx.stage.Stage#setTitle(String)
     */
    public abstract String titleKey();
}
