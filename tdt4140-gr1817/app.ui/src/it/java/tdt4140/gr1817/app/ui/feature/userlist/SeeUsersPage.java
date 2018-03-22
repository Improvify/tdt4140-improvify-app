package tdt4140.gr1817.app.ui.feature.userlist;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import org.testfx.api.FxRobot;
import org.testfx.matcher.base.NodeMatchers;
import tdt4140.gr1817.app.ui.javafx.Page;

import java.util.Set;

/**
 * Utility functions for accessing {@link Node Nodes} on a {@link Page}
 *
 * @author Kristian Rekstad
 */
public class SeeUsersPage {

    public static final String FIRSTNAME_COLUMN_ID = "#firstnameColumn";
    public static final String LASTNAME_COLUMN_ID = "#lastnameColumn";
    public static final String USER_TABLE_ID = "#userTable";
    public static final String VIEW_SELECTED_USER_ID = "#viewSelectedUser";
    public static final String GLOBAL_STATISTICS_ID = "#globalStatistics";

    private final FxRobot robot;

    public SeeUsersPage(FxRobot robot) {
        this.robot = robot;
    }

    public Set<Text> firstnameColumnTextsWithText(String text) {
        return robot.from(robot.lookup(FIRSTNAME_COLUMN_ID))
                .lookup(NodeMatchers.hasText(text))
                .match(node -> node instanceof Text)
                .queryAll();
    }

    public Set<Text> lastnameColumnTextWithText(String text) {
        return robot.from(robot.lookup(LASTNAME_COLUMN_ID))
                .lookup(NodeMatchers.hasText(text))
                .match(node -> node instanceof Text)
                .queryAll();
    }

    public Set<Node> ageColumnWithAge(int age) {
        return robot.from(robot.lookup("#ageColumn"))
                .lookup(String.valueOf(age))
                .queryAll();
    }

    public TableView<UserItem> userTable() {
        return robot.lookup(USER_TABLE_ID).query();
    }

    public Button viewSelectedUserButton() {
        return robot.lookup(VIEW_SELECTED_USER_ID).query();
    }

    public Button viewGlobalStatistics() {
        return robot.lookup(GLOBAL_STATISTICS_ID).query();
    }
}
