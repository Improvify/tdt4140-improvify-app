package tdt4140.gr1817.app.core.feature.user;

import com.google.inject.Singleton;
import lombok.Value;

import java.util.Optional;

/**
 * Service to keep a selection of a user.
 *
 * Currently a singleton, so the same instance must be reused.
 *
 * @author Kristian Rekstad
 */
@Singleton
public class UserSelectionService {
    private UserId selectedUserId = null;

    public Optional<UserId> getSelectedUserId() {
        return Optional.ofNullable(selectedUserId);
    }

    public void setSelectedUserId(UserId selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    @Value
    public static final class UserId {
        private final int id;
    }
}
