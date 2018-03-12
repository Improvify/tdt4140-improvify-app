package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetGoalByIdSpecification implements SqlSpecification {

    private final int id;

    public GetGoalByIdSpecification(int id) {
        this.id = id;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM goal WHERE id = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }
}
