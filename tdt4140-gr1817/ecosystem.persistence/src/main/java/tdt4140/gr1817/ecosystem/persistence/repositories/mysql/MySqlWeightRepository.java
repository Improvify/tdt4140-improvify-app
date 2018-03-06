package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
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

public class MySqlWeightRepository implements WeightRepository {

    private final Provider<Connection> connection;
    private final UserRepository userRepository;

    @Inject
    public MySqlWeightRepository(Provider<Connection> connection, UserRepository userRepository) {
        this.connection = connection;
        this.userRepository = userRepository;
    }


    @Override
    public void add(Weight weight) {
           /*
        Currently just a test implementation.
        This code is ugly, please find a cleaner way to do this.
         */
        try {
        String insertSql = "INSERT INTO weight(id, currentweight, date, measuredBy) "
                + "VALUES (?, ?, ?, ?)";
        try (
                Connection connection = this.connection.get();
                PreparedStatement preparedStatement = connection.prepareStatement(insertSql)
        ) {
            setParameters(preparedStatement, weight.getId(), weight.getCurrentWeight(), weight.getDate(),
                    weight.getUser().getId());
            /*                                   Usikker på om det her burde være .getUserAccount_ID()*/
            preparedStatement.execute();
        }
    } catch (SQLException e) {
        throw new RuntimeException("Failed to add workout session", e);
    }
}

    @Override
    public void add(Iterable<Weight> items) {
        for (Weight weight : items) {
            this.add(weight);
        }
    }

    @Override
    public void update(Weight weight) {
           float currentWeight = weight.getCurrentWeight();
           Date date = weight.getDate();
           User user = weight.getUser();

           String updateWeightSql = "UPDATE Weight SET currentWeight= ?, date = ?, measuredBy = ? WHERE id = ?";
           try (Connection connection = this.connection.get();
                PreparedStatement pst = connection.prepareStatement(updateWeightSql)
           ) {
               setParameters(pst, currentWeight, date, user.getId(), weight.getId());
               pst.execute();
           } catch (SQLException e) {
               throw new RuntimeException("Updating weight failed", e);
           }

    }

    @Override
    public void remove(Weight item) {
        try {
            int id = item.getId();
            String deleteWeightSql = "DELETE FROM Weight WHERE id = '" + id + "' ";
            Connection connection = this.connection.get();
            PreparedStatement pst = connection.prepareStatement(deleteWeightSql);
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Delete not successful");
        }
    }

    @Override
    public void remove(Iterable<Weight> items) {
        for (Weight weight : items) {
            this.remove(weight);
        }
    }

    @Override
    public void remove(Specification specification) {
        final List<Weight> weights = query(specification);
        for (Weight weight : weights) {
            remove(weight);
        }
    }

    @Override
    public List<Weight> query(Specification specification) {
        final SqlSpecification sqlSpecification = (SqlSpecification) specification;
        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = sqlSpecification.toStatement(connection);
                ResultSet resultSet = statement.executeQuery();
        ) {
            final ArrayList<Weight> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createWeightFromResult(resultSet, userRepository));
            }
            return results;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to get weights", ex);
        }
    }

    private static Weight createWeightFromResult(ResultSet resultSet, UserRepository userRepository)
            throws SQLException {
        final int id = resultSet.getInt("id");
        final float currentWeight = resultSet.getFloat("currentWeight");
        final Date date = new Date(resultSet.getDate("date").getTime());
        final int measuredByUserId = resultSet.getInt("measuredBy");

        final User user = userRepository.query(new GetUserByIdSpecification(measuredByUserId)).get(0);

        return new Weight(id, currentWeight, date, user);
    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }

}
