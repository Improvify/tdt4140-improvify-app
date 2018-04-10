package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify;

import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetWorkoutSessionByLoggedBySpecification implements SqlSpecification {
    private final int loggedBy;

    public GetWorkoutSessionByLoggedBySpecification(int loggedBy) {
        this.loggedBy = loggedBy;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM workoutsession WHERE loggedBy = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, loggedBy);
        return preparedStatement;
    }
}
