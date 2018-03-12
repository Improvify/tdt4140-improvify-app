package tdt4140.gr1817.app.ui.javafx;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ResourceBundle;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class JavaFxModuleTest {


    @Test
    public void shouldProvideUiBundle() throws Exception {
        // Given
        final JavaFxModule javaFxModule = new JavaFxModule(null);

        // When
        final ResourceBundle bundle = javaFxModule.provideUiResourceBundle();

        // Then
        assertThat(bundle, is(notNullValue()));
    }

    @Test
    public void shouldConnectFxmlLoaderToGuice() throws Exception {
        // Given
        final JavaFxModule javaFxModule = new JavaFxModule(null);
        final Injector injector = Mockito.spy(Guice.createInjector());

        // When
        final FXMLLoader fxmlLoader = javaFxModule.get(injector);
        fxmlLoader.getControllerFactory().call(String.class);

        // Then
        Mockito.verify(injector).getInstance(String.class);
    }
}