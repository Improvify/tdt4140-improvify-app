package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.util.List;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
@Slf4j
public class MySqlGoalRepository implements GoalRepository {

    private Provider<Connection> connection;
    private final UserRepository userRepository;

    @Inject
    public MySqlGoalRepository(Provider<Connection> connection, UserRepository userRepository) {
        this.connection = connection;
        this.userRepository = userRepository;
    }


    @Override
    public void add(Goal item) {

    }

    @Override
    public void add(Iterable<Goal> items) {

    }

    @Override
    public void update(Goal item) {

    }

    @Override
    public void remove(Goal item) {

    }

    @Override
    public void remove(Iterable<Goal> items) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<Goal> query(Specification specification) {
        return null;
    }
}
