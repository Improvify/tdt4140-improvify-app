package tdt4140.gr1817.app.ui.feature.workout;

import tdt4140.gr1817.ecosystem.persistence.data.User;

/**
 * Adapts (converts) a {@link User} object from persistance to a {@link UserListItem} from ui.
 *
 * @author Fredrik Jenssen
 */
public class UserListItemAdapter {

    public UserListItem adapt(User user) {
        return new UserListItem(user.getUsername(), user.getId());
    }
}

