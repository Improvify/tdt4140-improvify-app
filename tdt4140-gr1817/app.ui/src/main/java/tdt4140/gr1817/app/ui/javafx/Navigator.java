package tdt4140.gr1817.app.ui.javafx;

import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class Navigator {

    /**
     * Base path to fxml files in resources.
     */
    public static final String BASE_PACKAGE = "/tdt4140/gr1817/app/ui/fxml/";

    private final Stage stage;
    private ResourceBundle titleBundle;
    private final Provider<FXMLLoader> loaderProvider;
    private final SceneFactory sceneFactory;

    /**
     * <p>Create a navigator that navigates within the given {@code stage},
     * and loads titles from the given {@code titleBundle} using {@link Page#titleKey()}.</p>
     *
     * @param stage JavaFX stage
     * @param titleBundle bundle with keys for stage title, obtained using {@link ResourceBundle#getString(String)}
     *                    with {@link Page#titleKey()}
     * @param loaderProvider provider that creates new {@link FXMLLoader}s. It's important that the generated loader
     *                       uses {@link Injector} as a
     *                       {@link FXMLLoader#setControllerFactory(Callback) Controller factory}
     * @see Page
     * @see #navigate(Page)
     * @see Stage#setTitle(String)
     */
    @Inject
    public Navigator(Stage stage, ResourceBundle titleBundle, Provider<FXMLLoader> loaderProvider,
                     SceneFactory sceneFactory) {
        this.stage = stage;
        this.titleBundle = titleBundle;
        this.loaderProvider = loaderProvider;
        this.sceneFactory = sceneFactory;
    }

    /**
     * Navigate to the given page. Changes the current {@link Scene} in the {@link Stage}
     * @param page
     * @throws InvalidFxmlPathException if {@link Page#fxmlPath()} returns an invalid path
     */
    public void navigate(final Page page) throws InvalidFxmlPathException {
        String fxmlPath = BASE_PACKAGE + page.fxmlPath();
        isValidPath(fxmlPath);

        FXMLLoader fxmlLoader = loaderProvider.get();
        fxmlLoader.setResources(titleBundle);
        fxmlLoader.setLocation(getClass().getResource(fxmlPath));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            throw new InvalidFxmlPathException(fxmlPath, e);
        }

        stage.setTitle(titleBundle.getString(page.titleKey()));

        stage.setScene(sceneFactory.create(root));
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Validates a fxml path.
     * @param path the path to validate
     * @throws InvalidFxmlPathException if the path is invalid
     */
    protected void isValidPath(@NonNull final String path) throws InvalidFxmlPathException {
        URL resource = getClass().getResource(path);
        if (resource == null) {
            String searchPath = path;
            if (! path.startsWith("/")) {
                searchPath = "/" + getClass().getPackage().getName().replace(".", "/") + "/" + path;
            }

            FileNotFoundException cause = new FileNotFoundException(searchPath);
            throw new InvalidFxmlPathException(path, cause);
        }

        try {
            final String protocol = resource.getProtocol(); // jar, file, http
            if ("file".equals(protocol)) {
                File fxmlFile = new File(resource.toURI());
                if (!fxmlFile.exists() || !fxmlFile.isFile()) {
                    FileNotFoundException cause = new FileNotFoundException(resource.toURI().toString());
                    throw new InvalidFxmlPathException(path, cause);
                }
            } else if ("jar".equals(protocol)) {
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                if (jarURLConnection.getJarEntry() == null) {
                    throw new InvalidFxmlPathException(path);
                }
            }
        } catch (URISyntaxException | IOException ex) {
            throw new InvalidFxmlPathException(path, ex);
        }
    }

    /**
     * An exception indicating that a {@code path} does not point to a fxml file.
     */
    public static class InvalidFxmlPathException extends RuntimeException {
        public InvalidFxmlPathException(String path) {
            super(getMessageForPath(path));
        }

        public InvalidFxmlPathException(String path, Exception cause) {
            super(getMessageForPath(path), cause);
        }

        private static String getMessageForPath(String path) {
            return String.format("Path \"%s\" is not a valid FXML file", path);
        }
    }
}
