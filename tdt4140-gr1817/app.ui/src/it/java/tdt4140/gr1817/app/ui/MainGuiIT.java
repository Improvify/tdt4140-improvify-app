package tdt4140.gr1817.app.ui;

import javafx.stage.Stage;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.Matchers.*;

/**
 * Placeholder test to verify that Maven Failsafe picks it up
 */
@Ignore("Flaky UI test. Times out. Disable temporarily as it does nothing useful yet.")
public class MainGuiIT extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new Main().start(this.stage);
        stage.show();
    }



    @Test
    public void shouldOpenProgram() throws Exception {
        MatcherAssert.assertThat(stage.isShowing(), is(true));
    }
}
