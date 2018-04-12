package tdt4140.gr1817.app.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlConnectionModule;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlRepositoryModule;

@Slf4j
public class Main extends Application {


    private Injector injector;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO get database info from argLine or env or a config file.
        injector = Guice.createInjector(new JavaFxModule(primaryStage),
                new MySqlConnectionModule("root", "", "localhost", "ecosystem", 3306),
                new MySqlRepositoryModule());

        Navigator navigator = injector.getInstance(Navigator.class);
        navigator.navigate(getInitialPage());
    }

    /**
     * Returns the first page that the app should show.
     * <p>
     * For debugging purposes, it can be overridden with the VM option {@code -DstartPage=}.
     * Valid values for {@code startPage} can be found in {@link #getPageFromProperty(String)}.
     * </p>
     *
     * @return {@link Page} to start program on
     * @see #getPageFromProperty(String)
     */
    protected Page getInitialPage() {
        String initialPage = System.getProperty("startPage");
        if (initialPage != null) {
            log.info("Overriding initial page: {}", initialPage);
            return getPageFromProperty(initialPage);
        }
        return Page.CREATE_WORKOUT;
    }

    protected static Page getPageFromProperty(String startPageValue) {
        switch (startPageValue.toUpperCase()) {
            case "SEE_USERS":
                return Page.SEE_USERS;
            case "CREATE_WORKOUT":
                return Page.CREATE_WORKOUT;
            case "GLOBAL_STATISTICS":
                return Page.GLOBAL_STATISTICS;
            default:
                throw new IllegalArgumentException("Unrecognized page: " + startPageValue);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
