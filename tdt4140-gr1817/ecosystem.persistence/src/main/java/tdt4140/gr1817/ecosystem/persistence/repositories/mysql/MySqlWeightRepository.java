package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;
import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
@Slf4j

public class MySqlWeightRepository implements WeightRepository{

    private Provider<Connection> connection;

    @Inject
    public MySqlWeightRepository(Provider<Connection> connection) {
        this.connection = connection;
    }


    @Override
    public void add(Weight weight) {
           /*
        Currently just a test implementation.
        This code is ugly, please find a cleaner way to do this.
         */
        try {
        String insertSql = "INSERT INTO weight(id, `currentweight`, `date`, useraccount_id) "
                + "VALUES (?, ?, ?, ?)";
        try (
                Connection connection = this.connection.get();
                PreparedStatement preparedStatement = connection.prepareStatement(insertSql)
        ) {
            setParameters(preparedStatement, weight.getId(), weight.getDate(), weight.getCurrentWeight(), weight.getUser().getId());
            /*                                   Usikker på om det her burde være .getUserAccount_ID()*/
            preparedStatement.execute();
        }
    } catch (SQLException e) {
        throw new RuntimeException("Failed to add workout session", e);
    }
}

    @Override
    public void add(Iterable<Weight> items) {
        for (Weight weight: items){
            this.add(weight);
        }
    }

    @Override
    public void update(Weight item) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(Weight item) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(Iterable<Weight> items) {

    }

    @Override
    public void remove(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Weight> query(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }


    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }

}
