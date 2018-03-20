package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.improvify;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRowRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetWorkoutPlanByIdSpecification;
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
public class MySqlWorkoutPlanRowRepository implements WorkoutPlanRowRepository {

    private Provider<Connection> connection;
    private WorkoutPlanRepository workoutPlanRepository;

    @Inject
    public MySqlWorkoutPlanRowRepository(Provider<Connection> connection, WorkoutPlanRepository workoutPlanRepository) {
        this.connection = connection;
        this.workoutPlanRepository = workoutPlanRepository;
    }

    @Override
    public void add(WorkoutPlanRow workoutPlanRow) {

        try {
            String insertSql = "INSERT INTO workoutplanrow(id, description, durationSeconds, intensity, comment,"
                    + " workoutplan_id)"
                    + "VALUES(?,?,?,?,?,?)";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

                setParameters(preparedStatement, workoutPlanRow.getId(), workoutPlanRow.getDescription(),
                        workoutPlanRow.getDurationSeconds(), workoutPlanRow.getIntensity(), workoutPlanRow.getComment(),
                        workoutPlanRow.getWorkoutPlan().getId());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add workoutplanrow", e);
        }

    }

    @Override
    public void add(Iterable<WorkoutPlanRow> items) {
        for (WorkoutPlanRow wpr : items) {
            this.add(wpr);
        }
    }

    @Override
    public void update(WorkoutPlanRow item) {
        String description = item.getDescription();
        int duration = item.getDurationSeconds();
        String intensity = item.getIntensity();
        String comment = item.getComment();
        int workoutplanID = item.getWorkoutPlan().getId();

        String updateWorkoutPlanRowSql = "UPDATE workoutplanrow SET description = ?,"
                + " durationseconds= ?, intensity = ?, `comment` = ?, workoutplan_id = ?";

        try (
                Connection connection = this.connection.get();
                PreparedStatement pst = connection.prepareStatement(updateWorkoutPlanRowSql)
        ) {
            setParameters(pst, description,
                    duration, intensity, comment, workoutplanID);
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Update unsuccessful");
        }
    }


    @Override
    public void remove(WorkoutPlanRow item) {
        try (
                Connection connection = this.connection.get();
                PreparedStatement pst = connection.prepareStatement("DELETE  FROM  workoutplanrow WHERE id = ?")
        ) {
            setParameters(pst, item.getId());
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Delete unsuccessful");
        }
    }

    @Override
    public void remove(Iterable<WorkoutPlanRow> items) {
        for (WorkoutPlanRow wpr : items) {
            this.remove(wpr);
        }
    }

    @Override
    public void remove(Specification specification) {
        List<WorkoutPlanRow> toRemove = query(specification);
        remove(toRemove);
    }

    @Override
    public List<WorkoutPlanRow> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;
        try (
                Connection connection = this.connection.get();
                PreparedStatement preparedStatement = sqlSpecification.toStatement(connection);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            ArrayList<WorkoutPlanRow> results = new ArrayList<>();
            while (resultSet.next()) {
                WorkoutPlanRow workoutPlanRow = createFromResultSet(resultSet, workoutPlanRepository);
                results.add(workoutPlanRow);
            }
            return results;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to query workoutplanrow", ex);
        }
    }

    private static WorkoutPlanRow createFromResultSet(ResultSet resultSet, WorkoutPlanRepository repository) throws
            SQLException {
        int rowid = resultSet.getInt("id");
        int id = resultSet.getInt("userid");
        String description = resultSet.getString("description");
        int duration = resultSet.getInt("duration");
        String intensity = resultSet.getString("intensity");
        String comment = resultSet.getString("comment");
        WorkoutPlan workoutPlan = repository.query(new GetWorkoutPlanByIdSpecification(id)).get(0);
        return new WorkoutPlanRow(id, description, duration, intensity, comment, workoutPlan);

    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }

}
