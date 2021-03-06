package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetAllServiceProviderPermissionsForUserSpecification implements SqlSpecification {

    private final int userID;

    public GetAllServiceProviderPermissionsForUserSpecification(int userID) {
        this.userID = userID;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM serviceproviderpermissions WHERE UserAccount_id = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userID);
        return preparedStatement;
    }
}
