package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.GoalRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public void add(Goal goal) {
        final String sql = "INSERT INTO goal(id, description, isCompleted, isCurrent, createdBy)"
                + " VALUES (?, ?, ?, ?, ?)";
        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            setParameters(statement, goal.getId(), goal.getDescription(), goal.isCompleted(), goal.isCurrent(),
                    goal.getUser().getId());
            statement.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to add goal", ex);
        }
    }

    @Override
    public void add(Iterable<Goal> goals) {
        for (Goal goal : goals) {
            add(goal);
        }
    }

    @Override
    public void update(Goal goal) {
        final String sql = "UPDATE goal SET description=?, isCurrent=?, isCompleted=?, createdBy=? WHERE id = ?";

        try (
                Connection connection = this.connection.get();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            setParameters(preparedStatement, goal.getDescription(), goal.isCurrent(), goal.isCompleted(),
                    goal.getUser().getId(), goal.getId());
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to update user", ex);
        }
    }

    @Override
    public void remove(Goal goal) {
        try (
                Connection connection = this.connection.get();
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM goal WHERE id = ?");
        ) {
            setParameters(preparedStatement, goal.getId());
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to delete goal", ex);
        }
    }

    @Override
    public void remove(Iterable<Goal> goals) {
        for (Goal goal : goals) {
            remove(goal);
        }
    }

    @Override
    public void remove(Specification specification) {
        List<Goal> goals = query(specification);
        for (Goal goal : goals) {
            remove(goal);
        }
    }

    @Override
    public List<Goal> query(Specification specification) {
        final SqlSpecification sqlSpecification = (SqlSpecification) specification;

        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = sqlSpecification.toStatement(connection);
                ResultSet resultSet = statement.executeQuery();
        ) {
            final ArrayList<Goal> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createGoalFromResultSet(resultSet, userRepository));
            }
            return results;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to query goal", ex);
        }
    }

    private static Goal createGoalFromResultSet(ResultSet resultSet, UserRepository userRepository) {
        try {
            final int id = resultSet.getInt("id");
            final String description = resultSet.getString("description");
            final int createdByUserId = resultSet.getInt("createdBy");
            final boolean isCurrent = resultSet.getBoolean("isCurrent");
            final boolean isCompleted = resultSet.getBoolean("isCompleted");

            final User user = userRepository.query(new GetUserByIdSpecification(createdByUserId)).get(0);

            return new Goal(id, user, description, isCompleted, isCurrent);
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to create goal", ex);
        }
    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
