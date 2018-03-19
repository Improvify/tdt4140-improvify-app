package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;


import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.PeriodPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutPlanRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Slf4j
public class MySqlWorkoutPlanRepository implements WorkoutPlanRepository {

    private Provider<Connection> connection;

    @Inject
    public MySqlWorkoutPlanRepository(Provider<Connection> connection) {
        this.connection = connection;
    }

    @Override
    public void add(WorkoutPlan workoutPlan) {

        try {
            String insertSql = "INSERT INTO workoutplan(id, createdForUser, parentPlan, workoutplanrows)"
                    + "VALUES(?,?,?,?)";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

                setParameters(preparedStatement, workoutPlan.getId(), workoutPlan.getCreatedForUser(),
                        workoutPlan.getParentPlan(), workoutPlan.getRows());

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
        PeriodPlan parentPlan = item.getParentPlan();
        List<WorkoutPlanRow> rows = item.getRows();

        String updateWorkoutPlanSql = "UPDATE workoutplan SET createdForUser = ?,"
                + " parentPlan = ?, rows = ?";

        try (
                Connection connection = this.connection.get();
                PreparedStatement pst = connection.prepareStatement(updateWorkoutPlanSql)
        ) {
            setParameters(pst, createdforUser, parentPlan, rows);
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

    }

    @Override
    public List<WorkoutPlan> query(Specification specification) {
        return null;
    }

    // incomplete method
    private static WorkoutPlan createWorkOutPlanFromResultSet(ResultSet resultSet, UserRepository userRepository) {
        try {
            int id = resultSet.getInt("id");
            int createdForUserId = resultSet.getInt("createdForUser");

            User user = userRepository.query(new GetUserByIdSpecification(createdForUserId)).get(0);
            PeriodPlan pp = null; // midlertidig løsning ettersom periodplan ikke er ferdig implementer ennå
            //List<WorkoutPlan>  rows =  #placeholder, dette er ikke ferdig implementert

        } catch (Exception ex) {
            throw new RuntimeException("Failed to create workoutplan from resultset");
        }
        return null;
    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
