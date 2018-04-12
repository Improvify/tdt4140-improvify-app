package tdt4140.gr1817.app.core.feature.user;

import lombok.NonNull;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllUsersSpecification;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class GetAllUsers {

    private final UserRepository userRepository;

    @Inject
    public GetAllUsers(@NonNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        List<User> allUsers = userRepository.query(new GetAllUsersSpecification());
        // FIXME: 4/9/2018 only get users that the service provider has access to. Maybe use a different specification
        if (allUsers == null) {
            allUsers = new ArrayList<>(0);
        }
        return allUsers;
    }
}
