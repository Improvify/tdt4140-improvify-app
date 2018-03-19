package tdt4140.gr1817.app.ui.feature.userlist;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.inject.Inject;

/**
 * A screen for showing all users.
 *
 * @author Kristian Rekstad
 */
public class SeeUsersController {

    private final ObservableList<User> userList = FXCollections.observableArrayList();


    @FXML
    private Button viewSelectedUser;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> firstnameColumn;
    @FXML
    private TableColumn<User, String> lastnameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Integer> ageColumn;

    @Inject
    public SeeUsersController() {
    }

    @FXML
    public void initialize() {
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        userTable.setItems(userList);

        viewSelectedUser.disableProperty().bind(userTable.selectionModelProperty().isNull());

        //loadUsers();
    }

    private void loadUsers() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @FXML
    public void showSelectedUser() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @FXML
    public void showGlobalStatistics() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Just holds data about a user for the {@link TableView}.
     */
    public static class User {
        private SimpleStringProperty firstName;
        private SimpleStringProperty lastName;
        private SimpleStringProperty email;
        private SimpleIntegerProperty age;

        public User(String firstName, String lastName, String email, int age) {
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.email = new SimpleStringProperty(email);
            this.age = new SimpleIntegerProperty(age);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public SimpleStringProperty firstNameProperty() {
            return firstName;
        }

        public String getLastName() {
            return lastName.get();
        }

        public SimpleStringProperty lastNameProperty() {
            return lastName;
        }

        public String getEmail() {
            return email.get();
        }

        public SimpleStringProperty emailProperty() {
            return email;
        }

        public int getAge() {
            return age.get();
        }

        public SimpleIntegerProperty ageProperty() {
            return age;
        }
    }

}
