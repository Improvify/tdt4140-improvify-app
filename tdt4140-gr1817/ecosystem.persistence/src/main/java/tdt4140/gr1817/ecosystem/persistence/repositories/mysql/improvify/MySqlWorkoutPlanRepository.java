package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.improvify;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRepository;
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


@Slf4j
public class MySqlWorkoutPlanRepository implements WorkoutPlanRepository {

    private Provider<Connection> connection;
    private final UserRepository userRepository;

    @Inject
    public MySqlWorkoutPlanRepository(Provider<Connection> connection, UserRepository userRepository) {
        this.connection = connection;
        this.userRepository = userRepository;
    }

    @Override
    public void add(WorkoutPlan workoutPlan) {

        try {
            String insertSql = "INSERT INTO workoutplan(id, description, createdForUser)"
                    + "VALUES(?,?,?)";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

                setParameters(preparedStatement, workoutPlan.getId(), workoutPlan.getDescription(),
                        workoutPlan.getCreatedForUser());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add workoutplan", e);
        }

    }

    @Override
    public void add(Iterable<WorkoutPlan> items) {
        for (WorkoutPlan workoutPlan : items) {
            this.add(workoutPlan);
        }
    }

    @Override
    public void update(WorkoutPlan item) {
        User createdforUser = item.getCreatedForUser();
        String description = item.getDescription();

        String updateWorkoutPlanSql = "UPDATE workoutplan SET createdForUser = ?,"
                + " description = ?";

        try (
                Connection connection = this.connection.get();
                PreparedStatement pst = connection.prepareStatement(updateWorkoutPlanSql)
        ) {
            setParameters(pst, createdforUser, description);
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Update unsuccessful");
        }
    }

    @Override
    public void remove(WorkoutPlan item) {
        try (
                Connection connection = this.connection.get();
                PreparedStatement pst = connection.prepareStatement("DELETE  FROM  workoutplan WHERE id = ?")
        ) {
            setParameters(pst, item.getId());
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Delete unsuccessful");
        }
    }

    @Override
    public void remove(Iterable<WorkoutPlan> items) {
        for (WorkoutPlan workoutPlan : items) {
            this.remove(workoutPlan);
        }
    }

    @Override
    public void remove(Specification specification) {
        List<WorkoutPlan> toRemove = query(specification);
        remove(toRemove);
    }

    @Override
    public List<WorkoutPlan> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;
        try (
                Connection connection = this.connection.get();
                PreparedStatement preparedStatement = sqlSpecification.toStatement(connection);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            ArrayList<WorkoutPlan> results = new ArrayList<>();
            while (resultSet.next()) {
                WorkoutPlan workoutPlan = createWorkoutPlanFromResultSet(resultSet, userRepository);
                results.add(workoutPlan);
            }
            return results;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to query workoutplan", ex);
        }
    }

    // incomplete method
    private static WorkoutPlan createWorkoutPlanFromResultSet(ResultSet resultSet, UserRepository userRepository) {
        try {
            int id = resultSet.getInt("id");
            int createdForUserId = resultSet.getInt("createdForUser");
            String desc = resultSet.getString("description");
            User user = userRepository.query(new GetUserByIdSpecification(createdForUserId)).get(0);
            return new WorkoutPlan(id, desc, user);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to create workoutplan from resultset");
        }
    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
