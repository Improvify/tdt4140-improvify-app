package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetServiceProviderPermissionByIdSpecification implements SqlSpecification {

    private final int userId;
    private final int serviceProviderId;

    public GetServiceProviderPermissionByIdSpecification(int userId, int serviceProviderId) {
        this.userId = userId;
        this.serviceProviderId = serviceProviderId;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM serviceproviderpermissions "
                + "WHERE UserAccount_id = ? AND ServiceProvider_id = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, serviceProviderId);
        return preparedStatement;
    }
}
