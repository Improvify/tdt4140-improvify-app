package tdt4140.gr1817.app.ui.javafx;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 *
 * Created by Kristian Rekstad.
 * @author Kristian Rekstad
 */
public class InvalidFxmlPathExceptionTest {


    @Test
    public void constructorShouldPutPathInMessage() throws Exception {
        // Given
        final Navigator.InvalidFxmlPathException exception = new Navigator.InvalidFxmlPathException("myPath");

        // When
        final String message = exception.getMessage();

        // Then
        assertThat(message, Matchers.containsString("myPath"));
    }

    @Test
    public void constructorShouldTakeInCause() throws Exception {
        // Given
        final RuntimeException cause = new RuntimeException("cause");
        final Navigator.InvalidFxmlPathException exception = new Navigator.InvalidFxmlPathException("myPath", cause);

        // When
        final Throwable exceptionCause = exception.getCause();

        // Then
        assertThat(exceptionCause, is(sameInstance(cause)));
    }
}