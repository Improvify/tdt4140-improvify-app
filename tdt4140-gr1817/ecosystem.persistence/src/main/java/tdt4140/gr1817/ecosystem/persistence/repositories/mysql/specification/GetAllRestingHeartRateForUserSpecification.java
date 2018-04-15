package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetAllRestingHeartRateForUserSpecification implements SqlSpecification {

    private final int userID;

    public GetAllRestingHeartRateForUserSpecification(int userID) {
        this.userID = userID;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM restingheartrate WHERE measuredBy = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userID);
        return preparedStatement;
    }
}
