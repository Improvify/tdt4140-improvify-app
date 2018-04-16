package tdt4140.gr1817.app.ui.feature.userlist;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.app.core.feature.user.GetAllUsers;
import tdt4140.gr1817.app.core.feature.user.UserSelectionService;
import tdt4140.gr1817.app.ui.javafx.Navigator;
import tdt4140.gr1817.app.ui.javafx.Page;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A screen for showing all users.
 *
 * @author Kristian Rekstad
 */
@Slf4j
public class SeeUsersController {

    final ObservableList<UserItem> userItemList = FXCollections.observableArrayList();
    private final Provider<GetAllUsers> getAllUsersProvider;
    private final UserSelectionService userSelectionService;
    private final UserItemAdapter userItemAdapter;
    private final Navigator navigator;

    @FXML
    private Button viewSelectedUser;
    @FXML
    private Button createWorkoutButton;
    @FXML
    private TableView<UserItem> userTable;
    @FXML
    private TableColumn<UserItem, String> firstnameColumn;
    @FXML
    private TableColumn<UserItem, String> lastnameColumn;
    @FXML
    private TableColumn<UserItem, String> emailColumn;
    @FXML
    private TableColumn<UserItem, Integer> ageColumn;

    public ReadOnlyObjectProperty<UserItem> selectedUserItem = new SimpleObjectProperty<>();

    @Inject
    public SeeUsersController(Navigator navigator, Provider<GetAllUsers> getAllUsersProvider,
                              UserSelectionService userSelectionService, UserItemAdapter userItemAdapter) {
        this.navigator = navigator;
        this.getAllUsersProvider = getAllUsersProvider;
        this.userSelectionService = userSelectionService;
        this.userItemAdapter = userItemAdapter;
    }

    @FXML
    public void initialize() {
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        userTable.setItems(userItemList);
        selectedUserItem = userTable.getSelectionModel().selectedItemProperty();

        viewSelectedUser.disableProperty().bind(userTable.getSelectionModel().selectedItemProperty().isNull());

        loadUsers();
    }

    public void loadUsers() {
        final List<UserItem> userItems = getAllUsersProvider.get().getAll()
                .stream()
                .map(userItemAdapter::adapt)
                .collect(Collectors.toList());

        userItemList.setAll(userItems);
    }

    public void setSelectedUser(UserItem user) {
        if (user == null) {
            userSelectionService.setSelectedUserId(null);
            return;
        }
        UserSelectionService.UserId userId = new UserSelectionService.UserId(user.getUserId());
        userSelectionService.setSelectedUserId(userId);
    }

    @FXML
    public void showSelectedUser() {
        UserItem selectedUser = selectedUserItem.get();
        setSelectedUser(selectedUser);
        if (selectedUser == null) {
            log.error("Selected user is null! User table is {}null", userTable == null ? "" : "not ");
        }

        navigator.navigate(Page.VIEW_USER);
    }

    @FXML
    public void showWorkoutPage() {
        navigator.navigate(Page.CREATE_WORKOUT);
    }

    @FXML
    public void showGlobalStatistics() {
        navigator.navigate(Page.GLOBAL_STATISTICS);
    }

}
