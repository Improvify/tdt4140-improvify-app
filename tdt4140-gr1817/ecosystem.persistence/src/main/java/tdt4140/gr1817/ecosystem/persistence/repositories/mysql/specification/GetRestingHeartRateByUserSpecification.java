package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetRestingHeartRateByUserSpecification implements SqlSpecification {


    private final User user;

    public GetRestingHeartRateByUserSpecification(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM restingheartrate WHERE measuredBy = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getId());
        return preparedStatement;
    }
}
