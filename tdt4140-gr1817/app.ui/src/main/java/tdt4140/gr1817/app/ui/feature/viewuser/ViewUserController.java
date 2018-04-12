package tdt4140.gr1817.app.ui.feature.viewuser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.app.core.feature.user.GetUserWithId;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
@Slf4j
public class ViewUserController {
    private final UserSelectionService userSelectionService;
    private final Optional<UserSelectionService.UserId> selectedUser;
    private final GetUserWithId getUserWithId;
    private final Navigator navigator;

    private User user;

    public Label fullName;
    public Label height;
    public Label email;
    public Label age;


    @Inject
    public ViewUserController(UserSelectionService userSelectionService, GetUserWithId getUserWithId,
                              Navigator navigator) {
        this.userSelectionService = userSelectionService;
        selectedUser = userSelectionService.getSelectedUserId();
        this.getUserWithId = getUserWithId;
        this.navigator = navigator;
    }

    public void initialize() {
        if (selectedUser.isPresent()) {
            // Set fields and get data for graph
            int userId = selectedUser.get().getId();
            loadUserData(userId);
            displayUser();
        } else {
            log.warn("No user selected");
            // Either show a warning in UI, or navigate to user selection screen
        }
    }

    public void loadUserData(int userId) {
        user = getUserWithId.getUserWithId(userId);
    }

    public void displayUser() {
        if (user != null) {
            fullName.setText(user.getFirstName() + " " + user.getLastName());
            height.setText(Math.round(user.getHeight()) / 100 + "m tall");
            email.setText(user.getEmail());

            final GregorianCalendar birthDate = new GregorianCalendar();
            birthDate.setTime(user.getBirthDate());
            final LocalDate localBirthDate = LocalDate.of(birthDate.get(Calendar.YEAR),
                    birthDate.get(Calendar.MONTH) + 1, birthDate.get(Calendar.DAY_OF_MONTH));
            final int age = (int) ChronoUnit.YEARS.between(localBirthDate, LocalDate.now());
            this.age.setText(String.valueOf(age) + " years old");
        }
    }

    @FXML
    public void goToSeeUsers(ActionEvent event) {
        navigator.navigate(Page.SEE_USERS);
    }
}
