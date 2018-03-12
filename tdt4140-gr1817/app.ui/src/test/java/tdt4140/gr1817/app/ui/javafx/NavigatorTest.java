package tdt4140.gr1817.app.ui.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class NavigatorTest {

    private Stage stageMock;
    private ResourceBundle resourceBundle;
    private FXMLLoader fxmlLoaderMock;
    private Navigator navigator;

    @Before
    public void setUp() throws Exception {
        stageMock = Mockito.mock(Stage.class);
        fxmlLoaderMock = Mockito.mock(FXMLLoader.class);
        resourceBundle = new SingleKeyResourceBundle();

        SceneFactory mockSceneFactory = new SceneFactory() {
            @Override
            public Scene create(Parent root) {
                return Mockito.mock(Scene.class);
            }
        };

        navigator = new Navigator(stageMock, resourceBundle, () -> fxmlLoaderMock, mockSceneFactory);
    }

    @Test
    public void shouldSetStageTitleAndLoadPage() throws Exception {
        // Given
        final String title = SingleKeyResourceBundle.VALUE;

        Page page = Page.CREATE_WORKOUT;
        when(fxmlLoaderMock.load()).thenReturn(Mockito.mock(Parent.class));

        Scene sceneMock = mock(Scene.class);
        when(stageMock.getScene()).thenReturn(sceneMock);

        // When
        navigator.navigate(page);

        // Then
        verify(fxmlLoaderMock).setLocation(argThat(UrlPathMatcher.hasPath(page.fxmlPath())));
        verify(fxmlLoaderMock).load();
        verify(stageMock).setTitle(title);
        verify(stageMock).show();
    }

    @Test(expected = Navigator.InvalidFxmlPathException.class)
    public void shouldThrowOnNonExistingPath() throws Exception {
        // When
        navigator.isValidPath("invalid");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullPath() throws Exception {
        navigator.isValidPath(null);
    }

    @Test(expected = Navigator.InvalidFxmlPathException.class)
    public void shouldThrowWhenPathIsDirectory() throws Exception {
        navigator.isValidPath("/tdt4140/gr1817/app/ui");
    }

    /**
     * Checks if a {@link URL} path ends with a specific value.
     *
     * <p>
     * {@code "somePath"} matches both {@code "http://localhost/123/somePath"}
     * and {@code "ftp://somehost.com/123/somePath?asd=1"}
     * </p>
     */
    public static class UrlPathMatcher extends BaseMatcher<URL> {

        public static UrlPathMatcher hasPath(String path) {
            return new UrlPathMatcher(path);
        }

        private final String path;

        /**
         *
         * @param path the path the {@link URL} should end with
         */
        public UrlPathMatcher(String path) {
            this.path = path;
        }

        @Override
        public boolean matches(Object item) {
            if (item instanceof URL) {
                String path = ((URL) item).getPath();
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                return fileName.equals(this.path);
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("path containing ").appendValue(path);
        }
    }

    /**
     * For testing. Has a single key value pair: <code>{@value KEY} : {@value VALUE}</code>
     */
    static class SingleKeyResourceBundle extends ResourceBundle {

        public static final String VALUE = "Title";
        public static final String KEY = "main.title";

        @Override
        protected Object handleGetObject(String key) {
            return VALUE;
        }

        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(Arrays.asList(KEY));
        }
    }
}