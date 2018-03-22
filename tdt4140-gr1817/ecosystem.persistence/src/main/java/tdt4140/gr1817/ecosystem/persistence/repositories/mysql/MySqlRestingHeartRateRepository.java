package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
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
import java.util.Date;
import java.util.List;

@Slf4j
public class MySqlRestingHeartRateRepository implements RestingHeartRateRepository {

    private final Provider<Connection> connection;
    private final UserRepository userRepository;

    @Inject
    public MySqlRestingHeartRateRepository(Provider<Connection> connection, UserRepository userRepository) {
        this.connection = connection;
        this.userRepository = userRepository;
    }


    @Override
    public void add(RestingHeartRate restingHeartRate) {
           /*
        Currently just a test implementation.
        This code is ugly, please find a cleaner way to do this.
         */
        try {
            String insertSql = "INSERT INTO restingheartrate(id, heartrate, date, measuredBy) "
                    + "VALUES (?, ?, ?, ?)";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql)
            ) {
                setParameters(preparedStatement, restingHeartRate.getId(), restingHeartRate.getHeartRate(),
                        restingHeartRate.getMeasuredAt(), restingHeartRate.getMeasuredBy().getId());
                /*                                   Usikker på om det her burde være .getUserAccount_ID()*/
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add resting heart rate", e);
        }
    }

    @Override
    public void add(Iterable<RestingHeartRate> items) {
        for (RestingHeartRate rhr : items) {
            this.add(rhr);
        }
    }


    @Override
    public void update(RestingHeartRate item) {
        Date measuredAt = item.getMeasuredAt();
        int heartRate = item.getHeartRate();
        User measuredBy = item.getMeasuredBy();

        String updateHeartRateSql = "UPDATE restingheartrate SET date = ?, heartRate = ?, measuredBy = ? WHERE id = ?";
        try (
                Connection connection = this.connection.get();
                PreparedStatement pst = connection.prepareStatement(updateHeartRateSql)
        ) {
            setParameters(pst, measuredAt, heartRate, measuredBy.getId(), item.getId());
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Updating resting heart rate failed", e);
        }

    }

    @Override
    public void remove(RestingHeartRate item) {
        String deleteHeartRateSql = "DELETE FROM RestingHeartRate WHERE id = ?";
        try (
                Connection connection = this.connection.get();
             PreparedStatement pst = connection.prepareStatement((deleteHeartRateSql))
        ) {
            int id = item.getId();
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Delete not successful", e);
        }
    }

    @Override
    public void remove(Iterable<RestingHeartRate> items) {
        for (RestingHeartRate heartRate : items) {
            this.remove(heartRate);
        }

    }

    @Override
    public void remove(Specification specification) {
        final List<RestingHeartRate> heartRates = query(specification);
        for (RestingHeartRate heartRate : heartRates) {
            remove(heartRate);
        }
    }

    @Override
    public List<RestingHeartRate> query(Specification specification) {
        final SqlSpecification sqlSpecification = (SqlSpecification) specification;
        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = sqlSpecification.toStatement(connection);
                ResultSet resultSet = statement.executeQuery()
        ) {

            final ArrayList<RestingHeartRate> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createFromResultSet(resultSet, userRepository));
            }
            return results;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to get resting heart rate", ex);
        }
    }

    private static RestingHeartRate createFromResultSet(ResultSet resultSet, UserRepository userRepository)
            throws SQLException {
        final int id = resultSet.getInt("id");
        final Date measuredAt = new Date(resultSet.getDate("date").getTime());
        final int heartRate = resultSet.getInt("heartRate");
        final int measuredByUserId = resultSet.getInt("measuredBy");

        final User user = userRepository.query(new GetUserByIdSpecification(measuredByUserId)).get(0);

        return new RestingHeartRate(id, measuredAt, heartRate, user);
    }


    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }

}
