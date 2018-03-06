package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Slf4j
public class MySqlWorkoutSessionRepository implements WorkoutSessionRepository {


    private Provider<Connection> connection;

    @Inject
    public MySqlWorkoutSessionRepository(Provider<Connection> connection) {
        this.connection = connection;
    }


    @Override
    public void add(WorkoutSession workoutSession) {
        /*
        Currently just a test implementation.
        This code is ugly, please find a cleaner way to do this.
         */
        try {
            String insertSql = "INSERT INTO workoutsession(id, `time`, intensity, KCal, "
                    + "avgheartrate, maxheartrate, distancerun, loggedBy) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql)
            ) {
                setParameters(preparedStatement, workoutSession.getTime(), workoutSession.getIntensity(),
                        workoutSession.getKiloCalories(), workoutSession.getAverageHeartRate(),
                        workoutSession.getMaxHeartRate(), workoutSession.getUser().getId(),
                        workoutSession.getId());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add workoutsession", e);
        }
    }

    @Override
    public void add(Iterable<WorkoutSession> items) {
        for (WorkoutSession session : items) {
            this.add(session);
        }
    }

    @Override
    public void update(WorkoutSession item) {
            Date time = item.getTime();
            int intensity = item.getIntensity();
            float kiloCalories = item.getKiloCalories();
            float averageHeartRate = item.getAverageHeartRate();
            float maxHeartRate = item.getMaxHeartRate();
            float distanceRun = item.getDistanceRun();
            User user = item.getUser();
            String updateWorkoutSession = "UPDATE Workoutsession SET time = ?, intensity = ?,"
                    + " kCal = ?, avgHeartRate = ?, maxHeartRate = ?, distanceRun = ?, loggedBy = ?";
        try (Connection connection = this.connection.get();
             PreparedStatement pst = connection.prepareStatement(updateWorkoutSession)
        ) {
            setParameters(pst, time, intensity, kiloCalories, averageHeartRate, maxHeartRate, distanceRun, user);
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Update not successful");
        }
    }

    @Override
    public void remove(WorkoutSession item) {
        try {
            int id = item.getId();
            String deleteWorkoutSession = "DELETE FROM WorkoutSession WHERE id = '" + id + "' ";
            Connection connection = this.connection.get();
            PreparedStatement pst = connection.prepareStatement(deleteWorkoutSession);
            pst.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Delete not successful");
        }

    }

    @Override
    public void remove(Iterable<WorkoutSession> items) {
        for (WorkoutSession workoutSession : items) {
            this.remove(workoutSession);
        }

    }

    @Override
    public void remove(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<WorkoutSession> query(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }


    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}


