package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
                    + "avgheartrate, maxheartrate, distancerun, useraccount_id) "
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
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(WorkoutSession item) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(Iterable<WorkoutSession> items) {

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


