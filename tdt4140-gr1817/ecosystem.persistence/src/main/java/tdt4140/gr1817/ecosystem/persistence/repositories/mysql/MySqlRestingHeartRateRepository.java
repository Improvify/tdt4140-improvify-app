package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
@Slf4j
public class MySqlRestingHeartRateRepository implements RestingHeartRateRepository{

    private Provider<Connection> connection;

    @Inject
    public MySqlRestingHeartRateRepository(Provider<Connection> connection) {
        this.connection = connection;
    }


    @Override
    public void add(RestingHeartRate restingHeartRate) {
           /*
        Currently just a test implementation.
        This code is ugly, please find a cleaner way to do this.
         */
        try {
            String insertSql = "INSERT INTO restingheartrate(id, `heartrate`, `date`, useraccount_id) "
                    + "VALUES (?, ?, ?, ?)";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql)
            ) {
                setParameters(preparedStatement, restingHeartRate.getId(), restingHeartRate.getMeasuredAt(), restingHeartRate.getHeartRate(),
                        restingHeartRate.getMeasuredBy().getId() );
                /*                                   Usikker på om det her burde være .getUserAccount_ID()*/
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add resting heart rate", e);
        }
    }

    @Override
    public void add(Iterable<RestingHeartRate> items) {
        for (RestingHeartRate rhr: items)
                this.add(rhr);
              {

        }
    }


    @Override
    public void update(RestingHeartRate item) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(RestingHeartRate item) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(Iterable<RestingHeartRate> items) {

    }

    @Override
    public void remove(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<RestingHeartRate> query(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }


    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }

}
