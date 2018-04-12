package tdt4140.gr1817.app.ui.feature.util;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.mockito.Mockito;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.SceneFactory;

import java.util.ResourceBundle;

/**
 * Replaces the navigator with a Mockito spy version,
 * which allows {@link Mockito#verify(Object)} on {@link Navigator}.
 *
 * @author Kristian Rekstad
 */
public class NavigatorSpyModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public Navigator provideSpyNavigator(Stage stage1, ResourceBundle bundle, Provider<FXMLLoader> loader, SceneFactory sceneFactory) {
        final Navigator navigator = new Navigator(stage1, bundle, loader, sceneFactory);
        return Mockito.spy(navigator);
    }
}
