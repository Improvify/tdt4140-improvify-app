package tdt4140.gr1817.app.ui.feature.viewuser;

import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
@Slf4j
public class ViewUserController {
    private final UserSelectionService userSelectionService;

    public Label fullName;
    public Label age;
    public Label height;
    public Label email;

    @Inject
    public ViewUserController(UserSelectionService userSelectionService) {
        this.userSelectionService = userSelectionService;
        Optional<UserSelectionService.UserId> selectedUser = userSelectionService.getSelectedUserId();

        if (selectedUser.isPresent()) {
            // Set fields and get data for graph
            int userId = selectedUser.get().getId();

        } else {
            log.warn("No user selected");
            // Either show a warning in UI, or navigate to user selection screen
        }
    }

    public void initialize() {
    }
}
