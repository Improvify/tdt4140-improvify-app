package tdt4140.gr1817.app.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.app.ui.javafx.JavaFxModule;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.PropertyConnectionConfigurationSource;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlConnectionModule;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlRepositoryModule;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetServiceProviderByNameSpecification;

import java.sql.Connection;

@Slf4j
public class Main extends Application {


    private Injector injector;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO document that you can set database args using VM options. -Ddb.password="" for blank password etc.
        PropertyConnectionConfigurationSource conf = new PropertyConnectionConfigurationSource();

        injector = Guice.createInjector(new JavaFxModule(primaryStage),
                new MySqlConnectionModule(conf.user, conf.password, conf.host, "ecosystem", conf.port),
                new MySqlRepositoryModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {

                    }

                    @Provides
                    @Named("improvify")
                    public ServiceProvider providerImprovifyServiceProvider(ServiceProviderRepository repository) {
                        return repository.query(new GetServiceProviderByNameSpecification("improvify")).get(0);

                    }
                }
        );

        conf.validate(injector.getInstance(Connection.class));

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
        return Page.SEE_USERS;
    }

    protected static Page getPageFromProperty(String startPageValue) {
        switch (startPageValue.toUpperCase()) {
            case "SEE_USERS":
                return Page.SEE_USERS;
            case "WORKOUT_SESSION_DISPLAY":
                return Page.WORKOUT_SESSION_DISPLAY;
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
