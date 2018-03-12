package tdt4140.gr1817.app.ui.javafx;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javax.inject.Singleton;
import java.util.ResourceBundle;

/**
 * Guice module to provide classes needed for JavaFX.
 */
public class JavaFxModule extends AbstractModule {

    private final Stage stage;

    /**
     * Create the module with the given {@code stage}.
     * The stage will be provided to all objects that ask for a {@link Stage}.
     * @param stage
     */
    public JavaFxModule(Stage stage) {
        this.stage = stage;
    }

    @Override
    protected void configure() {
        bind(Stage.class).toInstance(stage);
    }

    @Provides
    public FXMLLoader get(Injector injector) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(injector::getInstance);
        return fxmlLoader;
    }

    @Provides @Singleton
    public ResourceBundle provideUiResourceBundle() {
        return ResourceBundle.getBundle("tdt4140.gr1817.app.ui.UI");
    }
}
