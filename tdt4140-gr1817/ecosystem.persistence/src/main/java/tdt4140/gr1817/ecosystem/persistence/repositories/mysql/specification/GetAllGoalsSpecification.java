package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class GetAllGoalsSpecification implements SqlSpecification {

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM goal");
    }
}
