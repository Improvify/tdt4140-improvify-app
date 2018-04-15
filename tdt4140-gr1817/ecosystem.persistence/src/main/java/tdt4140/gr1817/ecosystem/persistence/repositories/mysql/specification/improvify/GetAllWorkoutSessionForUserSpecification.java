package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify;

import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetAllWorkoutSessionForUserSpecification implements SqlSpecification {

    private final int userID;

    public GetAllWorkoutSessionForUserSpecification(int userID) {
        this.userID = userID;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM workoutsession WHERE loggedBy = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userID);
        return preparedStatement;
    }
}
