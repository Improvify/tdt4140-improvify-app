package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetWeightByUserSpecification implements SqlSpecification {

    private final User user;

    public GetWeightByUserSpecification(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM weight WHERE measuredBy = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getId());
        return preparedStatement;
    }
}
