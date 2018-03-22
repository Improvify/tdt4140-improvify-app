package tdt4140.gr1817.app.ui.feature.userlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.app.core.feature.user.GetAllUsers;
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
    private final UserItemAdapter userItemAdapter;
    private final Navigator navigator;

    @FXML
    private Button viewSelectedUser;
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

    @Inject
    public SeeUsersController(Navigator navigator, Provider<GetAllUsers> getAllUsersProvider,
                              UserItemAdapter userItemAdapter) {
        this.navigator = navigator;
        this.getAllUsersProvider = getAllUsersProvider;
        this.userItemAdapter = userItemAdapter;
    }

    @FXML
    public void initialize() {
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        userTable.setItems(userItemList);

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

    @FXML
    public void showSelectedUser() {
        if (userTable != null) {
            log.debug("Showing {}", userTable.getSelectionModel().getSelectedItem());
        }
        navigator.navigate(Page.VIEW_USER);
    }

    @FXML
    public void showGlobalStatistics() {
        navigator.navigate(Page.GLOBAL_STATISTICS);
    }

}
