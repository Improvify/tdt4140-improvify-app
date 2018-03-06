package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify;

import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetWorkoutSessionByIdSpecification implements SqlSpecification {

    private final int id;

    public GetWorkoutSessionByIdSpecification(int id) {
        this.id = id;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM workoutsession WHERE id = ?");
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }
}
