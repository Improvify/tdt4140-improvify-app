package tdt4140.gr1817.app.ui;

import javafx.stage.Stage;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.Matchers.is;

/**
 * Placeholder test to verify that Maven Failsafe picks it up
 */
public class MainGuiIT extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new FxApp().start(this.stage);
        stage.show();
    }



    @Test
    public void shouldOpenProgram() throws Exception {
        MatcherAssert.assertThat(stage.isShowing(), is(true));
    }
}
