package tdt4140.gr1817.app.core.feature.user;

import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class GetUserWithId {

    private final UserRepository userRepository;

    @Inject
    public GetUserWithId(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the user with the specified id
     * @param id the users id
     * @return the {@link User}, or {@code null} if none was found
     */
    public User getUserWithId(int id) {
        List<User> result = userRepository.query(new GetUserByIdSpecification(id));
        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }
}
