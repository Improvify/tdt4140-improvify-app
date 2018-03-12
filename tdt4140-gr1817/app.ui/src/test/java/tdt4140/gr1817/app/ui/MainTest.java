package tdt4140.gr1817.app.ui;

import org.junit.Test;
import tdt4140.gr1817.app.ui.javafx.Page;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MainTest {

    @Test
    public void shouldStartWithMainPage() {
        // Given
        Main main = new Main();

        // When
        Page initialPage = main.getInitialPage();

        // Then
        //FIXME: when it should start with something else than create workout, update this test
        assertThat("Returns wrong page", initialPage, is(Page.CREATE_WORKOUT));
    }

}
