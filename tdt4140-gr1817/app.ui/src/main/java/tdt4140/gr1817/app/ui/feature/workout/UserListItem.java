package tdt4140.gr1817.app.ui.feature.workout;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;

/**
 * Just holds data about a user for the {@link TableView}.
 */
public class UserListItem {
    private SimpleStringProperty username;
    private SimpleIntegerProperty id;

    public UserListItem(String username, int id) {
        this.username = new SimpleStringProperty(username);
        this.id = new SimpleIntegerProperty(id);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }
}
