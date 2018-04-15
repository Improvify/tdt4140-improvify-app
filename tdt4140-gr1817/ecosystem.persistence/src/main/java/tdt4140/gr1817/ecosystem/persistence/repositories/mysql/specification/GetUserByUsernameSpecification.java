package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetUserByUsernameSpecification implements SqlSpecification {

    private final String username;

    public GetUserByUsernameSpecification(String username) {
        this.username = username;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM useraccount WHERE username = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        return preparedStatement;
    }
}
