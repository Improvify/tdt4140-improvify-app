package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetServiceProviderPermissionByUserAndServiceProviderSpecification implements SqlSpecification {


    private final int nameID, serviceProviderID;

    public GetServiceProviderPermissionByUserAndServiceProviderSpecification(int nameID, int serviceProviderID) {
        this.nameID = nameID;
        this.serviceProviderID = serviceProviderID;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM useraccount WHERE useraccount_ID = ? AND serviceProviderID = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, nameID);
        preparedStatement.setInt(2, serviceProviderID);
        return preparedStatement;
    }
}
