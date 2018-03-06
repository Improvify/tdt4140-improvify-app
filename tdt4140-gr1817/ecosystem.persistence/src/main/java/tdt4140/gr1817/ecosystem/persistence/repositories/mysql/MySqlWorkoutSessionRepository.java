package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class MySqlWorkoutSessionRepository implements WorkoutSessionRepository {


    private Provider<Connection> connection;
    private final UserRepository userRepository;

    @Inject
    public MySqlWorkoutSessionRepository(Provider<Connection> connection, UserRepository userRepository) {
        this.connection = connection;
        this.userRepository = userRepository;
    }


    @Override
    public void add(WorkoutSession workoutSession) {
        /*
        Currently just a test implementation.
        This code is ugly, please find a cleaner way to do this.
         */
        try {
            String insertSql = "INSERT INTO workoutsession(id, time, intensity, KCal, "
                    + "avgheartrate, maxheartrate, distancerun, loggedBy) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql)
            ) {
                setParameters(preparedStatement, workoutSession.getId(), workoutSession.getTime(),
                        workoutSession.getIntensity(), workoutSession.getKiloCalories(),
                        workoutSession.getAverageHeartRate(), workoutSession.getMaxHeartRate(),
                        workoutSession.getDistanceRun(), workoutSession.getUser().getId());

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
        final SqlSpecification sqlSpecification = (SqlSpecification) specification;
        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = sqlSpecification.toStatement(connection);
                ResultSet resultSet = statement.executeQuery();
        ) {
            final ArrayList<WorkoutSession> results = new ArrayList<>();
            while (resultSet.next()) {
                final WorkoutSession workoutSession = createWorkoutSessionFromResult(resultSet, userRepository);
                results.add(workoutSession);
            }
            return results;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to get workout sessions", ex);
        }
    }


    private static WorkoutSession createWorkoutSessionFromResult(ResultSet resultSet, UserRepository userRepository) throws SQLException {
        int id = resultSet.getInt("id");
        Date time = new Date(resultSet.getDate("time").getTime());
        final int intensity = resultSet.getInt("intensity");
        final float kcal = resultSet.getFloat("kcal");
        final int avgHeartRate = resultSet.getInt("avgHeartRate");
        final int maxHeartRate = resultSet.getInt("maxHeartRate");
        final int distanceRun = resultSet.getInt("distanceRun");
        final int loggedByUserId = resultSet.getInt("loggedBy");

        final User user = userRepository.query(new GetUserByIdSpecification(loggedByUserId)).get(0);

        return new WorkoutSession(id, time, intensity, kcal, avgHeartRate, maxHeartRate, distanceRun, user);
    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}


