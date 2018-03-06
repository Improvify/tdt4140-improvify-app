package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetRestingHeartRateById implements SqlSpecification {

    private final int id;

    public GetRestingHeartRateById(int id) {
        this.id = id;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM restingheartrate WHERE id = ?");
        statement.setInt(1, id);
        return statement;
    }
}
