package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetWeightByIdSpecification implements SqlSpecification {

    private final int id;

    public GetWeightByIdSpecification(int id) {
        this.id = id;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM weight WHERE id = ?");
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }
}
