package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;

import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutPlanRowRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;


@Slf4j
public class MySqlWorkoutPlanRowRepository implements WorkoutPlanRowRepository {

    private Provider<Connection> connection;

    @Inject
    public MySqlWorkoutPlanRowRepository(Provider<Connection> connection) {
        this.connection = connection;
    }

    @Override
    public void add(WorkoutPlanRow workoutPlanRow) {

        try {
            String insertSql = "INSERT INTO workoutplanrow(id, description, durationSeconds, intensity, `comment`,"
                    + " workoutplan_id)"
                    + "VALUES(?,?,?,?,?,?)";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

                setParameters(preparedStatement, workoutPlanRow.getId(), workoutPlanRow.getDescription(),
                        workoutPlanRow.getDuration(), workoutPlanRow.getIntensity(), workoutPlanRow.getComment(),
                        workoutPlanRow.getWorkoutplanID());

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
        Duration duration = item.getDuration();
        String intensity = item.getIntensity();
        String comment = item.getComment();
        int workoutplanID = item.getWorkoutplanID();

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

    }

    @Override
    public List<WorkoutPlanRow> query(Specification specification) {
        return null;
    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }

}
