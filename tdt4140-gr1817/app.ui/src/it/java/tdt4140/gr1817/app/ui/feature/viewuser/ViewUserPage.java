package tdt4140.gr1817.app.ui.feature.viewuser;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.testfx.api.FxRobot;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class ViewUserPage {

    private static final String BACK_BUTTON_ID = "#back";
    public static final String FULLLNAME_LABEL_ID = "#fullName";
    public static final String EMAIL_LABEL_ID = "#email";
    public static final String AGE_LABEL_ID = "#age";

    private final FxRobot robot;

    public ViewUserPage(FxRobot robot) {
        this.robot = robot;
    }

    public Button backButton() {
        return robot.lookup(BACK_BUTTON_ID)
                .query();
    }

    Label fullNameLabel() {
        return robot.lookup(FULLLNAME_LABEL_ID)
                .query();
    }

    Label emailLabel() {
        return robot.lookup(EMAIL_LABEL_ID)
                .query();
    }

    Label ageLabel() {
        return robot.lookup(AGE_LABEL_ID)
                .query();
    }
}
