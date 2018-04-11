package tdt4140.gr1817.app.ui.javafx;

import java.util.ResourceBundle;

/**
 * Enum of all pages in the application that a user can navigate to.
 */
public enum Page {

    CREATE_WORKOUT("create_workout.fxml", "create_workout.title"),
    SEE_USERS("see_users.fxml", "see_users.title"),
    GLOBAL_STATISTICS("global_statistics.fxml", "global_statistics.title"),
    VIEW_USER("view_user.fxml", "view_user.title"),
    VITAL_DATA_CHART("vital_data_chart.fxml","vital_data_chart.title")
    ;

    private final String fxmlFile;
    private final String titleKey;

    /**
     * Create a new page.
     * @param fxmlFile filename of {@code .fxml} file
     * @param titleKey key in the {@link ResourceBundle} for titles. (Currently {@code resources/UI.properties})
     */
    Page(String fxmlFile, String titleKey) {
        this.fxmlFile = fxmlFile;
        this.titleKey = titleKey;
    }

    /**
     * Path to fxml file in resources.
     *
     * <p>
     * Relative to the package where the file is loaded ({@value Navigator#BASE_PACKAGE}).
     * <br/>
     * Use {@code /} to separate folders, eg.: {@code users/viewall/view_all_users.fxml}
     * </p>
     */
    public String fxmlPath() {
        return fxmlFile;
    }

    /**
     * The key in a {@link ResourceBundle} for the screen title.
     * @see javafx.stage.Stage#setTitle(String)
     */
    public String titleKey() {
        return titleKey;
    }
}
